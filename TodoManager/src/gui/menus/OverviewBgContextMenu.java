package gui.menus;

import gui.panels.overviewpage.BoxSummaryPanel;
import gui.panels.overviewpage.OverviewTabPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.ConfigurationHandlerImpl;
import controller.ImageController;


/**
 * Context-Menü für den Hintergrund des Box-Bereichs der Übersichts-Seite.
 * Zeigt Optionen zur Anzeige der Boxen.
 * @author Tobias Schaber
 */
public class OverviewBgContextMenu extends JPopupMenu implements ActionListener {
	
	private final JMenuItem			contextItemAddList 	= new JMenuItem("Neue Liste hinzufügen");
	
	private final JCheckBoxMenuItem	chkShowSummary		= new JCheckBoxMenuItem("Grafische Statistik anzeigen");
	private final JCheckBoxMenuItem	chkShowPriorityList = new JCheckBoxMenuItem("Zeige High Priority Todos");
	private final JCheckBoxMenuItem	chkShowLocked 		= new JCheckBoxMenuItem("Gesperrte Listen anzeigen");
	

	private OverviewTabPanel		otp;
	private BoxSummaryPanel			parent;
	
	public OverviewBgContextMenu(BoxSummaryPanel parent) {
		
		this.parent = parent;

		
		
			
		initComponents();
		initListeners();
		this.setSize(100, 100);
	}
	
	private void initComponents() {
		
		contextItemAddList.setIcon(ImageController.iconList);
		contextItemAddList.setToolTipText("Fügt dem Projekt eine neue Todo-Liste hinzu");
		
		/* Das Kontextmenü für die Boxen */
		
		chkShowLocked.setSelected(Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showLockedListsOnStartup")));
		chkShowPriorityList.setSelected(Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showHighPrioListOnStartup")));
		chkShowSummary.setSelected(Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showSummaryDiagramOnStartup")));
		
		
		chkShowPriorityList.setToolTipText("Liste der Todos mit höchster Priorität links unten anzeigen");
		chkShowLocked.setToolTipText("Gesperrte Listen anzeigen oder ausblenden");
		chkShowSummary.setToolTipText("Grafische Zusammenfassung (Statistik) rechts unten anzeigen");
		
		this.add(contextItemAddList);
		this.addSeparator();
		this.add(chkShowLocked);
		this.addSeparator();
		this.add(chkShowPriorityList);
		this.add(chkShowSummary);
		
	}
	
	
	private void initListeners() {
		contextItemAddList.addActionListener(this);
		chkShowPriorityList	.addActionListener(this);
		chkShowSummary		.addActionListener(this);
		chkShowLocked		.addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == chkShowSummary) {
			otp.showSummary(chkShowSummary.isSelected());
			return;
		}

		if(e.getSource() == chkShowPriorityList) {
			otp.showPriorityList(chkShowPriorityList.isSelected());
			return;
		}
		
		if(e.getSource() == chkShowLocked) {
			parent.showLocked(chkShowLocked.isSelected());
			return;
		}
		
		if(e.getSource() == contextItemAddList) {
			parent.getBaseController().addMenuItemList();
			return;
		}
		
	}


	/**
	 * 
	 * @param invoker
	 * @param x
	 * @param y
	 * @param i pseudo-parameter um methode nicht zu überschreiben
	 */
	public void show(Component invoker, int x, int y, OverviewTabPanel otp) {
		
		this.otp = otp;

		this.show(invoker, x, y);
		
	}
	
	
}
