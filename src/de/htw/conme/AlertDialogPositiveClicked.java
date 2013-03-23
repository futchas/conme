/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme;

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
			String key = "f409!k23c#d.92" + ssid.substring(5, 15);
//			String key = "badabum" + ssid.substring(8, 12);
			WifiConfig wifiConfig = new WifiConfig(ssid, key, false);

			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(wifiInfo.isConnectedOrConnecting()) {
				disconnect();
			}
			
			int netId = wifi.addNetwork(wifiConfig);
			wifi.enableNetwork(netId, true);
			wifi.reconnect();
			
			wifiDetails.setText("Connecting");
		    Toast.makeText(context,"You are connecting to following wifi network: " + ssid, Toast.LENGTH_SHORT).show();
		}
		
		wifi.startScan();

	}
	
	private void disconnect() {
		wifi.disconnect();
		wifiDetails.setText("Disconnected");
		Toast.makeText(context,"You are disconnecting from the current wifi network!", Toast.LENGTH_SHORT).show();
	}

}
