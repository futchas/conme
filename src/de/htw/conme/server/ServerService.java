/**
 * 
 */
package de.htw.conme.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.joda.time.DateTime;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import de.htw.conme.client.ConnectionInfo;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ServerService extends IntentService {

	private ServerSocket serverSocket;
	private String incomingMsg;
	private String outgoingMsg;
//	private String androidID;
	public static final int PORT = 3333; 
	
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
	        serverSocket = new ServerSocket(PORT);	      

	        while (true) {
	        	
	        	Socket socket = serverSocket.accept();
	        	
	        	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
	        	ConnectionInfo clientConInfo = (ConnectionInfo) objectIn.readObject();
	        	incomingMsg = clientConInfo.toString();
	    		
	        	String manufacturer = Build.MANUFACTURER;
		        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		        out.write(manufacturer + System.getProperty("line.separator"));
		        out.flush();
		        
		        startClientListUpdater("init", clientConInfo);
	        	
//				ConnectionInfo serverConInfo = new ConnectionInfo(androidID);
//				ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
//		    	objectOut.writeObject(serverConInfo);  
//		    	outgoingMsg = serverConInfo.toString();
		    	
		    	Log.i("Server", "Server received: " + incomingMsg);
		        Log.i("Server", "Server sent: " + manufacturer);
		    	
		        socket.setSoTimeout(30000);
		        
		    	do {
		    		processCurrentClient(socket, clientConInfo);
		    	} while(true);
		        
	        }
	    } catch(SocketTimeoutException e) {
	        Log.e("Server", "SOCKET TIMEOUT!", e);
	        e.printStackTrace();
	    } catch (InterruptedIOException e) {
	        //if timeout occurs
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
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
		startClientListUpdater("refresh", clientConInfo);
			
    	Log.i("Server", "Current Client Info " + clientConInfo);
	}

}
