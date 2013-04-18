/**
 * 
 */
package de.htw.conme.server;

import java.util.ArrayList;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
		if(intent.hasExtra("addInfo")) {
			conInfo = (ConnectionInfo) intent.getSerializableExtra("addInfo");
			clientList.add(conInfo);
			addToTable(context, conInfo);
		} else if(intent.hasExtra("updateInfo")) {
			conInfo = (ConnectionInfo) intent.getSerializableExtra("updateInfo");
			 for(int i=clientList.size()-1; i>=0; i--) {
			    
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
	    
	    TextView date = (TextView) row.findViewById(R.id.date_cell);
	    date.setText(conInfo.getStartDate());

	    TextView duration = (TextView) row.findViewById(R.id.duration_cell);
	    duration.setText(conInfo.getDuration());
	    
	    TextView dataUsage = (TextView) row.findViewById(R.id.data_usage_cell);
	    dataUsage.setText(conInfo.getTotalDataUsageInKB());
	}

	private void addToTable(Context context, ConnectionInfo conInfo) {
		
		TableRow row = new TableRow(context);
		row.setBackgroundColor(Color.parseColor("#DDEEFF"));
	    row.setGravity(Gravity.CENTER);
//	    row.setPadding(20, 5, 5, 5);
	    TextView device = new TextView(context);
	    device.setPadding(10, 0, 0, 0);
	    device.setText(conInfo.getDevice());
	    device.setId(R.id.device_cell);
	    
	    TextView date = new TextView(context);
	    date.setText(conInfo.getStartDate());
	    date.setId(R.id.date_cell);
	    
	    TextView duration = new TextView(context);
	    duration.setText(conInfo.getDuration());
	    duration.setId(R.id.duration_cell);

	    TextView dataUsage = new TextView(context);
	    dataUsage.setText(conInfo.getTotalDataUsageInKB());
	    dataUsage.setId(R.id.data_usage_cell);
	    
	    row.addView(device);
	    row.addView(date);
	    row.addView(duration);
	    row.addView(dataUsage);
	    table.addView(row);
	}

	public void cleanup() {
		// Don't remove the header row
		for(int i = 1; i < table.getChildCount(); i++){
				table.removeViewAt(i);
		}
		
		clientList.clear();
		table.setVisibility(View.GONE);
	}
	
	
	
}
