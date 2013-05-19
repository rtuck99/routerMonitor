package uk.me.majormajor.routerMonitor;

import java.util.prefs.Preferences;

public class PrefsHelper {
	private static Preferences getParentNode()
	{
		return Preferences.userRoot().node("/uk/me/majormajor/routerMonitor");
	}
	
	public static void setUserName(String text) {
		getParentNode().put("userName", text);
	}

	public static void setPassword(String text) {
		getParentNode().put("password", text);
	}

	public static String getUserName() {
		return getParentNode().get("userName", "admin");
	}

	public static String getPassword() {
		return getParentNode().get("password", "sky");
	}
}
