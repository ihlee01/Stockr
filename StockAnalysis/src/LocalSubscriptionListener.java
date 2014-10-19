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
            	System.out.println("Listening for local events");
                Socket client = ss.accept();
                System.out.println("Received Local Event Processing");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                StringBuilder info = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0)
                        break;
                    info.append(line);
                }
                System.out.println("JSON: " + info);
                JSONObject json = new JSONObject(info.toString());
                System.out.println(json.toString());
                double val;
                if(json.get("value") instanceof Integer){
                	System.out.println("It is Int");
                	int tmp = (Integer)json.get("value");
                	val = (double)tmp;
                }
                else{
                	System.out.println("It is Double");
                	val = json.getDouble("value");
                }
                LocalEventProcess.Event e = new LocalEventProcess.Event(val, json.getString("gcm"), json.getInt("subId"));
                if(json.getInt("association") == 1)
                	LocalEventProcess.addGreater(e);
                else
                	LocalEventProcess.addLesser(e); 
                System.out.println("Added local event");
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
