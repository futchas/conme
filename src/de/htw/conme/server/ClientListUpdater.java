/**
 * 
 */
package de.htw.conme.server;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.htw.conme.R;
import de.htw.conme.client.ConnectionInfo;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ClientListUpdater extends BroadcastReceiver {

	private ArrayList<ConnectionInfo> clientList;
	private TableLayout table;

	public ClientListUpdater(Activity activity) {

		clientList = new ArrayList<ConnectionInfo>();
		table = (TableLayout) activity.findViewById(R.id.clientList);
		table.setStretchAllColumns(true);  
	    table.setShrinkAllColumns(true);
	    table.setVisibility(View.GONE);
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		table.setVisibility(View.VISIBLE);
//		ConnectionInfo conInfo = (ConnectionInfo) intent.getParcelableExtra("clientConInfo");
		ConnectionInfo conInfo;
		if(intent.hasExtra("init")) {
			conInfo = (ConnectionInfo) intent.getSerializableExtra("init");
			clientList.add(conInfo);
			addToTable(context, conInfo);
		} else {
			conInfo = (ConnectionInfo) intent.getSerializableExtra("refresh");
			 for(int i=0; i<clientList.size(); i++) {
			    
			    ConnectionInfo previousConInfo = clientList.get(i);
			    
			    if(previousConInfo.getId().equals(conInfo.getId())) {
			    	clientList.set(i, conInfo);
			    	updateTableRow(conInfo, i + 1); // first row is table header
			    	break;
			    }
			    
			} //for
		} //else
	}

	private void updateTableRow(ConnectionInfo conInfo, int index) {

		TableRow row = (TableRow) table.getChildAt(index);
	    
		TextView device = (TextView) row.findViewById(R.id.device_cell);
	    device.setText(conInfo.getDevice());
	    
	    TextView duration = (TextView) row.findViewById(R.id.duration_cell);
	    duration.setText(conInfo.getDuration());
	    
	    TextView dataUsage = (TextView) row.findViewById(R.id.data_usage_cell);
	    dataUsage.setText(String.valueOf(conInfo.getTotalDataUsage()));
	}

	private void addToTable(Context context, ConnectionInfo conInfo) {
		
		TableRow row = new TableRow(context);
	    row.setGravity(Gravity.CENTER);
	    row.setPadding(5, 5, 5, 5);
	    
	    TextView device = new TextView(context);
	    device.setText(conInfo.getDevice());
	    device.setId(R.id.device_cell);
	    
	    TextView duration = new TextView(context);
	    duration.setText(conInfo.getDuration());
	    duration.setId(R.id.duration_cell);

	    TextView dataUsage = new TextView(context);
	    dataUsage.setText(String.valueOf(conInfo.getTotalDataUsage()));
	    dataUsage.setId(R.id.data_usage_cell);
	    
	    row.addView(device);
	    row.addView(duration);
	    row.addView(dataUsage);
	    table.addView(row);
	}
	
}
