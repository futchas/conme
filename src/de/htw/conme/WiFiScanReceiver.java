package de.htw.conme;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WiFiScanReceiver extends BroadcastReceiver {

	private WifiManager wifi;
	
	public WiFiScanReceiver(WifiManager wifi) {
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
			
			if(network != null && network.startsWith("Blablu") /*&& network.length() == 7 && isWPA*/) {
				conMeNetworks.add(result);
				if (bestSignal == null || WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0)
					bestSignal = result;
			} else
				otherNetworks.add(result);
		}
		
//		scanResultToast(c, bestSignal.SSID, conMeNetworks.size(), otherNetworks.size());
		
		ConnectActivity conActivity = (ConnectActivity)c;
		conActivity.showNetworksInList(conMeNetworks,otherNetworks);
	}


	private void scanResultToast(Context c, String ssid, int conMeSize, int othersSize) {
		StringBuilder message = new StringBuilder(conMeSize + " ConMe networks found. ");
		if(conMeSize == 0) 
			message.append(String.format("Choose from on of the %s other networks.", othersSize));
		else if(conMeSize > 1) 
			message.append(ssid + " is the strongest.");
		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
	}

}
