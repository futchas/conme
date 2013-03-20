package de.htw.conme;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ShareActivity extends Activity {

	private WifiConfig wifiConfig;
	private WifiApManager wifiApManager;
	private TextView listConnectedClients;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		listConnectedClients = (TextView) findViewById(R.id.listConnectedClients);
		
		wifiApManager = new WifiApManager(this);
		wifiConfig = new WifiConfig(true);
		
		scanConnectedClients();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_share, menu);
		return true;
	}

	private void scanConnectedClients() {
		
//		Log.i("Hotspot Info", "mWifiManager.getDhcpInfo().gateway: " + wifiApManager.getmWifiManager().getDhcpInfo().gateway);
//		Log.i("Hotspot Info", "mWifiManager.getDhcpInfo().dns1: " + wifiApManager.getmWifiManager().getDhcpInfo().dns1);
//		Log.i("Hotspot Info", "mWifiManager.getDhcpInfo().dns2: " + wifiApManager.getmWifiManager().getDhcpInfo().dns2);
		
		ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);
		
		listConnectedClients.setText(clients.size() + " " + getString(R.string.txt_clients) + "\n\n");

//		listConnectedClients.append("Clients: \n");
		for (ClientScanResult clientScanResult : clients) {
			//ip = clientScanResult.getIpAddr();
//			textView1.append("####################\n");
			listConnectedClients.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
//			textView1.append("Device: " + clientScanResult.getDevice() + "\n");
//			textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
//			textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
		}
		
//		textView1.append("gateway: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().gateway) + "\n");
//		textView1.append("dns1: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().dns1) + "\n");
//		textView1.append("dns2: " + intToIp(wifiApManager.getmWifiManager().getDhcpInfo().dns2) + "\n");
//		
//		textView1.append("wifiApManager.getWifiApConfiguration().SSID: " + wifiApManager.getWifiApConfiguration().SSID+ "\n");
//		textView1.append("wifiConfig.SSID: " + wifiConfig.SSID + "\n");
	}
	
	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}

	public void toggleAP(View view) {
		boolean on = ((ToggleButton) view).isChecked();
		if(on)
			wifiApManager.setWifiApEnabled(wifiConfig, true);
		else
			wifiApManager.setWifiApEnabled(wifiConfig, false);
	}


}
