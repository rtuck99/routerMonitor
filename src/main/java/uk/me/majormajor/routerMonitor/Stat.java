package uk.me.majormajor.routerMonitor;

public enum Stat {
	STAT_UPSTREAM_KBPS("UpstreamKbps", "Line Speed Upstream (kbps)"),
	STAT_DOWNSTREAM_KBPS("DownstreamKbps", "Line Speed Downstream (kbps)"),
	STAT_NOISE_MARGIN_UPSTREAM("noiseMarginUpstream", "Noise Margin Upstream (dB)"),
	STAT_NOISE_MARGIN_DOWNSTREAM("noiseMarginDownstream", "Noise Margin Downstream (dB)"),
	STAT_ATTENUATION_UPSTREAM("attenuationUpstream", "Line Attenuation Upstream (dB)"),
	STAT_ATTENUATION_DOWNSTREAM("attenuationDownstream", "Line Attenuation Downstream (dB)"),
	STAT_UP_TIME("UpTime", "WAN Uptime (s)"),
	STAT_RX_BYTES_PER_SEC("RxBytesPerSec", "WAN Rx (Bytes/s)"),
	STAT_TX_BYTES_PER_SEC("TxBytesPerSec", "WAN Tx (Bytes/s)"),
	STAT_COLLISIONS("Collisions", "WAN Collisions"),
	STAT_RX_PKTS("RxPkts", "WAN Rx (Pkts)"),
	STAT_TX_PKTS("TxPkts", "WAN Tx (Pkts)")
	;
	private final String key;
	private final String description;

	private Stat(String key, String description)
	{
		this.key = key;
		this.description = description;
	}

	public String getKey() {
		return key;
	}
	
	public String getDescription()
	{
		return description;
	}
}
