// Clase de cliente que proporciona los métodos para acceder a los ficheros.
// Corresponde al API ofrecida a las aplicaciones 

package client;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Arrays;
import java.util.List;
import java.nio.ByteBuffer;

import interfaces.*;

public class GFSFile {
	Master m;
	int chunkSize;
	File file;
	int seekValue;
    public GFSFile(String fileName){
    	try {

			m = (Master) Naming.lookup("//" + System.getenv("BROKER_HOST") + ":" + System.getenv("BROKER_PORT") + "/GFS_master");
			file = m.lookup(fileName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	chunkSize = Integer.parseInt(System.getenv("CHUNKSIZE"));
    	seekValue = 0;
    }
    // establece la posición de acceso al fichero
    public void seek(int off) {
    	seekValue = off;
    }
    // obtiene la posición de acceso al fichero
    public int getFilePointer() {
        return seekValue;
    }
    // obtiene la longitud del fichero en bytes
    public int length() throws RemoteException {
        return file.getNumberOfChunks() * chunkSize;
    }
    // lee de la posición actual del fichero la cantidad de datos pedida;
    // devuelve cantidad realmente leída, 0 si EOF;
    // actualiza la posición de acceso al fichero
    public int read(byte [] buf) throws RemoteException {
	int nChunks = buf.length / chunkSize;
    	int actualChunk = seekValue / chunkSize;
    	List<Chunk> chunks = file.getChunkDescriptors(actualChunk, nChunks);
    	int counter = 0;
    	byte [] buffer = new byte[chunkSize];
    	for(Chunk c: chunks) {
    		if(c != null) {
    			DataNode dataNode = c.getChunkDataNodes().get(0);
			try{
    				buffer = dataNode.readChunk(c.getChunkName());
			} catch(Exception e){
				return 0;
			}
        		System.arraycopy(buffer, 0, buf, counter*chunkSize, buffer.length);
    		} else {
    			Arrays.fill(buffer, (byte)0);
        		System.arraycopy(buffer, 0, buf, counter*chunkSize, buffer.length);
    		}
    		counter++;
    	}
    	seekValue += counter*chunkSize;
        return counter*chunkSize;
    }
    // escribe en la posición actual del fichero los datos especificados;
    // devuelve falso si se ha producido un error en writeChunk;
    // actualiza la posición de acceso al fichero
    public boolean write(byte [] buf) throws RemoteException {
    	int nChunks = buf.length / chunkSize;
    	int actualChunk = seekValue / chunkSize;
    	List<Chunk> chunks = file.allocateChunkDescriptors(actualChunk, nChunks);
    	int counter = 0;
    	for(Chunk c: chunks) {
    		List<DataNode> dataNodes = c.getChunkDataNodes();
    		DataNode primaryNode = dataNodes.remove(0);
    		byte [] buffer = Arrays.copyOfRange(buf, counter*chunkSize,(counter+1)*chunkSize);
    		primaryNode.writeChunk(dataNodes, c.getChunkName(), buffer);
    		counter++;
    	}
	seekValue += buf.length;
        return true;
    }
}

