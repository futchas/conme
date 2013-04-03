/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

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
import de.htw.conme.ChangedAPState;
import de.htw.conme.R;
import de.htw.conme.server.WifiApManager;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ConnectActivity extends Activity {

	private WifiManager wifi;
	private BroadcastReceiver receiver;
	private ListView conMeNetworkList;
	private ListView otherNetworkList;
	private NetChangedReceiver netChangedReceiver;
	private final int WIFI_SCAN_INTERVALS = 10000;
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		conMeNetworkList = (ListView) findViewById(R.id.conMeNetworkList);
		otherNetworkList = (ListView) findViewById(R.id.otherNetworkList);
		
		wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		
		if (receiver == null)
			receiver = new ScanWifiReceiver(wifi);
		
		if (netChangedReceiver == null) {
			netChangedReceiver = new NetChangedReceiver(this, wifi);
			registerReceiver(netChangedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
		}
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

	
	protected void onPause() {
		super.onPause();

		if(receiver != null)
			unregisterReceiver(receiver);
		if(timer != null)
			timer.cancel();
	}
	
	protected void onResume() {
		super.onResume();
		
		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
		WifiApManager accessPointManager = new WifiApManager(wifi);
		
		//Unfortunately it's not possible to tether and use Wifi simultaneously.
		//disable access point if it's enabled
		if(accessPointManager.isWifiApEnabled()) {
			ChangedAPState changeApState = new ChangedAPState(this, accessPointManager, false);
			changeApState.execute();
		}else
			onResumeFurther();
	}
	
	public void onResumeFurther() {
		//enable Wifi if it's disabled
		if(wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
			Toast.makeText(getApplicationContext(), "Wifi is disabled! It will be enabled now.",Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
		}
		
		timer = new Timer();
			
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				wifi.startScan();			
			}
		};
		
		//Run Task every configured interval in ms
		timer.scheduleAtFixedRate(timerTask, 0, WIFI_SCAN_INTERVALS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connect, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(netChangedReceiver != null)
			unregisterReceiver(netChangedReceiver);
	}

	
	

}
