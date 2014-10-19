import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONObject;


public class Test {
	public static void main(String [] args){
		Socket s;
		try {
			s = new Socket("10.184.100.240", 8001);
			PrintWriter chatWriter = new PrintWriter(s.getOutputStream(),true);
			JSONObject json = new JSONObject();
			JSONArray a = new JSONArray();
			a.put("IBM");
			json.put("symbol", a);
			json.put("value", 10.9);
			json.put("subId", 1);
			json.put("gcm", "gcm");
			json.put("type", 2);
			json.put("association", 1);
			json.put("timewindow", 10);
				chatWriter.println(json.toString());
			s.close();
		//	OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8);
		 //   out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
