package client;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote.CanvasMessageInterface;

//the message format for the communication between client and server
public class MessageWrapper extends UnicastRemoteObject implements CanvasMessageInterface{

	// inite the inportent message that we need to use 	
	private String drawState;
	private String clientName;
	private String mode;
	private Color color;
	private Point point;
	private String text;

	
	
	protected MessageWrapper(String state, String name, String mode, Color color, Point pt, String text) throws RemoteException{
		//constructe the messages.
		this.drawState = state;
		this.clientName = name;
		this.mode = mode;
		this.color = color;
		this.point = pt;
		this.text = text;
	}
	
	//	the get function for inportent messages 
	@Override
	public String getState() throws RemoteException {
		return this.drawState;
	}

	@Override
	public String getName() throws RemoteException {
		return this.clientName;
	}

	@Override
	public String getmode() throws RemoteException {
		return this.mode;
	}

	@Override
	public String gettext() throws RemoteException {
		return this.text;
	}

	@Override
	public Point getpoint() throws RemoteException {
		return this.point;
	}

	@Override
	public Color getcolor() throws RemoteException {
		return this.color;
	}
	
	
	
}
