package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

//Student Name: Zhuoyang Liu
//Student ID: 917183

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
			System.out.println("Connect start: "+ port);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
			gui.start();

		}catch (NumberFormatException e) {
			System.err.println("Invalid port number provided: " + e.getMessage());
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + address);
		} catch (IOException e) {
			System.err.println("Error establishing connection: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
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

					String[] MessageArray = msg.split("/");
					if(MessageArray.length > 2) {
						System.out.println(MessageArray[1]);
						System.out.println(MessageArray[0]);
						System.out.println(MessageArray[2]);

						Client_UI.textDictionary.setText(MessageArray[1]);
						Client_UI.editorPane.setText(MessageArray[2]);
					}else if (MessageArray.length == 2) {
						Client_UI.textDictionary.setText(MessageArray[1]);
						Client_UI.editorPane.setText(null);
					}



				}
			}catch (NullPointerException e) {
				System.err.println("Server reader is null. Exiting loop.");
				JOptionPane.showMessageDialog(null, "Server is out of service.");
				gui.interrupt();
				return;

			} catch (IOException e) {
				//				System.err.println("IOException occurred: " + e.getMessage());
				//				e.printStackTrace();
				gui.interrupt();
				return;
				// Handle IOException if necessary
			}
		}
	}
}
