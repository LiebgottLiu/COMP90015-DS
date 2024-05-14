package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import remote.CanvasMessageInterface;
import remote.CanvasServerInterface;

public interface CanvasClientInterface extends Remote {

	//get client name 
	public String getName() throws RemoteException;

	//set client name
	public void setName(String client_name) throws RemoteException;

	//assing client as image
	public void assignManager() throws RemoteException;
	
	//verify is the manager
	public boolean getManager() throws RemoteException;
	
	//ask manager permission
	public boolean askPermission(String name)throws RemoteException;
	
	//set the client permission state
	public void setPermission(boolean permission)throws RemoteException;
	
	// update new client to userlist
	public void updateUserList(Set<CanvasClientInterface> clientsList) throws RemoteException;
	
	//synca canvas for all user 
	public void syncCanvas(CanvasMessageInterface message)throws RemoteException;
	
	//refresh canvas for all user 
	public void refreshCanvas()throws RemoteException;
	
	//when user leave, close the ui pages
	public void closeUI()throws IOException;
	
	//send the image to server
	public byte[] sendImage()throws RemoteException, IOException;
	
	//drawing the opend image
	public void drawOpenedImage(byte[] rawImage)throws RemoteException, IOException;

	// drawing current boaed.
	public void drawBoard(CanvasServerInterface server)throws RemoteException;

	// get the current users permission
	public boolean getPermission()throws RemoteException;

	//add the chat box message to chat
	public void addChat(String text) throws RemoteException;
	
	
}
