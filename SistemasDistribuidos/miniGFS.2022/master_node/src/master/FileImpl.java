// Implementación de la interfaz remota para el acceso a la información de ubicación de un fichero

package master;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.*;
import java.rmi.server.*;
import manager.*;
import interfaces.*;

public class FileImpl extends UnicastRemoteObject implements File {
    public static final long  serialVersionUID=1234567891;
    Map<Integer, Chunk> chunkMap;
    ManagerImpl m;
    int replicaFactor;
    int numeroChunks;
    public FileImpl(ManagerImpl m, int replicaFactor) throws RemoteException {
    	chunkMap = new HashMap<Integer, Chunk>();
    	this.m = m;
    	this.replicaFactor = replicaFactor;
	numeroChunks = 0;
    }
    // nº de chunks del fichero
    public int getNumberOfChunks() throws RemoteException {
        return numeroChunks;
    }
    // obtiene información de ubicación de los chunks especificados del fichero
    public List <Chunk> getChunkDescriptors(int nchunk, int size) throws RemoteException {
    	ArrayList<Chunk> list = new ArrayList<Chunk>();
    	for(int i = nchunk; i < nchunk+size; i++) {
    		if(chunkMap.containsKey(i)) {
    			list.add(chunkMap.get(i));
    		}
    		else if(numeroChunks > i){
    			list.add(null);
    		}
    	}
        return list;
    }
    // reserva información de ubicación para los chunks especificados del fichero
    public List <Chunk> allocateChunkDescriptors(int nchunk, int size) throws RemoteException {
	if(nchunk+size > numeroChunks) {
    		numeroChunks = nchunk + size;
    	}	
    	List<Chunk> list = new ArrayList<Chunk>();
    	for(int j = nchunk; j < nchunk+size; j++) {
	    	if(chunkMap.containsKey(j)) {
	    		list.add(chunkMap.get(j));
	    	}
	    	else {
	    		ArrayList<DataNode> dn = new ArrayList<DataNode>();
	    		for(int i = 0; i < replicaFactor; i++) {
	    			dn.add(m.selectDataNode());
	    		}
	    		ChunkImpl chunk = new ChunkImpl(dn);
	    		chunkMap.put(j, chunk);
	    		list.add(chunk);
	    	}
    	}
    	return list;
    }
}

