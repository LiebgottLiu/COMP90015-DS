package Server;
//Student Name: Zhuoyang Liu
//Student ID: 917183

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

public class Dic_Server_main {




	public static void main(String args[]) {
		//		starting the server
		int port = 8080 ;
		String FileName = "";


		if(args.length == 2) {
			if(args[0] != null) {
				port = Integer.parseInt(args[0]);
			}
			if(args[1] != null) {
				FileName = args[1]; 
			}
		}
		//			else {
		//			System.err.println("Usage: java DictionaryServer <port> <dictionary-file>");
		//            System.exit(1);
		//		}

		Dic_server dic_server = new Dic_server(port, FileName); 
		dic_server.initialize();
	}

}
