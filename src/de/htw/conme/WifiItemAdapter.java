/**
 * 
 */
package de.htw.conme;

import java.util.List;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class WifiItemAdapter extends ArrayAdapter<ScanResult> {
	
	private final Activity context;
	private final List<ScanResult> networks;
	private WifiManager wifi;
	
	static class ViewHolder {
	    public TextView wifi_ssid;
	    public TextView wifi_details;
	    public ImageView wifiIcon;
	}
	
	public WifiItemAdapter(Activity context, List<ScanResult> networks, WifiManager wifi) {
	    super(context, R.layout.list_item, networks);
	    this.context = context;
	    this.networks = networks;  
	    this.wifi = wifi;
	}	
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View listItem = convertView;
        if (listItem == null) {
        	LayoutInflater inflater = context.getLayoutInflater();
            listItem = inflater.inflate(R.layout.list_item, null);
            
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.wifi_ssid = (TextView) listItem.findViewById(R.id.wifi_ssid);
            viewHolder.wifi_details = (TextView) listItem.findViewById(R.id.wifi_details);
            viewHolder.wifiIcon = (ImageView) listItem.findViewById(R.id.wifi_signal_icon);
            
            listItem.setTag(viewHolder);
        }
           
        ViewHolder holder = (ViewHolder) listItem.getTag();
        
        ScanResult result = networks.get(position);
        
        String ssid = result.SSID;
		String currentSSID = wifi.getConnectionInfo().getSSID();
		int level = WifiManager.calculateSignalLevel(result.level, 5) + 1;
		
		StringBuilder details = new StringBuilder();
		if(ssid.equals(currentSSID)) {
			details.append("Connected");
		} else {
			String cap = result.capabilities;
			details.append("Secured with ");
			String[] securityModes = { "WEP", "WPA", "WPA2" };
			boolean hasAlreadyMode = false;
			for(String mode : securityModes) {
				if(cap.contains(mode)) {
					if(hasAlreadyMode)
						details.append("/");
					else
						hasAlreadyMode = true;
					details.append(mode);
				}
			}
			if(!hasAlreadyMode)
				details  = new StringBuilder("Not secured");
		}

    	holder.wifi_ssid.setText(ssid);
        holder.wifi_details.setText(details);
        
        int imageID = context.getResources().getIdentifier("wifi" + level, "drawable", context.getPackageName());
        holder.wifiIcon.setImageResource(imageID);
	        
	    return listItem;
    }
    
}