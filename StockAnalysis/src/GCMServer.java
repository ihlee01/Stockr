import java.io.IOException;
import java.util.*;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public class GCMServer {

	public static void sendMessage(int subId, double value, long timestamp, String registerID) throws IOException {
		Sender sender = new Sender("AIzaSyD4Y6fCrmNvFWR59qzWrNwC10jEpja2J8w"); // API Key
		
		Message msg = new Message.Builder().addData("subId", ""+subId).addData("value", ""+value).
				addData("timestamp", ""+timestamp).build(); // message from server
		
		List<String> list = new ArrayList<String>();
		list.add(registerID);
		
		MulticastResult multiResult = sender.send(msg, list, 5);
		
		if(multiResult != null) {
			List<Result> resultList = multiResult.getResults();
			
			for(Result result : resultList) {
				System.out.println("GCM RESULTS: " + result.getMessageId());
			}
		}
	}
	
}
