package gui.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import controller.ImageController;

/**
 * Dieser Renderer wird genutzt, um die Combo-Box zum Auswählen des Status
 * eines Todos zu verändern. Zum Beispiel wird hier ein Haken hinter das
 * "DONE" gesetzt.
 * @author Tobias Schaber
 */
public final class ComboBoxStatusRenderer extends DefaultListCellRenderer {

	// combobox halten, um sie später färben zu können
	private final JComboBox<String> comboBox;

	public ComboBoxStatusRenderer(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
		
	
	}
	
	

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		JLabel returnLabel = new JLabel();
		returnLabel.setText((String)value);
		
		// default color für alle einträge
		comboBox.setForeground(Color.black);
		
		
		
		// isSelected = mouseover bzw. hover effekt und es ist nicht das ganz oberste element im kopf
		if(isSelected && index != -1) {
			returnLabel.setOpaque(true);
			returnLabel.setBackground(new Color(134, 134, 255));
			
		}		
		
		// todo-eintrag mit icon und in grüner farbe darstellen
		if(((String)value).equals("DONE")) {
			returnLabel.setIcon(ImageController.iconDone);
			returnLabel.setForeground(new Color(0, 150, 0));
			returnLabel.setIconTextGap(25);
			returnLabel.setHorizontalTextPosition(JLabel.LEFT);
			
		}

		if(((String)value).equals("ALARM")) {
			returnLabel.setForeground(Color.red);
			

		}

		
		
		return returnLabel;
	}
	
		
		
	

}
