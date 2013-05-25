package uk.me.majormajor.routerMonitor;

public enum Stat {
	STAT_UPSTREAM_KBPS("UpstreamKbps", "Line Speed Upstream (kbps)", Number.class, 0.0f),
	STAT_DOWNSTREAM_KBPS("DownstreamKbps", "Line Speed Downstream (kbps)", Number.class, 0.0f),
	STAT_NOISE_MARGIN_UPSTREAM("noiseMarginUpstream", "Noise Margin Upstream (dB)", Number.class, 0.0f),
	STAT_NOISE_MARGIN_DOWNSTREAM("noiseMarginDownstream", "Noise Margin Downstream (dB)", Number.class, 0.0f),
	STAT_ATTENUATION_UPSTREAM("attenuationUpstream", "Line Attenuation Upstream (dB)", Number.class, 0.0f),
	STAT_ATTENUATION_DOWNSTREAM("attenuationDownstream", "Line Attenuation Downstream (dB)", Number.class, 0.0f),
	STAT_UP_TIME("UpTime", "WAN Uptime (s)", Number.class, Float.NaN),
	STAT_RX_BYTES_PER_SEC("RxBytesPerSec", "WAN Rx (Bytes/s)", Number.class, 0.0f),
	STAT_TX_BYTES_PER_SEC("TxBytesPerSec", "WAN Tx (Bytes/s)", Number.class, 0.0f),
	STAT_COLLISIONS("Collisions", "WAN Collisions", Number.class, 0.0f),
	STAT_RX_PKTS("RxPkts", "WAN Rx (Pkts)", Number.class, Float.NaN),
	STAT_TX_PKTS("TxPkts", "WAN Tx (Pkts)", Number.class, Float.NaN),
	STAT_ERROR_RETRIEVING_DATA("ErrorRetrievingData", "Error Retrieving Data", MonitorError.class, Float.NaN)
	;
	private final String key;
	private final String description;
	private final Class<?> dataType;
	private final float minimumChartValue;

	private Stat(String key, String description, Class<?> dataType, float minimumChartValue)
	{
		this.key = key;
		this.description = description;
		this.dataType = dataType;
		this.minimumChartValue = minimumChartValue;
	}

	public String getKey() {
		return key;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Class<?> getDataType()
	{
		return dataType;
	}
	
	/** Get the minimum chart value. NaN means the chart minimum is auto-scaled. */
	public float getMinimumChartValue()
	{
		return minimumChartValue;
	}
}
