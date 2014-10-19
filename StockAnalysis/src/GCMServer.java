import java.io.IOException;
import java.util.*;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public class GCMServer {

	public static void sendMessage() throws IOException {
		Sender sender = new Sender("AIzaSyD4Y6fCrmNvFWR59qzWrNwC10jEpja2J8w"); // API Key
		String registerID = "APA91bExbq-NwhZaZCbJJXUvKuuHA_7HyRDd09ldtwzPM8nUdZKs6OXzRgkSxnld2GFjCIcBKMwCAxZMHtHb7tBOglnKtnpI9iCvuPhUiwu4P_f4psv4-fcPkDwRp8OUmGt67rCFu71YOCxVs38JgPFrs2EoJnU-PVP17KgPVDj2n9ofTiOald0";
		
		Message msg = new Message.Builder().addData("msg", "Push notify").addData("msg2", "Test").build(); // message from server
		
		List<String> list = new ArrayList<String>();
		list.add(registerID);
		
		MulticastResult multiResult = sender.send(msg, list, 5);
		
		if(multiResult != null) {
			List<Result> resultList = multiResult.getResults();
			
			for(Result result : resultList) {
				System.out.println(result.getMessageId());
			}
		}
	}
	
}
