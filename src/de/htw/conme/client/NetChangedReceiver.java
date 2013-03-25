/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class NetChangedReceiver extends BroadcastReceiver {

	private WifiManager wifi;

	/**
	 * 
	 */
	public NetChangedReceiver(WifiManager wifi) {
		this.wifi = wifi;
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		NetworkInfo netInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (netInfo.getState() == State.CONNECTED) {
        	WifiInfo wifiInfo = wifi.getConnectionInfo();
        	String ssid = wifiInfo.getSSID();
        	if (ssid.startsWith("WLAN-")) {
        		Client client = new Client(context, wifi);
        		client.execute(3333);
        	}
        }
//          notifyWifiState();

	}

}
