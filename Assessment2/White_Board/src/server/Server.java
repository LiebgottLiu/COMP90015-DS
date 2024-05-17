package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.CanvasServerInterface;
import static client.Util.popupDialog;

public class Server {
	
	private static int serverPort;
	
	public static void main(String[] args) {

		serverPort = util.parseArguments(args);
		System.out.println(serverPort);
		

		// try to set server ready
		try {
			CanvasServerInterface server = new CanvasServer();
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.bind("WhiteboardServer", server);
			popupDialog("WhiteBoard server is ready....");
		}catch(Exception e) {
			util.exitMessage("Port are already in use");
		}
	}


	
	

}
