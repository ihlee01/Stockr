import java.util.Arrays;


public class OutlierDetector {
	private double data[];
	private int currIndex;
	private double median;
	private double MAD;
	boolean full = false;
	public OutlierDetector(int index){
		currIndex = 0;
		data = new double[60];
	}
	
	public void addData(double d){
		data[currIndex] = d;
		currIndex++;
		if(currIndex >= data.length){
			full = true;
			currIndex = 0;
		}
	}
	
	public boolean computeMedian(){
		if(!full)
			return false;
		double tmp [] = data;
		Arrays.sort(data);
		median = data[29];
		data = tmp;
		return true;
	}
	
	public boolean computeMAD(){
		if(!full)
			return false;
		double mad[] = new double[60];
		for(int i=0; i<data.length; i++){
			mad[i] = Math.abs(data[i]-median);
		}
		Arrays.sort(mad);
		MAD = mad[29];
		return true;
	}
	
	public double getZScore(double d){
		return Math.abs((median-d)/MAD);
	}
	
	
	
}
