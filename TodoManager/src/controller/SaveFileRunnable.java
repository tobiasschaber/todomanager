package controller;

import gui.BaseFrame;

import java.io.File;

import javax.swing.JOptionPane;

import utils.TodoProjectToXmlConverter;
import data.TodoProject;

/**
 * Runnable um Dateien im Hintergrund zu speichern.
 * Dieses Runnable wird als Thread parallel zur normalen Verarbeitung gestartet.
 * Große Todo-Projekte können so gespeichert werden, ohne dass es auf der Oberfläche
 * zu Verzögerungen kommt. Im Fehlerfall erfolgt eine Fehlermeldung an die Oberfläche.
 * @author Tobias Schaber
 */
public class SaveFileRunnable implements Runnable {
	
	private File targetFile;
	private String mode;
	private TodoProject tp;
	private BaseFrame baseFrame;
	
	public SaveFileRunnable(File targetFile, String mode, TodoProject tp, BaseFrame baseFrame) {
		
		this.targetFile = targetFile;
		this.mode = mode;
		this.tp = tp;
		this.baseFrame = baseFrame;
		
		
		
	}
			
	@Override
	public void run() {

		try {
			
			if(mode.equals("tdo")) {
				PersistenceHandlerImpl.getInstance().storeData(targetFile, tp);			
			} else {
				PersistenceHandlerImpl.getInstance().storeXML(targetFile, TodoProjectToXmlConverter.convertTodoProjectToXML(tp));
			}
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(baseFrame, "Beim Speichern ist ein Fehler aufgetreten.", "Fehler beim Speichern", JOptionPane.WARNING_MESSAGE);
			
			baseFrame.setTitle(baseFrame.getTitle() + "*");
			tp.setUnsavedChanges(true);
			
		}
		
		
	}

}
