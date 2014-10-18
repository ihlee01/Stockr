import java.util.Arrays;
import java.util.Random;
import java.util.Timer;


public class Test {
	public static void main(String [] args){
		Timer timer;
		timer = new Timer();
		timer.scheduleAtFixedRate(new EventProcessTimer(), 0, 60000);
		
		
		Random r = new Random();
		int [] data = new int[10];
		data[0] = 10;
		data[1] = 12;
		data[2] = 15;
		data[3] = 9;
		data[4] = 15;
		data[5] = 13;
		data[6] = 18;
		data[7] = 30;
		data[8] = 25;
		data[9] = 40;
		for(int i=0; i<10; i++){
		//	data[i] = r.nextInt(10000);
		}
		for(int i=0; i<10; i++)
			System.out.print(data[i] + " ");
		System.out.println();
		int [] data2 = data;
		Arrays.sort(data);
		for(int i=0; i<10; i++)
			System.out.print(data[i] + " ");
		System.out.println();
		int median = data[4];
		System.out.println(median);
		int data3[] = new int[10];
		for(int i=0; i<10; i++){
			data3[i] = Math.abs(data2[i] - median);
			
		}
		for(int i=0; i<10; i++)
			System.out.print(data3[i] + " ");
		System.out.println();
		Arrays.sort(data3);
		int MAD = data3[4];
	
		int val = r.nextInt(50);
		int z = (median-val)/MAD;
		System.out.println(z);
		
	}
}
