import java.util.ArrayList;
import java.util.TimerTask;


public class EventProcessTimer extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//get StockTick event list
		exampleMain.processEvents();
	}

}
