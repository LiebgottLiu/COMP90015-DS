package Server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Local_File_handler {
	private String root_path = "/Users/user/Desktop/Unimelb/2024 s1/DS/COMP90015-DS/Assessment1/Multi_threaded_server/src";
	private String Targe_File_Paht;
	
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
			}catch(IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println(file.getName()+" : "+ file.getPath());
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			try {
				inputStreamReader = new InputStreamReader(new FileInputStream(this.Targe_File_Paht),"UTF-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				String line = null;
				while((line = bufferedReader.readLine()) != null) {
					String[] lines = line.split(":");
					dict.put(lines[0], lines[1]);
					
				}
				inputStreamReader.close();
				bufferedReader.close();
			}catch(Exception e) {
				
			}
			
			
		}	
		
		

		}
		
	
	 public static synchronized void saveToLocalFile(String File_path,ConcurrentHashMap<String, String> dict)throws IOException {
		 File file = new File(File_path);
		 try (BufferedWriter writter = new BufferedWriter(new FileWriter(file))){
			 for(Entry<String,String> entry: dict.entrySet()) {
				 String message = entry.getKey() + ":" + entry.getValue() + "\n";
				 writter.write(message);
			 }
			 writter.flush();
			 writter.close();
		 }catch(IOException e) {
			 e.printStackTrace();
		 }
	 }

	 
//		private void readDictionary() throws IOException{
//			File file = new File(this.Targe_File_Paht);
//			if(file.isFile() && file.exists()) {
//				InputStreamReader inputStreamReader = null;
//				BufferedReader bufferedReader = null;
//				try {
//					inputStreamReader = new InputStreamReader(new FileInputStream(this.Targe_File_Paht),"UTF-8");
//					bufferedReader = new BufferedReader(inputStreamReader);
//				}catch(Exception e) {
//					
//				}
//				
//				String line = null;
//				while((line = bufferedReader.readLine()) != null) {
//					String[] lines = line.split(":");
//					dict.put(lines[0], lines[1]);
//					
//				}
//				inputStreamReader.close();
//				bufferedReader.close();
//			}
//		}
		
	 
}
