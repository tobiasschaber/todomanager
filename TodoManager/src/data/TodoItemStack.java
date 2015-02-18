package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import main.Logger;
import utils.DateUtils;

/**
 * diese klasse repräsentiert ein komplettes todo-item. es erhält einen Namen
 * und besitzt eine history von todo items.
 * @author Tobias Schaber
 */
public class TodoItemStack implements Serializable, Comparable<TodoItemStack> {
	
	private static final long serialVersionUID = -9028563538032198753L;
	
	
	/* übergeordnetes todo projekt */
	private TodoList 		parent;
	
	/* angehängte attachments */
	private ArrayList<Attachment> attachments = new ArrayList<Attachment>();
	
	
	/* die history inkl. aktuelles element */
	private Stack<TodoItem> todoHistory;
	private int 			todoId;
	private String 			name;
	private int				priority;
	
	/* zeitplan informationen */
	private Date			planStart;
	private Date			planEnd;
	
	private String			category;
	
	
	/* Reminder information */
	private Date 	reminderDate;
	private boolean reminderIsActive = false;
	

	
	/**
	 * constructor with additional status information
	 * @param name
	 * @param id
	 * @param parent
	 * @param status
	 */
	public TodoItemStack(String name, int id, TodoList parent, String status, int priority, Date dateStart, Date dateEnd, ArrayList<Attachment> attachments)  {
		this.parent = parent;
		this.name = name;
		this.todoHistory = new Stack<TodoItem>();
		this.todoId = id;
		this.priority = priority;
		this.planStart = dateStart;
		this.planEnd = dateEnd;
		
		
		updateTodo(status, "", name);
	}
	
	
	/**
	 * Dieser Konstruktor ist für das Initialisieren gedacht
	 * @param name
	 * @param id
	 * @param parent
	 * @param initMode
	 */
	public TodoItemStack(String name, int id, TodoList parent, boolean initMode, int priority, Date start, Date end, String category) {
		this.parent = parent;
		this.name = name;
		this.todoHistory = new Stack<TodoItem>();
		this.todoId = id;
		this.priority = priority;
		this.planStart = start;
		this.planEnd = end;
		if(category != "") this.category = category;
	}
	
	
	
	/**
	 * führt eine Änderung an der todo-historie durch, das bedeutet, füge ein neues todo item
	 * an letzter stelle ein. todo items können nicht verändert sondern verbleiben auch beim 
	 * aktualisieren der liste in der history
	 * @param newStatus der neue status welcher gesetzt werden soll
	 * @param newText der neue text
	 * @param newDescription eine neue beschreibung
	 */
	public void updateTodo(String newStatus, String newText, String newDescription) {
		String setStatus;
		String setText;
		String setDesc;
		
		TodoItem lastItem = getLatestTodo();

		// Wenn bisher noch kein Element existiert, wird das default standard neues objekt verwendet
		if(lastItem == null) {

			lastItem = new TodoItem("Neues Item", "TODO", "", this);
		}

		if(newStatus == null) 		setStatus = lastItem.getStatus();
		else						setStatus =  newStatus;
		
		if(newText == null) 		setText = lastItem.getText();
		else						setText = newText;
		
		if(newDescription == null)	setDesc = lastItem.getDescription();
		else						setDesc = newDescription;
		
		TodoItem tmpItem = new TodoItem(setDesc, setStatus, setText, this);
		todoHistory.add(tmpItem);
		
		parent.setUnsavedChanges(this);
		
	}

	
	
	/**
	 * liefert das aktuellste todoitem zurück
	 * @return das aktuellste todo item oder null wenn keines existiert
	 */
	public TodoItem getLatestTodo() {
		
		try {
			return todoHistory.lastElement();
		}
		
		catch(NoSuchElementException nsee) {
			return null;
		}
		
	}

	
	
	/**
	 * liefert die todo-id dieses stacks
	 * @return
	 */
	public int getTodoId() {
		return todoId;
	}
	
	

	/**
	 * liefert den namen des stacks
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * liefert die priorität (skala 1-100)
	 * @return
	 */
	public int getPriority() {
		return priority;
	}
	
	public Date getPlanStart() {
		return planStart;
	}
	
	public Date getPlanEnd() {
		return planEnd;
	}
	
	public String getCategory() {
		return category;
	}
	
	public boolean getReminderActivationStatus() {
		return this.reminderIsActive;
	}
	
	public Date getReminderDate() {
		return this.reminderDate;
	}	
	
	
	public Stack<TodoItem> getTodoHistory() {
		return todoHistory;
	}


	
	public void setReminderActivationStatus(boolean newStatus) {

		this.reminderIsActive = newStatus;
		parent.setUnsavedChanges(this);
	}
	
	public void setCategory(String cat) {
		this.category = cat;
		parent.setUnsavedChanges(this);
	}
	
	public void setReminderDate(Date newDate) {
		this.reminderDate = newDate;
		parent.setUnsavedChanges(this);
	}
	
