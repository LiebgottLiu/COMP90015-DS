package server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;
import remote.CanvasServerInterface;
import server.util;
import static client.Util.popupDialog;

public class Server {
	
	private static int serverPort;
	
	public static void main(String[] args) {
//		parseArguments(args);
		
		try {
			
			CanvasServerInterface server = new CanvasServer();
			
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.bind("WhiteboardServer", server);
			JOptionPane.showMessageDialog(null, "WhiteBoard server is ready....");
		}catch(Exception e) {
			System.err.println("Port are already in use");
			System.exit(0);
		}
	}

	private static void parseArguments(String[] args) {
		if(args.length < 2) {
			popupDialog("Not enough argments! Should be <server address> <server port");
			System.exit(0);
		}
		
		serverPort = util.parsePort(args[1]);
		
	}
	
	

}
