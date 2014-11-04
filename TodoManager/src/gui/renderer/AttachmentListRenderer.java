package gui.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import controller.ConfigurationHandlerImpl;
import data.Attachment;


/**
 * Renderer für die Liste von Attachments.
 * Zeigt existierende Dateien grün, nicht existierende Dateien rot an.
 * @author Tobias Schaber
 */
public class AttachmentListRenderer implements ListCellRenderer<Attachment> {


	private static Color colSelection 	= new Color(-16776961);
	
	public AttachmentListRenderer() {
		
		try { colSelection = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorSelection"))); }
		catch(Exception e) {}
		
	}
	
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Attachment> list, Attachment value,
			int index, boolean isSelected, boolean cellHasFocus) {

		
		
		JLabel lab = new JLabel(value.getFileName());
		
			lab.setToolTipText(lab.getName());
			lab.setBackground(Color.white);
			
			if(value.getStorageMode() == Attachment.STORE_MODE_LOCAL) {
				
				if(value.getPathToFile().exists()) {
					// Darkgreen
					lab.setForeground(new Color(0, 150,0));
					lab.setToolTipText(value.getPathToFile().getAbsolutePath());
					
				} else {
					lab.setForeground(Color.red);
					lab.setToolTipText("Datei existiert nicht: " + value.getPathToFile().getAbsolutePath());
				}
				
			} else {
				lab.setToolTipText("Datei in der Todo-Liste enthalten");
				
			}

			
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
