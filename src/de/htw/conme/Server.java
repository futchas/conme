/**
 * 
 */
package de.htw.conme;

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

	private ServerSocket serverSocket = null;
	private TextView textView;
	
	public Server(TextView textView) {
		this.textView = textView;
		
	}
	
	@Override
	protected Socket doInBackground(Integer... params) {

	    try {
	        serverSocket = new ServerSocket(params[0]);	      
	        //ss.setSoTimeout(10000);

	        //accept connections
	        Socket socket = serverSocket.accept();
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
		
		try {
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String incomingMsg = in.readLine() + System.getProperty("line.separator");

	        //Log.i("TcpServer", "IP: " + ss.getInetAddress());
	        //textView1.append("\nIP: " + ss.getInetAddress() + "\n");
	        Log.i("TcpServer", "Server received: " + incomingMsg);
	        textView.append("Server received: " + incomingMsg + "\n");
	        
	      //send a message
	        String outgoingMsg = "goodbye " + System.getProperty("line.separator");

	        out.write(outgoingMsg);
	        out.flush();

	        textView.append("Server sent: " + outgoingMsg + "\n");
	        Log.i("TcpServer", "sent: " + outgoingMsg);
	        //SystemClock.sleep(5000);
	        
	        socket.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }


}
