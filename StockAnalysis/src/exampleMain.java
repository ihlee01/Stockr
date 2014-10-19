import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
 
public class exampleMain {
	static int count = 0;
	private static Configuration cepConfig;
	private static EPServiceProvider cep;
	private static EPRuntime cepRT;
	private static ArrayList<Tick> events = new ArrayList<Tick>();
	private static ArrayList<Tick> tmp = new ArrayList<Tick>();
	private static boolean isProcessing = false;
	
	public static void addEvent(Tick t){
		if(isProcessing)
			tmp.add(t);
		else
			events.add(t);
	}
	
	public static void clearList(){
		events.clear();
	}
	
	public static void processEvents(){
		isProcessing = true;
    	for(Tick tick: events){
    		cep.getEPRuntime().sendEvent(tick);
    	}
    	events.clear();
    	isProcessing = false;
    	events.addAll(tmp);
    	tmp.clear();
    }
	
	public static void init(){
		events = new ArrayList<Tick>();
		cepConfig = new Configuration();
        cepConfig.addEventType("StockTick", Tick.class.getName());
        cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
        cepRT = cep.getEPRuntime();
	}
	
    public static class Tick {
        String symbol;
        Double price;
        Date timeStamp;
        public Tick(String s, double p, long t) {
            symbol = s;
            price = p;
            timeStamp = new Date(t);
        }
        public double getPrice() {return price;}
        public String getSymbol() {return symbol;}
        public Date getTimeStamp() {return timeStamp;}
 
        @Override
        public String toString() {
            return "Symbol: " + symbol + " Price: " + price + " time: " + timeStamp.toString();
        }
    }
/* 
    private static Random generator = new Random();
 
    public static void GenerateRandomTick(EPRuntime cepRT) {
 
        double price = (double) generator.nextInt(10);
        long timeStamp = System.currentTimeMillis();
        String symbol = "IBM";
        Tick tick = new Tick(symbol, price, timeStamp);
        //System.out.println("Sending tick:" + tick);
        cepRT.sendEvent(tick);
 
    }
 */
    public static class CEPListener implements UpdateListener {
    	private String gcm;
    	private int subId;
    	
    	public CEPListener(String gcm, int subId){
    		this.gcm = gcm;
    		this.subId = subId;
    	}
    	
        public void update(EventBean[] newData, EventBean[] oldData) {
        	//System.out.println(newData[0].get("price"));
           System.out.println("Event received New: " + newData[0].getUnderlying().toString());
        }
    }
    
    public static class SpatialCEPListener implements UpdateListener {
    	private String gcm;
    	private int subId;
    	private int size;
    	private long lastTimeSpan;
    	private int count;
    	public SpatialCEPListener(String gcm, int subId, int size){
    		this.gcm = gcm;
    		this.subId = subId;
    		this.size = size;
    	}
    	
        public void update(EventBean[] newData, EventBean[] oldData) {
        	//System.out.println(newData[0].get("price"));
        	if(lastTimeSpan == -1){
        		lastTimeSpan = (Long) newData[0].get("timestamp");
        		count++;
        		return;
        	}
        	long timespan = (Long) newData[0].get("timestamp");
        	if(timespan - lastTimeSpan < 60)
        		count++;
        	if(count == size){
        		lastTimeSpan = -1;
        		//Send GCM Message	
        	}
        	
           //System.out.println("Event received New: " + newData[0].getUnderlying().toString());
        }
    }
    
/*    
    public static class CEPListener2 implements UpdateListener {
    	 
        public void update(EventBean[] newData, EventBean[] oldData) {
        	System.out.println("Add to window");
        	count++;
        }
    }
*/
 
    public static String createStreamQuery(String symbol, int type, double value, int timewindow, int association, String gcm, int subId){
    		String query; 
    		if(type == 1){
    			query = "select * from StockTick(symbol='" + symbol + "') having price ";
    			if(association == 1)
    				query += "> " + value;
    			else
    				query += "< " + value;
    		}
    		else{
    			query = "select * from StockTick(symbol='" + symbol + "').win:length(" + timewindow +
        				") having avg(price) ";
    			if(association == 1)
    				query += "> " + value;
    			else
    				query += "< " + value;
    		}
    		EPStatement stmt = cep.getEPAdministrator().createEPL(query);
    		CEPListener c = new CEPListener(gcm, subId);
    		stmt.addListener(c);
    		System.out.println("Created Query for Temporal: " + query);
    		return query;
    }
    
    public static String createStreamQuery(ArrayList<String> symbol, double value, int association, String gcm, int subId){
		String symbols = "symbol='" + symbol.get(0) + "'";
		for(int i=1; i<symbol.size(); i++)
			symbols += " or symbol='" + symbol.get(i) + "'";
		String query;
		query = "select * from StockTick(" + symbols + ") having price ";
		if(association == 1)
			query += "> " + value;
		else
			query += "< " + value;
		EPStatement stmt = cep.getEPAdministrator().createEPL(query);
		SpatialCEPListener c = new SpatialCEPListener(gcm, subId, symbol.size());
		stmt.addListener(c);
		System.out.println("Created Query for Spatial: " + query);
		return query;
    }
    /*
    public static void main(String[] args) {
 
        cepConfig = new Configuration();
        cepConfig.addEventType("StockTick", Tick.class.getName());
        cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
        cepRT = cep.getEPRuntime();
 
        EPAdministrator cepAdm = cep.getEPAdministrator();
        EPStatement cepStatement = cepAdm.createEPL("select * from " +
                "StockTick(symbol='AAPL' or symbol='IBM').win:length(2) " +
                "having avg(price) > 6.0");
        EPStatement cepStatement2 = cepAdm.createEPL("select * from " +
                "StockTick(symbol='AAPL').win:length(2) " +
                "having avg(price) > 7.0");
        
        CEPListener c = new CEPListener();
        cepStatement.addListener(c);
        cepStatement2.addListener(c);
        
        cepAdm.createEPL("create window AlertWindow.win:length_batch(2) as select price from StockTick").start();
   	 	
        EPStatement cepStatement3 = cepAdm.createEPL("insert into AlertWindow select price from StockTick(symbol='AAPL') having price > 0");
   	 	cepStatement3.addListener(new CEPListener2());
   	 	//"on QueryEvent as queryEvent select * from OrdersNamedWindow as win having median(price)-" + val + "
   	 	// We generate a few ticks...
   	 	boolean destroy = false;
        while(true){
        	System.out.println("Start");
        for (double  i = 0; i < 20; i++) {
        	if(!destroy && count > 10){
        		System.out.println("Destroyed Stmt " + i);
        		cepStatement3.destroy();
        		destroy = true;
        	}
            GenerateRandomTick(cepRT);
        }
        try {
        	System.out.println("Sleep");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        }
    }*/
}