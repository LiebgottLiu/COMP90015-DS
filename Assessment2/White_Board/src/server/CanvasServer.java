package server;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import javax.swing.JOptionPane;

import client.MessageWrapper;
import remote.CanvasClientInterface;
import remote.CanvasServerInterface;

public class CanvasServer extends UnicastRemoteObject implements CanvasServerInterface, Serializable {

	private ClientManager clientManager;
	
	protected CanvasServer() throws RemoteException{
		this.clientManager = new ClientManager(this);
	}

	@Override
	public void register(CanvasClientInterface client) throws RemoteException{
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
					System.err.println("there is an error");
				}
			}
		}
		
		// set client permission
		if(!permission) {
			try {
				client.setPermission(permission);
			}catch(Exception e) {
				System.err.println("there is an error");
			}
		}
		
		
		// add * before manager name
		if(client.getManager()) {
			client.setName("*" + client.getName());
		}
		
		//add client to client list 
		this.clientManager.addClient(client);
		
		//upate userlist for everyone
		for(CanvasClientInterface c: this.clientManager) {
			c.updateUserList(this.clientManager.getClientList());
		}
		
	}

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
					System.err.println("there is an remote error");
				}catch(IOException e2) {
					System.err.println("there is an IO error");
				}
				
			}
		}
		
		for(CanvasClientInterface c: this.clientManager) {
			c.updateUserList(this.clientManager.getClientList());
		}
		
	}

	// broadcast to update all client
	@Override
	public void broadCastCancas(CanvasMessageInterface message) throws RemoteException{
		for(CanvasClientInterface c: this.clientManager) {
			c.syncCanvas(message);
		}
		
	}
	
	@Override
	public void refreshCanvas() throws RemoteException {
		for(CanvasClientInterface c: this.clientManager) {
			c.refreshCanvas();
		}
		
	}

	@Override
	public byte[] sendImage() throws IOException, RemoteException {
		byte[] currentImage = null;
		for(CanvasClientInterface c: this.clientManager) {
			System.out.println("we are in the for loop ");
			if(c.getManager()) {
				currentImage = c.sendImage();
			}
		}
		return currentImage;
	}



	@Override
	public void sedOpenedImage(byte[] byteArray) throws IOException, RemoteException {
		for(CanvasClientInterface c: this.clientManager) {
			if(c.getManager() == false) {
				c.drawOpenedImage(byteArray);
			}
		}
		
	}

	@Override
	public void addChat(String string) throws RemoteException{
		for (CanvasClientInterface c: this.clientManager) {
			try {
				c.addChat(string);
			}catch(Exception e) {
				System.err.println("Server is down,");
				JOptionPane.showMessageDialog(null, "WhiteBoard server is down");
			}
		}
		
	}

	@Override
	public void removeAll() throws IOException, RemoteException {
		System.out.println("The manager has quit");
		for(CanvasClientInterface c : this.clientManager) {
			this.clientManager.deleteClient(c);
			c.closeUI();
		}
		
	}

	@Override
	public void removeMe(String clientName)  throws RemoteException{
		for (CanvasClientInterface c: this.clientManager) {
			if(c.getName().equals(clientName)) {
				this.clientManager.deleteClient(c);
				System.out.println(clientName +" has leave");
			}
		}
		//update user list for eveyone 
		for(CanvasClientInterface c: this.clientManager) {
			c.updateUserList(this.clientManager.getClientList());
		}
		
	}

	@Override
	public Set<CanvasClientInterface> getClientList() throws RemoteException {
		
		return this.clientManager.getClientList();
	}

}
