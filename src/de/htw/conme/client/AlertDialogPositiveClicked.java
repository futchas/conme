/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import de.htw.conme.WifiConfig;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class AlertDialogPositiveClicked implements OnClickListener {

	private boolean isConnected;
	private String ssid;
	private Context context;
	private WifiManager wifi;
	private TextView wifiDetails;

	/**
	 * @param context 
	 * @param wifi 
	 * @param ssid 
	 * @param isConnected 
	 * @param wifiDetails 
	 * 
	 */
	public AlertDialogPositiveClicked(Context context, WifiManager wifi, String ssid, boolean isConnected, TextView wifiDetails) {
		this.context = context;
		this.wifi = wifi;
		this.isConnected = isConnected;
		this.ssid = ssid;
		this.wifiDetails = wifiDetails;
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		if(isConnected) {
			disconnect();
		} else {
//			String key = "f409!k23c#d.92" + ssid.substring(5, 15);
			// random hex values (8 values)
			String uuid = ssid.substring(ssid.lastIndexOf('|') + 1);
			
			WifiConfig wifiConfig;
			try {
				wifiConfig = new WifiConfig(ssid, uuid, false);
				
				ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(wifiInfo.isConnectedOrConnecting()) {
					disconnect();
				}
				
				int netId = wifi.addNetwork(wifiConfig);
				wifi.enableNetwork(netId, true);
				wifi.reconnect();
				
				wifiDetails.setText("Connecting");
			    Toast.makeText(context,"You are connecting to " + ssid, Toast.LENGTH_SHORT).show();
			    
			} catch (Exception e) {
				Toast.makeText(context,"Could not be connected!" + ssid, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			
		}

	}
	
	private void disconnect() {
		wifi.disconnect();
		wifiDetails.setText("Disconnected");
		Toast.makeText(context,"You are disconnecting from the current wifi network!", Toast.LENGTH_SHORT).show();
	}

}
