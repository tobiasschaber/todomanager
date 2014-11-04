package controller;

import java.io.*;


/**
 * Interface für die Persistenz-Schicht. Soll eine andere Persistenz-Art
 * gewählt werden, so ist dieses Interface zu verwenden.
 * @author Tobias Schaber
 */
public interface PersistenceHandlerInterface {
	
	/**
	 * Erzeugt ein Objekt aus einer angegebenen Datei 
	 * @param sourceFile die Datei die geladen werden soll
	 * @return das aus der Datei gelesene Objekt
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public Object loadData(File sourceFile) throws IOException, FileNotFoundException, ClassNotFoundException;
	
	
	
	/**
	 * Speichert ein gegebenes Objekt unter dem angegebenen File
	 * @param targetFile die Datei in der das Objekt gespeichert werden soll
	 * @param objectToStore das Objekt, welches persistiert werden soll
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void storeData(File targetFile, Object objectToStore) throws FileNotFoundException, IOException;

	
	
	/**
	 * 
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public String loadXML(File sourceFile) throws IOException, FileNotFoundException;
	
	/**
	 * 
	 * @param targetFile
	 * @param xmlString
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void storeXML(File targetFile, String xmlString) throws IOException, FileNotFoundException;
}
