package client;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Util {
	
	 public static void popupErrorDialog(String message) {
	        JOptionPane.showConfirmDialog(
	                null,
	                message,
	                "Error",
	                JOptionPane.OK_CANCEL_OPTION
	        );
	        System.exit(1);
	    }
	 
	 
    public static void popupDialog(String message) {
        JOptionPane.showConfirmDialog(
                null,
                message,
                "Info",
                JOptionPane.OK_CANCEL_OPTION
        );
    }
    
	public void sendMessage(string state, String clientName,string mode, Color color, Point startPt,String text){
		MessageWrapper message = new MessageWrapper(state,clientName,
							mode,color,startPt,text);
					server.broadCastCancas(message);
	}
	 
	 
}
