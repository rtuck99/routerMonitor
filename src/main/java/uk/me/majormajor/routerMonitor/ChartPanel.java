package uk.me.majormajor.routerMonitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ChartPanel extends JPanel {
	private Main main;
	private Stat stat = Stat.STAT_NOISE_MARGIN_DOWNSTREAM;
	private float minValue;
	private float maxValue;
	private Long minTime;
	private Long maxTime;
	private String rangeHash = "";
	private DataSample highlightedDs;
	
	public ChartPanel (Main aMain)
	{
		this.main = aMain;
		setPreferredSize(new Dimension(320, 200));
		setOpaque(true);
		minValue = Float.MAX_VALUE;
		maxValue = Float.MIN_VALUE;
		minTime = Long.MAX_VALUE;
		maxTime = Long.MIN_VALUE;
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				List<DataSample> samples = main.getSamples();
				long t = timeForX(p.x);
				for(DataSample ds : samples)
				{
					if (ds.getTimestamp() > t)
					{
						highlightedDs = ds;
						DateFormat df = DateFormat.getTimeInstance();
						String tooltip = "(" + df.format(new Date(ds.getTimestamp())) + ", " + String.format("%.2f", ((Number) ds.getData().get(stat)).floatValue()) + ")";
						setToolTipText(tooltip );
						repaint();
						break;
					}
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		List<DataSample> samples = main.getSamples();
		Path2D.Double path = new Path2D.Double();
		Iterator<DataSample> i = samples.iterator();
		if (i.hasNext())
		{
			Point2D p = statToPoint(i.next());
			path.moveTo(p.getX(), p.getY());
		}
		while (i.hasNext())
		{
			Point2D p = statToPoint(i.next());
			path.lineTo(p.getX(), p.getY());
		}
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawTimeLines((Graphics2D) g2);
		g2.setColor(Color.BLUE);
		((Graphics2D)g2).draw(path);
		
		drawHighlight(g2);
		g2.dispose();
	}
	
	private void drawHighlight(Graphics2D g2) {
		if (highlightedDs != null)
		{
			Point2D p = statToPoint(highlightedDs);
			Ellipse2D c = new Ellipse2D.Double(p.getX() - 3, p.getY() - 3, 6, 6);
			g2.fill(c);
		}
		
	}

	private void drawTimeLines(Graphics2D g) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date(minTime));
		
		GregorianCalendar endCal = new GregorianCalendar();
		endCal.setTime(new Date(maxTime));
		
		cal.set(GregorianCalendar.SECOND, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		
		Rectangle rc = SwingUtilities.calculateInnerArea(this, null);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.GRAY);
		BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 15.0f, new float[]{8, 5}, 0);
		BasicStroke solidStroke = new BasicStroke(1.0f);
		g2.setStroke(dashedStroke);
		while (cal.compareTo(endCal) < 0)
		{
			cal.add(GregorianCalendar.MINUTE, 10);
			g2.setStroke(cal.get(GregorianCalendar.MINUTE) == 0 ? solidStroke : dashedStroke);
			long t = cal.getTimeInMillis();
			float x = xForTime(t);
			g2.drawLine((int) x, rc.y, (int) x, rc.y + rc.height);
		}
		g2.dispose();
	}

	public Point2D statToPoint(DataSample ds)
	{
		Rectangle rc = SwingUtilities.calculateInnerArea(this, null);
		float x = xForTime(ds.getTimestamp());
		Number value = (Number)ds.getData().get(stat);
		if (value == null)
		{
			value = Float.valueOf(0);
		}
		float y = 0;
		y = rc.y + rc.height - rc.height * (value.floatValue() - minValue) / (maxValue - minValue);
		return new Point2D.Float(x, y);
	}
	
	private float xForTime(long t)
	{
		Rectangle rc = SwingUtilities.calculateInnerArea(this, null);
		return rc.x + ((float) rc.width) * (t - minTime) / (maxTime -  minTime);
	}

	private long timeForX(float x)
	{
		Rectangle rc = SwingUtilities.calculateInnerArea(this, null);
		return minTime + (long) ((maxTime - minTime) * (x - rc.x) / rc.width);
	}
	
	public void refreshRanges()
	{
		if (!(stat + ":" + main.getSamples().size()).equals(rangeHash))
		{
			List<DataSample> samples = main.getSamples();
			minValue = Float.MAX_VALUE;
			maxValue = Float.MIN_VALUE;
			minTime = Long.MAX_VALUE;
			maxTime = Long.MIN_VALUE;
			
			for (DataSample ds : samples)
			{
				long t = ds.getTimestamp();
				Number v = (Number) ds.getData().get(stat);
				
				if (v != null)
				{
					minValue = Math.min(minValue, v.floatValue());
					maxValue = Math.max(maxValue, v.floatValue());
				}
				minTime = Math.min(minTime, t);
				maxTime = Math.max(maxTime, t);
			}
			rangeHash = stat + ":" + main.getSamples().size();
		}
	}
	
	public Stat getStat() {
		return stat;
	}

	public void setStat(Stat stat) {
		this.stat = stat;
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public Long getMinTime() {
		return minTime;
	}

	public Long getMaxTime() {
		return maxTime;
	}
}
