package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Client {

	static Socket client;
	static BufferedReader reader = null ;
	static BufferedWriter writer = null ; 
	
	public static void main(String[] args)  {
		
		int port = 8080;
		String address = "localhost";
		Client_UI gui = new Client_UI();

		
		if(args.length == 2) {
			if(args[1] != null) {
				port = Integer.parseInt(args[1]);
			}
			if(args[0] != null) {
				address = args[0]; 
			}
		}

		try {
			 client = new Socket(address,port);
			 gui.start();
			
		}catch( Exception e) {
			e.printStackTrace();
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		while(true) {
			
			try {
				if (reader == null) {
		            System.out.println("Reader is null. Exiting loop.");
		            break;
		        }
				String msg = reader.readLine();
		        if (msg != null || !msg.isEmpty()) {
		            System.out.println("message: " + msg);
		        }
					
			}catch(Exception e) {
				e.printStackTrace();
				
			}
		}
	}
}
