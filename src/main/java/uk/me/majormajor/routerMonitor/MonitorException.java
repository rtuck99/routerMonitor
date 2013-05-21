package uk.me.majormajor.routerMonitor;

public class MonitorException extends Exception {
	private MonitorError error;
	
	public MonitorException(MonitorError error)
	{
		this.error = error;
	}

	@Override
	public String getMessage() {
		return error.getDescription();
	}
	
	public MonitorError getMonitorError()
	{
		return error;
	}
	
}
