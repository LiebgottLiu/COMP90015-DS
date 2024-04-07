package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

//Student Name: Zhuoyang Liu
//Student ID: 917183

public class Dic_server {

	//	local dict name
	private  String FileName = "";
	private ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<String,String>();

	//	set port and counter ready
	private int port = 8080;
	private int counter = 0;
	private Server_UI serverUI;

	public Dic_server(int port, String fileName) {
		this.port = port;
		this.FileName = fileName;

	}

	public void initialize() {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		ServerSocket dic_server = null;
		read_dict();

		Server_UI gui = new Server_UI();
		this.serverUI = gui;



		try {
			dic_server = new ServerSocket(this.port);
			System.out.println("Server started, listening on port :"+ port );
			//			dic_server = new ServerSocket(8080);
			this.serverUI.start();

		} catch (IOException e) {
			System.err.println("Failed to start the server: " + e.getMessage());
			e.printStackTrace(); 
			return;
		}

		while(true) {
			try {
				Socket request = dic_server.accept();
				counter++;
				String clientID = UUID.randomUUID().toString();

				new Connection(request, dict,counter,clientID,serverUI).start();	

				String UI_message = ("Connected Clients: "+ this.counter);
				String full_message = (clientID + " has connected to server");
				this.serverUI.lblConnectedClients.setText(UI_message);
				serverUI.updateClientInfo(clientID, "Connected");


				System.out.println("client conection number" + counter +" " +port + " request: "+request + "\n");
			}catch (SocketException se) {
				System.err.println("Socket exception occurred: " + se.getMessage());
				se.printStackTrace();
			} catch (IOException e) {
				System.err.println("Error accepting client connection: " + e.getMessage());
				e.printStackTrace(); 
			} catch (Exception e) {
				System.err.println("An unexpected error occurred: " + e.getMessage());
				e.printStackTrace(); 
			}

		}

	}


	public  void read_dict() {
		Local_File_handler L_handler = new Local_File_handler(this.FileName);
		L_handler.inital_handler(dict);

		if(dict != null ) {	
			System.out.println("Success read local file");
		}else {
			System.out.print("empty locl file");
		}

	}

	public void convertStringToHashMap(String inputString) {
		if (inputString == null || inputString.isEmpty()) {
			System.err.println("Input string is null or empty.");
			return;
		}

		try {
			String[] keyValuePairs = inputString.split(",");

			for (String pair : keyValuePairs) {
				if (pair == null || pair.isEmpty() || !pair.contains(":")) {
					System.err.println("Invalid key-value pair: " + pair);
					continue;
				}

				String[] keyValue = pair.split(":");

				if (keyValue.length != 2) {
					System.err.println("Invalid key-value pair format: " + pair);
					continue;
				}

				String key = keyValue[0].trim().replaceAll("\"", "");
				String value = keyValue[1].trim().replaceAll("\"", "");

				if (key.isEmpty()) {
					System.err.println("Key is empty in pair: " + pair);
					continue;
				}

				if (dict.containsKey(key)) {
					System.err.println("Key already exists in the dictionary: " + key);
					continue;
				}

				dict.put(key, value);
			}
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			e.printStackTrace(); // Print stack trace for debugging
		}
	}

	public int get_ThreadCOunt() {
		return this.counter;
	}


	public String get_Dict_File_name() {
		return FileName;
	}
}




