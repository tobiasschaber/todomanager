package gui.panels.configuration.configPanels;

import gui.panels.configuration.AbstractConfigurationPanel;
import gui.panels.configuration.configPanels.subPanels.BoxColorModel;
import gui.panels.configuration.configPanels.subPanels.BoxConfigSubPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import controller.ConfigurationHandlerImpl;


public class ConfigBoxPanel extends AbstractConfigurationPanel implements ActionListener {
	
	private final BoxConfigSubPanel 		configBoxEditorPanel;
	private static final JLabel				labConfColor   		= new JLabel("Konfiguriere Farbe:");
	private static final JLabel				labDescription 		= new JLabel("<html>Hier wird das Aussehen der Boxen der " +
																			 "Übersicht eingestellt. Ändere die Größe des Fensters " +
																			 "oder verschiebe die Symbole an andere Stellen. Linke Seite " +
																			 "der Box klicken um Neben-, rechte Seite um Haupt-Farbe zu ändern.</html>");
	
	public final JComboBox<String>			comboboxColorCase	= new JComboBox<String>();
	
	// enthält name des properties von farben sowie eine dazugehörige Beschreibung
	private final HashMap<String, String>	colKeys				= new HashMap<String, String>();
	
	
	public ConfigBoxPanel() {
		
		
		colKeys.put("box.color.left", 		"Standard Haupt-Box-Farbe");
		colKeys.put("box.color.left.alarm", "Haupt-Box-Farbe bei Alarm");
		colKeys.put("box.color.left.clean", "Haupt-Box-Farbe ohne offene Todos");
		colKeys.put("box.color.left.locked","Haupt-Box-Farbe, wenn Liste gesperrt ist");
		

		configBoxEditorPanel = new BoxConfigSubPanel(this);
		comboboxColorCase.setModel(new BoxColorModel(colKeys));
		comboboxColorCase.setSelectedIndex(0);
		
		initListeners();
		initLayout();
		
	}
	
	
	@Override
	protected void initComponents() {
		// no fields to set
	}
	
	
	private void initLayout() {
		
		configBoxEditorPanel.setBorder(new LineBorder(Color.white));
		

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(configBoxEditorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(labConfColor)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(comboboxColorCase, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap(60, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(configBoxEditorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labConfColor)
                        .addComponent(comboboxColorCase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(labDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addContainerGap())
            );
	}
	
	
	private void initListeners() {
		comboboxColorCase.addActionListener(this);
	}
	
	
	
	@Override
	public String toString() {
		return "Box-Layout";
		
	}

	
	@Override
	public String getHeadline() {
		return "Einstellungen zur Anzeige der Boxen";
	}

	
	/**
	 * liefert für einen key ein property, entweder vom config handler,
	 * oder wenn es schon ein geändertes gibt, dann dieses
	 * @param key
	 * @return
	 */
	public String getNewestProperty(String key) {
		
		if(getChangedProperties().containsKey(key)) return getChangedProperties().getProperty(key);
		else return ConfigurationHandlerImpl.getInstance().getProperty(key);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		configBoxEditorPanel.repaint();
		
	}
	
	
	/**
	 * liefert den key dessen, was in der unteren kombobox gerade ausgewählt ist,
	 * also z.B. "box.color.left.alarm", wenn "Farbe der Box bei Alarm" ausgewählt ist
	 * @return
	 */
	public String getBoxLeftCase() {
		return getKeyByName((String)comboboxColorCase.getSelectedItem());
	}
	
	private String getKeyByName(String name) {
		
		Iterator<String> it = colKeys.keySet().iterator();
		
		while(it.hasNext()) {
			
			String key = it.next();
			
			String val = colKeys.get(key);
			
			if(val.equals(name)) {
				return key;
				
			}
		}
		
		return "";
	}
	
}
