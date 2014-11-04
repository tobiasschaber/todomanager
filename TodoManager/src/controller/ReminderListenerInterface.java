package controller;

import data.TodoItemStack;


/**
 * interface, welches ein reminder listener implementieren muss,
 * um über aktive reminder informiert werden zu können.
 * @author Tobias Schaber
 */
public interface ReminderListenerInterface {
	
	
	/**
	 * wird aufgerufen, sobald ein reminder aktiv wurde
	 * @param message die reminder nachricht
	 * @param headline die Überschrift des reminds 
	 */
	public void showAlert(TodoItemStack tis);

}
