package uk.me.majormajor.routerMonitor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.JComponent;


public class YAxis extends JComponent {
	private ChartPanel chartPanel;

	public YAxis(ChartPanel chartPanel)
	{
		this.chartPanel = chartPanel;
		setPreferredSize(new Dimension(80, 240));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		chartPanel.refreshRanges();

		Graphics2D g2 = (Graphics2D)g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
	
		String minText = String.format("%.2f", chartPanel.getMinValue());
		String maxText = String.format("%.2f", chartPanel.getMaxValue());
		
		TextLayout tl = new TextLayout(maxText, g2.getFont(), g2.getFontRenderContext());
		Rectangle2D rc = tl.getBounds();
		g2.drawString(maxText, (int)(getWidth() - rc.getWidth()) - 4, 12);
		TextLayout tl2 = new TextLayout(minText, g2.getFont(), g2.getFontRenderContext());
		Rectangle2D rc2 = tl2.getBounds();
		g2.drawString(minText, (int)(getWidth() - rc2.getWidth()) - 4, getHeight());
		g2.dispose();
	}

}
