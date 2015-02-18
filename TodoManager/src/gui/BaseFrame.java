package gui;

import gui.alerts.*;
import gui.components.CloseableTabHead;
import gui.dragdrop.TodoItemStackDropTargetListener;
import gui.panels.configuration.ConfigurationFrame;
import gui.panels.overviewpage.OverviewTabPanel;
import gui.panels.todoeditor.TodoListBasePanel;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.JPopupMenu.Separator;

import main.*;
import controller.*;
import data.*;


/**
 * Diese klasse enthält das komplette hauptfenster mit toolbar, tabbed pane und allem.
 * @author Tobias Schaber
 */
public class BaseFrame extends JFrame implements ActionListener, Observer {
	
	private final BaseController		controller;
	
	private ReminderHandler				reminderHandler;
	private final TrayIconWindowHandler	trayIconHandler;
	
	// liste der zuletzt geöffneten dateien
	private final File[]				lastOpened = new File[5];
	
	// buttons für die zuletzt geöffneten dateien
	private final JMenuItem[]			lastOpenedItems = new JMenuItem[5]; 
	
	/* swing gui elements */
	private final OverviewTabPanel	overviewPanel;
	private final JTabbedPane 		tabbedPane;
	private final JPanel 			basePanel;
	
    private final JMenuBar 			mainMenuBar 		= new JMenuBar();
    
    private final JMenu				menuProject 		= new JMenu("Projekt");
    private final JMenu 			menuFile 			= new JMenu("Datei");
    private final JMenu				menuSecretDoor		= new JMenu("Debugging");
    private final JMenu				menuSettings 		= new JMenu("Einstellungen");
    private final JMenu				menuHelp 			= new JMenu("?");
    
    private final JMenuItem 		menuItemLoadProject = new JMenuItem("Projekt öffnen");
    private final JMenuItem			menuItemExit		= new JMenuItem("Beenden");
    private final JMenuItem			menuItemSave		= new JMenuItem("Speichern");
    private final JMenuItem			menuItemSaveUnder	= new JMenuItem("Speichern unter...");
    private final JMenuItem			menuItemNew			= new JMenuItem("Neues Projekt");
    private final JMenuItem			menuItemAddList		= new JMenuItem("Neue Liste hinzufügen");
    private final JMenuItem			menuItemShowLog		= new JMenuItem("Show Log");
    private final JMenuItem			menuItemSearch		= new JMenuItem("Filtern");
    private final JMenuItem			menuItemInfo		= new JMenuItem("Über TodoManager");
    private final JMenuItem			menuItemConfig		= new JMenuItem("Einstellungen");
    private final JMenuItem			menuItemShowTrace	= new JMenuItem("Trace anzeigen");

    public final static JCheckBoxMenuItem	menuItemEnableHints = new JCheckBoxMenuItem("Benutzerhinweise aktivieren");
    
    
    
    /* gibt den index der zuletzt gerollten datei im startmenü an, die zuletzt geöffnet wurden */
    private static int 				lastUpdatedFile = 0;
    
    /* extra menü-separator, der nur eingeblendet wird, wenn eine oder mehr bereits geöffnete dateien im menü stehen */
    private static final Separator	openedFileHistorySeparator = new Separator();
    
        
	
    /**
     * constructor
     * @param bh parent object
     */
	public BaseFrame(BaseController bh) {
			
		this.controller = bh;
		
        basePanel 			= new JPanel();
        tabbedPane 			= new JTabbedPane();
        overviewPanel		= new OverviewTabPanel(controller);
		trayIconHandler		= new TrayIconWindowHandler(controller);
		
		initComponents();
		initLayout();
		initListeners();
		
		// der Splash-Screen muss nach dem Laden ausgeblendet werden
		LoadingSplashScreen.getInstance().hideSplash();
		
	}
	
