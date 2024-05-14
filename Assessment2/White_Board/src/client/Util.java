package client;


import javax.imageio.ImageIO;
import javax.swing.*;

import remote.CanvasServerInterface;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
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
    
	public static void sendMessage(String state, String clientName,String mode, Color color, Point startPt,String text, CanvasServerInterface server) throws RemoteException{
		MessageWrapper message = new MessageWrapper(state,clientName,
							mode,color,startPt,text);
					server.broadCastCancas(message);
	}

	public static String getText(){
		String text;
		text = JOptionPane.showInputDialog("Adding text here");
		if(text == null) 
			text = "";
		return text;
	}
	 

	// draw different shapes
	// draw line 
	public static Shape makeLine(Shape shape, Point start, Point end) {
		shape = new Line2D.Double(start.x,start.y,end.x,end.y);
		return shape;
	}
	
	// draw rectangle
	public static Shape makeRect(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Rectangle2D.Double(x,y,width,height);
		return shape;
	}
	
	// draw circle
	public static Shape makeCircle(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Ellipse2D.Double(x,y,Math.max(width, height), Math.max(width, height));
		return shape;
	}
	
	// draw Oval
	public static Shape makeOval(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Ellipse2D.Double(x,y,width,height);
		return shape;
	}
	
	// draw text
	public static Shape makeText(Shape shape, Point start) {
		int x = start.x -5;
		int y = start.y -20;
		int width = 130;
		int height = 25;
		shape = new RoundRectangle2D.Double(x,y,width,height, 15,15);
		return shape;
		
	}
	 
}
