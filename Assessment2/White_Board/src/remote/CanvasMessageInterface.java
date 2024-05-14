package remote;

import java.awt.Color;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CanvasMessageInterface extends Remote{
	//get functions for the message wrapper
	
	// get current message state
	public String getState() throws RemoteException;
	
	//get client name
	public String getName() throws RemoteException;
	
	// get current draw mode
	public String getmode() throws RemoteException;
	
	//get chat box text 
	public String gettext() throws RemoteException;
	
	//get mouse location
	public Point getpoint() throws RemoteException;
	
	//get current color
	public Color getcolor() throws RemoteException;
}


