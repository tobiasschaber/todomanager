package gui.panels.configuration.configPanels;

import gui.dialogs.ChooseColorInputDialog;
import gui.panels.configuration.AbstractConfigurationPanel;
import gui.renderer.ComboBoxCategoryRenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import controller.ConfigurationHandlerImpl;
import controller.ImageController;

/**
 * Konfigurations-Panel für die Kategorien und deren Darstellung (Farben) im Time-Panel.
 * @author Tobias Schaber
 */
public class ConfigTimeCategoryPanel extends AbstractConfigurationPanel implements MouseListener, ActionListener, ItemListener {
	
    private final JButton 			butAddCategory = new JButton("Hinzu");
    private final JComboBox<String> comboboxCategories = new JComboBox<String>();
    private final JLabel 			labAddCategory = new JLabel("Neue Kategorie hinzu");
    private final JLabel 			labCategories = new JLabel("Kategorien verwalten");
    private final JLabel			labDelete = new JLabel(" ");
    private final JTextField 		txtAddCategory = new JTextField("");
    
    /* Wechselt Hintergrundfarbe je nach gewählter Farbe */
    private final JTextField 		txtChangeColor = new JTextField("");
    	
	public ConfigTimeCategoryPanel() {

		labDelete.setIcon(ImageController.iconExit);
		labDelete.setToolTipText("Kategorie entfernen");
		
		labDelete.addMouseListener(new MouseAdapter() {   
			public void mouseEntered(MouseEvent e)  {
			
				Component component = e.getComponent();  
			
				if (component instanceof JLabel) { 
					((JLabel)component).setBorder(new EtchedBorder()); 
				}   
			}        
			 
			/**
			 * für hover-effekte über dem reminder deaktivieren button zu verwenden
			 */
			public void mouseExited(MouseEvent e) { 
	
				Component component = e.getComponent(); 
				
				if (component instanceof JLabel) { 
					((JLabel)component).setBorder(null); 
				} 
		
			}	
		});

		
		initComponents();
		initListeners();
		initLayout();
	}
	
	@Override
	protected void initComponents() {
		
		txtChangeColor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		comboboxCategories.setModel(new DefaultComboBoxModel<String>(getComboboxCategories()));
		txtChangeColor.setBackground(
				ConfigurationHandlerImpl.
				getInstance().
				getCategoryColor(
						(String)comboboxCategories.getSelectedItem())
					);
		
		comboboxCategories.setRenderer(new ComboBoxCategoryRenderer(comboboxCategories));
	}
	
	
		
	private String[] getComboboxCategories() {
		
		Properties categories = ConfigurationHandlerImpl.getInstance().getCategories();
		String[] array = new String[categories.size()];
		return categories.keySet().toArray(array);
		
	}

	
	
	private void initListeners() {
		
		butAddCategory.addActionListener(this);
		txtChangeColor.addMouseListener(this);
		comboboxCategories.addItemListener(this);
		labDelete.addMouseListener(this);
		txtAddCategory.addActionListener(this);
			
	}
	
	
	private void initLayout() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboboxCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtChangeColor, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(labAddCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtAddCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(butAddCategory)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(labCategories)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboboxCategories)
                        .addComponent(txtChangeColor))
                    .addComponent(labDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(labAddCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(butAddCategory))
                .addContainerGap(171, Short.MAX_VALUE))
        );
		
	}

	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	
	@Override
	public String toString() {
		return "Kategorien";
	}
	
	@Override
	public String getHeadline() {
		return "Konfiguration der Kategorien";
	}
	
		
	@Override
	public void actionPerformed(ActionEvent e) {
		String newCatName = txtAddCategory.getText();
		
		// Kategorie existiert bereits
		if(ConfigurationHandlerImpl.getInstance().getCategories().containsKey(newCatName)) {
			
			JOptionPane.showMessageDialog(this, "Eine Kategorie mit diesem Namen existiert bereits.", "Fehler beim Anlegen", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			
			if(newCatName != null && newCatName.length() != 0) {
				
				ConfigurationHandlerImpl.getInstance().addCategory(txtAddCategory.getText());
				this.initComponents();
				txtAddCategory.setText("");
				
			}
		}
				
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == txtChangeColor) {
			
			Color c = ChooseColorInputDialog.showInputDialog(
					"Farbe auswählen (Box rechts)",
					txtChangeColor.getBackground());
			
			txtChangeColor.setBackground(c);
			
			updateProperty((String)comboboxCategories.getSelectedItem(), ""+txtChangeColor.getBackground().getRGB());
			
			comboboxCategories.setModel(new DefaultComboBoxModel<String>(getComboboxCategories()));
			comboboxCategories.repaint();
			

			
		}
		
		if(e.getSource() == labDelete) {
			if(comboboxCategories.getSelectedItem() != null) {
				ConfigurationHandlerImpl.getInstance().getCategories().remove(comboboxCategories.getSelectedItem());
				initComponents();
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		try {
			txtChangeColor.setBackground(new Color(Integer.parseInt(
		
					getChangedProperties().getProperty((String)comboboxCategories.getSelectedItem()
					))));
		} catch(Exception ex) {
		
			txtChangeColor.setBackground(
			ConfigurationHandlerImpl.
			getInstance().
			getCategoryColor(
					(String)comboboxCategories.getSelectedItem())
				);
			
		}

	}

}
