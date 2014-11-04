package controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import main.Logger;

/**
 * Singleton Implementierung zur Verwaltung der Konfiguration des gesamten
 * Programms. Besitzt einen Thread der die Konfigurations-Datei auf Änderungen
 * überwacht und diese ggf. einliest. Verwaltet außerdem die Kategorie-Liste,
 * die ebenfalls in einer Datei abgelegt wird, sowie die Liste zuletzt
 * geöffneter Dateien.
 * @author Tobias Schaber
 */
public class ConfigurationHandlerImpl implements ConfigurationHandlerInterface, Runnable {
	/* singleton implementation */
	private static ConfigurationHandlerImpl instance = new ConfigurationHandlerImpl();
	
	private File	   		configFile;
	private File			categoriesFile;
	private File			lastOpenedFilesFile;
	private Properties 		properties;
	private Properties		categories;
	
	// konfiguration des programms zur laufzeit
	
	public static boolean reminderIsActive = true;
	
	/**
	 * default constructor
	 */
	private ConfigurationHandlerImpl() {
		Thread t = new Thread(this);
		t.start();
	}
	
	
	/**
	 * singleton access method
	 * @return the singleton config object
	 */
	public static ConfigurationHandlerImpl getInstance() {

		return instance;
	}
	

	@Override
	public void initConfiguration(File configFolder) {
		
		this.configFile = new File(configFolder.getAbsolutePath() + "/todoConfig.xml");
		this.categoriesFile = new File(configFolder.getAbsolutePath() + "/categories.xml");
		this.lastOpenedFilesFile = new File(configFolder.getAbsolutePath() + "/openedFilesHistory.xml");
		
		
		properties = new Properties();
		categories = new Properties();
		
		try {
			properties.loadFromXML(new FileInputStream(configFile));
			
		} catch(Exception e) {
			Logger.getInstance().logException("Properties-Datei nicht vorhanden. Verwende Default-Werte.", e);
		}
		
		addDefaultValues();
		
		try {
			categories.loadFromXML(new FileInputStream(categoriesFile));
		} catch(Exception e) {
			Logger.getInstance().logException("Categories-Datei nicht vorhanden. Keine Kategorien verfügbar.", e);
		}
	}
	
	@Override
	public String getProperty(String key) {
			String repl = properties.getProperty(key);
			
			if(repl == null) {
			
				Logger.getInstance().log("Konfigurationsfehler. Property existiert nicht: " + key, Logger.LOGLEVEL_ERROR);
				return "";
			}
			
		return repl;
	}
		
	
	/**
	 * dieser thread pollt die konfigurations-datei zyklisch auf Änderungen und
	 * liest die datei ggf. neu ein
	 */
	public void run() {
		
		Logger.getInstance().log("Starte Überwachung der Konfigurationsdatei", Logger.LOGLEVEL_INFO);

		Date lastChangeOnConfigFile = new Date();
		
		// default value für config file change polling
		long pollingInterval = 1000;
		
		try {
			while(true) {

				try { pollingInterval = Long.parseLong(getProperty("configFilePollingIntervalMs")); }
				catch(NumberFormatException nfe) {}
				
				Thread.sleep(pollingInterval);
				
				Date lastRealChange = new Date(configFile.lastModified());
				
				if(lastRealChange.after(lastChangeOnConfigFile)) {
					lastChangeOnConfigFile = lastRealChange;
					
					try {
						initConfiguration(new File(configFile.getParent()));
						Logger.getInstance().log("Lese Konfigurationsdatei neu ein", Logger.LOGLEVEL_DEBUG);
						
					} catch(Exception e) {
						Logger.getInstance().logException("Fehler beim update der Konfigurationsdatei", e);
						
					}
				}
				
				
				
			}
		
		} catch(InterruptedException ie) {

		}
	}
	
	
	
	/**
	 * Füllt die Properties, die bisher noch nicht gesetzt wurden, mit den Default-Werten auf
	 */
	public void addDefaultValues() {
		Logger.getInstance().log("Vervollständige Konfiguration mit Default-Werten", Logger.LOGLEVEL_INFO);
		
		overrideDefaultValue("", "");

		
		overrideDefaultValue("DefaultSelectionMode", "TODO WAIT PENDING ALARM");
		overrideDefaultValue("DefaultDateFormat", "dd.MM.yyyy HH:mm");
		overrideDefaultValue("logLevel", "INFO");
		overrideDefaultValue("secretDoor", "disabled");
		overrideDefaultValue("reminderDelayTimeSec", "900");
		overrideDefaultValue("configFilePollingIntervalMs", "3000");
		overrideDefaultValue("enableHints", "true");
		overrideDefaultValue("maxLogEntries", "-1");
		overrideDefaultValue("highPrioCount", "7");
		overrideDefaultValue("todoListColorAlarm", "-65536");
		overrideDefaultValue("todoListColorDone", "-16726016");
		overrideDefaultValue("todoListColorSelection", "-6832897");
		overrideDefaultValue("todoListColorDefault", "-16777216");
		overrideDefaultValue("todoListColorDefaultBg", "-1");
		overrideDefaultValue("createTabsOnLoadFile", "false");
		
		overrideDefaultValue("showLockedListsOnStartup", "true");
		overrideDefaultValue("showHighPrioListOnStartup", "true");
		overrideDefaultValue("showSummaryDiagramOnStartup", "true");
		
		overrideDefaultValue("box.width", "170");
		overrideDefaultValue("box.height", "45");
		overrideDefaultValue("box.warning.x", "140");
		overrideDefaultValue("box.warning.y", "5");
		overrideDefaultValue("box.reminder.x", "15");
		overrideDefaultValue("box.reminder.y", "30");

		overrideDefaultValue("box.color.right", "-1");
		overrideDefaultValue("box.color.left", "-1");
		overrideDefaultValue("box.color.left.clean", "-2886445");
		overrideDefaultValue("box.color.left.alarm", "-46261");
		overrideDefaultValue("box.color.left.locked", "-4737097");
		
		overrideDefaultValue("timepanel.startup.default.view", "day");	// accept "day" and "week"

	}	
	

