package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Connection extends Thread {
	
	private  Socket socket = null;
	private  ConcurrentHashMap<String,String> dict;
	private  BufferedReader reader;
	private  BufferedWriter writer;
	
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
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
//			writer.write("hello \n");
//			writer.flush();
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
