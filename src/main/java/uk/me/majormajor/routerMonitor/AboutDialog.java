package uk.me.majormajor.routerMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class AboutDialog extends JDialog {
	public AboutDialog()
	{
		super((Window)null, "About");
		
		setPreferredSize(new Dimension(240, 200));
		
		Border emptyBorder = BorderFactory.createEmptyBorder(8, 8, 8, 8);

		getContentPane().setLayout(new BorderLayout(4, 4));
		Box mainPanel = new Box(BoxLayout.Y_AXIS);
		mainPanel.setBorder(emptyBorder);
		Package thisPackage = getClass().getPackage();
		String productName = thisPackage.getImplementationTitle();
		String version = thisPackage.getImplementationVersion();
		String vendor = thisPackage.getImplementationVendor();

		JLabel productNameLabel = new JLabel("<html><div align='center'><p style='font-size:20pt'/>" + productName + 
				"<p/><p/>Version " + version + 
				"<p/><p/>" + vendor + 
				"<p/><p/><a href='mailto:robt@majormajor.me.uk'>feedback</a></div></html>");
		getContentPane().add(productNameLabel, BorderLayout.CENTER);
		productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		mainPanel.add(Box.createVerticalStrut(8));
//		
//		JLabel versionLabel = new JLabel("<html><h2>Version " + version + "</h2></html>");
//		versionLabel.setAlignmentX(0.5f);
//		mainPanel.add(versionLabel);
//		mainPanel.add(Box.createVerticalStrut(8));
//
//		JLabel vendorLabel = new JLabel("<html><h2>" + vendor + "</h2></html>");
//		vendorLabel.setAlignmentX(0.5f);
//		mainPanel.add(vendorLabel);
		
//		getContentPane().add(mainPanel, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel(new BorderLayout(4, 4));

		JButton okButton = new JButton(new AbstractAction("Ok")
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.setVisible(false);
				AboutDialog.this.dispose();
			}
		});
		bottomPanel.add(okButton, BorderLayout.EAST);
		bottomPanel.setBorder(emptyBorder);
		
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		pack();
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
