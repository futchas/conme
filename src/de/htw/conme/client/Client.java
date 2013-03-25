/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Iyad Al-sahwi
 *
 */
public class Client extends AsyncTask<Integer, Void, Void> {

	private WifiManager wifi;
	private Context context;
	private String outMsg;
	private String inMsg;
	private Socket socket;
	
	public Client(Context context, WifiManager wifiManager) {
		this.context = context;
		this.wifi = wifiManager;
	}
	
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			Log.d("Client", "Closung the client connection cause problem");
			e.printStackTrace();
		}		
	}
	
	@Override
	protected Void doInBackground(Integer... params) {

	
	    try {
//	    	InetAddress ip2 =InetAddress.getByName("Blablub");
//	    	ip = getLocalIpAddress();
//	    	int ipAdress = wifi.getConnectionInfo().getIpAddress();
	    	String gateway = intToIp(wifi.getDhcpInfo().gateway);
//	    	InetAddress ip2 =InetAddress.getByName(gateway);
	    	socket = new Socket(gateway, params[0]);
	    	
	    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
	    	outMsg = "Client is connecting" + System.getProperty("line.separator"); 
	    	out.write(outMsg);
			out.flush();
			
			//accept server response
			inMsg = in.readLine() + System.getProperty("line.separator");
            	    	
//	        return socket;
	
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
		return null; 
	}
	
	
	public String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}
	
	
	protected void onPostExecute() {

		if(socket != null) {
				
        	Log.i("Client", "sent: " + outMsg);
			Toast.makeText(context, "\nclient sent: " + outMsg + "\n", Toast.LENGTH_LONG).show();

	        Log.i("Client", "received: " + inMsg);
			Toast.makeText(context, "client received: " + inMsg + "\n", Toast.LENGTH_LONG).show();
		        
		} else {
			Log.d("Client", "Can't connect to server!");
			Toast.makeText(context, "Can't connect to server!", Toast.LENGTH_LONG).show();
		}
		
    }


}
