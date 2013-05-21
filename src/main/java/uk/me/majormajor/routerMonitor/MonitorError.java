package uk.me.majormajor.routerMonitor;

import java.net.HttpURLConnection;

public class MonitorError {
	public static final MonitorError ANOTHER_ADMINISTRATOR_LOGGED_IN = new MonitorError("Another administrator is already logged in");
	public static final MonitorError HTTP_UNAUTHORIZED = new MonitorError("Authentication error: Server returned 401 Unauthorized", HttpURLConnection.HTTP_UNAUTHORIZED);
	
	private final String description;
	private final int httpResponseCode;
	
	private MonitorError(String description)
	{
		this(description, -1);
	}
	
	private MonitorError(String description, int httpResponseCode)
	{
		this.description = description;
		this.httpResponseCode = httpResponseCode;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getHttpResponseCode()
	{
		return httpResponseCode;
	}
	
	public static MonitorError errorForException(Exception e)
	{
		return new MonitorError(e.getMessage());
	}
	
	public static MonitorError errorForResponseCode(int responseCode)
	{
		for (MonitorError error : new MonitorError[]{HTTP_UNAUTHORIZED})
		{
			if (error.getHttpResponseCode() == responseCode)
			{
				return error;
			}
		}
		return null;
	}
}
