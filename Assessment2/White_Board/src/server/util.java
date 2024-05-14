package server;

import static client.Util.popupDialog;

import java.rmi.RemoteException;

import remote.CanvasClientInterface;

/**
 * Zhuoyang Liu, 917183
 *
 * @create 2024-05-08
 * description:
 **/

public class util {

	private static int serverPort;


	// check the argments have enough input
	public static int parseArguments(String[] args) {
		if(args.length < 2) {
			popupDialog("Not enough argments! Should be <server address> <server port");
			System.exit(0);
		}
		
		serverPort = parsePort(args[1]);
		return serverPort;
		
	}

	//check the port number is valid
	public static int parsePort(String port) {
		 int result = 8080;
		 try {
	            result = Integer.parseInt(port);

	            if (result < 1024 || result > 65535) {
	                popupDialog("server port number should be 1024-65535");
	                System.exit(1);
	            }
	        } catch (NumberFormatException e) {
	            popupDialog("server port number should be an integer");
	            System.exit(1);
	        }

	        return result;
	    }

		//print out the error message and exit
		public static void exitMessage(String message){
			errorMessage(message);
			System.exit(0);
		}

		//print out the error message 
		public static void errorMessage(String message){
			System.err.println(message);
		}


		public static void upateAllUser(ClientManager clientManager) throws RemoteException{
			for(CanvasClientInterface c: clientManager) {
			c.updateUserList(clientManager.getClientList());
		}
		}
}