	/**
	 * Fügt den Properties einen Key mit dem übergebenen Wert hinzu
	 * @param key
	 * @param defaultValue
	 */
	public void overrideDefaultValue(String key, String defaultValue) {
		
		if(key != null && key != "") {
			
			if(!properties.containsKey(key)) {
				properties.put(key, defaultValue);
				Logger.getInstance().log("Config: Verwende Default-Wert für Key [" + key + "] : " + defaultValue, Logger.LOGLEVEL_INFO);
			}
		}
		
	}
	
	
	/**
	 * liefert das ganze Set Properties zurück
	 * @return
	 */
	public Properties getProperties() {
		return properties;
		
	}
	
	/**
	 * liefert das ganze Set an Kategorien zurück
	 * @return
	 */
	public Properties getCategories() {
		return categories;
		
	}
	
	
	
	/**
	 * speichert das neue config-file. übermittelt werden nur die
	 * geänderten properties
	 */
	public void updateConfigFile(Properties newProp) {
		
		/* ünderungen mergen. fügt keine neuen properties hinzu, aktualisiert
		   nur die alten properties */
		
		for(Object s : newProp.keySet()) {
			
			String key = (String)s;
			if(properties.containsKey(key)) {
				properties.setProperty(key, newProp.getProperty(key));
			}
		}
		
		for(Object s : newProp.keySet()) {
			
			String key = (String)s;
			if(categories.containsKey(key)) {
				categories.setProperty(key, newProp.getProperty(key));
			}
		}
	
		try {
			
			// persistieren
			properties.storeToXML(new FileOutputStream(configFile), "");
			categories.storeToXML(new FileOutputStream(categoriesFile), "");
			
		} catch(Exception e){
			
			e.printStackTrace();
			
		}

	}
	
	
	/**
	 * liefert zu einer gegebenen Kategorie die eingetragene Farbe
	 * @param cat die Kategorie für die eine Farbe gesucht wird
	 * @return die Farbe der Kategorie
	 */
	public Color getCategoryColor(String cat) {
		if(cat == null) return Color.white;
		
		if(categories.containsKey(cat)) {
			return new Color(Integer.parseInt(categories.getProperty(cat)));
		}
		
		return Color.white;
	
	}	
	
	
	public void addCategory(String newCatName) {
		
		categories.setProperty(newCatName, "0");
		
		try {
			categories.storeToXML(new FileOutputStream(categoriesFile), "");
		} catch(Exception e) {
			Logger.getInstance().logException("Fehler beim Speichern der Kategorien-Datei", e);
		}
		
	}
	
	
	/**
	 * Liest die Konfigurations-Datei für zuletzt geöffnete Dateien ein und liefert die Liste zurück
	 * @return 
	 */
	public  ArrayList<File> getLastOpenedFiles() {
		
		Properties lastOpenedFiles = new Properties();
		ArrayList<File> fileList = new ArrayList<File>();
		
		try {
			
			lastOpenedFiles.loadFromXML(new FileInputStream(lastOpenedFilesFile));
			

			for(Object file : lastOpenedFiles.keySet()) {
				
				File tmpFile = new File((String)file);
				fileList.add(tmpFile);
				
			}

		} catch(Exception e) {
			
			Logger.getInstance().logException("Fehler beim Laden der zuletzt geöffneten Dateien Liste", e);
		}
		
		return fileList;
		
	}
	
	
	
	/**
	 * Speichert die Liste zuletzt geöffneter Dateien in einer XML-Properties-Datei
	 * @param fileList
	 */
	public  void setLastOpenedFiles(File[] fileList) {


		Properties lastFileProps = new Properties();
		
		for(int i=0; i<fileList.length; i++) {

			if(fileList[i] != null) {
				lastFileProps.setProperty(fileList[i].getAbsolutePath(), fileList[i].getName());
			}
		}
		
		try {
			
			lastFileProps.storeToXML(new FileOutputStream(lastOpenedFilesFile), "");
			
		} catch(Exception e) {
			
			Logger.getInstance().logException("Fehler beim Speichern der zuletzt geöffneten Dateien Liste", e);
		}
	}
}



