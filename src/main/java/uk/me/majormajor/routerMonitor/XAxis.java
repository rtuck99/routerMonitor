package uk.me.majormajor.routerMonitor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.JComponent;


public class XAxis extends JComponent {
	private ChartPanel chartPanel;
	public XAxis(ChartPanel chartPanel)
	{
		this.chartPanel = chartPanel;
		setPreferredSize(new Dimension(320, 20));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		chartPanel.refreshRanges();
		Graphics2D g2 = (Graphics2D)g.create();
		g2.drawLine(0, 0, getWidth(), 0);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		String startText = new Date(chartPanel.getMinTime()).toString();
		String endText = new Date(chartPanel.getMaxTime()).toString();
		
		g2.drawString(startText, 0, 14);
		TextLayout tl = new TextLayout(endText, g2.getFont(), g2.getFontRenderContext());
		Rectangle2D rc = tl.getBounds();
		g2.drawString(endText, (int)(getWidth() - rc.getWidth()) - 1, 14);
		
		g2.dispose();
	}
}
