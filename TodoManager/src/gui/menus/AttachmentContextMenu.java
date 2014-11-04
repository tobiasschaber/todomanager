package gui.menus;

import gui.panels.todoeditor.TodoDetailPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import main.Logger;
import data.Attachment;

/**
 * Context-Menü für die Attachment-Liste bei den Todo-Details.
 * Zeigt die Option an, das Quell-Verzeichnis einer Datei im Browser zu öffnen.
 * @author Tobias Schaber
 */
public class AttachmentContextMenu extends JPopupMenu implements ActionListener {

	
	private final JMenuItem 	contextItemShowSourceFolder = new JMenuItem("Öffne übergeordnetes Verzeichnis");
	private Attachment attachment;
	
	public AttachmentContextMenu() {
		
		
		initComponents();
		initListeners();
		this.setSize(100, 100);
		
	}
	
	private void initComponents() {
		this.add(contextItemShowSourceFolder);
	}
	
	private void initListeners() {
		contextItemShowSourceFolder.addActionListener(this);
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == contextItemShowSourceFolder) {

			if(attachment != null) {
				
				File openFile = new File("");
				
				if(attachment.getStorageMode() == Attachment.STORE_MODE_LOCAL) {
					openFile = attachment.getPathToFile().getParentFile();
				}
				
				if(attachment.getStorageMode() == Attachment.STORE_MODE_INTERN) {
					openFile = new File(TodoDetailPanel.ATTACHMENT_TEMP_FOLDER);
				}

				
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + openFile);
				} catch(Exception ex) {
					Logger.getInstance().logException("Fehler beim öffnen des Verzeichnisses eines Attachments", ex);
				}
			}
			
		}
	}
	
	
	public void show(Component invoker, int x, int y, Attachment att) {
		
		this.attachment = att;
		
		if(attachment.getStorageMode() == Attachment.STORE_MODE_LOCAL) {
			contextItemShowSourceFolder.setText("Öffne übergeordnetes Verzeichnis");
		}
		
		if(attachment.getStorageMode() == Attachment.STORE_MODE_INTERN) {
			contextItemShowSourceFolder.setText("Öffne temporäres Attachment-Verzeichnis");
		}
		
		
			this.show(invoker, x, y);
		
		
	}
	
	
}
