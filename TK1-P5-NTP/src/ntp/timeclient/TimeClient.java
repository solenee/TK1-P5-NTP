package ntp.timeclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TimeClient {
	public final static int port = 2048;

    public static void main (String[] str) {
        // str [0] = nom machine
        try {
			
            Socket s = new Socket("localhost",port);
            BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Answer from "+s.getRemoteSocketAddress()+":");
            System.out.println(d.readLine());
            s.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
