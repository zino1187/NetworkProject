package net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IPManager {
	
	public static String getIp() {
		String ip=null;
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
			System.out.println("나의 아이피는 "+ip);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
