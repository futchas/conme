/**
 * 
 */
package de.htw.conme.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import de.htw.conme.ConnectionInfo;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ServerService extends IntentService {

	private ServerSocket serverSocket;
	private String incomingMsg;
	private String outgoingMsg;
	private String androidID;
	public static final int PORT = 3333; 
	
	public ServerService() {
		super("ServerIntentService");
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		androidID = intent.getStringExtra("androidID");
			    
		try {
	        serverSocket = new ServerSocket(PORT);	      

	        while (true) {
	        	
	        	Socket socket = serverSocket.accept();
	        	
	        	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
	        	ConnectionInfo clientConInfo = (ConnectionInfo) objectIn.readObject();
	        	incomingMsg = clientConInfo.toString();
				
				ConnectionInfo serverConInfo = new ConnectionInfo(androidID);
				ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
		    	objectOut.writeObject(serverConInfo);  
		    	outgoingMsg = serverConInfo.toString();
		    	
		    	Log.i("Server", "Server received: " + incomingMsg);
		        Log.i("Server", "Server sent: " + outgoingMsg);
		    	
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

	private void processCurrentClient(Socket socket, ConnectionInfo initClientConInfo) throws StreamCorruptedException, IOException, ClassNotFoundException {
    	
		ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
		ConnectionInfo clientConInfo = (ConnectionInfo) objectIn.readObject();
		
//		if(initClientConInfo.getId() == clientConInfo.getId())
			//add to connectedClient list
			
    	Log.i("Server", "Current Client Info " + clientConInfo);
	}

}
