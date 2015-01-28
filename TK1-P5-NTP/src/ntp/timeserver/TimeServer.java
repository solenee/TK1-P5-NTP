package ntp.timeserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class TimeServer {

	public final static int PORT = 2048;

	public final static int ARTIFICIAL_OFFSET = 1000;
	
	public static void main(String[] args) {

		ServerSocket ss;

		try {
			ss = new ServerSocket(PORT);
			while (true) {
				SlaveServer slv = new SlaveServer(ss.accept());
				slv.start();
			}	
		} catch (IOException e) {
			System.err.println(e);
		}
	} 
}


class SlaveServer extends Thread {

	Socket s;

	public SlaveServer(Socket s) {
		this.s = s;
	}
	
	public void run() {
		try {
			// Time of reception
			Timestamp t2 = new Timestamp(System.currentTimeMillis() + TimeServer.ARTIFICIAL_OFFSET);
			System.out.println (s.getRemoteSocketAddress() + " asks time.");
			
			// Processing time
			PrintStream p = new PrintStream(s.getOutputStream());
			Random r = new Random();
	    	sleep(r.nextInt(91) + 10); //random delay between 10 ms and 100 ms
	    	
	    	// Send the message
	    	Timestamp t3 = new Timestamp(System.currentTimeMillis() + TimeServer.ARTIFICIAL_OFFSET);
			p.println(t2);
			p.println(t3);
			
			s.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}

