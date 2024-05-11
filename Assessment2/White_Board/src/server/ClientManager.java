package server;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import remote.CanvasClientInterface;

public class ClientManager implements Iterable<CanvasClientInterface> {

	private Set<CanvasClientInterface> clientList;
	private int UserCounter = 0;
	private String managerUID = null;
	
	public ClientManager(CanvasServer canvasServer) {
		this.clientList = Collections.newSetFromMap(new ConcurrentHashMap<CanvasClientInterface, Boolean>());
		
	}
	
	public void addClient(CanvasClientInterface client) throws RemoteException {
		String uid = String.format("%s (%d)", client.getName(), UserCounter);
		
		if(managerUID == null) {
			managerUID = uid;
		}
		UserCounter++;
		this.clientList.add(client);
	}
	
	public void deleteClient(CanvasClientInterface client) {
		this.clientList.remove(client);
	}
	
	public Set<CanvasClientInterface> getClientList(){
		return this.clientList;
	}
	
	public boolean isEmpty() {
		return this.clientList.size() == 0; 
	}

	@Override
	public Iterator<CanvasClientInterface> iterator() { 
		return clientList.iterator();
	}

}
