/**
 * 
 */
package de.htw.conme.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;
import de.htw.conme.ConMe;
import de.htw.conme.ConnectionInfo;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class Server extends AsyncTask<Integer, Void, Void> {

	private ServerSocket serverSocket;
	private String incomingMsg;
	private String outgoingMsg;
	private boolean isListeningToClients;
	private String androidID;
	public static final int PORT = 3333; 
	
	public Server(String androidID) {
		this.androidID = androidID;
		isListeningToClients = true;
	}
	
	
	public void setListeningToClients(boolean isListeningToClients) {
		this.isListeningToClients = isListeningToClients;
	}


	public void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			Log.d("Server", "Closung the server caused a problem");
			e.printStackTrace();
		}		
	}
	
	
	@Override
	protected Void doInBackground(Integer... params) {

	    try {
	        serverSocket = new ServerSocket(PORT);	      
	        //ss.setSoTimeout(10000);

	        while (true) {
	        	
		        //accept connections
	        	Socket socket = serverSocket.accept();
		        
//		        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				incomingMsg = in.readLine() + System.getProperty("line.separator");
//				String androidID = Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID); 
//		    	send a message
//				outgoingMsg = "You are connected to " + androidID + System.getProperty("line.separator");
//		        out.write(outgoingMsg);
//		        out.flush();

	        	ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream()); 
	        	ConnectionInfo conInfoIn = (ConnectionInfo) objectIn.readObject();
	        	incomingMsg = conInfoIn.toString();
				
				ConnectionInfo conInfoOut = new ConnectionInfo(androidID);
				ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());  
		    	objectOut.writeObject(conInfoOut);  
		    	outgoingMsg = conInfoOut.toString(); 
		        
		        Log.i("Server", "Server received: " + incomingMsg);
		        Log.i("Server", "Server sent: " + outgoingMsg);
	        }
			
	    } catch (InterruptedIOException e) {
	        //if timeout occurs
	        e.printStackTrace();

	    } catch (IOException e) {
	        e.printStackTrace();

	    } 
	    catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
//	    finally {
//        if (serverSocket != null) {
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
	    
		return null;
	}

}
