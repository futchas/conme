/**
 * 
 */
package de.htw.conme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Iyad Al-sahwi
 *
 */
public class Client extends AsyncTask<Integer, Void, Socket> {

	private WifiApManager wifiApManager;
	private TextView textView1;
	
	public Client(WifiApManager wifiApManager, TextView textView1) {
		this.wifiApManager = wifiApManager;
		this.textView1 = textView1;
	}
	
	@Override
	protected Socket doInBackground(Integer... params) {

	//	ip = getLocalIpAddress();
		String ip = "192.168.1.1";
	    try {
	    	//InetAddress ip2 =InetAddress.getByName("Blablub");
	    	//Log.i("TcpClient", "sent: " + ip2);
	    	int ipAdress = wifiApManager.getmWifiManager().getConnectionInfo().getIpAddress();
	    	String gateway = intToIp(wifiApManager.getmWifiManager().getDhcpInfo().gateway);
	    	Socket socket = new Socket(gateway, params[0]);
	        return socket;
	
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
	
	
	protected void onPostExecute(Socket socket) {

        //send output msg
        String outMsg = "Client is connecting" + System.getProperty("line.separator"); 
        try {
        	
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
			out.write(outMsg);
			out.flush();
			
			textView1.append("\nclient sent: " + outMsg + "\n");
	        Log.i("TcpClient", "sent: " + outMsg);

	        //accept server response
	        String inMsg = in.readLine() + System.getProperty("line.separator");

	        Log.i("TcpClient", "received: " + inMsg);
	        textView1.append("client received: " + inMsg + "\n");
	        
	        socket.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
    }


}
