// Implementación de la interfaz remota Master

package master;
import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

import manager.*;
import interfaces.*;

public class MasterImpl extends UnicastRemoteObject implements Master {
	Map<String, File> fileMap; 
    public static final long  serialVersionUID=1234567891;
    ManagerImpl m;
    int replica;

    public MasterImpl(ManagerImpl m, int replica) throws RemoteException {
	fileMap= new HashMap<String, File>();
    	this.m = m;
    	this.replica = replica;
    }
    // obtiene acceso a la metainformación de un fichero
    public synchronized File lookup(String fname) throws RemoteException {
    	if(fileMap.containsKey(fname)) {
    		return fileMap.get(fname);
    	}
    	else {
    		File file = new FileImpl(m, replica);
    		fileMap.put(fname, file);
    		return file;
    	}
    }
}
