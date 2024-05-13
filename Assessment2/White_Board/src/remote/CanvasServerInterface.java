package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import client.MessageWrapper;

public interface CanvasServerInterface extends Remote {

	public Set<CanvasClientInterface> getClientList()throws RemoteException;

	public void register(CanvasClientInterface client) throws RemoteException;

	public void deleteClient(String name) throws RemoteException, IOException;

	public void broadCastCancas(CanvasMessageInterface message)throws RemoteException;

	public byte[] sendImage()throws IOException, RemoteException;

	public void refreshCanvas() throws RemoteException;

	public void sedOpenedImage(byte[] byteArray)throws IOException, RemoteException;

	public void addChat(String text) throws RemoteException;

	public void removeAll() throws IOException, RemoteException;

	public void removeMe(String clientName) throws RemoteException;


}
