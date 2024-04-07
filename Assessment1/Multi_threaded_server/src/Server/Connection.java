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
			//			System.out.println("message send");
		} catch (Exception e) {
			e.printStackTrace();
		}



		while(true) {
			try {
				String request;
				request = reader.readLine();
				System.out.println("request "+ request + "\n");
				if(request != null) {

					process_request(request);

				}else {
					System.out.println("request is not null.");
					return;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void process_request(String request) {
		if(request.isBlank()) {
			System.out.println("no info");
			try {
				writer.write("Please enter correct infor"+"\n");
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		String[] reqArray = request.split(":");
		String title = reqArray[0];
		String[] info = reqArray[1].split("/");
		String world ="";
		if(info[0] == null || info[0] == "") {
			try {
				writer.write("Please enter the word "+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		}else {
			 world = info[0];
			}
		
		 world = info[0].replace("}", "");

		String means = "";
		if (info.length > 1) {
			means = info[1];
		}
		if(title == "Update" && means == null) {
			try {
				writer.write("Please enter the world meaning "+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		switch(title) {
		case "Add":
			if(dict.get(world) != null) {
				try {
					writer.write("Warring/ The word: " + world + "is already in dict. / " + dict.get(world)+"\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				dict.put(world, means);
				try {
					Local_File_handler.saveToLocalFile(dict);
					writer.write("Returning / The word: " + world + " has been add to dict "+"\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;
		case "Delet":
			if(dict.get(world) == null) {
				try {
					writer.write("Warring/ The word: " + world + " donot exist "+"\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				dict.remove(world);
				try {
					Local_File_handler.saveToLocalFile(dict);
					writer.write("Returning / The word: " + world + " has been remove from dict "+"\n");
					writer.flush();;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case "Update":
			if(dict.get(world) == null) {
				try {
					writer.write("Warring/ The word: " + world + " do not exisit in dict. please add it first "+"\n");
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dict.get(world).equals(means)) {
				try {
					writer.write("Warring / the same meaning are already exists"+ " \n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			else {
				dict.remove(world);
				dict.put(world, means);
				try {
					Local_File_handler.saveToLocalFile(dict);
					writer.write("Returning / The word: " + world + " has been update "+" /" +  means +"\n");
					writer.flush();;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		case "Search":
			if (dict.get(world) != null) {
				try {
					String meaning = dict.get(world);
					writer.write("Returning / The word: " + world + " has been finded"+" / " +  meaning +"\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}else {
				try {
					writer.write("Warring/ The word: " + world + " do not exisit in dict. please add it first "+"\n");
					writer.flush();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}


		}


	}

}
