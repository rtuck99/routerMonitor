package uk.me.majormajor.routerMonitor;

import java.util.prefs.Preferences;

import javax.net.ssl.SSLEngineResult.Status;

import uk.me.majormajor.routerMonitor.TrayMonitor.TrayStatus;

public class PrefsHelper {
	private static final String KEY_USER_NAME = "userName";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_IP_ADDRESS = "ipAddress";

	private static Preferences getParentNode()
	{
		return Preferences.userRoot().node("/uk/me/majormajor/routerMonitor");
	}
	
	public static void setUserName(String text) {
		getParentNode().put(KEY_USER_NAME, text);
	}

	public static void setPassword(String text) {
		getParentNode().put(KEY_PASSWORD, text);
	}

	public static String getUserName() {
		return getParentNode().get(KEY_USER_NAME, "admin");
	}

	public static String getPassword() {
		return getParentNode().get(KEY_PASSWORD, "sky");
	}
	
	public static float getThreshold(Stat stat, TrayStatus status)
	{
		float amberDefault = stat == Stat.STAT_DOWNSTREAM_KBPS ? 6000 : (stat == Stat.STAT_UPSTREAM_KBPS ? 256 :6);
		float def = status == TrayStatus.RED ? 0 : amberDefault;
		return getParentNode().node(stat.getKey()).getFloat(status.name(), def);
	}
	
	public static void setThreshold(Stat stat, TrayStatus status, float value)
	{
		getParentNode().node(stat.getKey()).putFloat(status.name(), value);
	}

	public static String getIPAddress() {
		return getParentNode().get(KEY_IP_ADDRESS, "192.168.0.1");
	}

	public static void setIPAddress(String text) {
		getParentNode().put(KEY_IP_ADDRESS, text);
	}
}
