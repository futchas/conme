/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme;

import de.htw.conme.client.ConnectActivity;
import de.htw.conme.server.ShareActivity;
import de.htw.conme.server.WifiApManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ChangeAPState extends AsyncTask<WifiConfig, Void, Boolean> {

	private WifiApManager wifiApManager;
	private Activity activity;
	private boolean isEnabled;

	public ChangeAPState(Activity activity, WifiApManager wifiApManager, Boolean isAPEnabled) {
		this.activity = activity;
		this.wifiApManager = wifiApManager;
		this.isEnabled = isAPEnabled;
	}

	@Override
	protected Boolean doInBackground(WifiConfig... wifiConfig) {
		boolean hasChangedAPState;
		if(wifiConfig.length == 0)
			hasChangedAPState = wifiApManager.setWifiApEnabled(null, isEnabled);
		else
			hasChangedAPState = wifiApManager.setWifiApEnabled(wifiConfig[0], isEnabled);
			
		return hasChangedAPState;
	}

	@Override
	protected void onPostExecute(Boolean hasStateChanged) {
		super.onPostExecute(hasStateChanged);
		
		if(activity instanceof ShareActivity) {
			if(!hasStateChanged) {
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		        alert.setTitle("Problem with Tethering!")
		        	.setMessage("You mobile doesn't allow to enable/disable Tethering!")
		        	.setNegativeButton("Ok", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
		        alert.show();
			}
			else 
				((ShareActivity) activity).showConnectedClients(hasStateChanged, isEnabled);
		} else if(activity instanceof ConnectActivity) {
			
			Toast.makeText(activity, "Tethering will be disabled!",Toast.LENGTH_SHORT).show();	
			((ConnectActivity) activity).onResumeFurther();
		}
		

	}

	
}
