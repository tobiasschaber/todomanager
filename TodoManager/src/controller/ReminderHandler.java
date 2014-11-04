package controller;

import java.util.*;

import main.Logger;
import data.*;


/**
 * Diese Klasse verwaltet den Thread zur überwachung der Reminder-Aktivitäten.
 * Es wird in einem zeitlichen Abstand geprüft, ob einer der Reminder aktiviert
 * werden muss, dann wird dieser ausgelöst.Es  können mehrere ReminderListener
 * angefügt werden, die bei aktivieren dann alle ausgelöst werden.
 * @author Tobias Schaber
 */
public class ReminderHandler extends Thread {
	
	// enthält alle derzeit aktiven reminder
	private TodoProject currentTodoProject;
	
	// enthält alle listener, die auf auslösende reminder warten
	private ArrayList<ReminderListenerInterface> listeners = new ArrayList<ReminderListenerInterface>();
	
	
	/**
	 * fügt einen reminderListener hinzu, welcher informiert wird, sobald ein reminder aktiv wird
	 * @param newListener der zu registrierende listener
	 */
	public void addListener(ReminderListenerInterface newListener) {
		this.listeners.add(newListener);
	}
	
	
	/**
	 * constructor
	 * es wird das zu überwachende todo project übergeben
	 */
	public ReminderHandler(TodoProject currentTodoProject) {
		this.setName("ReminderHandlerThread");
		this.currentTodoProject = currentTodoProject;
		Logger.getInstance().log("Erzeuge Reminder Handler", Logger.LOGLEVEL_INFO);
		
		
		
	}
	
	/**
	 * thread zur Überwachung der reminder
	 */
	public void run() {

		Logger.getInstance().log("Starte reminder Handler thread", Logger.LOGLEVEL_INFO);

		while(!isInterrupted()) {
			
			try {
				Thread.sleep(3000);
				
				// wenn der reminder nicht deaktiviert ist
				if(ConfigurationHandlerImpl.reminderIsActive) {
					
					Iterator<TodoList> it1 = currentTodoProject.getLists().iterator();
	
					while(it1.hasNext()) {
	
						TodoList tl = it1.next();
						
						Iterator<TodoItemStack> it2 = tl.getTodoList().iterator();
						
						// Über alle todoitemstacks iterieren, die aktive reminder enthalten können
						while(it2.hasNext()) {
							TodoItemStack tis = it2.next();
							
							// wenn ein reminder aktiv ist
							if(tis.getReminderActivationStatus()) {
								
								// wenn der reminder an oder über der zeit ist
								if(tis.getReminderDate().before(new Date())) {
									
									// wenn der status "done" ist, dann deaktiviere reminder
									if(tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
										tis.setReminderActivationStatus(false);
										Logger.getInstance().log("DONE-Todo mit aktivem Reminder gefunden." +
												" Reminder nun deaktiviert", Logger.LOGLEVEL_DEBUG);
									} else {
		
										// über alle registrierten listener iterieren
										for(ReminderListenerInterface ri : this.listeners) {
											
											// rufe bei allen registrierten listenern die methode showAlert(..) auf
											ri.showAlert(tis);
											
											Thread.sleep(3000);
											
										}
									}
								}
							}
						}
					}
				}
			}
				
				
			catch ( InterruptedException e ) {
				interrupt(); 
				Logger.getInstance().log("Interrupted ReminderHandler", Logger.LOGLEVEL_INFO);
			}
			catch(ConcurrentModificationException cme) {
				// die concurrend mod. exception ist nicht so tragisch und wird nur bei info geloggt
				Logger.getInstance().log(cme.getLocalizedMessage() + " im Reminder Thread", Logger.LOGLEVEL_INFO);
			}
			
			catch(Exception e) {
				Logger.getInstance().logException("Fehler im Reminder Thread", e);
			}
			
			
		}
		
	}

}
