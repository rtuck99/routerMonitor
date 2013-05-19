package uk.me.majormajor.routerMonitor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SettingsDialog extends JDialog {
	private Main main;
	private JTextField userNameField;
	private JPasswordField passwordField;
	public SettingsDialog(Main aMain)
	{
		super((Window)null, "Settings");
		this.main = aMain;
		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(new JLabel("User name:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(new JLabel("Password:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		userNameField = new JTextField(20);
		userNameField.setText(PrefsHelper.getUserName());
		mainPanel.add(userNameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		passwordField = new JPasswordField(20);
		passwordField.setText(PrefsHelper.getPassword());
		mainPanel.add(passwordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		Box okCancelPanel = new Box(BoxLayout.X_AXIS);
		okCancelPanel.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("Ok");
		okCancelPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrefsHelper.setUserName(userNameField.getText());
				String password = new String(passwordField.getPassword());
				PrefsHelper.setPassword(password);
				main.setUserNameAndPassword(userNameField.getText(), password);
				setVisible(false);
				dispose();
			}
		});
		
		okCancelPanel.add(Box.createHorizontalStrut(4));
		JButton cancelButton = new JButton("Cancel");
		okCancelPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
		
		pack();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
