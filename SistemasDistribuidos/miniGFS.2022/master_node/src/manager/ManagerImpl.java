// Implementación de la interfaz del manager

package manager;
import java.util.List;
import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.*;

import interfaces.*;

public class ManagerImpl extends UnicastRemoteObject implements Manager {
    public static final long  serialVersionUID=1234567891;
    ArrayList<DataNode> dataNodes;
    int contador;

    public ManagerImpl() throws RemoteException {
    	contador = -1;
    	dataNodes = new ArrayList<DataNode>();
    }
    // alta de un nodo de datos
    public synchronized void addDataNode(DataNode n) throws RemoteException {
    	dataNodes.add(n);
    }
    // obtiene lista de nodos de datos del sistema
    public synchronized List <DataNode> getDataNodes() throws RemoteException {
        return dataNodes;
    }
    // método no remoto que selecciona un nodo de datos para ser usado
    // para almacenar un chunk
    public synchronized DataNode selectDataNode() {
    	if(contador + 1 < dataNodes.size()) {
    		contador++;
    	}
    	else {
    		contador = 0;
    	}
        return dataNodes.get(contador);
    }
}

