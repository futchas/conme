/**
 * 
 */
package de.htw.conme.server;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;
import de.htw.conme.ChangeAPState;
import de.htw.conme.R;
import de.htw.conme.WifiConfig;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ShareActivity extends Activity {

	private WifiConfig wifiConfig;
	private WifiApManager wifiApManager;
//	private TextView listConnectedClients;
	private ToggleButton toggleAPButton;
	private Intent serverIntent;
	private String ssid;
	private ClientListUpdater clientListUpdater;
	private boolean isServiceRunning;
	public static final String NETWORK_BRANDING = "WLAN||"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
//		listConnectedClients = (TextView) findViewById(R.id.listConnectedClients);
		toggleAPButton = (ToggleButton) findViewById(R.id.toggleAP);
		
		wifiApManager = new WifiApManager(this);
		
		// generate uniqueNumber
		//SSID = "WLAN-" + uniqueNumber
		//KEY = encrypt(uniqueNumber);
		
		ssid = NETWORK_BRANDING + "T3XH7UW2B5";
		String key = "f409!k23c#d.92" + ssid.substring(5, 15);
		wifiConfig = new WifiConfig(ssid, key, true);
		
		if (clientListUpdater == null)
			clientListUpdater = new ClientListUpdater(this);
		
		isServiceRunning = false;
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();

	}


	@Override
	protected void onResume() {
		super.onResume();
		
		boolean isWifiApEnabled = wifiApManager.isWifiApEnabled();

		toggleAPButton.setChecked(isWifiApEnabled);
		showConnectedClients(true, isWifiApEnabled);
	}



	public void showConnectedClients(boolean hasAPStateChanged, boolean isAPEnabled) {
		
		//if AP couldn't be enabled/disabled then toggle back the button
		if(!hasAPStateChanged) 
			toggleAPButton.setChecked(!toggleAPButton.isChecked());
		else {
			if(isAPEnabled) {
				
				registerReceiver(clientListUpdater, new IntentFilter("de.htw.conme.UPDATE_CLIENT_LIST"));
				isServiceRunning = true;
				
//				String androidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
				
				serverIntent = new Intent(this, ServerService.class);
//				serverIntent.putExtra("androidID", androidID);
				startService(serverIntent);
				
				
			}else if(isServiceRunning){
				clientListUpdater.cleanup();
				unregisterReceiver(clientListUpdater);
				stopService(serverIntent);
				isServiceRunning = false;
				
//				listConnectedClients.setText("");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_share, menu);
		return true;
	}

//	private void loadConnectedClients() {
//		
//		ClientListTask clientIPTask = new ClientListTask(wifiApManager, listConnectedClients);
//		clientIPTask.execute();
//		
////		textView1.append("gateway: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().gateway) + "\n");
////		textView1.append("dns1: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().dns1) + "\n");
////		textView1.append("dns2: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().dns2) + "\n");
////		
////		textView1.append("wifiApManager.getWifiApConfiguration().SSID: " + wifiApManager.getWifiApConfiguration().SSID+ "\n");
////		textView1.append("wifiConfig.SSID: " + wifiConfig.SSID + "\n");
//	}
	
	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}

	public void toggleAP(View view) {
		
		boolean isChecked = ((ToggleButton) view).isChecked();
		
//		if(!isChecked && serverSocket != null)
//			serverSocket.closeServer();
		
		ChangeAPState changeApState = new ChangeAPState(this, wifiApManager, isChecked);
		changeApState.execute(wifiConfig);
	}


}
