package uk.me.majormajor.routerMonitor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import uk.me.majormajor.routerMonitor.TrayMonitor.TrayStatus;

public class SettingsDialog extends JDialog {
	private Main main;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private SpinnerNumberModel downKbpsRedModel;
	private SpinnerNumberModel downKbpsAmberModel;
	private SpinnerNumberModel downNoiseRedModel;
	private SpinnerNumberModel downNoiseAmberModel;
	private SpinnerNumberModel upNoiseRedModel;
	private SpinnerNumberModel upNoiseAmberModel;
	private SpinnerNumberModel upKbpsAmberModel;
	private SpinnerNumberModel upKbpsRedModel;
	private JTextField ipAddressField;
	
	public SettingsDialog(Main aMain)
	{
		super((Window)null, "Settings");
		this.main = aMain;
		setLayout(new BorderLayout(4, 4));
		Box mainPanel = new Box(BoxLayout.Y_AXIS);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(4,  4,  4, 4));
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder("Authentication"));
		topPanel.setLayout(new GridBagLayout());
		topPanel.add(new JLabel("Router IP:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		topPanel.add(new JLabel("User name:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		topPanel.add(new JLabel("Password:"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		ipAddressField = new JTextField(20);
		ipAddressField.setText(PrefsHelper.getIPAddress());
		topPanel.add(ipAddressField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		userNameField = new JTextField(20);
		userNameField.setText(PrefsHelper.getUserName());
		topPanel.add(userNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		passwordField = new JPasswordField(20);
		passwordField.setText(PrefsHelper.getPassword());
		topPanel.add(passwordField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(topPanel);
		
		mainPanel.add(thresholdPanel());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		Box okCancelPanel = new Box(BoxLayout.X_AXIS);
		okCancelPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		okCancelPanel.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("Ok");
		okCancelPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrefsHelper.setIPAddress(ipAddressField.getText());
				PrefsHelper.setUserName(userNameField.getText());
				String password = new String(passwordField.getPassword());
				PrefsHelper.setPassword(password);
				main.setUserNameAndPassword(userNameField.getText(), password);
				PrefsHelper.setThreshold(Stat.STAT_DOWNSTREAM_KBPS, TrayStatus.RED, ((Number)(downKbpsRedModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_DOWNSTREAM_KBPS, TrayStatus.AMBER, ((Number)(downKbpsAmberModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_UPSTREAM_KBPS, TrayStatus.RED, ((Number)(upKbpsRedModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_UPSTREAM_KBPS, TrayStatus.AMBER, ((Number)(upKbpsAmberModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_NOISE_MARGIN_DOWNSTREAM, TrayStatus.RED, ((Number)(downNoiseRedModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_NOISE_MARGIN_DOWNSTREAM, TrayStatus.AMBER, ((Number)(downNoiseAmberModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_NOISE_MARGIN_UPSTREAM, TrayStatus.RED, ((Number)(upNoiseRedModel.getValue())).floatValue());
				PrefsHelper.setThreshold(Stat.STAT_NOISE_MARGIN_UPSTREAM, TrayStatus.AMBER, ((Number)(upNoiseAmberModel.getValue())).floatValue());
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
	
	private JPanel thresholdPanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Status Thresholds"));
		Insets insets = new Insets(4, 4, 4, 4);
		panel.add(new JLabel("Red"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		panel.add(new JLabel("Amber"), new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		
		panel.add(new JLabel(Stat.STAT_DOWNSTREAM_KBPS.getDescription()), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));
		downKbpsRedModel = new SpinnerNumberModel((int)PrefsHelper.getThreshold(Stat.STAT_DOWNSTREAM_KBPS, TrayStatus.RED), 0, 1000000, 100);
		downKbpsAmberModel = new SpinnerNumberModel((int)PrefsHelper.getThreshold(Stat.STAT_DOWNSTREAM_KBPS, TrayStatus.AMBER), 0, 1000000, 100);
		panel.add(new JSpinner(downKbpsRedModel),  new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(new JSpinner(downKbpsAmberModel),  new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));

		panel.add(new JLabel(Stat.STAT_UPSTREAM_KBPS.getDescription()), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));
		upKbpsRedModel = new SpinnerNumberModel((int)PrefsHelper.getThreshold(Stat.STAT_UPSTREAM_KBPS, TrayStatus.RED), 0, 1000000, 64);
		upKbpsAmberModel = new SpinnerNumberModel((int)PrefsHelper.getThreshold(Stat.STAT_UPSTREAM_KBPS, TrayStatus.AMBER), 0, 1000000, 64);
		panel.add(new JSpinner(upKbpsRedModel),  new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(new JSpinner(upKbpsAmberModel),  new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		panel.add(new JLabel(Stat.STAT_NOISE_MARGIN_DOWNSTREAM.getDescription()), new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));
		downNoiseRedModel = new SpinnerNumberModel(PrefsHelper.getThreshold(Stat.STAT_NOISE_MARGIN_DOWNSTREAM, TrayStatus.RED), 0.0, 1000000.0, 1.0);
		downNoiseAmberModel = new SpinnerNumberModel(PrefsHelper.getThreshold(Stat.STAT_NOISE_MARGIN_DOWNSTREAM, TrayStatus.AMBER), 0.0, 1000000.0, 1.0);
		panel.add(new JSpinner(downNoiseRedModel),  new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(new JSpinner(downNoiseAmberModel),  new GridBagConstraints(2, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		panel.add(new JLabel(Stat.STAT_NOISE_MARGIN_UPSTREAM.getDescription()), new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));
		upNoiseRedModel = new SpinnerNumberModel(PrefsHelper.getThreshold(Stat.STAT_NOISE_MARGIN_UPSTREAM, TrayStatus.RED), 0.0, 1000000.0, 1.0);
		upNoiseAmberModel = new SpinnerNumberModel(PrefsHelper.getThreshold(Stat.STAT_NOISE_MARGIN_UPSTREAM, TrayStatus.AMBER), 0.0, 1000000.0, 1.0);
		panel.add(new JSpinner(upNoiseRedModel),  new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(new JSpinner(upNoiseAmberModel),  new GridBagConstraints(2, 4, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		return panel;
	}
}
