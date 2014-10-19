import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;


public class StockPriceListener extends Thread{

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
			System.out.println("Listening for non Outliers/Alerts");
			try{
				client = listener.accept();
				reader = new BufferedReader(new InputStreamReader(client.getInputStream())); 
				msg = reader.readLine();
				System.out.println("Recv Msg: " + msg);
				JSONObject json = new JSONObject(msg);
				//Received an alert to be sent using GCM
				GCMServer.sendMessage();
				//exampleMain.Tick t = new exampleMain.Tick((String)json.get("symbol"), (Double)json.get("value"), (Long)json.get("timestamp"));
				//exampleMain.addEvent(t);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//listener.close();
	}
}
