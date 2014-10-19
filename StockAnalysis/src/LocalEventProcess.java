import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;


public class LocalEventProcess {
	public static class Event{
		Double val;
		String gcm;
		int subId;
		public Event(Double val, String gcm, int subId){
			this.val = val;
			this.gcm = gcm;
			this.subId = subId;
		}
	}
	
	private static ArrayList<Event> greater = new ArrayList<Event>();
	private static ArrayList<Event> lesser = new ArrayList<Event>();
	
	public static void addGreater(Event e){
		greater.add(e);
	}
	
	public static void addLesser(Event e){
		lesser.add(e);
	}
	
	public static void processEvents(Double price){
		ArrayList<Event> toSend = new ArrayList<Event>();
		for(Event e: greater){
			if(price > e.val)
				toSend.add(e);
		}
		for(Event e: lesser){
			if(price < e.val)
				toSend.add(e);
		}
		Socket s;
		try {
			
			s = new Socket("127.0.0.1", 9001);
			PrintWriter chatWriter = new PrintWriter(s.getOutputStream(),true);
			for(Event e: toSend){
				JSONObject json = new JSONObject();
				json.put("value", e.val);
				json.put("subId", e.subId);
				json.put("gcm", e.gcm);
				chatWriter.println(json.toString());
			}
			s.close();
		//	OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8);
		 //   out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
