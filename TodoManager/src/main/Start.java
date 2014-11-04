package main;

import gui.alerts.LoadingSplashScreen;

import javax.swing.UIManager;

/**
 *
 *
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * 		       
 *   ##########            #                   #    ##
 * 	#	###                #                  ##   # ##            
 *     ###     ###     ### #   ###           #  # #  ##    ###     ## ##    ###      ####    ###    ## #
 *     ##     #   #   #    #  #   #         #   # #  ##   #   #    ##   #  #   #    #    #  # # #   ###
 *     ##  #  #   #   #    #  #   #        ##    #   ##   #   #    ##   #  #   #    #    #  #  #    ##
 *     ####    ###     # # #   ###        #           ##   #####   ##   #   #####   ### ##   #####  ##
 *                      																 #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * 																					#    #
 * @author Tobias Schaber														     ####
 * 
 * Root-Class mit main() zum Start des Programms.
 * 
 * Aktuelle Änderungen und Versions-Verlauf: Siehe ReleaseNotes.txt
 *
 */
public class Start {

	//TODO: Speichern-Button in der Detail-Ansicht ausgrauen wenn keine Änderungen existieren
	
	//TODO: Breite einer Einheit im Zeitstrahl (Tag/Woche) konfigurierbar machen und dann über GUI einstellbar

	//TODO: Update-Saving implementieren, so dass in TODOs nur immer der geänderte Wert gespeichert wird.
	//		Kann sogar so weit gehen, dass die im Speicher gehaltenen Objekte nicht existierende Werte immer
	// 		vom vorherigen Element anfragen :)
		
	//TODO: Start-Ansicht (Tages/Wochen-Ansicht) des Time-Panels konfigurierbar machen via gui
	//TODO: Konfigurierbar machen in GUI: Beim Starten des Programms High-Prio-Liste anzeigen
	//TODO: Konfigurierbar machen in GUI: Beim Starten des Programms Grafische Übersicht anzeigen
	
	//TODO: Jeder Status ein Icon. Dann kann im Time Panel ein "Verlauf" eingeblendet werden für ein Todo und an jeder Änderung wird nur das icon gezeichnet
	//TODO: Testen des Scroll-Verhalten im TimePanel umfangreich, ob noch alle Bounds und Zeichen-Datums stimmen etc.
	//TODO: Idee: Rechte Maustaste auf das TimePanel auf einen Tag -> "Als Abwesend markieren"..sowas wie eine zusätzliche Terminliste einbauen?
	
	//TODO: Vielleicht lässt sich das TimePanel beim wieder kleiner machen / neu laden auch im speicher zu verkleinern
	public static final String version 		= "1.74";
	public static final String releaseDate	= "NYR"; 
	
	
	public static void main(String[] args) {

		/* Fange mit dem Splash-Screen an. Ist der erst mal zu sehen sind dem User
		 * stundenlange Wartezeiten bestimmt egal :) */
		LoadingSplashScreen.getInstance().showSplash("Starte Todo Manager..Hab Geduld!");
			
		try {
			/* Default Look&Feel verwenden. Niemand mag das Java L&F */
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch(Exception e) {
			Logger.getInstance().logException("Fehler beim Laden des System Look&Feel", e);
		}
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				LoadingSplashScreen.getInstance().setPercentage(100);
				BaseController bc = new BaseController();
				bc.launch();
			}
		});	
	}	
}

