package controller;

import java.io.*;

import org.apache.commons.io.FileUtils;

/**
 * Singleton Implementierung der Persistenz-Schicht. Kapselt die Prozesse
 * zum Lesen und Schreiben von Projekt-Dateien in serialisierte Objekte sowie XML.
 * @author Tobias Schaber
 */
public final class PersistenceHandlerImpl implements PersistenceHandlerInterface {
	
	private static final PersistenceHandlerImpl instance = new PersistenceHandlerImpl();
	private PersistenceHandlerImpl() {};
	public static PersistenceHandlerImpl getInstance() {
		return instance;
	}
	
	@Override
	public Object loadData(File sourceFile) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sourceFile));
		
		
		try {
			return ois.readObject();
		} 
		
		finally{
			ois.close();
		}
	}
	
	@Override
	public void storeData(File targetFile, Object objectToStore) throws FileNotFoundException, IOException {
		

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile));
		oos.writeObject(objectToStore);	
		oos.close();


	}
	
	@Override
	public void storeXML(File targetFile, String xmlString) throws FileNotFoundException, IOException, UnsupportedEncodingException {
		
		FileUtils.writeStringToFile(targetFile, xmlString, "UTF-8");

	}
	
	@Override
	public String loadXML(File sourceFile) throws FileNotFoundException, IOException {
		
		
		String s = FileUtils.readFileToString(sourceFile, "UTF-8");
			
		return s;
	}

}
