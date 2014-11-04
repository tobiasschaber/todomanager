package gui.dragdrop;

import gui.models.TodoListModel;
import gui.panels.todoeditor.TodoListBasePanel;

import java.awt.dnd.*;

import main.Logger;
import data.TodoItemStack;
import data.TodoList;


/**
 * Dieser Listener lauscht auf dem Ziel-Objekt auf Drop-Operationen.
 * Außerdem beinhaltet er alle notwendigen Informationen über die Ziel-Tabelle,
 * in die das Drag-Objekt verschoben werden soll.
 * @author Tobias Schaber
 */
public class TodoItemStackDropTargetListener implements DropTargetListener {

	// In diese TodoList wird das neue Element nacher eingefügt
	private TodoList targetList;
	
	// Das Model der Ziel-Tabelle, damit es über Änderungen informiert werden kann
	private TodoListModel targetModel;


	/**
	 * Constructor
	 * @param tl die TodoListe, in die ein Oojekt eingefügt wird, wenn dieser Listener das Droppen abfängt
	 * @param targetPanel Das Panel, in dem sich die Ziel-Tabelle befindet
	 */
	public TodoItemStackDropTargetListener(TodoList tl, TodoListBasePanel targetPanel) {
		targetList = tl;
		
		// über das Panel wird das entsprechende Model organisiert
		this.targetModel = targetPanel.getTodoListPanel().getTodoListModel();
	}
	
	public void dropActionChanged(DropTargetDragEvent e) {}
	public void dragEnter(DropTargetDragEvent e) {} 
	public void dragExit(DropTargetEvent e) {}
	public void dragOver(DropTargetDragEvent e) {}
	
	/**
	 * Ein Element wird auf das überwachte Objekt gedropt
	 */
	public void drop(DropTargetDropEvent e) {
		
		try {
			
			e.acceptDrop(DnDConstants.ACTION_MOVE);
			
			// aus dem übermittelten Objekt den TodoItemStack extrahieren
        	TodoItemStack tis =  (TodoItemStack)e.getTransferable().getTransferData(TodoItemStackTransferable.TIS_FLAVOR);
        	
        	/* Do not accept drops on locked lists */
        	if(!targetList.isLocked()) {
        		
        		Logger.getInstance().log("Füge Element zu Ziel hinzu nach Drag&Drop : " + tis.getName(), Logger.LOGLEVEL_INFO);
        		/* der ziel-liste hinzufügen */
        		targetList.addTodoItemStack(tis);
        	
        		/* das Ziel-Model über Änderungen informieren */
        		targetModel.fireTableDataChanged();

        		e.getDropTargetContext().dropComplete(true);
        	} else {
        		Logger.getInstance().log("Kein Drag&Drop,  da die Ziel-Liste gesperrt ist", Logger.LOGLEVEL_INFO);
        	}
			
		} catch (Throwable t) { }

//		e.rejectDrop();
  }
	
	
	
}
