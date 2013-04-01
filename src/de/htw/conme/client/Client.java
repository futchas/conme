/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;
import de.htw.conme.ConnectionInfo;
import de.htw.conme.server.Server;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class Client extends AsyncTask<Integer, Void, Socket> {

	private WifiManager wifi;
	private Context context;	
	private ConnectionInfo serverConInfo;
	private ConnectionInfo clientConInfo;
	private String incomingMsg;
	private String outgoingMsg;
	private boolean isConnected;
	private long startReceivedData;
	private long startTransmittedData;
	private String androidID;
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
	    	String gateway = intToIp(wifi.getDhcpInfo().gateway);
	    	final Socket socket = new Socket(gateway, Server.PORT);
	    	
//	    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	    	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//	    	outMsg = "Android " + androidID + ", IP: " + ipAdress +", Mac: "+ mac + " is connecting!" + System.getProperty("line.separator"); 
//	    	out.write(outMsg);
//			out.flush();
			//accept server response
//			inMsg = in.readLine() + System.getProperty("line.separator");

	    	
//			long receivedData = TrafficStats.getTotalRxBytes();
//			long transmittedData = TrafficStats.getTotalTxBytes();
            
	    	String mac = wifi.getConnectionInfo().getMacAddress();
	    	String ipAdress = intToIp(wifi.getConnectionInfo().getIpAddress());
			
	    	initSend(socket);
	    	initReceive(socket);
	    	
	    	Timer timer = new Timer();
			
			TimerTask timerTask = new TimerTask() {
				public void run() {
					try {
						send(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			timer.scheduleAtFixedRate(timerTask, SENDING_INTERVAL, SENDING_INTERVAL);
	    	
//	    	receive();
	    	
//	    	ConnectionInfo conInfoOut = new ConnectionInfo(androidID, startTransmittedData, startReceivedData);
//			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
//	    	objectOut.writeObject(conInfoOut);  
//	    	outgoingMsg = conInfoOut.toString(); 
//			
//	    	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
//	    	ConnectionInfo conInfoIn = (ConnectionInfo) objectIn.readObject();
//	    	incomingMsg = conInfoIn.toString();

	    	return socket;
	
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    
		return null; 
	}

	private void initSend(Socket socket) throws IOException {

    	clientConInfo = new ConnectionInfo(androidID, startTransmittedData, startReceivedData);
		ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
    	objectOut.writeObject(clientConInfo);
    	outgoingMsg = clientConInfo.toString(); 
	}
	
	private void initReceive(Socket socket) throws StreamCorruptedException, IOException, ClassNotFoundException {

    	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
    	serverConInfo = (ConnectionInfo) objectIn.readObject();
    	incomingMsg = serverConInfo.toString();
	}

	private void send(Socket socket) throws IOException {
		Log.i("Client", "Send request from Client to Server");
		
		long currentTransmittedData = TrafficStats.getTotalTxBytes();
		long currentReceivedData = TrafficStats.getTotalRxBytes();
		
//		clientConInfo = new ConnectionInfo(androidID, startTransmittedData, startReceivedData);
		clientConInfo.setTotalDataUsage(currentTransmittedData, currentReceivedData);
		
		ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
    	objectOut.writeObject(clientConInfo);
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
