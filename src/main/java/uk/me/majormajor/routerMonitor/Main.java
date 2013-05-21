package uk.me.majormajor.routerMonitor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.DatatypeConverter;


public class Main extends SwingWorker<Void, DataSample>{
	private static final String ROUTER_STATS_PATH = "/sky_system.html";
	private String userName = PrefsHelper.getUserName();
	private String password = PrefsHelper.getPassword();
	
	private List<DataSample> samples = new ArrayList<DataSample>();
	
	private MainWindow mainWindow;
	private TrayMonitor trayMonitor;
	
	public static void main(String[] args)
	{
		try {
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run() {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						// Never mind
					}
				}
			});
		} catch (Exception e) {
			// Never mind
		}
		new Main().run();
	}

	protected Void doInBackground()
	{
		try {
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run() {
					mainWindow = new MainWindow(Main.this);
					trayMonitor = TrayMonitor.tryStartTray(Main.this);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			while (!isCancelled() && ! Thread.interrupted())
			{
				try {
					HttpURLConnection connection = getConnection();
					Map<Stat, Object> stats = null;
					try
					{
						String s = getStatusDocument(connection);
						stats = extractStats(s);
					}
					catch (MonitorException e)
					{
						stats = new HashMap<Stat, Object>();
						if (e.getMonitorError() != null)
						{
							stats.put(Stat.STAT_ERROR_RETRIEVING_DATA, e.getMonitorError());
						}
					}
					publish(new DataSample(stats));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						Thread.currentThread().interrupt();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
			System.out.println("Was cancelled or interrupted!");
		}
		catch (Throwable t)
		{
			System.out.println("Caught unexpected exception: " + t);
			t.printStackTrace();
			throw t;
		}
		return null;
	}

	@Override
	protected void done() {
		if (mainWindow != null)
		{
			mainWindow.setVisible(false);
			mainWindow.dispose();
		}
	}

	private HttpURLConnection getConnection() throws IOException
	{
		HttpURLConnection connection;
			connection = (HttpURLConnection) new URL(getRouterURL()).openConnection();
		connection.setAllowUserInteraction(false);
		connection.setUseCaches(false);
		String authenticationString = userName + ":" + password;
		String base64encoded = DatatypeConverter.printBase64Binary(authenticationString.getBytes());
		String value = "Basic " + base64encoded;
		connection.setRequestProperty("Authorization", value);
		connection.setRequestMethod("GET");

		return connection;
		
	}

	private String getRouterURL() {
		return "http://" + PrefsHelper.getIPAddress() + ROUTER_STATS_PATH;
	}
	
	private static long parseDuration(String s)
	{
		String[] hhmmss = s.split(":");
		int secs = Integer.parseInt(hhmmss[hhmmss.length - 1]);
		int mins = Integer.parseInt(hhmmss[hhmmss.length - 2]);
		int hours = Integer.parseInt(hhmmss[hhmmss.length - 3]);
		return secs + mins * 60 + hours * 60 * 60;
	}
	
	private static long parseLong(String s)
	{
		try
		{
			return Long.parseLong(s);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
	
	private static float parseFloat(String s)
	{
		try
		{
			return Float.parseFloat(s);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	private Map<Stat, Object> extractStats(String document)
	{
		String[] lines = document.split("(\\r\\n|\\r|\\n)");
		Pattern wanStatsPat = Pattern.compile("var wanSts = '(.*)';");
		Pattern lanStatsPat = Pattern.compile("var lanSts = '(.*)';");
		Pattern wlanSts = Pattern.compile("var wlanSts = '(.*)';");
		Pattern dslStatsPat = Pattern.compile("var dslSts = '(.*)';");
		Map<Stat, Object> stats = new HashMap<Stat, Object>();
		
		for (Iterator<String> it = Arrays.asList(lines).iterator(); it.hasNext();)
		{
			String l = it.next();
			Matcher wsm = wanStatsPat.matcher(l);
			if (wsm.matches())
			{
				String[] wsmStats = wsm.group(1).split("_");
				stats.put(Stat.STAT_TX_PKTS, parseLong(wsmStats[1]));
				stats.put(Stat.STAT_RX_PKTS, parseLong(wsmStats[2]));
				stats.put(Stat.STAT_COLLISIONS, parseLong(wsmStats[3]));
				stats.put(Stat.STAT_TX_BYTES_PER_SEC, parseLong(wsmStats[4]));
				stats.put(Stat.STAT_RX_BYTES_PER_SEC, parseLong(wsmStats[5]));
				stats.put(Stat.STAT_UP_TIME, parseDuration(wsmStats[6]));
			}
			
			if (!stats.containsKey(Stat.STAT_DOWNSTREAM_KBPS))
			{
				if (l.matches(".*<th>Connection Speed</th>.*"))
				{
					Pattern p = Pattern.compile("\\s*<td>(\\d*)&nbsp;kbps</td>.*");
					Matcher m = p.matcher(it.next()); 
					if (m.matches())
					{
						stats.put(Stat.STAT_DOWNSTREAM_KBPS, parseLong(m.group(1)));
					}
					m = p.matcher(it.next());
					if (m.matches())
					{
						stats.put(Stat.STAT_UPSTREAM_KBPS, parseLong(m.group(1)));
					}
				}
			}
			
			Matcher m = dslStatsPat.matcher(l); 
			if (m.matches())
			{
				String[] dslStats = m.group(1).split("_");
				stats.put(Stat.STAT_ATTENUATION_DOWNSTREAM, parseFloat(dslStats[3]));
				stats.put(Stat.STAT_ATTENUATION_UPSTREAM, parseFloat(dslStats[4]));
				stats.put(Stat.STAT_NOISE_MARGIN_DOWNSTREAM, parseFloat(dslStats[5]));
				stats.put(Stat.STAT_NOISE_MARGIN_UPSTREAM, parseFloat(dslStats[6]));
			}
		}
		return stats;
	}
	
	private String getStatusDocument(HttpURLConnection connection) throws MonitorException
	{
		String s = "Error: failed to read content";
		try {
			connection.connect();

			int status = connection.getResponseCode();

			if (status == HttpURLConnection.HTTP_UNAUTHORIZED &&
					connection.getHeaderFields().containsKey("WWW-Authenticate") &&
					connection.getHeaderField("WWW-Authenticate").startsWith("Basic Realm"))
			{	// retry to become authorized
				connection.disconnect();
				connection = getConnection();
				connection.connect();
				status = connection.getResponseCode();
				System.out.println("Retried and got code " + status);
			}
			if (status != HttpURLConnection.HTTP_OK)
			{
				throw new MonitorException(MonitorError.errorForResponseCode(status));
			}
			InputStream is = (InputStream) connection.getContent();

			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[4096];
				while (0 < (bytesRead = is.read(buffer))) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				s = bos.toString();
			} finally {
				is.close();
			}
		}
		catch (IOException e)
		{
			throw new MonitorException(MonitorError.errorForException(e));
		}
		finally
		{
			connection.disconnect();
		}
		return s;
	}

	@Override
	protected void process(List<DataSample> chunks) {
		samples.addAll(chunks);
		if (mainWindow != null)
		{
			mainWindow.refreshStatChooser();
			mainWindow.repaint();
		}
		if (trayMonitor != null)
		{
			trayMonitor.refreshStatus();
		}
	}

	public List<DataSample> getSamples() {
		return samples;
	}

	public void mainWindowsClosed() {
		if (trayMonitor == null)
		{
			cancel(true);
		}
		mainWindow = null;
	}	

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void showMainWindow() {
		if (mainWindow == null)
		{
			mainWindow = new MainWindow(this);
		}
		mainWindow.setVisible(true);
	}

	public void setUserNameAndPassword(String aUserName, String aPassword) {
		userName = aUserName;
		password = aPassword;
	}
}

