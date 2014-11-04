package main;

import static main.Logger.LOGLEVEL_INFO;
import gui.BaseFrame;
import gui.alerts.FileChooserAlert;
import gui.alerts.LoadingSplashScreen;
import gui.panels.todoeditor.TodoDetailPanel;
import gui.panels.todoeditor.TodoListBasePanel;

import java.io.File;

import javax.swing.JOptionPane;

import main.annotations.OverriddenByConfiguration;

import org.apache.commons.io.FileUtils;

import utils.TodoProjectToXmlConverter;
import controller.*;
import data.*;

/**
 * Der Basehandler ist die zentrale Komponente, welche die Randsysteme
 * anspricht und die hauptoberfläche zeichnet.
 * @author Tobias Schaber
 */
public class BaseController {

	/* das model ist immer das derzeit geöffnete todo-projekt */
	private TodoProject			currentTodoProject = new TodoProject();
	private BaseFrame			baseFrame;
	
	private final FileChooserAlert	fca = new FileChooserAlert(FileChooserAlert.OPEN_PROJECT_MODE);
		
	
	/**
	 * Start application
	 */
	public void launch() {
				
		ConfigurationHandlerImpl.getInstance().initConfiguration(new File("config/"));
		
		/* root default loglevel setzen */
		Logger.getInstance().setLoglevel(Logger.LOGLEVEL_NONE);
		
		try {
			
			@OverriddenByConfiguration
			String loglevel = ConfigurationHandlerImpl.getInstance().getProperty("logLevel");
			
			Logger.MAX_LOG_ENTRIES = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("maxLogEntries"));
			
			if(loglevel.equals("NONE")) 	Logger.getInstance().setLoglevel(Logger.LOGLEVEL_NONE);
			if(loglevel.equals("DEBUG")) 	Logger.getInstance().setLoglevel(Logger.LOGLEVEL_DEBUG);
			if(loglevel.equals("INFO")) 	Logger.getInstance().setLoglevel(Logger.LOGLEVEL_INFO);
			if(loglevel.equals("ERROR")) 	Logger.getInstance().setLoglevel(Logger.LOGLEVEL_ERROR);
			
		} catch(Exception e) {}
		
		/* Verzeichnis temporärer Attachments leeren */
		clearTemporaryAttachmentFolder();
		
		baseFrame = new BaseFrame(this);

		currentTodoProject.addObserver(baseFrame);
		
