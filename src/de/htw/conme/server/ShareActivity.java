/**
 * 
 */
package de.htw.conme.server;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.htw.conme.ChangeAPState;
import de.htw.conme.ConMeUtils;
import de.htw.conme.R;
import de.htw.conme.WifiConfig;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ShareActivity extends Activity {

	private WifiConfig wifiConfig;
	private WifiApManager wifiApManager;
	private ToggleButton toggleAPButton;
	private Intent serverIntent;
	private String ssid;
	private ClientListUpdater clientListUpdater;
	private boolean isServiceRunning;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		toggleAPButton = (ToggleButton) findViewById(R.id.toggleAP);
		
		wifiApManager = new WifiApManager(this);
		
		// random hex values (8 values)
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		ssid = ConMeUtils.NETWORK_BRANDING + uuid;
		try {
			wifiConfig = new WifiConfig(ssid, uuid, true);
		} catch (Exception e) {
			Toast.makeText(this,"Automatic Wifi configurations couldn't be set!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
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
				
				serverIntent = new Intent(this, ServerService.class);
//				serverIntent.putExtra("androidID", androidID);
				startService(serverIntent);
				
				
			}else if(isServiceRunning){
				clientListUpdater.cleanup();
				unregisterReceiver(clientListUpdater);
				stopService(serverIntent);
				isServiceRunning = false;
				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_share, menu);
		return true;
	}

	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}

	public void toggleAP(View view) {
		
		boolean isChecked = ((ToggleButton) view).isChecked();
		
		ChangeAPState changeApState = new ChangeAPState(this, wifiApManager, isChecked);
		changeApState.execute(wifiConfig);
	}

}
