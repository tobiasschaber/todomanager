package gui.panels.configuration.configPanels;

import gui.panels.configuration.AbstractConfigurationPanel;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Logger;
import controller.ConfigurationHandlerImpl;



public class ConfigSystemPanel extends AbstractConfigurationPanel implements ChangeListener, KeyListener, ActionListener {
	
    private final JComboBox<String> 	cbxLogLevel 				= new JComboBox<String>();
    private final JCheckBox 			chkbx_enableSecretDoor 		= new JCheckBox("Schnittstelle aktivieren");
    private final JCheckBox 			chkbx_genTabs 				= new JCheckBox("Tabs automatisch anlegen");
    
    private static final JLabel			labDescSec 					= new JLabel("Millisekunden");
    private static final JLabel			labDescEntries 				= new JLabel("Einträge");
    private static final JLabel			labDescConfigPollingInterv 	= new JLabel("Polling-Intervall auf die Konfigurations-Datei");
    private static final JLabel			labDescGenTabs 				= new JLabel("Tabs beim Laden automatisch anlegen. Deaktivieren reduziert Speicherverbrauch erheblich.");
    private static final JLabel 		labDescLogLevel 			= new JLabel("<html>Log-Level für die Anwendung <u>?</u></html>");
    private static final JLabel 		labDescMaxLogEntries 		= new JLabel("Maximale Anzahl von Log-Einträgen");
    private static final JLabel 		labDescSecretDoor 			= new JLabel("Debugging-Schnittstelle aktivieren");
  
    
    private final JTextField 			txtLogCount 				= new JTextField();
    private final JTextField 			txtPollingInt 				= new JTextField();
	    

	public ConfigSystemPanel() {

		cbxLogLevel.setModel(new javax.swing.DefaultComboBoxModel<String>(Logger.logLevelNames));
		
		initComponents();
		initLayout();
		initListeners();
		
		
	}
	
	
	@Override
	protected void initComponents() {
		
	    txtLogCount.setText(ConfigurationHandlerImpl.getInstance().getProperty("maxLogEntries"));
	    txtPollingInt.setText(ConfigurationHandlerImpl.getInstance().getProperty("configFilePollingIntervalMs"));
	    
		
	    if(ConfigurationHandlerImpl.getInstance().getProperty("secretDoor").equals("enabled")) 				chkbx_enableSecretDoor.setSelected(true);
	    else																								chkbx_enableSecretDoor.setSelected(false);
	    if(ConfigurationHandlerImpl.getInstance().getProperty("createTabsOnLoadFile").equals("true")) 		chkbx_genTabs.setSelected(true);
	    else																								chkbx_genTabs.setSelected(false);
	    
	    cbxLogLevel.setSelectedItem(ConfigurationHandlerImpl.getInstance().getProperty("logLevel"));
	    
	    
	}
	
	
	
	private void initListeners() {
		
		chkbx_enableSecretDoor.addChangeListener(this);
		chkbx_genTabs.addChangeListener(this);
		cbxLogLevel.addActionListener(this);
		txtLogCount.addKeyListener(this);
		txtPollingInt.addKeyListener(this);
		
		labDescLogLevel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null, "<html>Die Log-Level haben die folgenden Bedeutungen: " +
						"<table>" +
						"<tr><td><b>ERROR</b></td><td>Nur Fehler werden protokolliert.</td></tr>" +
						"<tr><td><b>NONE</b></td><td>Normale Nachrichten werden nicht protokolliert.</td></tr>" +
						"<tr><td><b>DEBUG</b></td><td>Viele zusätzliche Nachrichten werden protokolliert.</td></tr>" +
						"<tr><td><b>INFO</b></td><td>Alle Nachrichten werden protokolliert.</td></tr>" +
						"</table>");

				
			}
		});
		   
	}
	
	
	
	private void initLayout() {
		
	       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(labDescConfigPollingInterv, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(labDescMaxLogEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addGap(10, 10, 10)
	                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                            .addGroup(layout.createSequentialGroup()
	                                                .addComponent(txtPollingInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                                .addComponent(labDescSec))
	                                            .addGroup(layout.createSequentialGroup()
	                                                .addComponent(txtLogCount, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                                .addComponent(labDescEntries)))))
	                                .addGap(0, 0, Short.MAX_VALUE)))
	                        .addContainerGap())
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                                .addComponent(labDescGenTabs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addGroup(layout.createSequentialGroup()
	                                    .addGap(10, 10, 10)
	                                    .addComponent(chkbx_genTabs)
	                                    .addGap(44, 44, 44)))
	                            .addComponent(labDescLogLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(10, 10, 10)
	                                .addComponent(cbxLogLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(10, 10, 10)
	                                .addComponent(chkbx_enableSecretDoor))
	                            .addComponent(labDescSecretDoor, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGap(0, 0, Short.MAX_VALUE))))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(labDescGenTabs)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(chkbx_genTabs)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(labDescMaxLogEntries)
	                .addGap(6, 6, 6)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(txtLogCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(labDescEntries))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(labDescLogLevel)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(cbxLogLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(labDescSecretDoor)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(chkbx_enableSecretDoor)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(labDescConfigPollingInterv)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(txtPollingInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(labDescSec))
	                .addContainerGap(55, Short.MAX_VALUE))
	        );
		
	}
	
	@Override
	public String toString() {
		return "System";
	}
	
	@Override
	public String getHeadline() {
		return "System-Eigenschaften";
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(e.getSource() == chkbx_enableSecretDoor) {
			
			String newVal = "disabled";
			if(chkbx_enableSecretDoor.isSelected()) newVal = "enabled";
			
			updateProperty("secretDoor", newVal);
		}
		
		
		if(e.getSource() == chkbx_genTabs) {
			
			String newVal = "false";
			if(chkbx_genTabs.isSelected()) newVal = "true";
			
			updateProperty("createTabsOnLoadFile", newVal);
		}
		
	}
	
	
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		
		if(e.getSource() == txtLogCount) {
			
			updateProperty("maxLogEntries", txtLogCount.getText());
			
		}
	
		
		
		if(e.getSource() == txtPollingInt) {
			updateProperty("configFilePollingIntervalMs", txtPollingInt.getText());
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cbxLogLevel) {
			updateProperty("logLevel", (String)cbxLogLevel.getSelectedItem());
		}
	}
	
	 
	
	

}
