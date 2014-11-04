package gui.panels.configuration;

import gui.models.ConfigTreeModel;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import main.BaseController;
import main.Logger;
import controller.ConfigurationHandlerImpl;
import controller.ImageController;

/**
 * Haupt-Frame zur Anzeige der Einstellungen in einem neuen Fenster.
 * Zeigt auf der linken Seite einen Baum zur Navigation, auf der rechten Seite
 * die Einstellungen der jeweiligen Auswahl.
 * @author Tobias Schaber
 */
public class ConfigurationFrame extends JFrame implements ActionListener, TreeSelectionListener {
	
	@SuppressWarnings("unused")
	private final BaseController 				controller;
	
	
	// enthält alle von den tabs zusammengetragenen Änderungen
	private final Properties					changeProperties		= new Properties();
	
	private Rectangle							bounds;
	
	
    private static final JLabel 				labHeadline				= new JLabel("Konfiguration");
    private final JButton 						butCancel 				= new JButton("Cancel");
    private final JButton 						butSave					= new JButton("OK");
    private final JButton						butApply				= new JButton("Übernehmen");
    private final JButton						butReset				= new JButton("Reset");
    
    private final JScrollPane 					scrollPaneConfigTree 	= new JScrollPane();
    private final JSeparator 					separatorButtons		= new JSeparator();
    
    private final ConfigTreeModel				configTreeModel			= new ConfigTreeModel();
    private final JTree 						configTree				= new JTree(configTreeModel);
    
    private AbstractConfigurationPanel 			configPanel				= new AbstractConfigurationPanel() { public void initComponents() {}};
    
    
    /**
     * 
     * @param controller
     * @param baseFrameBounds bounds des baseFrames, um das Fenster in der Mitte davon auszurichten 
     */
	public ConfigurationFrame(BaseController controller, Rectangle baseFrameBounds) {
		
		this.controller = controller;
		
		int width 	= 650;
		int height 	= 500;		
		bounds = new Rectangle(baseFrameBounds.x+(baseFrameBounds.width/2)-(width/2), baseFrameBounds.y+(baseFrameBounds.height/2)-(height/2), width, height);
		
		initComponents();
		initLayout();
		initListeners();

		
	}

	
	
	
	private void initComponents() {
		
		labHeadline.setFont(new Font("tahoma", Font.BOLD, 14));
		
		setBounds(bounds);
		setTitle("Einstellungen anpassen");
		setIconImage(ImageController.imgTrayIcon);
		
		
		butReset.setToolTipText("Setze die Änderungen dieser Seite zurück");
		
		
		
		 // ausklappen
		configTree.expandRow(0);
		configTree.expandRow(1);
		configTree.expandRow(2);
		configTree.expandRow(3);
		configTree.expandRow(4);
		configTree.expandRow(5);
		configTree.expandRow(6);
		configTree.expandRow(7);
		configTree.expandRow(8);
		configTree.expandRow(9);
		
		scrollPaneConfigTree.setViewportView(configTree);
	}

	
	private void initLayout() {		

        butApply.setText("Übernehmen");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollPaneConfigTree, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separatorButtons)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labHeadline)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(butApply)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(butReset)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                                .addComponent(butSave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(butCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(configPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneConfigTree, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labHeadline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(configPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(separatorButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(butSave)
                        .addComponent(butApply)
                        .addComponent(butReset))
                    .addComponent(butCancel))
                .addContainerGap())
        );
        
        pack();
		
	}
	
	
	private void initListeners() {
		
		butSave.addActionListener(this);
		butReset.addActionListener(this);
		butCancel.addActionListener(this);
		butApply.addActionListener(this);
		configTree.addTreeSelectionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource() == butCancel) {
			
			this.setVisible(false);
						
		}
		
		if(e.getSource() == butReset) {
			
			resetChanges();
			
			
			
		}
		
		
		if(e.getSource() == butApply) {
			
			// kopiere die geänderten properties
			applyChanges();
			

		}
		
		
		if(e.getSource() == butSave) {
			
		
			
			this.saveChanges();
			this.setVisible(false);

		}

		
	}
	
	
//	/**
//	 * fragt den benutzer in einem dialog wenn Änderungen gemacht wurden,
//	 * ob diese übernommen werden sollen
//	 */
//	public void askSaveChanges() {
//		
//		if(configPanel.hasChanged()) {
//			
//			int ch = JOptionPane.showConfirmDialog(this, "Änderungen beibehalten?", "Änderungen behalten?", JOptionPane.YES_NO_OPTION);
//			
//			// 0 = speichern vor dem schließen
//			if(ch == 0) {
//				applyChanges();
//			}
//		}
//		
//	}
//	
//	
	
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		
		
		
		// wenn das ausgewählte Element ein Config-Panel und kein String ist -> panel wechseln
		if(((DefaultMutableTreeNode)configTree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof AbstractConfigurationPanel) {

			this.remove(configPanel);
			
			configPanel = (AbstractConfigurationPanel)((DefaultMutableTreeNode)configTree.getSelectionPath().getLastPathComponent()).getUserObject();
			
			this.add(configPanel);
			
			// layout neu einrichten
			initLayout();

			labHeadline.setText(configPanel.getHeadline());

		// bei String -> nichts tun
		} else {
			return;
		}

	}
	
	
	
	
	/**
	 * Überträgt die Änderungen aus den properties des aktuellen
	 * panels an die haupt-properties
	 */
	private void applyChanges() {
		
		changeProperties.putAll(configPanel.getChangedProperties());
		
	}
	
	
	
	/**
	 * setzt die bisher getätigten Änderungen zurück
	 */
	private void resetChanges() {
		configPanel.resetChangedProperties();
		configPanel.initComponents();
		configPanel.repaint();
		
		
	}
	
	
	/**
	 * Übermittelt die geänderten properties an den
	 * config-handler zum speichern in der config-datei
	 */
	private void saveChanges() {
		
		applyChanges();
		
		ConfigurationHandlerImpl.getInstance().updateConfigFile(changeProperties);
		Logger.getInstance().log("Konfiguration wird in Datei geschrieben", Logger.LOGLEVEL_INFO);
	}
	
}
