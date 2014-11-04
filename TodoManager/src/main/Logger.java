package main;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextArea;

import main.annotations.OverriddenByConfiguration;

/**
 * Logger zur Protokollierung von Fehlern und Meldungen, je nach Log-Level.
 * Ausgabe geht an System.out.
 * @author Tobias Schaber
 */
public class Logger {
	
	/* maximale anzahl von logeinträgen die gespeichert werden */
	@OverriddenByConfiguration
	public static int MAX_LOG_ENTRIES	= 0;
	
	/* je höher das hier gesetzte LogLevel, 
	desto seltener wird ein event geloggt.
	info wird immer, error selten geloggt */ 
	public static final int LOGLEVEL_ERROR  = 8;
	public static final int LOGLEVEL_NONE 	= 5;
	public static final int LOGLEVEL_DEBUG 	= 3;
	public static final int LOGLEVEL_INFO 	= 1;
	
	// hier kann sich ein textfeld (vom log-fenster) registrieren
	public static JTextArea txtAreaLog;
	
	public static final String[] logLevelNames = new String[] {"", "INFO", "", "DEBUG", "", "NONE", "", "", "ERROR"};

	
	/* das developer log protkolliert zusätzliche informationen über den logger */
	private static final boolean activateDeveloperLog = true;
	
	private final ArrayList<String> log = new ArrayList<String>();
	private int logLevel;
	
	private static final Logger instance = new Logger();
	
	private Logger() {}
	
	public static Logger getInstance() {
		return instance;
	}
	
	
	
	/**
	 * Fügt eine Nachricht dem Log hinzu
	 * @param msg die zu loggende Nachricht
	 */
	public void log(String msg, int level) {
		if(level >= logLevel) {
		
			log.add(logLevelNames[level] + " : " + msg);
			if(activateDeveloperLog) System.out.println("Developer Log: [" + logLevelNames[level] + "] " + msg);
			
			
			// wenn das target textfield gesetzt wurde, dann wird auch hieraus geloggt
			if(txtAreaLog != null) {
				txtAreaLog.setText(txtAreaLog.getText() + "\n" + logLevelNames[level] + " : " + msg);
			}
			
			// wenn zu viele log-einträge existieren, wird der älteste gelöscht. kann mit -1 deaktiviert werden
			if(MAX_LOG_ENTRIES != -1 && log.size() > MAX_LOG_ENTRIES) log.remove(0);
		}
	}
	
	public void setLoglevel(int loglevel) {
		this.logLevel = loglevel;
		if(activateDeveloperLog) {
			
			System.out.println("Developer Log: Setze Loglevel auf : " + loglevel + " / " + logLevelNames[loglevel]);
		}
	}
	
	
	
	public void postLog() {
		
		System.out.println("--------------------------------------------------------");
		Iterator<String> it = log.iterator();
		
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("--------------------------------------------------------");
	}
	
	
	
	public String getLog() {
		
		String retStr = "";
		
		retStr += "\n--------------------------------------------------------";
		Iterator<String> it = log.iterator();
		
		while(it.hasNext()) {
			retStr += "\n" + it.next();
		}
		retStr += "\n--------------------------------------------------------";
		
		return retStr;
	}
	
	
	
	/**
	 * registriert ein jtextarea. ist dieses registriert, 
	 * so wird die log-ausgabe hier auch ausgegeben. Wird verwendet um
	 * das Log-Fenster zu aktualisieren
	 * @param ta
	 */
	public static void registerConsumer(JTextArea ta) {
		txtAreaLog = ta;
		
	}
	
	
	
	/**
	 * logt eine Exception. Gibt zusätzlich den Exception Stacktrace aus
	 * @param msg
	 * @param e
	 */
	public void logException(String msg, Exception e) {
		log(msg, LOGLEVEL_ERROR);
		
		for(int i=0; i<e.getStackTrace().length ;i++) {
			log.add(logLevelNames[LOGLEVEL_ERROR] + " : " + e.getStackTrace()[i]);
		}
		
		e.printStackTrace();
	}
}
