package ntp.timeclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TimeClient {
	public final static int port = 2048;

	public static void main (String[] str) {
		System.out.println("new client");
		Client c = new Client(port);
		c.start();
	}
}	

class Client extends Thread {
	//str [0] = nom machine

	public static int port;
	Date dateRequest;
	public Client(int port){
		this.port = port;
	}

	public double measureOffset(Timestamp t1, Timestamp t2, Timestamp t3, Timestamp t4) {
		return 0;
	}
	
	public double measureDelay(Timestamp t1, Timestamp t2, Timestamp t3, Timestamp t4) {
		return 0;
	}
	
	public void run() {
		System.out.println("run client");
		try {
			dateRequest = new Date();
			Random r = new Random();
			sleep(r.nextInt(91) + 10); //random delay between 10 ms and 100 ms

			Socket s = new Socket("localhost",port);
			BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("Answer from "+s.getRemoteSocketAddress()+":");
			String dateString = d.readLine();
			System.out.println(dateString);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			//Date dateResponse = format.parse(dateString);
			String[] parts = dateString.split("\\s");
			System.out.println(parts.toString());
			Date dateResponse = format.parse(parts[1]);
			double duration = dateResponse.getTime() - dateRequest.getTime();
			System.out.println("duration = " + duration);
			
			s.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
