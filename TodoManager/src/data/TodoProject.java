package data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import main.Logger;


/**
 * diese klasse repräsentiert ein ganzes projekt mit mehreren todo listen,
 * die gemeinsam gespeichert werden. Es ist als Observable implementiert,
 * um von der GUI beobachtet werden zu können.
 * @author Tobias Schaber
 */
public class TodoProject extends Observable implements Serializable {
	
	private static final long serialVersionUID = -9028548317032394183L;

	/* Liste aller Todo-Listen die in diesem Projekt enthalten sind */
	private ArrayList<TodoList> todoProjects = new ArrayList<TodoList>();
	
	/* gibt an ob an diesem projekt ungespeicherte Änderungen existieren */
	public boolean				unsavedChanges = false;
	
	/* Pfad von wo das Projekt geladen wurde, wenn es geladen und nicht neu angelegt wurde */ 
	private File					savedUnder;
	
	

	
	public File getSavedUnder() {
		return savedUnder;
	}
	
	public void setSavedUnder(File f) {
		this.savedUnder = f;
		setChanged();
		notifyObservers(this);
	}
	
	/**
	 * fügt dem projekt eine neue todoliste hinzu
	 * @param list
	 */
	public void addList(TodoList list) {
		
		this.unsavedChanges = true;
		todoProjects.add(list);
		
		setChanged();
		notifyObservers(this);
	}
	
	
	
	/**
	 * Liefert die Liste aller TodoProjects 
	 * @return
	 */
	public ArrayList<TodoList> getLists() {
		return todoProjects;
	}
	
	
	/**
	 * Benachrichtigt alle Oberserver, wenn sich Änderungen ergeben haben.
	 * Diese können auch in irgend einem Child-Element passiert sein
	 * @param tl
	 */
	public void setUnsavedChanges(Object tl) {
		this.unsavedChanges = true;
		Logger.getInstance().log("objekt modifiziert. benachrichtige observer.", Logger.LOGLEVEL_INFO);
		setChanged();
		notifyObservers(tl);
		
	}
}