	/**
	 * initialisiert alle grafischen komponenten
	 */
	private void initComponents() {
		

        
        mainMenuBar.setToolTipText("Ein- und Ausblenden mit [ALT]");
        
        menuItemEnableHints.setToolTipText("Benutzerhinweise helfen Einsteigern mit Tipps zur Bedienung");
        menuItemSave		.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
        menuItemLoadProject	.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
        menuItemNew			.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
        menuItemShowLog		.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, java.awt.Event.CTRL_MASK));
        menuItemSearch		.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.Event.CTRL_MASK));
        
        
        
        menuItemSave.setIcon(ImageController.iconSave);
        menuItemSaveUnder.setIcon(ImageController.iconSaveUnder);
        menuItemExit.setIcon(ImageController.iconExit);
        menuItemNew.setIcon(ImageController.iconNewFile);
        menuItemLoadProject.setIcon(ImageController.iconOpenFile);
        menuItemAddList.setIcon(ImageController.iconList);
        menuItemSearch.setIcon(ImageController.iconSearch);
        menuItemInfo.setIcon(ImageController.iconInfo);
        
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuProject);
        mainMenuBar.add(menuSettings);
        
        
        if(ConfigurationHandlerImpl.getInstance().getProperty("secretDoor").equals("enabled")) {
        	Logger.getInstance().log("Debugging activated", Logger.LOGLEVEL_DEBUG);
        	mainMenuBar.add(menuSecretDoor);
        }
        
        mainMenuBar.add(menuHelp);
                
        openedFileHistorySeparator.setVisible(false);
        
        menuFile.add(menuItemNew);
        menuFile.add(menuItemLoadProject);
        menuFile.addSeparator();
        menuFile.add(menuItemSave);
        menuFile.add(menuItemSaveUnder);
        menuFile.add(openedFileHistorySeparator);
        
        // füge bereits geöffnete dateien hinzu
        for(int i=0; i<lastOpenedItems.length; i++) {
        	lastOpenedItems[i] = new JMenuItem("");
        	lastOpenedItems[i].setVisible(false);
        	lastOpenedItems[i].addActionListener(this);
        	menuFile.add(lastOpenedItems[i]);
        	
        }

        menuFile.addSeparator();
        menuFile.add(menuItemExit);
        
        menuProject.add(menuItemAddList);
        menuProject.add(menuItemSearch);
        menuSecretDoor.add(menuItemShowLog);
        menuSecretDoor.add(menuItemShowTrace);
        
        menuHelp.add(menuItemInfo);
        
        menuSettings.add(menuItemEnableHints);
        menuSettings.add(menuItemConfig);
        
        if(ConfigurationHandlerImpl.getInstance().getProperty("enableHints").equals("true")) {
        	menuItemEnableHints.setSelected(true);
        } else {
        	menuItemEnableHints.setSelected(false);
        }
        
        setJMenuBar(mainMenuBar);
        
        tabbedPane.addTab("Übersicht", overviewPanel);
        
        this.setSize(900, 700);
        this.setIconImage(ImageController.imgTrayIcon);
        
        setTitle("TodoManager " + Start.version);
                
        
        menuItemAddList.setToolTipText("Fügt dem Projekt eine neue Todo-Liste hinzu");
        
        // drop target für file drag&drop hinzufügen
		this.setDropTarget(new DropTarget() {
			
			public synchronized void drop(DropTargetDropEvent evt) {

				try { 
		            evt.acceptDrop(DnDConstants.ACTION_COPY); 
		            @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor); 
		            for (File file : droppedFiles) { 
		                controller.loadProject(file);
		            } 
		        } catch (Exception ex) {
		            
		        } 
		    } 

			
		});
		
		ArrayList<File> lastOpenedFiles = ConfigurationHandlerImpl.getInstance().getLastOpenedFiles();
		
		for(File f : lastOpenedFiles) {
			addLastOpenedFile(f);
		}
        
        
	}
 
	@Override
	protected JRootPane createRootPane() 
	{
	    JRootPane root = super.createRootPane();
	    
	    // Listener für ALT zum Hauptmenü ausbleiden registrieren
	    root.registerKeyboardAction(new ActionListener() 
	    {
	        @Override
	        public void actionPerformed(ActionEvent event) 
	        {
	            switchMainMenuVisibleState();
	        }
	    }, KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
	    
	    
	    // Listener für STRG+S für Speichern registrieren
	    root.registerKeyboardAction(new ActionListener() 
	    {
	        @Override
	        public void actionPerformed(ActionEvent event) 
	        {
	        	/* dieser shortcut darf nur aktiv sein, wenn das hauptmenü unsichtbar ist, denn dann
	        	 * reagiert der shortcut aus dem hauptmenü nicht. ansonsten würde 2x. gespeichert werden!
	        	 */
	        	if(!mainMenuBar.isVisible()) {
	        		controller.saveCurrentTodoProject(false);
	        	}
	        }
	    }, KeyStroke.getKeyStroke(KeyEvent.VK_S,  KeyEvent.CTRL_DOWN_MASK, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
	    
	    // Listener für STRG+F für die Suche registrieren
	    root.registerKeyboardAction(new ActionListener() 
	    {
	        @Override
	        public void actionPerformed(ActionEvent event) 
	        {
	        	/* dieser shortcut darf nur aktiv sein, wenn das hauptmenü unsichtbar ist, denn dann
	        	 * reagiert der shortcut aus dem hauptmenü nicht. ansonsten würde 2x. gespeichert werden!
	        	 */
	        	if(!mainMenuBar.isVisible()) {
	        		openSearchDialog();
	        	}
	        }
	    }, KeyStroke.getKeyStroke(KeyEvent.VK_F,  KeyEvent.CTRL_DOWN_MASK, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
	    

	    // Listener für STRG+W für aktuelles Tab schliessen
	    root.registerKeyboardAction(new ActionListener() 
	    {
	        @Override
	        public void actionPerformed(ActionEvent event) 
	        {
	        	
	        	/* Tab nur schliessen wenn nicht das Haupt-Panel selektiert ist */
	        	if(tabbedPane.getSelectedIndex() > 0) {
	        		tabbedPane.remove(tabbedPane.getSelectedIndex());
	        	}
	        	
	        }
	    }, KeyStroke.getKeyStroke(KeyEvent.VK_W,  KeyEvent.CTRL_DOWN_MASK, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
	    
	    
	   
	    return root;
	} 
	
	
	/**
	 * initialisiert alle listener
	 */
	private void initListeners() {
	
		menuItemExit.addActionListener(this);
		menuItemLoadProject.addActionListener(this);
		menuItemSave.addActionListener(this);
		menuItemSaveUnder.addActionListener(this);
		menuItemNew.addActionListener(this);
		menuItemAddList.addActionListener(this);
		menuItemShowLog.addActionListener(this);
		menuItemSearch.addActionListener(this);
		menuItemEnableHints.addActionListener(this);
		menuItemInfo.addActionListener(this);
		menuItemConfig.addActionListener(this);
		menuItemShowTrace.addActionListener(this);

	}
	
	
	
	@Override
	/**
	 * Action Listener, e.g. for the main menu
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		
		/* ********************************************************************* ZULETZT GEÖFFNET BUTTONS */
		for(int i=0; i<lastOpenedItems.length; i++) {
			if(e.getSource() == lastOpenedItems[i]) {
				try {
					File f = new File(((JMenuItem)e.getSource()).getName());
					
					// anhand des bisher gespeicherten files den modus erkennen (.xml oder .tdo datei)
					String mode = "xml";
					if(f.getName().endsWith(".tdo")) {
						mode = "tdo";
					}
					
					controller.loadProject(f, mode);
				} catch(Exception exc) {
					Logger.getInstance().logException("Fehler beim Laden der Datei", exc);
				}
			}
		}
		
		/* ********************************************************************* SHOW TRACE BUTTON */
		if(e.getSource() == menuItemShowTrace)
			for ( StackTraceElement trace : Thread.currentThread().getStackTrace() )      Logger.getInstance().log(trace.toString(), Logger.LOGLEVEL_DEBUG);
		/* ********************************************************************* EXIT BUTTON */
		if(e.getSource() == menuItemExit)			controller.exit();
		/* ********************************************************************* LOAD PROJECT BUTTON */
		if(e.getSource() == menuItemLoadProject) 	controller.loadProject(null);
		/* ********************************************************************* SPEICHERN BUTTON */
		if(e.getSource() == menuItemSave)			controller.saveCurrentTodoProject(false);
		/* ********************************************************************* SPEICHERN UNTER BUTTON */
		if(e.getSource() == menuItemSaveUnder) 		controller.saveCurrentTodoProject(true);
		/* ********************************************************************* SPEICHERN UNTER BUTTON */
		if(e.getSource() == menuItemNew) 			controller.newProject();
		/* ********************************************************************* NEUES PROJEKT BUTTON */
		if(e.getSource() == menuItemAddList)		controller.addMenuItemList();
		/* ********************************************************************* SPEICHERN UNTER BUTTON */
		if(e.getSource() == menuItemInfo)			showInfo();
		/* ********************************************************************* SPEICHERN UNTER BUTTON */
		if(e.getSource() == menuItemSearch) 		openSearchDialog();
		/* ********************************************************************* OPEN CONFIGURATION */
		if(e.getSource() == menuItemConfig) 		openConfigWindow();
		
		/* ********************************************************************* SHOW LOG */
		if(e.getSource() == menuItemShowLog) {
			JFrame jf = new JFrame();
			JTextArea jta = new JTextArea(Logger.getInstance().getLog());
			jf.add(new JScrollPane(jta));
			jf.setSize(600, 400);
			jf.setPreferredSize(new Dimension(1160, 940));
			jf.setVisible(true);
			
			Logger.registerConsumer(jta);
			
			Logger.getInstance().postLog();
		}
		/* ********************************************************************* ENABLE HINTS */
		if(e.getSource() == menuItemEnableHints) {
			repaint();
			
		}
	}
	
	
	/**
	 * öffnet ein neues Fenster mit der Konfiguration für das Programm
	 */
	private void openConfigWindow() {
		
		ConfigurationFrame cf = new ConfigurationFrame(controller, getBounds());
		cf.setVisible(true);
		
	}

	/**
	 * öffent einen Suchen-Dialog
	 */
	public void openSearchDialog() {

		// Der Search Dialog kann nicht in jedem Tab angezeigt werden. Im Overview-Tab gibt es eine class cast exception
		try {
			TodoListBasePanel tlbp = (TodoListBasePanel) tabbedPane.getSelectedComponent();
			tlbp.switchFilterEnabled();
		} catch(Exception e) {}

	}
	
	
	/**
	 * ändert den sichtbarkeits-status des hauptmenüs von sichtbar auf unsichtbar oder umgekehrt
	 */
	public void switchMainMenuVisibleState() {
		
		if(mainMenuBar.isVisible()) {
			tabbedPane.setToolTipText("Menü mit [ALT] einblenden");
			mainMenuBar.setVisible(false);
		}
		else {
			tabbedPane.setToolTipText("");
			mainMenuBar.setVisible(true);
		}
		
	}
	

	/**
	 * initialisiert das layout
	 */
	private void initLayout() {
				
		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		
//        javax.swing.GroupLayout overviewPaneLayout = new javax.swing.GroupLayout(overviewPanel);
//        overviewPanel.setLayout(overviewPaneLayout);
//        overviewPaneLayout.setHorizontalGroup(
//            overviewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 743, Short.MAX_VALUE)
//        );
//        overviewPaneLayout.setVerticalGroup(
//            overviewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 419, Short.MAX_VALUE)
//        );


        javax.swing.GroupLayout basePanelLayout = new javax.swing.GroupLayout(basePanel);
        basePanel.setLayout(basePanelLayout);
        basePanelLayout.setHorizontalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
        );
        basePanelLayout.setVerticalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        
	}
	
	
	
	/**
	 * Adds a new tab to the tabbed pane
	 * @param tp the TodoProject which is shown in the new tab
	 */
	public void addProjectPanel(TodoList tl) {
		
		// konfiguration prüfen, ob beim laden tabs gleich angelegt werden sollen
		if(ConfigurationHandlerImpl.getInstance().getProperty("createTabsOnLoadFile").equals("true")) {

			// Erzeuge das Kopf-Element des Tabbed-Pane
			CloseableTabHead thp = new CloseableTabHead(tl, this);
			
			// Erzeuge das neue Haupt-Element
			TodoListBasePanel tlbp = new TodoListBasePanel(tl, controller);
			
			// Deklariere das neue Kopf-Element als Target für Drop-Aktivitäten
			new DropTarget(thp, new TodoItemStackDropTargetListener(tl, tlbp));
			
			tabbedPane.add(tlbp);
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, thp);

		}
		
		// dem overview-tab hinzufügen
		overviewPanel.addProject(tl);
		
		// wenn tabs nicht angelegt null, darum prüfen
		if(getTodoListBasePanelByTodoList(tl) != null) getTodoListBasePanelByTodoList(tl).setCursorLocked(tl.isLocked());
	}
	
	
	
	/**
	 * fügt eine Datei der Liste zuletzt geöffneter Dateien hinzu
	 * @param path
	 */
	public void addLastOpenedFile(File file) {
		
		openedFileHistorySeparator.setVisible(true);

		/* wenn die datei schon in der liste steht, beende verarbeitung */
		for(int i=0; i<lastOpened.length; i++) {
			if(lastOpened[i] != null) {
				if(lastOpened[i].equals(file)) {

					return;
				}
				
			}
		}
		
		// nach freiem platz in der liste suchen und diesen belegen
		for(int i=0; i<lastOpened.length; i++) {
			if(lastOpened[i] == null) {
				lastOpened[i] = file;
				lastOpenedItems[i].setText(file.getName());
				lastOpenedItems[i].setName(file.getAbsolutePath());
				lastOpenedItems[i].setVisible(true);
				ConfigurationHandlerImpl.getInstance().setLastOpenedFiles(lastOpened);
				return;
			}
		}
		
		
		++lastUpdatedFile;
		if(lastUpdatedFile > lastOpened.length-1) lastUpdatedFile = 0;
		
		lastOpened[lastUpdatedFile] = file;
		lastOpenedItems[lastUpdatedFile].setText(file.getName());
		lastOpenedItems[lastUpdatedFile].setVisible(true);
		ConfigurationHandlerImpl.getInstance().setLastOpenedFiles(lastOpened);
		
		
		
	}

	
	
	/**
	 * zeigt ein komplettes TodoProject an
	 * @param tp
	 */
	public void openTodoProject(TodoProject tp) {
		
		if(reminderHandler != null) reminderHandler.interrupt();		
		
		trayIconHandler.removeReminders();
		
		if(tp.getSavedUnder() != null) addLastOpenedFile(tp.getSavedUnder());
		
		/* die datei wurde gerade frisch geöffnet oder angelegt, also gibts auch keine Änderungen */
		tp.unsavedChanges = false;
		
		

		int tabCount = tabbedPane.getTabCount();

		// alle aktuellen tabs außer das overview tab entfernen
		for(int i=0; i<(tabCount-1); i++) {
			tabbedPane.remove(1);
		}
		

		// Overview-Tab leeren
		overviewPanel.removeAllProjects();
		
		
				
		// current Todo aktualisieren
		this.controller.setCurrentTodoProject(tp);
		
		
		reminderHandler = new ReminderHandler(controller.getCurrentTodoProject());
		reminderHandler.addListener(trayIconHandler);
		reminderHandler.setName("Reminder Handler Thread");
		reminderHandler.start();
		
		Logger.getInstance().log("Reinitialisiere reminderHandler Thread", Logger.LOGLEVEL_INFO);

		
		Iterator<TodoList> it = tp.getLists().iterator();
		int i = 1;
		while(it.hasNext()) {
			++i;
			TodoList tl = it.next();
			
			addProjectPanel(tl);
			LoadingSplashScreen.getInstance().setPercentage((100/tp.getLists().size()) * i);
			
			
		}
		
		/* Titelleiste aktualisieren */
		if(tp.getSavedUnder() != null) {
			this.setTitle("TodoManager " + Start.version + " - " + tp.getSavedUnder().getAbsolutePath());
		} else {
			this.setTitle("TodoManager " + Start.version + " - Nicht gespeichertes Todo-Projekt");
		}
	}


	
	/**
	 * schließt ein tab
	 * @param thpane das kopf-element des tabs, welches geschlossen werden soll
	 */
	public void closePanel(CloseableTabHead thpane) {

		// entferne das tab
		tabbedPane.remove(tabbedPane.indexOfTabComponent(thpane));
	
	}
	

	
	
	public TodoListBasePanel getTodoListBasePanelByTodoList(TodoList tl) {
	
		int max = tabbedPane.getComponentCount()-1;
		
		for(int i=1; i<max; i++) {
			TodoListBasePanel tlbp = (TodoListBasePanel) tabbedPane.getComponentAt(i);	
				
			if(tlbp.getTodoList().equals(tl)) {
				return tlbp;
			}
		}
		
		return null;
	}
	
	/**
	 * zeigt das dem übergebenen TodoProject zugehörige Panel an
	 * @param tp
	 */
	public TodoListBasePanel showProjectInPanel(TodoList tl) {

		int max = tabbedPane.getComponentCount()-1;
		
		for(int i=1; i<max; i++) {
			TodoListBasePanel tlbp = (TodoListBasePanel) tabbedPane.getComponentAt(i);	
				
			if(tlbp.getTodoList().equals(tl)) {
				tabbedPane.setSelectedComponent(tlbp);
				return tlbp;
			}
		}
		
		
		// Erzeuge das Kopf-Element des Tabbed-Pane
		CloseableTabHead thp = new CloseableTabHead(tl, this);
		
		// Erzeuge das neue Haupt-Element
		TodoListBasePanel tlbp = new TodoListBasePanel(tl, controller);
		
		// Deklariere das neue Kopf-Element als Target für Drop-Aktivitäten
		new DropTarget(thp, new TodoItemStackDropTargetListener(tl, tlbp));
		tabbedPane.add(tlbp);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, thp);
		tabbedPane.setSelectedIndex(tabbedPane.getComponentCount()-2);
		getTodoListBasePanelByTodoList(tl).setCursorLocked(tl.isLocked());
		tlbp.updateTodoList();

		return tlbp;
	}
	
	
	
	/**
	 * Aktiviert das Tab, in dem das übergebene TodoItemStack enthalten ist
	 * @param tis TodoItemStack, welcher im gesuchten tab enthalten ist
	 */
	public void setSelectedTabByTodoItemStack(TodoItemStack tis) {
		
		
		
		TodoListBasePanel tlbp = showProjectInPanel(tis.getParent());
		
		tlbp.setSelectedTodoItemStack(tis);
	
	}
	
	
	
	public void renameTabByTodoItemStack(TodoList tis) {
		
		int max = tabbedPane.getComponentCount()-1;
		
		for(int i=1; i<max; i++) {
			TodoListBasePanel tlbp = (TodoListBasePanel) tabbedPane.getComponentAt(i);	
				
				if(tlbp.getTodoList().equals(tis)) {

					tabbedPane.getTabComponentAt(i).setName(tis.getListName());


					return;
				}
		
		}		

	}
	

	
	@Override
	public void update(Observable o, Object arg) {
		
		if(arg instanceof TodoItem) System.out.println("todoItem Änderung");
		if(arg instanceof TodoItemStack) {
			System.out.println("TodoItemStack Änderung");
		}
		if(arg instanceof TodoList) {
			System.out.println("todoList Änderung");
		}
		
		
		if(arg instanceof TodoProject) {
			Logger.getInstance().log("Änderung am TodoProject", Logger.LOGLEVEL_INFO);
			
			TodoProject tp = (TodoProject) arg;
			
			if(tp.getSavedUnder() != null) {
				
				this.setTitle("TodoManager " + Start.version + " - " + tp.getSavedUnder().getAbsolutePath());
				
			} else {
				
				this.setTitle("TodoManager " + Start.version + " - Nicht gespeichertes Todo-Projekt");
			}
			

		}

		
		if(arg instanceof TodoItem || arg instanceof TodoItemStack || arg instanceof TodoList || arg instanceof TodoProject) {
			if(!this.getTitle().endsWith("*")) { 
				this.setTitle(this.getTitle() + "*");
			}
		}
		
		// overview panel auf "geändert" setzen damit der graph mitaktualisiert wird
		overviewPanel.setStateChanged();
		
	}
	
	
	/**
	 * zeigt ein info-fenster mit informationen über das programm an
	 */
	private void showInfo() {
		
		new InfoWindow();
		
		
	}

	
	/**
	 * Liefert die derzeit ausgewählte TodoListe zurück
	 * @return
	 */
	public TodoList getSelectedTodoList() {
		return overviewPanel.getSelectedTodoList();
	}
	
}

