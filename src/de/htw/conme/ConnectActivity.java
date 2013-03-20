package de.htw.conme;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ConnectActivity extends Activity {

	private WifiManager wifi;
	private BroadcastReceiver receiver;
	private ListView conMeNetworkList;
	private ListView otherNetworkList;
//	private NetChangedReceiver netChangedReceiver;
	private final int wifiScanInterval = 10000;
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		conMeNetworkList = (ListView) findViewById(R.id.conMeNetworkList);
		otherNetworkList = (ListView) findViewById(R.id.otherNetworkList);
		
		wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//		scanWifiNetworks();
		
		if (receiver == null)
			receiver = new WiFiScanReceiver(wifi);
		
//		if (netChangedReceiver == null)
//			netChangedReceiver = new NetChangedReceiver();
		
//		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//		registerReceiver(netChangedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
		
	}
	
	public void showNetworksInList(List<ScanResult> conMeNetworks, List<ScanResult> otherNetworks){
		
		WifiItemAdapter conMeAdapter = new WifiItemAdapter(this, conMeNetworks, wifi);
		conMeNetworkList.setAdapter(conMeAdapter); 

		WifiItemAdapter othersAdapter = new WifiItemAdapter(this, otherNetworks, wifi);
		otherNetworkList.setAdapter(othersAdapter);
		
		NetworkItemClicked networkClicked = new NetworkItemClicked(this, wifi, conMeNetworks);
//		// Click on a Conme network in the list will display a alert Dialog to connect/disconnect
		conMeNetworkList.setOnItemClickListener(networkClicked);
				
		// Click on other available networks in the list redirects to Wifi settings
		otherNetworkList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				//Just call Wifi settings
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
				
	}

	
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		timer.cancel();
//		unregisterReceiver(netChangedReceiver);
	}
	
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
//		scanWifiNetworks();
		
		timer = new Timer();
				
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				scanWifiNetworks();				
			}
		};
		//Run Task every 5 seconds
		timer.scheduleAtFixedRate(timerTask, 0, wifiScanInterval);

//		registerReceiver(netChangedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
//		registerReceiver(netChangedReceiver, new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
	}
	
	private void scanWifiNetworks() {
		
		//enable Wifi if it's disabled
		if(wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
			Toast.makeText(getApplicationContext(), "Wifi is disabled! It will be enabled now.",Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
		}
		
		wifi.startScan();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connect, menu);
		return true;
	}


}