		baseFrame.setVisible(true);
		baseFrame.openTodoProject(new TodoProject());

	}
		
	
	
	/**
	 * Löscht alle Dateien die im Temp-Verzeichnis für Attachments enthalten sind
	 */
	private void clearTemporaryAttachmentFolder() {
		File attachmentsDir = new File(TodoDetailPanel.ATTACHMENT_TEMP_FOLDER);
		
		try {
			FileUtils.cleanDirectory(attachmentsDir);
		} catch(Exception e) {
			Logger.getInstance().logException("Fehler beim löschen des temporären Attachment-Verzeichnisses", e);
		}
	}



	/**
	 * speichert das übergebene projekt in eine datei
	 * @param targetFile zielpfad der datei
	 * @param tp das zu speichernde objekt
	 * @throws Exception
	 */
	private void saveProject(File targetFile, TodoProject tp, String mode) throws Exception {
	
		Logger.getInstance().log("Speichere im Modus [" + mode + "]", Logger.LOGLEVEL_INFO);

		
		// Nachfragen wenn das File schon existiert
		if(targetFile.exists() && !targetFile.equals(currentTodoProject.getSavedUnder())) {
			int c = JOptionPane.showConfirmDialog(baseFrame, "Die Datei existiert bereits. überschreiben?", "Datei überschreiben?", JOptionPane.YES_NO_OPTION);
			
			// nicht überschreiben ( c == 1)
			if(c == 1) {
				return;
				
			} 
		}

		tp.unsavedChanges = false;
		
		

		// Gespeichert wird in einem separaten Thread
		SaveFileRunnable sfr = new SaveFileRunnable(targetFile, mode, tp, baseFrame);
		Thread saveThread = new Thread(sfr);
		saveThread.start();
		
		
		currentTodoProject.setSavedUnder(targetFile);
		baseFrame.addLastOpenedFile(currentTodoProject.getSavedUnder());

		// den stern am ende entfernen weil gespeichert wurde
		if(baseFrame.getTitle().endsWith("*")) {
			baseFrame.setTitle(baseFrame.getTitle().substring(0, baseFrame.getTitle().length()-1));
		}	
	}
	
	
	
	/**
	 * liest ein projekt aus der angegebenen datei ein
	 * @param sourceFile quelldatei, welche eingelesen werden soll
	 * @throws Exception
	 */
	public void loadProject(File sourceFile, String mode) throws Exception {

		Logger.getInstance().log("Lade Datei im Modus [" + mode + "]", Logger.LOGLEVEL_INFO);
		
		if(sourceFile != null) {
			
			if(mode.equals("xml")) {
				currentTodoProject = (TodoProject) TodoProjectToXmlConverter.convertXMLToTodoProject(PersistenceHandlerImpl.getInstance().loadXML(sourceFile));
			} else {
				currentTodoProject = (TodoProject) PersistenceHandlerImpl.getInstance().loadData(sourceFile);
			}
			currentTodoProject.setSavedUnder(sourceFile);
			currentTodoProject.addObserver(baseFrame);
		
			
			Runnable runnable = new Runnable() {
				public void run() {
					LoadingSplashScreen.getInstance().showSplash("Lade Projekt...bloß keine Eile!");
					
					// dem hauptfenster hinzufügen
					baseFrame.openTodoProject(currentTodoProject);
					
					LoadingSplashScreen.getInstance().hideSplash();
				}
			};
		
			Thread t = new Thread(runnable);
			t.start();	
		}
	}
	
	
	
	/**
	 * Setzt ein neues TodoProject als derzeit ausgewählt.
	 * @param tp
	 */
	public void setCurrentTodoProject(TodoProject tp) {
		this.currentTodoProject = tp;
		tp.addObserver(baseFrame);
	}
	

	
	/**
	 * Versucht, das Programm zu beenden. Fragt vorher zu speichernde Projekte ab etc.
	 */
	public void exit() {
		
		/* räume attachment temp files auf */
		clearTemporaryAttachmentFolder();
		
		if(currentTodoProject.unsavedChanges) {

			int c = JOptionPane.showConfirmDialog(baseFrame, "Es gibt ungespeicherte Änderungen. Trotzdem beenden? Änderungen gehen dabei verloren!", "Wirklich beenden?", JOptionPane.YES_NO_OPTION);
				
			if(c == 0) {
				System.exit(1);
			}
		} else {
			System.exit(1);
		}
	}
	
	
	
	/**
	 * läd ein neues projekt. fragt vorher zu speichernde projekte ab etc.
	 * @param useFile optionales file. wenn nicht gesetzt, wird open file dialog angezeigt
	 */
	public void loadProject(File useFile) {
		int c = 0;
		
		// nachfragen ob Änderungen verworfen werden sollen
		if(currentTodoProject.unsavedChanges) {
			c = JOptionPane.showConfirmDialog(baseFrame, "Es gibt ungespeicherte Änderungen. Trotzdem öffnen? Änderungen gehen dabei verloren!", "Wirklich neues Projekt laden?", JOptionPane.YES_NO_OPTION);
			
		}
		
		if(c == 0) {
			int opt = 0;
			
			// wenn kein file zum öffnen vorgegeben wurde, öffne file öffnen dialog
			if(useFile == null) {
				opt = fca.showOpenDialog(FileChooserAlert.OPEN_PROJECT_MODE);
			}
			
			if(opt == 0) {
				
				// wenn kein file zum öffnen vorgegeben wurde, nehme das, welches aus dem file öffnen dialog kommt
				if(useFile == null) useFile = fca.getSelectedFile();
				
	
				/* hier wird ein runnable erzeugt, das an die awt event queue geschickt wird, um parallel ausgeführt zu werden,
				 * damit die jprogressbar live gezeichnet werden kann */
	
				try {		
					String mode;
					// anhand des bisher gespeicherten files den modus erkennen (.xml oder .tdo datei)
					if(useFile.getName().endsWith(".xml")) mode = "xml";
					else {
						if(useFile.getName().endsWith(".tdo")) mode = "tdo";
						else {
							throw new Exception("Ungültige Datei-Endung: Nur .xml und .tdo-Dateien sind gültig.");
						}
					}
					

					loadProject(useFile, mode);
	
				} catch(Exception exc) {
					JOptionPane.showMessageDialog(baseFrame, "Beim Öffnen der Datei ist ein Fehler aufgetreten. Möglicherweiße ist die Datei defekt oder keine TodoManager Projekt-Datei.\n\nDetailierte Fehlerursache:\n" + exc.getMessage(), "Fehler beim Öffnen der Datei", JOptionPane.ERROR_MESSAGE);
					Logger.getInstance().logException("Fehler beim Laden der Datei", exc);
				}
			}
		}
	}
	
	
	
	/**
	 * kapselt den speicherungsprozess mit abfragen etc.
	 * @param tp
	 */
	public void saveCurrentTodoProject(boolean saveUnder) {

		try {
			/* wenn ein pfad gesetzt ist und speichern anstatt speichern unter gewählt wurde */
			if(currentTodoProject.getSavedUnder() != null && !saveUnder) {
				
				if(currentTodoProject.unsavedChanges) {
						
					Logger.getInstance().log("Speichere unter " + saveUnder, LOGLEVEL_INFO);
					
					
					// anhand des bisher gespeicherten files den modus erkennen (.xml oder .tdo datei)
					String mode = "xml";
					if(currentTodoProject.getSavedUnder().getName().endsWith(".tdo")) {
						mode = "tdo";
					}
					
					saveProject(currentTodoProject.getSavedUnder(), currentTodoProject, mode);
					baseFrame.addLastOpenedFile(currentTodoProject.getSavedUnder());
				}
				
			} else {
				/* es ist noch kein saveunder-file gesetzt oder es soll save-under ausgeführt werden --> datei abfragen */
				
				// JItemChooser starten und file wählen, und dann dort speichern
				int resp = fca.showOpenDialog(FileChooserAlert.SAVE_PROJECT_MODE);
				
				// resp = 1 -> Abbruch des Speicherns
				if(resp != 1) {
				
					File targetFile = fca.getSelectedFile();
					
			
					/* wenn der dialog nicht abgebrochen wurde */
					if(targetFile != null) {
						
						// anhand des bisher gespeicherten files den modus erkennen (.xml oder .tdo datei)
						String mode = "xml";
						if(targetFile.getName().endsWith(".tdo")) {
							mode = "tdo";
						}
							
						saveProject(targetFile, currentTodoProject, mode);	
					}					
				}
			}
		} catch(Exception exc) {
			
			Logger.getInstance().logException("Fehler beim Speichern des aktuellen Projektes", exc);
		}		
	}
	
	
	
	/**
	 * legt ein neues todo project an. fragt vorher zu speichernde Änderungen ab etc.
	 */
	public void newProject() {
		int c = 0;
		
		if(currentTodoProject.unsavedChanges) {
			c = JOptionPane.showConfirmDialog(baseFrame, "Es gibt ungespeicherte Änderungen. Trotzdem neues Projekt anlegen? Änderungen gehen dabei verloren!", "Wirklich neues Projekt anlegen?", JOptionPane.YES_NO_OPTION);
			
		}
		
		if(c == 0) {
		
			baseFrame.openTodoProject(new TodoProject());
		}

	}
	
	
	/**
	 * Fügt eine neue Todo-Liste hinzu. Fragt in einem Dialog nach dem Namen der Liste
	 */
	public void addMenuItemList() {
		/* fenster zum eingeben des neuen projektnamens anzeigen */
		String newName = JOptionPane.showInputDialog(null, "Name der Liste:", "Neue Liste anlegen", JOptionPane.PLAIN_MESSAGE);
		
		if(newName != null && !newName.equals("")) {
			TodoList tl = new TodoList(newName, currentTodoProject);
			currentTodoProject.addList(tl);
			baseFrame.addProjectPanel(tl);	
		}
	}
	
	
	/**
	 * Liefert das aktuell ausgewählte TodoProject
	 * @return das aktuelle TodoProject
	 */
	public TodoProject getCurrentTodoProject() {
		return currentTodoProject;
	}
	
	
	
	/**
	 * Der Aufruf dieser Methode zeigt das Fenster an bzw. macht es sichtbar
	 */
	public void showWindow() {
		baseFrame.setVisible(true);
	}
	
	
	
	/**
	 * leitet einen aufruf zum anzeigen eines bestimmten todo tabs weiter
	 */
	public void setSelectedTabByTodoItemStack(TodoItemStack stack) {
		baseFrame.setSelectedTabByTodoItemStack(stack);
	}
	
	
	
	public void showProjectInPanel(TodoList tp) {
		baseFrame.showProjectInPanel(tp);
	}
	
	
	
	/**
	 * benennt eine todoliste um. der neue name wird über ein dialog-fenster eingegeben
	 * @param toBeRenamed
	 */
	public void renameTodoList(TodoList toBeRenamed) {


		String newName = (String) JOptionPane.showInputDialog(null, "message", "title", JOptionPane.PLAIN_MESSAGE, null, null, toBeRenamed.getListName());
		
		if(newName != null && !newName.equals("")) {
			toBeRenamed.setListName(newName);
			baseFrame.renameTabByTodoItemStack(toBeRenamed);
		}

		baseFrame.repaint();
	}
	
	
	/**
	 * finalisiert eine todo-liste, also macht sie unbearbeitbar
	 * @param toBeLocked
	 */
	public void switchLockStatus(TodoList toBeLocked) {
		
		
		// setze den status der todoliste auf "locked"
		toBeLocked.switchLockStatus();
		TodoListBasePanel tlbp = baseFrame.getTodoListBasePanelByTodoList(toBeLocked);
		if(tlbp != null) tlbp.setCursorLocked(toBeLocked.isLocked());
	}
	
	
	
	public BaseFrame getBaseFrame() {
		return baseFrame;
	}

}


