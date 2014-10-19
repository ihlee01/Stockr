import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;


public class LocalSubscriptionListener extends Thread {
	int port;
	public LocalSubscriptionListener(int port){
		this.port = port;
	}
	
	public void run(){
		try {
            // Get the port to listen on
            // Create a ServerSocket to listen on that port.
            ServerSocket ss = new ServerSocket(port);
            // Now enter an infinite loop, waiting for & handling connections.
            while(true) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                StringBuilder info = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0)
                        break;
                    info.append(line);
                }
                JSONObject json = new JSONObject(info);
                LocalEventProcess.Event e = new LocalEventProcess.Event(json.getDouble("value"), json.getString("gcm"), json.getInt("subId"));
                if(json.getInt("association") == 1)
                	LocalEventProcess.addGreater(e);
                else
                	LocalEventProcess.addLesser(e);             
                in.close();
                } // Now loop again, waiting for the next connection
            }
            // If anything goes wrong, print an error message
            catch (Exception e) {
                System.err.println(e);
                System.err.println("Usage: java Server <port > 1023>");
            }
	}
}
