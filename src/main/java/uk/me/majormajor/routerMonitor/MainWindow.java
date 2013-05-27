package uk.me.majormajor.routerMonitor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;


public class MainWindow extends JFrame {
	private Main main;
	private JComboBox<Stat> statChooser;
	private GraphPanel graphPanel;
	
	public MainWindow(Main aMain)
	{
		super("Router Monitor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(4, 4));
		graphPanel = new GraphPanel(aMain);
		Border emptyBorder = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		graphPanel.setBorder(emptyBorder);
		this.main = aMain;
		getContentPane().add(graphPanel, BorderLayout.CENTER);
		
		Box comboPanel = new Box(BoxLayout.X_AXIS);
		comboPanel.setBorder(emptyBorder);
		statChooser = new JComboBox<Stat>();
		statChooser.setRenderer(new ListCellRenderer<Stat>() {
			private DefaultListCellRenderer r = new DefaultListCellRenderer();
			
			@Override
			public Component getListCellRendererComponent(
					JList<? extends Stat> list, Stat value, int index,
					boolean isSelected, boolean cellHasFocus) {
				String s = value == null ? "" : value.getDescription();
				return r.getListCellRendererComponent(list, s, index, isSelected, cellHasFocus);
			}
		});
		statChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				graphPanel.getChartPanel().setStat((Stat)statChooser.getSelectedItem());
				repaint();
			}
		});
		comboPanel.add(statChooser);
		comboPanel.add(Box.createHorizontalGlue());
		getContentPane().add(comboPanel, BorderLayout.NORTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void refreshStatChooser()	
	{
		if (statChooser.getItemCount() == 0 && ! main.getSamples().isEmpty())
		{
			List<Stat> chartableStats = new ArrayList<Stat>();
			for (Stat stat : Stat.values())
			{
				if (Number.class.equals(stat.getDataType()))
				{
					chartableStats.add(stat);
				}
			}
			statChooser.setModel(new DefaultComboBoxModel<Stat>(chartableStats.toArray(new Stat[0])));
			statChooser.setSelectedItem(Stat.STAT_NOISE_MARGIN_DOWNSTREAM);
		}
	}
	
	public void dispose()
	{
		super.dispose();
		main.mainWindowsClosed();
	}
}
