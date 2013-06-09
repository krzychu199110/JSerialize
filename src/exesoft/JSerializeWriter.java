package exesoft;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;


public interface JSerializeWriter{
	
	Map<String, Object> prepareMap(Object ob);   /// WILL BE REMOVED FROM INTERFACE
	boolean writeObject(OutputStream os, Object ob);
	void printMap(Map<String,  Object> map); //FOR TESTING ONLY. WILL BE REMOVED
}
