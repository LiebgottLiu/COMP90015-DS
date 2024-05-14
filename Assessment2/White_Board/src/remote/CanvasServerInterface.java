package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import client.MessageWrapper;

public interface CanvasServerInterface extends Remote {

	//get all user client list 
	public Set<CanvasClientInterface> getClientList()throws RemoteException;

	//register user in to client
	public void register(CanvasClientInterface client) throws RemoteException;

	//delete select client
	public void deleteClient(String name) throws RemoteException, IOException;

    // upadate to all user 
	public void broadCastCancas(CanvasMessageInterface message)throws RemoteException;

	//sendImage to user
	public byte[] sendImage()throws IOException, RemoteException;

	//refresh canvasn as user request
	public void refreshCanvas() throws RemoteException;

	//send the open image to user
	public void sedOpenedImage(byte[] byteArray)throws IOException, RemoteException;

	//add chat text to user client
	public void addChat(String text) throws RemoteException;

	//remove all user when manager is quit
	public void removeAll() throws IOException, RemoteException;

	//remove self user
	public void removeMe(String clientName) throws RemoteException;


}
