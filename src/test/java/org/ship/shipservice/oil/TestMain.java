package org.ship.shipservice.oil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestMain {
	public static void main(String[] args) throws UnknownHostException {
		 String ip = InetAddress.getLocalHost().getHostAddress();
	     System.out.println(ip);
	}
}