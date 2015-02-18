package data;

import java.io.Serializable;
import java.util.Date;


/**
 * ein todo item repräsentiert ein item der todo history (auch das
 * aktuellste element) auf unterster Ebene.
 * @author Tobias Schaber
 */
public class TodoItem implements Serializable {
	
	private static final long serialVersionUID = 2410032485289192666L;
	
	/* übergeordnetes element */
	private TodoItemStack parent;
	
	public static final String STATUS_TODO 		= "TODO";
	public static final String STATUS_PENDING 	= "PENDING";
	public static final String STATUS_WAIT 		= "WAIT";
	public static final String STATUS_DONE 		= "DONE";
	public static final String STATUS_ALARM 	= "ALARM";	
	
	/* Basic information about the todo */
	private String 	description;
	private String 	status;
	private String 	text;
	private Date 	lastModified;
	

	/**
	 * constructor
	 * @param description
	 * @param status
	 * @param text
	 * @param parent
	 */
	public TodoItem(String description, String status, String text, TodoItemStack parent) {
		this.parent = parent;
		this.description = description;
		this.status = status;
		this.text = text;
		this.lastModified = new Date();
	}
	
	
	/**
	 * constructor
	 * Dieser Konstruktor wird nur verwendet, wenn ein XML-Objekt in ein TodoItem umgewandelt werden muss
	 * @param description
	 * @param status
	 * @param text
	 * @param lastModified
	 * @param parent
	 */
	public TodoItem(String description, String status, String text, Date lastModified, TodoItemStack parent) {
		this.parent = parent;
		this.description = description;
		this.status = status;
		this.text = text;
		this.lastModified = lastModified;
		 
		
	}
	
	
	
	public String getDescription() {
		
		/* wenn null, hole wert von vorherigem todoitem */
		if(description == null) {
			return parent.getPreviousTodoItem(this).getDescription();
		}
		
		return description;
	}
	
	
	
	/**
	 * Liefert den status des TodoItems. Ist dieser nicht gesetzt, wird der Status des vorangegangenen
	 * TodoItems erfragt.
	 * @return
	 */
	public String getStatus() {

		/* wenn null, hole wert von vorherigem todoitem */
		if(status == null) {
			return parent.getPreviousTodoItem(this).getStatus();
		}
		return status;
	}

	
	
	public String getText() {
		
		/* wenn null, hole wert von vorherigem todoitem */
		if(text == null) {
			return parent.getPreviousTodoItem(this).getText();
		}
		return text;
	}

	
	
	public void setDescription(String description) {
		this.description = description;
		parent.setUnsavedChanges(this);
	}

	
	
	public void setStatus(String status) {
		this.status = status;
		parent.setUnsavedChanges(this);
	}



	public void setText(String text) {
		this.text = text;
		parent.setUnsavedChanges(this);
	}
	
	
	
	/**
	 * gibt das datum zurück, an dem das todo item erzeugt wurde
	 * @return Date Zeitpunkt der Erzeugung des Todo Items
	 */
	public Date getModificationDate() {
		return lastModified;
	}


}
