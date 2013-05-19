package uk.me.majormajor.routerMonitor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;


public class GraphPanel extends JComponent {
	private ChartPanel chartPanel;
	private XAxis xaxis;
	private YAxis yaxis;
	
	public GraphPanel(Main main)
	{
		chartPanel = new ChartPanel(main);
		xaxis = new XAxis(getChartPanel());
		yaxis = new YAxis(getChartPanel());
		
		setLayout(new GridBagLayout());
		add(getChartPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(xaxis, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(yaxis, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}
}
