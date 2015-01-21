package ntp.timeserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeServer {

	 public final static int port = 2048;

	    public static void main(String[] args) {

	        ServerSocket ss;

	        try {
	            ss = new ServerSocket(port);
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
	            System.out.println (s.getRemoteSocketAddress() + " asks time.");
	            PrintStream p = new PrintStream(s.getOutputStream());
	            p.println(new Date());
	            s.close();
	        } catch (Exception e) {
	            System.err.println(e);
	        }
	    }
	}

