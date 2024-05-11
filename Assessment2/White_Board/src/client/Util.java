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
    
	 
	 
}
