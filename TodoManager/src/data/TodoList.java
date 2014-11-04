package data;

import java.io.Serializable;
import java.util.*;

/**
 * die todo liste repräsentiert eine ganze liste an todos, welche
 * zusammengefasst zu einer ganzen todoliste werden.
 * @author Tobias Schaber
 */
public class TodoList implements Serializable {
	
	private static final long serialVersionUID = -9028563538032394183L;
	
	private TodoProject			parent;
	
	private String 				listName;
	private List<TodoItemStack>	todoList;
	
	/* "auto-increment" vergabe von ids an todoitemstacks */
	public int					lastUsedId;
	
	private boolean				isLocked;


	
	/**
	 * constructor
	 * @param listName
	 */
	public TodoList(String listName, TodoProject parent) {
		this.parent   = parent;
		this.listName = listName;
		this.todoList = new ArrayList<TodoItemStack>();
		this.isLocked = false;
	}

	/**
	 * liefert die anzahl aller todos zurück
	 * @return
	 */
	public int countTodos() {
		return todoList.size();		
	}
	
	
	/**
	 * liefert die anzahl aller todos in diesem status zurück
	 * @param forStatus
	 * @return
	 */
	public int countTodos(String forStatus) {
		int ct = 0;
		Iterator<TodoItemStack> it = todoList.iterator();
		
		while(it.hasNext()) {
			TodoItemStack tis = it.next();

			if(tis.getLatestTodo().getStatus().equals(forStatus)) {
				ct++;
			}
		}
		
		return ct;
		
	}
	
	
	
	/**
	 * liefert die anzahl aller todos zurück, die eine Priorität
	 * kleiner oder gleich der übergebenen Priorität haben.
	 * @param priority
	 * @return
	 */
	public int countTodosByPriority(int priority) {
		int ct = 0;
		Iterator<TodoItemStack> it = todoList.iterator();
		
		while(it.hasNext()) {
			TodoItemStack tis = it.next();
			
			if(tis.getPriority() <= priority) {
				ct++;
			}
			
		}
		return ct;
	}
	
	
	
	/**
	 * liefert zurück, ob die liste ein element enthält, welches sich derzeit im status "alarm" befindet
	 * @return
	 */
	public boolean hasAlarm() {
		
		if(todoList.size() == 0) return false;
		
		Iterator<TodoItemStack> it = todoList.iterator();
		
		while(it.hasNext()) {
			TodoItemStack tisTmp = it.next();
			
			if(tisTmp.getLatestTodo() != null && tisTmp.getLatestTodo().getStatus().equals(TodoItem.STATUS_ALARM)) {
				return true;
			}
			
		}
		
		return false;
	}

	
	
	/**
	 * returns the number of todos in this list which were not "DONE"
	 * @return number of unsolved todos
	 */
	public int countUnsolvedTodos() {
		
		if(todoList.size() == 0) return 0;
		
		int ct = 0;
		
		Iterator<TodoItemStack> it = todoList.iterator();
				
		while(it.hasNext()) {
			TodoItemStack tisTmp = it.next();
			
			
			if(tisTmp.getLatestTodo() != null && tisTmp.getLatestTodo().getStatus() != null && !(tisTmp.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE))) {
				ct++;
			}
	
		}
		
