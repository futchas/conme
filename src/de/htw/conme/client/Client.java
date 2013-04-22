/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.htw.conme.ConMeUtils;
import de.htw.conme.R;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class Client extends AsyncTask<Void, Void, Void> {

	private WifiManager wifi;
	private ConnectionInfo clientConInfo;
	private String incomingMsg;
	private long startReceivedData;
	private long startTransmittedData;
	private String androidID;
	private Socket socket;
	private Timer timerSendingToServer;
	
	private TextView device;
	private TextView startDate;
	private TextView duration;
	private TextView endDate;
	private TextView transmittedData;
	private TextView receivedData;
	private TextView totalData;
	
	public static final int SENDING_INTERVAL = 10000; //in ms 
	
	public Client(Context context, WifiManager wifiManager, View conDetailsView) {
		this.wifi = wifiManager;
		startTransmittedData = TrafficStats.getTotalTxBytes();
		startReceivedData = TrafficStats.getTotalRxBytes();
		androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		device = (TextView) conDetailsView.findViewById(R.id.server_device);
		startDate = (TextView) conDetailsView.findViewById(R.id.startDate);
		endDate = (TextView) conDetailsView.findViewById(R.id.EndDate);
		duration = (TextView) conDetailsView.findViewById(R.id.connection_duration);
		transmittedData = (TextView) conDetailsView.findViewById(R.id.data_transmitted);
		receivedData = (TextView) conDetailsView.findViewById(R.id.data_received);
		totalData = (TextView) conDetailsView.findViewById(R.id.total_data_usage);
		
	}

	@Override
	protected Void doInBackground(Void... params) {
	
	    try {
	    	
	    	String gateway = intToIp(wifi.getDhcpInfo().gateway);
	    	socket = new Socket(gateway, ConMeUtils.PORT);
	    	socket.setSoTimeout(30000);
	    	
	    	String device = Build.MODEL;
	    	clientConInfo = new ConnectionInfo(androidID, device, startTransmittedData, startReceivedData);
			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
	    	objectOut.writeObject(clientConInfo);
	    	
	    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			incomingMsg = in.readLine() + System.getProperty("line.separator");
			
			publishProgress();
			
			Log.i("Client", "Received from Server: " + incomingMsg);
			
	    	timerSendingToServer = new Timer();
			
			TimerTask timerTask = new TimerTask() {
				public void run() {
					try {
						send();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

			timerSendingToServer.scheduleAtFixedRate(timerTask, 0, SENDING_INTERVAL);
	
	    } catch(SocketTimeoutException e) {
	    	wifi.disconnect();
	        Log.e("Client", "CLIENT SOCKET TIMEOUT!", e);
	        e.printStackTrace();
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
		return null; 
	}

	private void send() throws IOException {

		long currentTransmittedData = TrafficStats.getTotalTxBytes();
		long currentReceivedData = TrafficStats.getTotalRxBytes();
		
		clientConInfo.setTotalDataUsage(currentTransmittedData, currentReceivedData);
		clientConInfo.setEndDate(new DateTime());
		
		ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
    	objectOut.writeObject(clientConInfo);
    	
    	publishProgress();
    	
    	Log.i("Client", "Send from Client to Server: " + clientConInfo.toString());
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		device.setText(clientConInfo.getDevice());
		startDate.setText(clientConInfo.getStartDate());
		endDate.setText(clientConInfo.getEndDate());
		duration.setText(clientConInfo.getDuration());
		transmittedData.setText(clientConInfo.getTransmittedData());
		receivedData.setText(clientConInfo.getReceivedData());
		totalData.setText(clientConInfo.getTotalDataUsageInKB());
	}

	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}
	
//	protected void onPostExecute(Socket socket) {
//
//		if(socket != null) {
//				
//        	Log.i("Client", "Client sent: " + outgoingMsg);
//			Toast.makeText(context, "\nClient sent: " + outgoingMsg + "\n", Toast.LENGTH_LONG).show();
//
//	        Log.i("Client", "Client received: " + incomingMsg);
//			Toast.makeText(context, "Client received: " + incomingMsg + "\n", Toast.LENGTH_LONG).show();
//			
////			Intent conDetails = new Intent(context, ConnectionDetailsActivity.class);
////			conDetails.putExtra("clientInfo", clientConInfo);
////			context.startActivity(conDetails);
//		        
//		} else {
//			Log.d("Client", "Can't connect to server!");
//			Toast.makeText(context, "Can't connect to server!", Toast.LENGTH_LONG).show();
//		}
//		
//    }

	public void stop() {
		if(timerSendingToServer != null)
			timerSendingToServer.cancel();
	}

}
