package data;

import java.io.File;
import java.io.Serializable;

import main.Logger;

import org.apache.commons.io.FileUtils;

/**
 * Ein Attachment ist eine Datei, die einem Todo-Item angehängt wurde.
 * Es kann entweder als Referenz auf eine externe Datei, oder aber als
 * binäre Repräsentation vorliegen. Dies wird durch die unterschiedlichen
 * STORE_MODE_* abgebildet.
 * 
 * Soll eine intern gespeicherte Datei geöffnet werden, so wird sie zunächst
 * in ein temporäres Verzeichnis kopiert und mit einem eindeutigen Prefix versehen.
 * @author Tobias Schaber
 */
public class Attachment implements Serializable {
	
	private static final long serialVersionUID = 2410032935889192666L;
	
	/* Modi der Speicherung */
	public static final int STORE_MODE_NOSELECTION = -1; 	// Keine Auswahl bisher
	public static final int STORE_MODE_INTERN = 1;			// Intern
	public static final int STORE_MODE_LOCAL  = 2;			// Extern als Referenz

	/* basic information about the attachment */
	private int storageMode;
	
	/* inhalt der datei, wenn storeMode INTERN gewählt ist */
	private byte[] content;
	
	/* Pfad zur Datei wenn storeMode EXTERN gewählt ist */
	private File pathToFile;
	
	private String fileName;
	

	/* zufallszahl die beim ersten erzeugen einer temporären datei hiervon angelegt wird */
	private transient long tempRandomNr = 0;

	
	/**
	 * Constructor for recreation only
	 * @param parent übergeordnetes Element
	 * @param storageMode Modus wie das Attachment gespeichert wird. Eins von STORE_MODE_...
	 * @param content Inhalt der Datei für storageMode INTERN
	 * @param pathToFile Pfad zur Datei für storageMode LOCAL
	 */
	public Attachment(TodoItemStack parent, int storageMode, File pathToFile, byte[] content) {
//		this.parent = parent;
		this.storageMode = storageMode;
		this.pathToFile = pathToFile;
		this.fileName = pathToFile.getName();
		this.content = content;
	}
	
	
	/**
	 * Default constructor for attachments
	 * @param parent übergeordnetes Element
	 * @param storageMode Modus wie das Attachment gespeichert wird. Eins von STORE_MODE_...
	 * @param content Inhalt der Datei für storageMode INTERN
	 * @param pathToFile Pfad zur Datei für storageMode LOCAL
	 */
	public Attachment(int storageMode, File pathToFile) {
		
		this.storageMode = storageMode;
		this.fileName = pathToFile.getName();
		this.pathToFile = pathToFile;
		
		if(storageMode == STORE_MODE_INTERN) {
				
			try {
				content = FileUtils.readFileToByteArray(pathToFile);
				
			} catch(Exception e) {
				Logger.getInstance().logException("Kann Datei nicht einlesen", e);

			}
		}
		
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public int getStorageMode() {
		return storageMode;
	}
	
	public File getPathToFile() {
		return pathToFile;
	}
	
	public byte[] getContent() {
		return content;
	}


	/**
	 * Liefert einen temporären Dateinamen für diese Datei zurück, der jedoch
	 * immer gleich ist. Dadurch muss die Temp-Datei nicht jedes mal neu geschrieben werden 
	 * @return Temporärer, eindeutiger Dateiname
	 */
	public String getTemporaryFileName() {

		if(tempRandomNr == 0) {
			
			tempRandomNr = Math.round(Math.random()*1000000000);
		}
		
		return tempRandomNr + "_" + getFileName();

	}
	
}
