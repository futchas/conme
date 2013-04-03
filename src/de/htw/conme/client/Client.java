/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
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
import android.widget.Toast;
import de.htw.conme.server.ServerService;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class Client extends AsyncTask<Integer, Void, Socket> {

	private WifiManager wifi;
	private Context context;	
	private ConnectionInfo clientConInfo;
	private String incomingMsg;
	private String outgoingMsg;
	private boolean isConnected;
	private long startReceivedData;
	private long startTransmittedData;
	private String androidID;
	private Socket socket;
	public static final int SENDING_INTERVAL = 10000; //in ms 
	
	public Client(Context context, WifiManager wifiManager) {
		isConnected = true;
		this.context = context;
		this.wifi = wifiManager;
		startTransmittedData = TrafficStats.getTotalTxBytes();
		startReceivedData = TrafficStats.getTotalRxBytes();
		androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
	
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public boolean isConnected() {
		return isConnected;
	}


	@Override
	protected Socket doInBackground(Integer... params) {
	
	    try {
//	    	InetAddress ip2 =InetAddress.getByName("Blablub");
//	    	ip = getLocalIpAddress();
//	    	InetAddress ip2 =InetAddress.getByName(gateway);
	    	
//	    	String mac = wifi.getConnectionInfo().getMacAddress();
//	    	String ipAdress = intToIp(wifi.getConnectionInfo().getIpAddress());
	    	
	    	String gateway = intToIp(wifi.getDhcpInfo().gateway);
	    	socket = new Socket(gateway, ServerService.PORT);

	    	String manufacturer = Build.MANUFACTURER;
	    	clientConInfo = new ConnectionInfo(androidID, manufacturer, startTransmittedData, startReceivedData);
			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
	    	objectOut.writeObject(clientConInfo);
	    	outgoingMsg = clientConInfo.toString(); 
	    	
	    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			incomingMsg = in.readLine() + System.getProperty("line.separator");
			
			Log.i("Client", "Received from Server: " + incomingMsg);
			
	    	socket.setSoTimeout(30000);
	    	
	    	Timer timer = new Timer();
			
			TimerTask timerTask = new TimerTask() {
				public void run() {
					try {
						send();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			timer.scheduleAtFixedRate(timerTask, SENDING_INTERVAL, SENDING_INTERVAL);
	    	
//	    	ConnectionInfo conInfoOut = new ConnectionInfo(androidID, startTransmittedData, startReceivedData);
//			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
//	    	objectOut.writeObject(conInfoOut);  
//	    	outgoingMsg = conInfoOut.toString(); 
//			
//	    	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
//	    	ConnectionInfo conInfoIn = (ConnectionInfo) objectIn.readObject();
//	    	incomingMsg = conInfoIn.toString();

	    	return socket;
	
	    } catch(SocketTimeoutException e) {
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
    	Log.i("Client", "Send from Client to Server: " + clientConInfo.toString());
	}

	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}
	
	
	protected void onPostExecute(Socket socket) {

		if(socket != null) {
				
        	Log.i("Client", "Client sent: " + outgoingMsg);
			Toast.makeText(context, "\nClient sent: " + outgoingMsg + "\n", Toast.LENGTH_LONG).show();

	        Log.i("Client", "Client received: " + incomingMsg);
			Toast.makeText(context, "Client received: " + incomingMsg + "\n", Toast.LENGTH_LONG).show();
		        
		} else {
			Log.d("Client", "Can't connect to server!");
			Toast.makeText(context, "Can't connect to server!", Toast.LENGTH_LONG).show();
		}
		
    }

}
