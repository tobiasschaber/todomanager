package gui.models;

import javax.swing.AbstractListModel;

import data.Attachment;
import data.TodoItemStack;


/**
 * Model für Attachments. Enthält als Element den aktuellen TodoItemStack
 * und ändert diesen sofort. Dadurch ist derzeit keine Bestätigung durch
 * den "Speichern"-Button eines Todos erforderlich.
 * @author Tobias Schaber
 */
public class AttachmentListModel extends AbstractListModel<Attachment> {
	
	/* das TodoItemStack auf dem die Attachments geändert werden sollen */
	private TodoItemStack parent;
	
	
	
	public AttachmentListModel(TodoItemStack parent) {
		this.parent = parent;
		
	}
	
	
	
	/**
	 * Ändert den TodoItemStack, wenn ein anderes Todo Item ausgewählt wurde
	 * @param parent der neue TodoItemStack
	 */
	public void setTodoItemStack(TodoItemStack parent) {
		this.parent = parent;
		fireContentsChanged(this, 0, getSize()-1);
	}

	
	@Override
	public int getSize() {
		if(parent == null) return 0;
		return parent.getAttachments().size();
	}


	
	/**
	 * Fügt der Liste ein neues Attachment hinzu
	 * @param attachment
	 */
	public void addElement(Attachment attachment) {
		
		if(parent != null) {
			
			parent.addAttachment(attachment);
			fireContentsChanged(this, 0, getSize()-1);
		}
		
	}
	
	
	
	@Override
	public Attachment getElementAt(int position) {
		if(getSize() != 0 && position >= 0) {
			return parent.getAttachments().get(position);
		}
		return null;
	}
	
	
	/**
	 * Entfernt das Element an der gegebenen Stelle
	 * @param index
	 */
	public void removeElementAt(int index) {
		
		if(index != -1 && getSize() > 0 && index <= getSize()-1) {
			parent.removeAttachment(getElementAt(index));
			fireContentsChanged(this, 0, getSize()-1);
		}
		
	}
	
	
	
}


