package remote;

import java.awt.Color;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CanvasMessageInterface extends Remote{
	//get functions for the message wrapper
	
	public String getState() throws RemoteException;
	
	public String getName() throws RemoteException;
	
	public String getmode() throws RemoteException;
	
	public String gettext() throws RemoteException;
	
	public Point getpoint() throws RemoteException;
	
	public Color getcolor() throws RemoteException;
}


