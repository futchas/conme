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
import de.htw.conme.server.ServerService;
import de.htw.conme.server.ShareActivity;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class NetChangedReceiver extends BroadcastReceiver {

	private WifiManager wifi;
	private Client client;

	public NetChangedReceiver(Context context, WifiManager wifi) {
		this.wifi = wifi;
		client = new Client(context, wifi);
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		NetworkInfo netInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (netInfo.getState() == State.CONNECTED) {
        	String ssid = wifi.getConnectionInfo().getSSID();
        	if (ssid.startsWith(ShareActivity.NETWORK_BRANDING)) {
        		if(!client.isConnected()){
//    			if(client.getStatus() != AsyncTask.Status.RUNNING || client == null){
        			client.setConnected(true);
        			client.execute(ServerService.PORT);
        		}
        	}
        } else if (netInfo.getState() == State.DISCONNECTED) {
//        	client.cancel(true);
        	client.setConnected(false);
        }
	}
}
