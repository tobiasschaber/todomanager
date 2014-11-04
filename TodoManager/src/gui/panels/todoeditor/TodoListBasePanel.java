package gui.panels.todoeditor;

import gui.components.TodoTextEditor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import main.BaseController;
import controller.ImageController;
import data.TodoItemStack;
import data.TodoList;

/**
 * The TodoListBasePanel is the main panel representing a complete project.
 * @author Tobias Schaber
 */
public class TodoListBasePanel extends JPanel implements MouseListener {
	
	private static final Cursor cursorLocked = Toolkit.getDefaultToolkit().createCustomCursor(ImageController.imgCursorLocked, new Point(0, 0), "Locked Cursor");
	
	
	@SuppressWarnings("unused")
	private BaseController controller;
	
	private TodoList tl;
	
	private final JSplitPane splitPane = new JSplitPane();
	

    
    private final TodoDetailPanel todoDetailPanel;
    private final TodoListPanel todoListPanel;
    
    
    

    
    
    /**
     * constructor
     * @param tp TodoProject to be shown
     */
	public TodoListBasePanel(TodoList tl, BaseController bc) {
		this.controller = bc;
		this.tl = tl;
		this.setName(tl.getListName());
		
		todoDetailPanel = new TodoDetailPanel(this);
		todoListPanel = new TodoListPanel(this, tl);

		initComponents();
		initLayout();

	}
	
	
	/**
	 * liefert das beinhaltete todoproject zurück
	 * @return
	 */
	public TodoList getTodoList() {
		return tl;
	}
	
	
	/**
	 * initialisiert die grafischen komponenten
	 */
	private void initComponents() {
		
		
		

		

		
		
		/* split pane so einstellen, dass beim vergrößern immer oben, nicht 
		unten hinzugegeben wird. unten bleibt somit gleich */
		splitPane.setResizeWeight(1.0);
		
		
		splitPane.setDividerLocation(-250);
		
		// split pane beim resizen weiter rendern
		splitPane.setContinuousLayout(true);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        
        
        
        

	}
	
	



	
	
	/**
	 * initialisiert das layout
	 */
	private void initLayout() {
		

       
        
        splitPane.setBottomComponent(todoDetailPanel);
        splitPane.setLeftComponent(todoListPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
        );
        
	}
	
	
	




	/**
	 * leitet die information an das model der tabelle weiter, dass sich etwas geändert hat
	 */
	public void updateTodoList() {
		todoListPanel.updateTodoList();
	}
	

	
	public void mouseClicked(MouseEvent e)  {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mousePressed(MouseEvent e)  {

		
		// trennung zwischen close-button und open-label-click über typ
		if(e.getSource() instanceof JLabel) {
		
			if(((JLabel)e.getSource()).isEnabled()) {
			
				// aktuelle split-höhe merken
				int i = splitPane.getDividerLocation();
				
				// großen text-editor erzeugen und ihm den initialen inhalt übergeben aus dem kleinen editor 
				TodoTextEditor tte = new TodoTextEditor(this, todoDetailPanel.getTextAreaText());
				
				//	splitPane.getBottomComponent().addComponentListener(tte);
				splitPane.setBottomComponent(tte);
				splitPane.setDividerLocation(i);
			
			}
		} else {
	
			// aktuell angezeigten todo text editor holen
			TodoTextEditor tte = (TodoTextEditor)splitPane.getBottomComponent();
			
			// Text aus dem großen editor in den kleinen übernehmen
			todoDetailPanel.setTextAreaText(tte.getText());
			
			// Großen Text-Editor anzeigen
			splitPane.setBottomComponent(todoDetailPanel);
		}
	}

	
	/**
	 * aktiviert oder deaktiviert das filter-fenster
	 * @param status
	 */
	public void switchFilterEnabled() {
		
		todoListPanel.switchFilterVisible();
	}
	
	public TodoListPanel getTodoListPanel() {
		return todoListPanel;
	}
	
	
	public void setCursorLocked(boolean status) {

		if(!status)		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		else			this.setCursor(cursorLocked);
	}


	public void setSelectedTodoItemStack(TodoItemStack tis) {
		todoListPanel.setSelectedTodoItemStack(tis);
		
	}

	
	
	public void showSingleStack(TodoItemStack tisToShow) {
		
		todoDetailPanel.showSingleStack(tisToShow);
		
		if( splitPane.getBottomComponent() instanceof TodoTextEditor) {

			splitPane.setBottomComponent(todoDetailPanel);
		}
	}
	
}





