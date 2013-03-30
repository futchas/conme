/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import de.htw.conme.server.Server;
import de.htw.conme.server.ShareActivity;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class NetChangedReceiver extends BroadcastReceiver {

	private WifiManager wifi;
	private Client client;

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
        	String ssid = wifi.getConnectionInfo().getSSID();
        	if (ssid.startsWith("WLAN-")) {
        		client = new Client(context, wifi);
        		client.execute(Server.PORT);
        	}
        } else if (netInfo.getState() == State.DISCONNECTED && client != null) {
        	client.setConnected(false);
        }
	}
}
