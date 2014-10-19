import java.util.Timer;


public class Main {
	public static void main(String [] args){
		//Start Server for Incoming subscriptions
		//Listen for Outliers
		exampleMain.init();
		StockAlertListener sa = new StockAlertListener();
		sa.start();
		StockPriceListener sp = new StockPriceListener();
		sp.start();
		//Schedule Event Process Timer
		Timer t = new Timer();
		t.scheduleAtFixedRate(new EventProcessTimer(), 5000, 60000);
		Server.start();
	}
}
