package gui.renderer;

import gui.components.PriorityComponent;
import gui.models.TodoListModel;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import main.annotations.OverriddenByConfiguration;

import controller.ConfigurationHandlerImpl;
import controller.ImageController;
import data.TodoItem;
import data.TodoItemStack;

/**
 * Dieser Renderer wird für die TodoListe benutzt um sie farbig zu
 * machen und beim reminder eine alarm-grafik einzubinden.
 * @author Tobias Schaber
 */
public class TodoListRenderer extends DefaultTableCellRenderer {
	
	
	@OverriddenByConfiguration private static Color colAlarm		= new Color(-65536);
	@OverriddenByConfiguration private static Color colDone 		= new Color(-16711936);
	@OverriddenByConfiguration private static Color colSelection 	= new Color(-16776961);
	@OverriddenByConfiguration private static Color colDefault 		= new Color(-16777216);
	@OverriddenByConfiguration private static Color colDefaultBg 	= new Color(-1);
	
	public TodoListRenderer() {

		
		/* Farben auf default setzen und danach versuchen, einlesen aus der config datei */		
		try { colAlarm = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorAlarm"))); } 
		catch(Exception e) {}
		
		try { colDone = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorDone"))); }
		catch(Exception e) {}
		
		try { colSelection = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorSelection"))); }
		catch(Exception e) {}
		
		try { colDefault = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorDefault"))); }
		catch(Exception e) {}
		
		try { colDefaultBg = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorDefaultBg"))); }
		catch(Exception e) {}
		
	}
	
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		
		/* hole das model der tabelle, denn das model kennt das todoproject! */
		TodoListModel tlm = (TodoListModel)table.getModel();
		
		/* lese die gerade bearbeitete id aus der tabelle */
		int currentId = (Integer)(table.getValueAt(row, 0));
		
		/* hole den gerade bearbeiteten toditemstack */
		TodoItemStack currentTis = tlm.getTodoProject().getTodoItemStackById(currentId);
		
		String currentStatus = currentTis.getLatestTodo().getStatus();
		
		
		/* wenn das gerade bearbeitete Feld in einer Zeile mit Reminder Aktiv steht */
		if(currentTis.getReminderActivationStatus()) {
			
			/* wenn die letzte Zeile (mit dem Status) anvisiert ist */
			if(column == 3) {
				
				/* erzeuge neues label, dass den status-text mit reminder-grafik anzeigt */
				
				
				JLabel labtmp = new JLabel(new SimpleDateFormat(
						ConfigurationHandlerImpl
						.getInstance()
						.getProperty("DefaultDateFormat"))
						.format(currentTis.getReminderDate()));
								
				/* icon dem jlabel hinzufügen */
				labtmp.setIcon(ImageController.iconReminder);
				
				
				/* wenn der status auf alarm steht, muss hier extra noch mal rot geschrieben werden */
				if(currentStatus.equals(TodoItem.STATUS_ALARM)) {
					labtmp.setForeground(colAlarm);
				}
				
				/* wenn der status auf alarm steht, muss hier extra noch mal rot geschrieben werden */
				else if(currentStatus.equals(TodoItem.STATUS_DONE)) {
					labtmp.setForeground(colDone);
				}
				
				/* mittig ausrichten (wenns überhaupt hilft...) */
				labtmp.setVerticalAlignment(JLabel.CENTER);
				
				/* text links von dem icon anordnen damit das icon rechts steht */
				labtmp.setHorizontalTextPosition(JLabel.LEFT);

				if(isSelected) {
					labtmp.setOpaque(true);
					labtmp.setBackground(colSelection);
				} else {
					labtmp.setOpaque(true);
					labtmp.setBackground(colDefaultBg);
				}
				
				labtmp.setToolTipText(currentTis.getLatestTodo().getDescription());
				
				return labtmp;
				
			}

		}
		
		// PRIORITY
		if(column == 4) {
			
			PriorityComponent pc = new PriorityComponent(currentTis.getPriority());
			
			
			// Die Hintergrundfarbe bei ausgewählter Zeile muss hier separat berücksichtigt werden
			if(isSelected) {
				pc.setBackground(colSelection);
			} else {
				pc.setBackground(colDefaultBg);
			}
			
			return pc;
			
		}
		
		
		/* wenn gerade ein Feld aus einer Zeile im Status "ALARM" bearbeitet wird */
		if(currentStatus.equals(TodoItem.STATUS_ALARM)) {
			
			setForeground(colAlarm);
			
		}
		/* wenn gerade ein Feld aus einer Zeile im Status "DONE" bearbeitet wird */
		else if(currentStatus.equals(TodoItem.STATUS_DONE)) {
			setForeground(colDone);
		}
		/* alle anderen Zeilen */
		else {
			if(currentStatus.equals(TodoItem.STATUS_PENDING) || currentStatus.equals(TodoItem.STATUS_WAIT)) {
				setForeground(Color.gray);
			} else {
				setForeground(colDefault);
			}
		}
		
		/* standard-selektion einfärben */
		if(isSelected) {
			setBackground(colSelection);
		} else {
			setBackground(colDefaultBg);
		}
		
		setText(value.toString());
		
		JLabel labtmp = (JLabel)this;
		
		labtmp.setToolTipText(currentTis.getLatestTodo().getDescription());
		
		return labtmp;
	}

}
