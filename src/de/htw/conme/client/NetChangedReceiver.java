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
import android.view.View;
import de.htw.conme.ConMeUtils;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class NetChangedReceiver extends BroadcastReceiver {

	private WifiManager wifi;
	private Client client;
	private boolean isRunning;

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

        	if (ConMeUtils.isConMeNetwork(ssid)) {
        		
        		ConnectActivity conActivity = ((ConnectActivity) context);
        				
        		conActivity.createNewConnectionDetails();
        		View conDetailsView = conActivity.getConnectionDetails();
        		client = new Client(context, wifi, conDetailsView);
        		client.execute();
        		
//				clientIntent = new Intent(context, ClientService.class);
//				clientIntent.putExtra("gateway", wifi.getDhcpInfo().gateway);
//				context.startService(clientIntent);
				
				isRunning = true;
        	}
        	wifi.startScan();
        	
        } else if (netInfo.getState() == State.DISCONNECTED) {

        	if(isRunning) {
	        	client.stop();
	        	client.cancel(true);
//        		context.stopService(clientIntent);
        		isRunning = false;
        	}
        	
        	wifi.startScan();

        }
	}
}
