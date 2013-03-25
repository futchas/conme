/**
 * 
 */
package de.htw.conme.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Iyad Al-sahwi
 *
 */
public class Server extends AsyncTask<Integer, Void, Socket> {

	private ServerSocket serverSocket;
	private TextView textView;
	private String incomingMsg;
	private String outgoingMsg;
	
	public Server(TextView textView) {
		this.textView = textView;
	}
	
	public void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			Log.d("Server", "Closung the server cause problem");
			e.printStackTrace();
		}		
	}
	
	
	@Override
	protected Socket doInBackground(Integer... params) {

	    try {
	        serverSocket = new ServerSocket(params[0]);	      
	        //ss.setSoTimeout(10000);

	        //accept connections
	        Socket socket = serverSocket.accept();
	        
	        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			incomingMsg = in.readLine() + System.getProperty("line.separator");
	        
			//send a message
			outgoingMsg = "goodbye " + System.getProperty("line.separator");
				
	        out.write(outgoingMsg);
	        out.flush();
			
	        return socket;


	    } catch (InterruptedIOException e) {
	        //if timeout occurs
	        e.printStackTrace();

	    } catch (IOException e) {
	        e.printStackTrace();

	    } finally {
	        if (serverSocket != null) {
	            try {
	                serverSocket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
//	    publishProgress(values)
	    
		return null;
	}
	
	
	protected void onPostExecute(Socket socket) {
		
		if(socket != null) {
			try {
	
		        //Log.i("TcpServer", "IP: " + ss.getInetAddress());
		        //textView1.append("\nIP: " + ss.getInetAddress() + "\n");
		        Log.i("Server", "Server received: " + incomingMsg);
		        textView.setText("Server received: " + incomingMsg + "\n");
		        
		        textView.append("Server sent: " + outgoingMsg + "\n");
		        Log.i("Server", "Server sent: " + outgoingMsg);
		        
		        socket.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
        

    }


}
