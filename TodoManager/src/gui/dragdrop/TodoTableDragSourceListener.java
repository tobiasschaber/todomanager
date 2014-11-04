package gui.dragdrop;

import gui.models.TodoListModel;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;

import main.Logger;
import data.TodoItemStack;

/**
 * Dieser DragSourceListener informiert die Quelle (Hier Todo-Tabelle), sobald
 * ein Drag&Drop-Vorgang beendet wurde. Dann muss das Element aus der
 * Liste gelöscht werden.
 * @author Tobias Schaber
 */
public class TodoTableDragSourceListener implements DragSourceListener {
	
	private final TodoListModel tlm;
	
	/**
	 * Constructor
	 * @param tlm
	 */
	public TodoTableDragSourceListener(TodoListModel tlm) {
		this.tlm = tlm;
	}
	
	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {}
	@Override
	public void dragOver(DragSourceDragEvent dsde) {}
	@Override
	public void dragExit(DragSourceEvent dse) {}
	@Override
	public void dragEnter(DragSourceDragEvent dsde) {}
	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		// Der Drag&Drop-Vorgang wurde beendet
		try {
			
			// Wenn der Drag&Drop-Vorgang erfolgreich war...
			if(dsde.getDropSuccess()) {
				
				Transferable tr = dsde.getDragSourceContext().getTransferable();
				TodoItemStack tis =  (TodoItemStack)tr.getTransferData(TodoItemStackTransferable.TIS_FLAVOR);
				
				Logger.getInstance().log("Entferne Element aus Quelle nach Drag&Drop : " + tis.getName(), Logger.LOGLEVEL_INFO);

				tis.getParent().removeTodoItemStack(tis);
				
				// Quell-Tabelle über Änderung informieren
				tlm.fireTableDataChanged();
			}
		} catch(Exception e) {}
		

		
	}

}
