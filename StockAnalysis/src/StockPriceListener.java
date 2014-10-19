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
			listener = new ServerSocket(9002);
		}catch(Exception e){
			e.printStackTrace();
		}
		while(true){
			try{
				client = listener.accept();
				reader = new BufferedReader(new InputStreamReader(client.getInputStream())); 
				msg = reader.readLine();
				System.out.println("Recv Msg for processing data: " + msg);
				JSONObject json = new JSONObject(msg);
				double val;
				if(json.get("value") instanceof Integer){
					int tmp = (Integer) json.get("value");
					val = (double)tmp;
				}
				else{
					val = (Double)json.get("value");
				}
				exampleMain.Tick t = new exampleMain.Tick((String)json.get("symbol"), (Double)json.get("value"), System.currentTimeMillis());
				exampleMain.addEvent(t);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
