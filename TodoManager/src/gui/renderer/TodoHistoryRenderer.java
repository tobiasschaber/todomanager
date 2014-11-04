package gui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.*;

import controller.ConfigurationHandlerImpl;
import data.TodoItem;

public class TodoHistoryRenderer implements ListCellRenderer<TodoItem> {
	
	private static Color colSelection 	= new Color(-16776961);
	
	public TodoHistoryRenderer() {
		
		try { colSelection = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorSelection"))); }
		catch(Exception e) {}
		
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends TodoItem> list, TodoItem value,
			int index, boolean isSelected, boolean cellHasFocus) {

		
		
		JLabel lab = new JLabel(value.getDescription() + " (" + value.getStatus() + ")");
		// datum in den namen schreiben damit der renderer es lesen kann...
		
		
		lab.setName(new SimpleDateFormat(
				ConfigurationHandlerImpl
				.getInstance()
				.getProperty("DefaultDateFormat"))
				.format(value.getModificationDate()));
		
		if(value.getStatus().equals(TodoItem.STATUS_ALARM)) {
			lab.setForeground(Color.red);
			lab.setOpaque(true);
		}
		if(value.getStatus().equals(TodoItem.STATUS_DONE)) {
			lab.setForeground(Color.green);
			lab.setOpaque(true);
		}
		
		
			

			lab.setToolTipText(lab.getName());
			lab.setBackground(Color.white);

			
			if(isSelected) { 
				lab.setOpaque(true);
				lab.setBackground(colSelection);

				if(lab.getForeground().equals(Color.white)) lab.setForeground(Color.black);
				
			} else {
				if(lab.getForeground().equals(Color.black)) lab.setBackground(Color.white);
			}
		


		return lab;
	}

}
