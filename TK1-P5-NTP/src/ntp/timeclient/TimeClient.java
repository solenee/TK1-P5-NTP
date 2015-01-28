package ntp.timeclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TimeClient {
	public final static int port = 2048;

	public static void main (String[] str) {
		System.out.println("New client");
		ClientSlave c = new ClientSlave(port);
		c.start();
	}
}	

class ClientSlave extends Thread {

	int port;
	static final int NMAX_MEASURES = 10;
	static final int INTERTIME = 500;
	
	Date dateRequest;
	
	public ClientSlave(int port){
		this.port = port;
	}

	public double measureOffset(Timestamp t1, Timestamp t2, Timestamp t3, Timestamp t4) {
		// TODO
		return 0;
	}
	
	public double measureDelay(Timestamp t1, Timestamp t2, Timestamp t3, Timestamp t4) {
		// TODO
		return 0;
	}
	
	public void run() {
		System.out.println("[CLIENT] Start computing...");
		double offsets[] = new double[NMAX_MEASURES];
		double delays[] = new double[NMAX_MEASURES];
		int imin = 0;
		try {
			// make NMAX_MEASURES
			for (int i=0; i<NMAX_MEASURES; i++) {
				Timestamp t[] = getTimestamps();
				offsets[i] = measureOffset(t[0], t[1], t[2], t[3]);
				delays[i] = measureDelay(t[0], t[1], t[2], t[3]);
				if (delays[i] < delays[imin]) {
					imin = i;
				}
				System.out.println("[CLIENT] Measure "+i+" : offset = "+offsets[i]+" / delay = "+delays[i]);
				sleep(INTERTIME);
			}
			// display the more accurate result
			System.out.println("[CLIENT] SOLUTION :  offset = "+offsets[imin]+" / delay = "+delays[imin]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Timestamp[] getTimestamps() throws InterruptedException, UnknownHostException, IOException, ParseException  {
		// TODO
		Timestamp t[] = new Timestamp[4];
		dateRequest = new Date();
		Random r = new Random();
		sleep(r.nextInt(91) + 10); //random delay between 10 ms and 100 ms

		Socket s = new Socket("localhost",port);
		
		BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println("Answer from "+s.getRemoteSocketAddress()+":");
		String dateString = d.readLine();
		System.out.println(dateString);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String[] parts = dateString.split("\\s");
		System.out.println(parts.toString());
		Date dateResponse = format.parse(parts[1]);
		double duration = dateResponse.getTime() - dateRequest.getTime();
		System.out.println("duration = " + duration);
		
		s.close();
		return t;
		
	}
	
}
