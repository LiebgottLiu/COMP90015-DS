package Server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Local_File_handler {
	private String root_path = "/Users/user/Desktop/Unimelb/2024 s1/DS/COMP90015-DS/Assessment1/Multi_threaded_server/src";
	private static String Targe_File_Paht;

	public Local_File_handler(String File_path) {

		if(File_path == "") {
			this.Targe_File_Paht = this.root_path + "/word_Dictionary.json";

		}else {
			this.Targe_File_Paht = this.root_path + File_path;
		}


	}

	public void inital_handler(ConcurrentHashMap<String, String> dict) {

		File file = new File(Targe_File_Paht);
		StringBuilder jsonContent = new StringBuilder();	
		if(!file.exists()) {
			try {
				System.out.print(this.Targe_File_Paht);
				if(file.createNewFile()) {
					System.out.print("File Created: " + file.getPath());
				}else {
					System.out.print("File Created failed: " + file.getPath());

				}
			}catch (IOException e) {
				showMessageDialog("An error occurred while creating the file. Please try again later.");
				int retryCount = 0;
				int maxRetries = 3;
				while (retryCount < maxRetries) {
					try {
						if (file.createNewFile()) {
							System.out.print("File Created: " + file.getPath());
						} else {
							System.out.print("File creation failed: " + file.getPath());
						}
						break;
					} catch (IOException ex) {
						retryCount++;

						int delay = (int) Math.pow(2, retryCount) * 1000; 
						try {
							Thread.sleep(delay);
						} catch (InterruptedException ie) {
							Thread.currentThread().interrupt(); 
						}
					}
				}

				if (retryCount == maxRetries) {
					// Notify the user about the failure after maximum retries
					showMessageDialog("File creation failed after multiple attempts. Please check your network connection and try again later.");
				}
			}
		}else {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty() || line.equals("[") || line.equals("]")) {
						continue;
					}

					// Remove leading and trailing square brackets from the line
					line = line.substring(1, line.length() - 1);

					// Split the line into key-value pairs
					String[] keyValue = line.split(":");
					if (keyValue.length == 2) {
						// Extract key and value, removing surrounding quotes
						String key = keyValue[0].trim().replaceAll("\"", "").replaceAll("}", "");
						String value = keyValue[1].trim().replaceAll("\"", "").replaceAll("}", "");
						dict.put(key, value);
					}
				}
			}catch (FileNotFoundException e) {
			    System.err.println("File not found: " + e.getMessage());
			} catch (UnsupportedEncodingException e) {
			    System.err.println("Unsupported encoding: " + e.getMessage());
			} catch (IOException e) {
			    System.err.println("Error reading file: " + e.getMessage());
			} catch (Exception e) {
			    System.err.println("An unexpected error occurred: " + e.getMessage());
			    e.printStackTrace(); 


			}
		}



	}

	private void showMessageDialog(String message) {
		System.out.println(message);
	}

	public synchronized static void saveToLocalFile(ConcurrentHashMap<String, String> dict) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Targe_File_Paht))) {
			writer.write("[\n");
			boolean first = true;
			for (Map.Entry<String, String> entry : dict.entrySet()) {
				if (!first) {
					writer.write(",\n");
				} else {
					first = false;
				}
				String key = entry.getKey();
				String value = entry.getValue();

				String message = "  {\"" + key + "\": \"" + value + "\"}";
				System.out.println("from save to dict: " + message );
				writer.write(message);
			}
			writer.write("\n]");
			writer.close();
			//            writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//		private void readDictionary() throws IOException{



}
