package uk.me.majormajor.routerMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.html.HTMLDocument;

public class AboutDialog extends JDialog {
	private List<String> supportedRouters = Arrays.asList(
			"Sagemcom F@ST2504n"
		);
	
	public AboutDialog()
	{
		super((Window)null, "About");
		
		Border emptyBorder = BorderFactory.createEmptyBorder(8, 8, 8, 8);

		getContentPane().setLayout(new BorderLayout(4, 4));
		Package thisPackage = getClass().getPackage();
		String productName = thisPackage.getImplementationTitle();
		String version = thisPackage.getImplementationVersion();
		String vendor = thisPackage.getImplementationVendor();

		String routers = "";
		for (String s : supportedRouters)
		{
			routers += "<p/>" + s;
		}
		Box mainPanel = new Box(BoxLayout.Y_AXIS);
		JLabel productNameLabel = new JLabel("<html><div align='center'><p style='font-size:20pt'/>" + productName + 
				"<p/><p/>Version " + version + 
				"<p/><p/>" + vendor + 
				"<p/><p/>Supported Routers:" +
				routers + 
				"</div></html>");
		productNameLabel.setBorder(emptyBorder);
		mainPanel.add(productNameLabel);
		final String mailToLink = "mailto:robt@majormajor.me.uk?subject=Router%20Monitor";
		JLabel feedback = new JLabel("<html><a href='" + mailToLink  + "'>Send Feedback</a></html>");
		feedback.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported())
				{
					try {
						Desktop.getDesktop().browse(new URI(mailToLink));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
				super.mouseClicked(e);
			}
		});
		feedback.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		feedback.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(feedback);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

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
