package gui.panels.configuration.configPanels;

import gui.panels.configuration.AbstractConfigurationPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import controller.ConfigurationHandlerImpl;


public class ConfigOverviewPanel extends AbstractConfigurationPanel implements ActionListener {
	
	private static final JCheckBox 	chkboxEnableHintsOnStartup 		= new JCheckBox("Zeige Benutzer-Hinweise beim Starten des Programms");
    private static final JCheckBox 	chkboxShowLockedListsOnStartup 	= new JCheckBox("Zeige gesperrte Listen (Box) beim Starten des Programms");
    private static final JLabel 	labShowLockedListsOnStartup 	= new JLabel("Anzeige von Elementen");
	
	public ConfigOverviewPanel() {
		
		initComponents();
		initListeners();
		initLayout();
		
	}
	
	
	@Override
	protected void initComponents() {
		
		chkboxShowLockedListsOnStartup.setSelected(Boolean.parseBoolean(ConfigurationHandlerImpl.getInstance().getProperty("showLockedListsOnStartup")));
		chkboxEnableHintsOnStartup.setSelected(Boolean.parseBoolean(ConfigurationHandlerImpl.getInstance().getProperty("enableHints")));
	}
	
	
	private void initLayout() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labShowLockedListsOnStartup, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkboxShowLockedListsOnStartup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkboxEnableHintsOnStartup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labShowLockedListsOnStartup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkboxShowLockedListsOnStartup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkboxEnableHintsOnStartup)
                .addContainerGap(220, Short.MAX_VALUE))
        );
        
	}
	
	
	private void initListeners() {
		chkboxShowLockedListsOnStartup.addActionListener(this);
		chkboxEnableHintsOnStartup.addActionListener(this);

	}
	
	
	
	@Override
	public String toString() {
		return "Übersichts-Seite";
		
	}

	
	@Override
	public String getHeadline() {
		return "Einstellungen rund um die Übersichts-Seite";
	}


	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		updateProperty("showLockedListsOnStartup", ""+chkboxShowLockedListsOnStartup.isSelected());
		updateProperty("enableHints", ""+chkboxEnableHintsOnStartup.isSelected());

	}
	

	
}
