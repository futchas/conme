/**
 * 
 */
package de.htw.conme.server;

import java.util.ArrayList;


import android.os.AsyncTask;
import android.widget.TextView;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ClientListTask extends AsyncTask<Void, Void, ArrayList<ClientScanResult>> {
	
	private WifiApManager wifiApManager;
	private TextView listConnectedClients;

	public ClientListTask(WifiApManager wifiApManager, TextView listConnectedClients) {
		this.wifiApManager = wifiApManager;
		this.listConnectedClients = listConnectedClients;
	}

	@Override
	protected ArrayList<ClientScanResult> doInBackground(Void... params) {

		ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);
		return clients;
	}

	@Override
	protected void onPostExecute(ArrayList<ClientScanResult> clients) {
		super.onPostExecute(clients);
		
		listConnectedClients.setText("Connected Client: " + clients.size() + "\n\n");

		for (ClientScanResult clientScanResult : clients) {
//			textView1.append("####################\n");
			listConnectedClients.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
			listConnectedClients.append("Device: " + clientScanResult.getDevice() + "\n");
			listConnectedClients.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
			listConnectedClients.append("isReachable: " + clientScanResult.isReachable() + "\n");
		}
		
	}

	
}
