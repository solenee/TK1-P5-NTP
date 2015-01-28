package run;

import ntptimeclient.TimeClient;
import ntptimeserver.TimeServer;

public class runMeasurements {

	public static void main(String[] args) {
		TimeServer server = new TimeServer();
		System.out.println ("Server launched");
		TimeClient client = new TimeClient();
		System.out.println ("Client launched");		
		
		
		
		
	}

}
