package gui.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import main.BaseController;
import controller.ImageController;
import data.TodoList;

/**
 * Context-Menü für die Boxen auf der Übersichts-Seite. Zeigt Optionen für einzelne
 * Todo-Listen.
 * @author Tobias Schaber
 */
public class OverviewBoxContextMenu extends JPopupMenu implements ActionListener {
	
	private final JMenuItem			contextItemRename 	= new JMenuItem("Liste umbenennen");
	private final JMenuItem			contextItemLock		= new JMenuItem("Liste abschließen");

	private	final BaseController	controller;
	private TodoList				lastSelectedTodoList;
	
	public OverviewBoxContextMenu(BaseController controller) {

		
		this.controller = controller;
			
		initComponents();
		initListeners();
		this.setSize(100, 100);
	}
	
	private void initComponents() {
		
		/* Das Kontextmenü für die Boxen */

		contextItemLock.setIcon(ImageController.iconLock);
		contextItemRename.setIcon(ImageController.iconRename);

		this.add(contextItemRename);
		this.addSeparator();
		this.add(contextItemLock);
//		this.addSeparator();
		
	}
	
	
	private void initListeners() {

		contextItemRename	.addActionListener(this);
		contextItemLock		.addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == contextItemRename) {
			controller.renameTodoList(lastSelectedTodoList);
		}
		
		if(e.getSource() == contextItemLock) {
			controller.switchLockStatus(lastSelectedTodoList);
			controller.getBaseFrame().repaint();
		}

		
	}


	public void show(Component invoker, int x, int y, TodoList selectedTodoList) {
		lastSelectedTodoList = selectedTodoList;

		if(lastSelectedTodoList.isLocked()) {
			
			contextItemLock.setText("Wieder öffnen");
			contextItemLock.setIcon(ImageController.iconUnlock);
			contextItemLock.setToolTipText("Nach dem Entsperren kann die Liste wieder bearbeitet werden");
			
		} else {
			
			contextItemLock.setText("Liste abschließen");
			contextItemLock.setIcon(ImageController.iconLock);
			contextItemLock.setToolTipText("Eine gesperrte Liste kann nicht bearbeitet werden");
			
		}
		
		
		this.show(invoker, x, y);
		
	}
	
	
	
	
}
