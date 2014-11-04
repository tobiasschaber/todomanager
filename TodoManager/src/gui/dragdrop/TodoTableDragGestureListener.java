package gui.dragdrop;

import gui.panels.todoeditor.TodoListPanel;

import java.awt.dnd.*;

import main.Logger;

/**
 * Dieser DragGestureListener wird an die Todo-Tabelle gehängt und erkennt
 * eine Drag-Bewegung. Sobald diese erkannt wird, wird die entsprechende Methode
 * aufgerufen und der Drag&Drop-Vorgang eingeleitet.
 * @author Tobias Schaber
 */
public class TodoTableDragGestureListener implements DragGestureListener {
	
	private final DragSource ds;

	// Wird gehalten um später das zu übermittelnde TodoItemStack Objekt holen zu können
	private final TodoListPanel tlp;
	
	/**
	 * Constructor
	 * @param ds
	 * @param tlp
	 */
	public TodoTableDragGestureListener(DragSource ds, TodoListPanel tlp) {
		this.ds = ds;
		this.tlp = tlp;
		
	}
	
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		
		/* Do not accept drops from locked lists */
		if(tlp.getSelectedTodoItemStack().getParent().isLocked()) {
			Logger.getInstance().log("Kein Drag&Drop,  da die Liste gesperrt ist", Logger.LOGLEVEL_INFO);
			
		} else {
			// Startet einen Drag&Drop-Vorgang und sendet das derzeit anvisierte TodoItemStack mit
			ds.startDrag(dge, DragSource.DefaultMoveDrop, new TodoItemStackTransferable(tlp.getSelectedTodoItemStack()), null);			
		}

		
	}
	

}
