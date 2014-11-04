package gui.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import controller.ConfigurationHandlerImpl;

/**
 * Dieser Renderer wird genutzt, um die Combo-Box zum Auswählen der Kategorie zu verändern.
 * @author Tobias Schaber
 */
public final class ComboBoxCategoryRenderer extends DefaultListCellRenderer {
	
	private static Color colSelection 	= new Color(-16776961);

	public ComboBoxCategoryRenderer(JComboBox<String> comboBox) {
		
		try { colSelection = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorSelection"))); }
		catch(Exception e) {}
	
	}
	
	

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		if(value != null) {
		
		JLabel returnLabel = new JLabel();
		returnLabel.setText((String)value);
	
		Color c = ConfigurationHandlerImpl.getInstance().getCategoryColor(value.toString());
		
		// wenn es für die Kategorie eine Farbe gibt
		if(c != null) {
			returnLabel.setForeground(c);
		} else {
			returnLabel.setForeground(Color.black);
		}

		// Das selektierte ("hover") Element der Liste bekommt farbigen hintergrund
		if(isSelected) {
			returnLabel.setOpaque(true);
			returnLabel.setBackground(colSelection);
		}
		
		// selektiertes kopf-element
		if(index == -1) {
		}
		

			
		
		
		return returnLabel;
	}
	
		else return new JLabel("");
	}
		
	

}
