package uk.me.majormajor.routerMonitor;
import java.util.Date;
import java.util.Map;


public class DataSample {
	private Map<Stat, Object> data;
	private long timestamp;
	
	public DataSample(Map<Stat, Object> data) {
		super();
		this.data = data;
		this.timestamp = new Date().getTime();
	}

	public Map<Stat, Object> getData() {
		return data;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public String getAsString(Stat stat)
	{
		Number n = (Number) data.get(stat);
		if (n == null)
		{
			return "0";
		}
		else
		{
			return String.format("%.2f", n.floatValue());
		}
	}
}
