package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;


public class Dic_server {
	
 //	local dict name
	private  String FileName = "";
	private ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<String,String>();
	
//	set port and counter ready
	private int port = 8080;
	private int counter = 0;
	
	
	public Dic_server(int port, String fileName) {
		this.port = port;
		this.FileName = fileName;
	}

	public void initialize() {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		ServerSocket dic_server = null;
		read_dict();

		try {
			dic_server = new ServerSocket(this.port);
			System.out.println("Server started, listening on port :"+ port );
//			dic_server = new ServerSocket(8080);
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(true) {
			try {
				Socket request = dic_server.accept();
				new Connection(request, dict).start();	
				counter++;
				System.out.println("client conection number" + counter +" " +port + " request: "+request);
			}catch(Exception e) {	
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
	        String[] keyValuePairs = inputString.split(",");

	        for (String pair : keyValuePairs) {
	        	
	            String[] keyValue = pair.split(":");
	            String key = keyValue[0].trim().replaceAll("\"", "");
	            String value = keyValue[1].trim().replaceAll("\"", "");
	            dict.put(key, value);
	        }
	    }
	
	
	
	public String get_Dict_File_name() {
		return FileName;
	}
}