		return ct;
	}

	
	
	/**
	 * liefert die todo liste zurück
	 * @return
	 */
	public List<TodoItemStack> getTodoList() {
		return todoList;
	}
	
	
	/**
	 * liefert die todo liste ohne DONEs zurück
	 * @return
	 */
	public List<TodoItemStack> getUnDoneTodoList() {
		List<TodoItemStack> tmpList = new ArrayList<TodoItemStack>();
		
		for(TodoItemStack tis : todoList){
			if(!tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
				tmpList.add(tis);
			}
		}
		
		return tmpList;
	}
	
	
	
	/**
	 * liefert einen todo item stack auf basis seiner id zurück
	 * @param id
	 * @return
	 */
	public TodoItemStack getTodoItemStackById(int id) {
		Iterator<TodoItemStack> it = todoList.iterator();
		
		while(it.hasNext()) {
			TodoItemStack tmpStack = it.next();
			if(tmpStack.getTodoId() == id) {
				return tmpStack;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * liefert den namen der liste zurück
	 * @return
	 */
	public String getListName() {
		return listName;
	}
	
	
	
	/**
	 * aktualisiert den namen der tod liste
	 * @param newName der neue name
	 */
	public void setListName(String newName) {
		listName = newName;
		parent.setUnsavedChanges(this);
	}
	
	
	/**
	 * aktualisiert den locked-status
	 * @param locked
	 */
	public void switchLockStatus() {
		
		if(isLocked) isLocked = false;
		else		 isLocked = true;
		
		parent.setUnsavedChanges(this);
	}
	
	
	/**
	 * setzt den status von isLocked auf einen bestimmten Wert. Erzeugt KEINE Änderungsnotifikation.
	 * Darf NUR beim erzeugen von TodoListen beim Laden einer neuen Datei verwendet werden!
	 * @param isLocked
	 */
	public void setLockedStatus(boolean isLocked) {
		
		this.isLocked = isLocked;
	}
	
	
	/**
	 * liefert den aktuellen locked-status
	 * @return
	 */	
	public boolean isLocked() {
		return isLocked;
	}
	
	
	/**
	 * liefert true, wenn in der Liste derzeit nur Todos mit dem Status "Wait"
	 * vorhanden sind, andernfalls false. Ignoriert alle DONE Elemente.
	 * @return
	 */
	public boolean hasOnlyWaitingTodos() {
		
		if(todoList.size() == 0) return false;
		
		for(TodoItemStack tis : todoList) {
			if(!tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_WAIT)) {
				if(!tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	
	
	/**
	 * fügt der todo-liste ein neues element hinzu
	 * @param name name des neuen elements
	 * @param status initialer status des elements
	 */
	public void addTodoItemStack(String name, String status, int priority) {
		
		TodoItemStack tis = new TodoItemStack(name, (++lastUsedId), this, status, priority, null, null, null);
		todoList.add(tis);
		parent.setUnsavedChanges(this);
		
	}

	
	
	/**
	 * prüft, ob die todoliste einen oder mehrere aktive reminder besitzt
	 * @return true wenn einer oder mehr reminder aktiv sind, sonst false
	 */
	public boolean hasActiveReminders() {
		
		if(todoList.size() == 0) return false;
		
		Iterator<TodoItemStack> it = todoList.iterator();
		
		while(it.hasNext()) {
			TodoItemStack tisTmp = it.next();
			
			if(tisTmp.getReminderActivationStatus() && tisTmp.getLatestTodo().getStatus() != TodoItem.STATUS_DONE)
				return true;	
		}
		
		return false;
	
	}

	
	
	/**
	 * propagiere unsaved status an übergeordnetes element
	 */
	public void setUnsavedChanges(Object tis) {
		parent.setUnsavedChanges(tis);
	}
	
	
	/**
	 * Entfernt ein TodoItemStack von der Liste.
	 * Wird derzeit benötigt, um das Element bei Drag&Drop aus der
	 * Quell-Liste zu entfernen
	 * @param tisRemove
	 */
	public void removeTodoItemStack(TodoItemStack tisRemove) {

		todoList.remove(tisRemove);

	}
	
	
	/**
	 * Fügt der Liste ein bereits existierendes, vollständiges, neues
	 * Element hinzu. Wird benötigt bei Drag&Drop da das Element dort
	 * ja bereits existiert
	 * @param newStack
	 */
	public void addTodoItemStack(TodoItemStack newStack) {
		newStack.setNewID(++lastUsedId);
		newStack.setNewParent(this);
		todoList.add(newStack);
		parent.setUnsavedChanges(this);
	}

}
