import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;


public class StockAlertListener extends Thread{
	public void run(){
		ServerSocket listener = null;
		Socket client;
		BufferedReader reader;
		String msg;
		try{
			listener = new ServerSocket(9001);
		}catch(Exception e){
			e.printStackTrace();
		}
		while(true){
			try{
				client = listener.accept();
				reader = new BufferedReader(new InputStreamReader(client.getInputStream())); 
				msg = reader.readLine();
				System.out.println("Sending Inst Alert: " + msg);
				JSONObject json = new JSONObject(msg);
				//Received an alert to be sent using GCM
				GCMServer.sendMessage(json.getInt("subId"), json.getDouble("value"), System.currentTimeMillis(), json.getString("gcm"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
