package uk.me.majormajor.routerMonitor;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;

public class TrayMonitor {
	private Main main;
	private TrayIcon trayIcon;
	
	private final class ShowMonitorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			main.showMainWindow();
		}
	}

	public enum TrayStatus
	{
		NOT_CONNECTED,
		GREEN,
		AMBER,
		RED
	}
	
	private TrayMonitor(Main aMain)
	{
		this.main = aMain;
		SystemTray systemTray = SystemTray.getSystemTray();
		trayIcon = new TrayIcon(getImage(getStatus()));
		trayIcon.setImageAutoSize(true);
		PopupMenu trayPopup = new PopupMenu();
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.cancel(true);
				SystemTray.getSystemTray().remove(trayIcon);
			}
		});
		trayPopup.add(exitMenuItem);
		MenuItem showMonitorMenuItem = new MenuItem("Show Monitor");
		showMonitorMenuItem.addActionListener(new ShowMonitorAction());
		trayIcon.addActionListener(new ShowMonitorAction());
		trayPopup.add(showMonitorMenuItem);
		
		MenuItem settingsMenuItem = new MenuItem("Settings...");
		settingsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsDialog(main).setVisible(true);
			}
		});
		trayPopup.add(settingsMenuItem);
		
		trayIcon.setPopupMenu(trayPopup);
		trayIcon.setToolTip("Router Monitor");
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private TrayStatus getStatus() {
		List<DataSample> samples = main.getSamples();
		if (samples.isEmpty())
		{
			return TrayStatus.NOT_CONNECTED;
		}
		DataSample mostRecent = samples.get(samples.size() - 1);
		
		if (mostRecent.getData().containsKey(Stat.STAT_ERROR_RETRIEVING_DATA))
		{
			return TrayStatus.NOT_CONNECTED;
		}
		
		TrayStatus status = status((Number)mostRecent.getData().get(Stat.STAT_NOISE_MARGIN_DOWNSTREAM), Stat.STAT_NOISE_MARGIN_DOWNSTREAM, null); 
		status = status((Number)mostRecent.getData().get(Stat.STAT_NOISE_MARGIN_UPSTREAM), Stat.STAT_NOISE_MARGIN_UPSTREAM,  status); 
		status = status((Number)mostRecent.getData().get(Stat.STAT_DOWNSTREAM_KBPS), Stat.STAT_DOWNSTREAM_KBPS,  status); 
		status = status((Number)mostRecent.getData().get(Stat.STAT_UPSTREAM_KBPS), Stat.STAT_UPSTREAM_KBPS,  status); 
		
		return status;
	}

	private TrayStatus status(Number n, Stat stat, TrayStatus currentStatus)
	{
		if (n == null)
		{
			return currentStatus;
		}
		if (currentStatus == TrayStatus.RED ||
				n.floatValue() <= PrefsHelper.getThreshold(stat, TrayStatus.RED))
		{
			return TrayStatus.RED;
		}
		if (currentStatus == TrayStatus.AMBER ||
				n.floatValue() <= PrefsHelper.getThreshold(stat, TrayStatus.AMBER))
		{
			return TrayStatus.AMBER;
		}
		return TrayStatus.GREEN;
	}
	
	private Image getImage(TrayStatus status)
	{
		String resourceName;
		switch (status)
		{
		case AMBER:
			resourceName = "statusAmber.png";
			break;
		case NOT_CONNECTED:
			resourceName = "statusBlack.png";
			break;
		case GREEN:
			resourceName = "statusGreen.png";
			break;
		case RED:
		default:
			resourceName = "statusRed.png";
			break;
		}
		ImageIcon icon = new ImageIcon(getClass().getResource(resourceName));
		return icon.getImage();
	}
	
	public static TrayMonitor tryStartTray(Main main)
	{
		if (SystemTray.isSupported())
		{
			return new TrayMonitor(main);
		}
		return null;
	}

	public void refreshStatus() {
		trayIcon.setImage(getImage(getStatus()));
		DataSample ds = main.getSamples().isEmpty() ? null : main.getSamples().get(main.getSamples().size() - 1);
		String tt = "Router Monitor";
		if (ds != null)
		{
			if (ds.getData().containsKey(Stat.STAT_ERROR_RETRIEVING_DATA))
			{
				tt += " \r\n" + ((MonitorError)ds.getData().get(Stat.STAT_ERROR_RETRIEVING_DATA)).getDescription();
			}
			else
			{
				String snrUp = ds.getAsString(Stat.STAT_NOISE_MARGIN_UPSTREAM);
				String snrDown = ds.getAsString(Stat.STAT_NOISE_MARGIN_DOWNSTREAM);
				String lineSpeed = ds.getAsString(Stat.STAT_DOWNSTREAM_KBPS);
				tt += " \r\nLine Speed:" + lineSpeed + "kbps \r\nSNR Down:" + snrDown + "dB \r\nSNR Up:" + snrUp + "dB";
			}
		}
		trayIcon.setToolTip(tt);
	}
}
