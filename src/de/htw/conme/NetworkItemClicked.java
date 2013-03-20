/**
 * 
 */
package de.htw.conme;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class NetworkItemClicked implements OnItemClickListener {

	private List<ScanResult> conMeNetworks;
	private WifiManager wifi;
	private Context context;

	/**
	 * @param context 
	 * @param wifi 
	 * @param conMeNetworks 
	 * 
	 */
	public NetworkItemClicked(Context context, WifiManager wifi, List<ScanResult> conMeNetworks) {
		this.context =  context;
		this.wifi = wifi;
		this.conMeNetworks = conMeNetworks;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		
		final String ssid = conMeNetworks.get(pos).SSID;
		final String currentSSID = wifi.getConnectionInfo().getSSID();
		final boolean isConnected = ssid.equals(currentSSID);
		
		String statusAction;
		String status;
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		if(isConnected) {
			statusAction = "Disconnect";
			status = "You are currently connected to "+ ssid +"! Do you want to get disconnected?"; 
		} else {
			statusAction = "Connect";
			status = "Do you want to get connected?"; 
		}
		
		alertDialogBuilder.setTitle(ssid)
				.setMessage(status)
				.setPositiveButton(statusAction,new AlertDialogPositiveClicked(context, wifi, ssid, isConnected))
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
		
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show(); 

	}

}
