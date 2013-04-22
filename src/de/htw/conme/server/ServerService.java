/**
 * 
 */
package de.htw.conme.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import de.htw.conme.ConMeUtils;
import de.htw.conme.client.ConnectionInfo;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ServerService extends IntentService {

	private ServerSocket serverSocket;
	private String incomingMsg;
//	private String androidID;
	
	public ServerService() {
		super("ServerIntentService");
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		
//		androidID = intent.getStringExtra("androidID");
		
        try {
			serverSocket = new ServerSocket(ConMeUtils.PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	      

        while (!serverSocket.isClosed()) {
	        	
	        try {
	        	Socket socket = serverSocket.accept();
	        	socket.setSoTimeout(30000); //client timeout after 30 seconds
	        	
	        	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
	        	ConnectionInfo clientConInfo = (ConnectionInfo) objectIn.readObject();
	        	incomingMsg = clientConInfo.toString();
	    		
	        	String device = Build.MODEL;
		        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		        out.write(device + System.getProperty("line.separator"));
		        out.flush();
		        
		        startClientListUpdater("addInfo", clientConInfo);
	        	
//				ConnectionInfo serverConInfo = new ConnectionInfo(androidID);
//				ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
//		    	objectOut.writeObject(serverConInfo);  
//		    	outgoingMsg = serverConInfo.toString();
		    	
		    	Log.i("Server", "Server received: " + incomingMsg);
		        Log.i("Server", "Server sent: " + device);
		        
		    	do {
		    		processCurrentClient(socket, clientConInfo);
		    	} while(!socket.isClosed());
		    	
        	} catch(SocketTimeoutException e) {
    	        Log.e("Server", "SOCKET TIMEOUT!", e);
//    	        e.printStackTrace();
    	    } catch (StreamCorruptedException e) {
    	    	Log.e("Server", "StreamCorruptedException", e);
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("Server", "IOException", e);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				Log.e("Server", "ClassNotFoundException", e);
				e.printStackTrace();
			}
        }
        
        Log.i("Server", "Service is ending!");
	    
	}

	private void startClientListUpdater(String state, ConnectionInfo clientConInfo) {
		Intent clientListIntent = new Intent("de.htw.conme.UPDATE_CLIENT_LIST");
    	clientListIntent.putExtra(state, clientConInfo);
		getApplicationContext().sendBroadcast(clientListIntent);
	}

	private void processCurrentClient(Socket socket, ConnectionInfo previousClientConInfo) throws StreamCorruptedException, IOException, ClassNotFoundException {
    	
		ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
		ConnectionInfo clientConInfo = (ConnectionInfo) objectIn.readObject();
//		clientConInfo.setEndDate(new DateTime());
		startClientListUpdater("updateInfo", clientConInfo);
		
    	Log.i("Server", "Current Client Info " + clientConInfo);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			serverSocket.close();
			Log.i("Server", "Server Socket closed!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
