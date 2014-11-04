package gui.alerts;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * This alert frame is an file chooser, specialized to opening files.
 * @author Tobias Schaber
 */
public class FileChooserAlert extends JFileChooser {
	
	public static final int OPEN_PROJECT_MODE = 1;
	public static final int SAVE_PROJECT_MODE = 2;
	public static final int EXPORT_EXCEL_MODE = 3;
	
	
	
	public int showOpenDialog(int mode) {
		
		if(mode == OPEN_PROJECT_MODE) {
			this.setDialogTitle("Öffnen");
		}
				
		if(mode == SAVE_PROJECT_MODE) {
			this.setDialogTitle("Projekt Speichern");
			
		}
		
		if(mode == EXPORT_EXCEL_MODE) {
			this.setDialogTitle("Nach Excel exportieren");
		}
		
		return super.showOpenDialog(this);
	}
	
	
	
	
	/**
	 * constructor
	 * in diesem konstruktor werden alle file-filter hinzugefügt
	 * @param mode
	 */
	public FileChooserAlert(int mode) {
		
		this.setDialogTitle("Öffnen");
		
		if(mode == SAVE_PROJECT_MODE || mode == OPEN_PROJECT_MODE) {
			
		
			
			// mehrere file-filter, für jeden dateityp einen
				
			// allow .tdo-files
			this.setFileFilter(new FileFilter() {				
				public boolean accept(File f) {
					if(f.getName().endsWith(".tdo") || !f.isFile()) return true;
					else							 				return false;
				}
					
				public String getDescription() {
					return "Todo-Files (TDO)";
				}
			});
			
			// allow .xml-files
			this.setFileFilter(new FileFilter() {				
				public boolean accept(File f) {
					if(f.getName().endsWith(".xml") || !f.isFile()) return true;
					else							 				return false;
				}
					
				public String getDescription() {
					return "Todo-Dateien (XML)";
				}
			});
			
		} 
		
		if(mode == EXPORT_EXCEL_MODE) {
			
			this.setFileFilter(new FileFilter() {				
				public boolean accept(File f) {
					if(f.getName().endsWith(".xls") || !f.isFile()) return true;
					else							 				return false;
				}
					
				public String getDescription() {
					return "Excel-Datei (XLS)";
				}
			});
			
		}


	}
	
}
