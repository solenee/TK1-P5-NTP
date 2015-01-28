package ntptimeclient;

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
		//-oi =-1⁄2(Ti-2 -Ti-3 +Ti-1 -Ti)
		return -0.5*(t2.getTime()-t1.getTime()+t3.getTime()-t4.getTime());
	}

	public double measureDelay(Timestamp t1, Timestamp t2, Timestamp t3, Timestamp t4) {
		//di = t + t’ = Ti-2 - Ti-3 + Ti - Ti-1
		return t2.getTime() - t1.getTime() + t4.getTime() - t3.getTime();
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
				System.out.println("[CLIENT] Measure "+i+" : offset = "+offsets[i]+"ms / delay = "+delays[i]+"ms");
				sleep(INTERTIME);
				System.out.println("///////////////////////////////////////////////");
			}
			// display the more accurate result
			System.out.println("[CLIENT] SOLUTION :  offset = "+offsets[imin]+"ms / delay = "+delays[imin]+"ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Timestamp[] getTimestamps() throws InterruptedException, UnknownHostException, IOException, ParseException  {
		// TODO
		Timestamp t[] = new Timestamp[4];
		dateRequest = new Date();
		t[0] = new Timestamp(System.currentTimeMillis());
		
		Random r = new Random();
		sleep(r.nextInt(91) + 10); //random delay between 10 ms and 100 ms

		Socket s = new Socket("localhost",port);
		
		BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println("Answer from "+s.getRemoteSocketAddress()+":");
		String dateStringT2 = d.readLine();
		t[1] = Timestamp.valueOf(dateStringT2);
		String dateStringT3 = d.readLine();
		t[2] = Timestamp.valueOf(dateStringT3);
		/*
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String[] parts = dateString.split("\\s");
		System.out.println(parts.toString());
		Date dateResponse = format.parse(parts[1]);
		double duration = dateResponse.getTime() - dateRequest.getTime();
		System.out.println("duration = " + duration);
		*/
		t[3] = new Timestamp(System.currentTimeMillis());
		for (int i=0; i<4;i++)
			System.out.println("t("+i+") = " + t[i]);
		s.close();
		return t;
		
	}
	
}
