import java.util.Timer;


public class LocalMain {

	public static void main(String [] args){
		if(args.length <2){
			System.exit(1);
		}
		Timer t = new Timer();
		t.scheduleAtFixedRate(new LocalTimer(), 5000, 60000);
		LocalSubscriptionListener l = new LocalSubscriptionListener(Integer.parseInt(args[0]));
		l.start();
		try {
			IntradayTickExample.run(args[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
}
