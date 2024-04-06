package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Connection extends Thread {
	
	Socket socket = null;
	ConcurrentHashMap<String,String> dict;
	BufferedReader reader;
	BufferedWriter writer;
	
	public Connection(Socket socket, ConcurrentHashMap<String, String> dict1) {
		this.socket = socket;
		this.dict = dict1;
	}

	@Override
	public void run() {
		
		try {
			if (socket == null) {
	            System.out.println("Socket is null. Exiting thread.");
	            return; 
	        }
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"));
			writer.write("hello");
			writer.flush();
			System.out.println("message send");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String request;
		while(true) {
			try {
				request = reader.readLine();
				System.out.println("request"+ request);
				if(request != null) {
					System.out.println("request is not null.");
					 
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
