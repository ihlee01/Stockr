import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;

import org.json.JSONObject;


public class LocalTimer extends TimerTask {
	int count=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(count*100 >= IntradayTickExample.TRADE_VALUE.size())
			count = 0;
		
		double val;
		if(IntradayTickExample.TRADE_VALUE.size() == 0)
			val = 40.98;
		else
			val = IntradayTickExample.TRADE_VALUE.get((count*100));
		count++;
		LocalEventProcess.processEvents(val);
		Socket s;
		try {
			String[] sym = IntradayTickExample.d_security.split(" ");
			s = new Socket("127.0.0.1", 9002);
			PrintWriter chatWriter = new PrintWriter(s.getOutputStream(),true);
				JSONObject json = new JSONObject();
				json.put("value", val);
				json.put("symbol", sym[0]);
				chatWriter.println(json.toString());
			s.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}
