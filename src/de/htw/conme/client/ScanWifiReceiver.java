package de.htw.conme.client;

import java.util.ArrayList;
import java.util.List;

import de.htw.conme.server.ShareActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ScanWifiReceiver extends BroadcastReceiver {

	private WifiManager wifi;
	
	public ScanWifiReceiver(WifiManager wifi) {
		super();
		this.wifi = wifi;
	}
	
	@Override
	public void onReceive(Context c, Intent intent) {

		//Won't find hidden networks 
		List<ScanResult> results = wifi.getScanResults();

		List<ScanResult> conMeNetworks = new ArrayList<ScanResult>();
		List<ScanResult> otherNetworks = new ArrayList<ScanResult>();
		
		ScanResult bestSignal = null;
		for (ScanResult result : results) {
			
			String network = result.SSID;
//			String cap = result.capabilities;
//			boolean isWPA = result.capabilities.contains("WPA");
			
			if(network != null && network.startsWith(ShareActivity.NETWORK_BRANDING) /*&& network.length() == 7 && isWPA*/) {
				conMeNetworks.add(result);
				if (bestSignal == null || WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0)
					bestSignal = result;
			} else
				otherNetworks.add(result);
		}
		
		ConnectActivity conActivity = (ConnectActivity)c;
		conActivity.showNetworksInList(conMeNetworks,otherNetworks);
	}

}
