package remote;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

import remote.CanvasMessageInterface;
import remote.CanvasServerInterface;

public interface CanvasClientInterface {

	public String getName() throws RemoteException;

	public void setName(String client_name) throws RemoteException;

	public void assignManager() throws RemoteException;
	
	public boolean getManager() throws RemoteException;
	
	public boolean askPermission(String name)throws RemoteException;
	
	public void setPermission(boolean permission)throws RemoteException;
	
	public void updateUserList(Set<CanvasClientInterface> clientsList) throws RemoteException;
	
	public void syncCanvas(CanvasMessageInterface message)throws RemoteException;
	
	public void refreshCanvas()throws RemoteException;
	
	public void closeUI()throws RemoteException, IOException;
	
	public byte[] sendImage()throws RemoteException, IOException;
	
	public void drawOpenedImage(byte[] rawImage)throws RemoteException, IOException;

	public void drawBoard(CanvasServerInterface server);

	public boolean getPermission()throws RemoteException;

	public void addChat(String text) throws RemoteException;
	
	
}
