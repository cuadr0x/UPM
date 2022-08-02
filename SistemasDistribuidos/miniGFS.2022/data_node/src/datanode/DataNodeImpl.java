// Implementación de la interfaz de un nodo de datos

package datanode;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.io.File;
import java.nio.file.Files;

import interfaces.*;

public class DataNodeImpl extends UnicastRemoteObject implements DataNode {
    public static final long  serialVersionUID=1234567891;
    String name;

    public DataNodeImpl(Manager m, String n) throws RemoteException {
    	m.addDataNode(this);
    	name = n;
    }
    // nombre del nodo
    public String getName() throws RemoteException {
        return name;
    }
    // lee el fichero que contiene un chunk
    public byte [] readChunk(String chunkName) throws RemoteException {
	byte [] chunkData = new byte[64000000];
            FileInputStream input;
			try {
				java.io.File file = new java.io.File("./" + name + "/" + chunkName);
				chunkData = Files.readAllBytes(file.toPath());
				//input = new FileInputStream(file);
				//input.read(chunkData);
				//input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return chunkData;
    }
    // escribe en un fichero local el contenido del chunk;
    // si la lista de nodos pasada como parámetro no esta vacía,
    // propaga la escritura a los nodos de datos de la lista
    public boolean writeChunk(List <DataNode> nodes, String chunkName, byte [] buffer) throws RemoteException {
    	if(nodes == null) {
	    	try 	{ 
			java.io.File f = new java.io.File("./" +name + "/" + chunkName);
				if (!f.getParentFile().exists()){
    					f.getParentFile().mkdirs();
				}
				if (!f.exists()){
    					f.createNewFile();
				}
				FileOutputStream output=new FileOutputStream(f);
				output.write(buffer);
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
    	}
	else {
		LockManager lm = new LockManager();
            	LockManager.Lock lck;
            	java.io.File f = new java.io.File("./" + name + "/" + chunkName);
            	if (!f.getParentFile().exists()){
                    f.getParentFile().mkdirs();
	            }
	            if (!f.exists()){
	                    try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
	            }
	            FileOutputStream output;
				try {
					output = new FileOutputStream(f);
					output.write(buffer);
		            output.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
            	for(DataNode d: nodes) {
            		(lck = lm.openLock(chunkName)).lock();
            		d.writeChunk(null, chunkName, buffer);
                	lck.unlock();
            	}
        }
        return true;
        
    }
}