	public void setPriority(int prio) {
		this.priority = prio;
		parent.setUnsavedChanges(this);
	}
	
	public void setPlanStart(Date d) {

		this.planStart = DateUtils.removeTimeFromDate(d);
		parent.setUnsavedChanges(this);
	}
	
	public void setPlanEnd(Date d) {
		this.planEnd = DateUtils.removeTimeFromDate(d);
		parent.setUnsavedChanges(this);
	}
	

	
	/**
	 * aktualisiert den änderungen-status des übergeordneten todo project
	 */
	public void setUnsavedChanges(Object ti)  {
		parent.setUnsavedChanges(ti);
	}
	

	/**
	 * durchsucht den ganzen todoitemstack nach dem gegebenen string
	 * @param search der suchstring
	 * @return true wenn der string irgendwo enthalten ist, ansonsten false
	 */
	public boolean contains(String search) {
		search = search.toLowerCase();
		
		if(name.toLowerCase().contains(search)) return true;
		
		Iterator<TodoItem> it = todoHistory.iterator();
		
		while(it.hasNext()) {
			TodoItem ti = it.next();
			
			
			if(ti.getDescription().toLowerCase().contains(search)) return true;
			if(ti.getStatus().toLowerCase().contains(search)) return true;
			if(ti.getText().toLowerCase().contains(search)) return true;

		}
		
		return false;
		
		
	}
	
	
	/**
	 * liefert das übergeordnete TodoList-Objekt zurück
	 * Wird benötigt bei Drag&Drop
	 * @return
	 */
	public TodoList getParent() {
		return parent;
	}
	
	
	/**
	 * Entfernt die alte ID und setzt eine neue ID.
	 * Wird benötigt bei Drag&Drop, da für die Ziel-Liste eine neue ID benötigt wird
	 * @param newID
	 */
	public void setNewID(int newID) {
		this.todoId = newID;
		if(parent != null)		parent.setUnsavedChanges(this);
	}
	
	
	/**
	 * Entfernt das alte +bergeordnete Element und ersetzt es durch ein neues.
	 * Wird benötigt bei Drag&Drop da es dort einem neuen Parent zugeordnet wird.
	 * @param newParent
	 */

	public void setNewParent(TodoList newParent) {
		this.parent = newParent;
		if(parent != null) parent.setUnsavedChanges(this);
	}
	
	
	/**
	 * Diese Methode vergleicht zwei TodoItemStacks auf Basis Ihrer Priorität, und zwar absteigend
	 */
	@Override
	public int compareTo(TodoItemStack o) {
		
		if(getPriority() < o.getPriority()) return 1;
		if(getPriority() > o.getPriority()) return -1;

		return 0;
	}
	
	



	/**
	 * Fügt dem TodoItemStack ein neues Attachment hinzu
	 * @param storageMode Die Art wie die Datei gespeichert wird (Attachment.STORE_MODE_...)
	 * @param content Der Inhalt der Datei (wenn StorageMode _INTERN)
	 * @param pathToFile Pfad zur Datei (wenn StorageMode _LOCAL)
	 */
	public void addAttachment(Attachment newAttachment) {
		
		for(Attachment att : attachments) {
			
			if(att.getPathToFile() != null && att.getPathToFile().equals(newAttachment.getPathToFile())) {
				Logger.getInstance().log("Datei existiert bereits in den Attachments. " + att.getPathToFile().getAbsolutePath(), Logger.LOGLEVEL_INFO);
				return;
			}
		}
	
		attachments.add(newAttachment);
		
		parent.setUnsavedChanges(this);
				
	}
	
	
	/**
	 * Entfernt ein Attachment aus der Liste
	 * @param attachment
	 */
	public void removeAttachment(Attachment attachment) {
		
		attachments.remove(attachment);
		
		parent.setUnsavedChanges(this);
	}
	
	
	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}
	
	
	/**
	 * Liefert in Stunden die Zeit zurück, seit der der TIS nicht mehr modifiziert wurde
	 * @return Zeit in Stunden
	 */
	public int getNotModifiedDurationHours() {
		
		
		Date modDate = getLatestTodo().getModificationDate();
		Date now = new Date();
		
		/* Berechne Dauer seit der letzten Veränderung bis heute */
		long dif = now.getTime() - modDate.getTime();
				
		/* Umrechnungsfaktor auf Stunden */
		final long div = 1000*60*60;
		
		return (int)(dif/div);
		
		
	}


	
	/**
	 * Liefere für ein gegebenes TodoItem den Vorgänger
	 * @param callingTodoItem
	 * @return
	 */
	public TodoItem getPreviousTodoItem(TodoItem callingTodoItem) {
		
		int ancestorIndex = todoHistory.indexOf(callingTodoItem)-1;
		
		if(ancestorIndex >= 0) {
		
			return todoHistory.get(ancestorIndex);
			
		} else {
			return null;
		}
		
	}
	

}
