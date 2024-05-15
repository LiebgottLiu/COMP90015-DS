package server;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import javax.swing.JOptionPane;

import remote.CanvasClientInterface;
import remote.CanvasMessageInterface;
import remote.CanvasServerInterface;

import server.util;
import client.*;

public class CanvasServer extends UnicastRemoteObject implements CanvasServerInterface, Serializable {

	private ClientManager clientManager;
	
	protected CanvasServer() throws RemoteException{
		this.clientManager = new ClientManager(this);
	}


	// register client to clientManager list 
	@Override
	public void register(CanvasClientInterface client) throws RemoteException{
		//if the user is the first join, set it as manager
		if(this.clientManager.isEmpty()) {
			client.assignManager();
		}
		
		// ask manager for permission to join
		boolean permission = true;
		for(CanvasClientInterface c: this.clientManager) {
			if(c.getManager()) {
				try {
					permission = c.askPermission(client.getName());
				}catch(Exception e){
					util.errorMessage("there is an error");	
				}
			}
		}
		
		// set client permission
		if(!permission) {
			try {
				client.setPermission(permission);
			}catch(Exception e) {
				util.errorMessage("there is an error");
			}
		}
		
		
		// add * before manager name
		if(client.getManager()) {
			updateManagerName(client);
		}
		
		//add client to client list 
		this.clientManager.addClient(client);
		
		//upate userlist for everyone
		for(CanvasClientInterface c: this.clientManager) {
			c.updateUserList(this.clientManager.getClientList());
		}
		util.upateAllUser(this.clientManager);
		
	}

	// update manage name with * symbel
	private void updateManagerName(CanvasClientInterface client) throws RemoteException{
		client.setName("*" + client.getName());
	}

	// remove client from client list
	@Override
	public void deleteClient(String name) throws RemoteException {
		System.out.println("Remove " + name);
		for(CanvasClientInterface c : this.clientManager) {
			if(c.getName().equals(name)) {
				this.clientManager.deleteClient(c);
				System.out.println(name+ " is kicked out");
				try {
					c.closeUI();
				}catch(RemoteException e) {
					util.errorMessage("there is an remote error");
				}catch(IOException e2) {
					util.errorMessage("there is an IO error");
				}
				
			}
		}

		util.upateAllUser(clientManager);
		
	}

	// broadcast to update all client
	@Override
	public void broadCastCancas(CanvasMessageInterface message) throws RemoteException{
		for(CanvasClientInterface c: this.clientManager) {
			c.syncCanvas(message);
		}
		
	}
	
	// ask all user to refresh they canvs
	@Override
	public void refreshCanvas() throws RemoteException {
		for(CanvasClientInterface c: this.clientManager) {
			c.refreshCanvas();
		}
		
	}

	// send images throuth connection
	@Override
	public byte[] sendImage() throws IOException, RemoteException {
		byte[] currentImage = null;
		for(CanvasClientInterface c: this.clientManager) {
			if(c.getManager()) {
				currentImage = c.sendImage();
			}
		}
		return currentImage;
	}


	// read a loacl image and sent it out to all users
	@Override
	public void sedOpenedImage(byte[] byteArray) throws IOException, RemoteException {
		for(CanvasClientInterface c: this.clientManager) {
			if(c.getManager() == false) {
				c.drawOpenedImage(byteArray);
			}
		}
		
	}

	// adding user input chat
	@Override
	public void addChat(String string) throws RemoteException{
		for (CanvasClientInterface c: this.clientManager) {
			try {
				c.addChat(string);
			}catch(Exception e) {
				util.errorMessage("Server is down,");
				Util.popupDialog("WhiteBoard server is down");
				
			}
		}
		
	}

	//remove all client when manager is quit the canvas
	@Override
	public void removeAll() throws IOException, RemoteException {
		System.out.println("The manager has quit");
		for(CanvasClientInterface c : this.clientManager) {
			this.clientManager.deleteClient(c);
			c.closeUI();
		}
		
	}


	// remove one select user
	@Override
	public void removeMe(String clientName)  throws RemoteException{
		for (CanvasClientInterface c: this.clientManager) {
			if(c.getName().equals(clientName)) {
				this.clientManager.deleteClient(c);
				System.out.println(clientName +" has leave");
			}
		}
		//update user list for eveyone 
		util.upateAllUser(clientManager);
		
	}

	// getter function for client list
	@Override
	public Set<CanvasClientInterface> getClientList() throws RemoteException {
		return this.clientManager.getClientList();
	}

}
