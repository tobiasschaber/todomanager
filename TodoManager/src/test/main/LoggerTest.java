package test.main;

import static org.junit.Assert.assertEquals;
import main.Logger;

import org.junit.Test;

public class LoggerTest {
	
	@Test
	public void testMaxLogEntries() {
		
		/* Definiere maximale Länge des Logs */
		final int maxEntries = 10;
		
		/* Definiere Text für Testnachricht zur Identifikation */
		final String logMessage="TestNachricht";
		
		/* Setze Limit */
		Logger.MAX_LOG_ENTRIES = maxEntries;

		Logger.getInstance().setLoglevel(Logger.LOGLEVEL_INFO);

		
		/* Schreibe maxEntries * 2 Einträge, damit es auf jeden Fall viel mehr
		 * Einträge werden, als maximal enthalten sein dürfen
		 */
		for(int i=0; i<=maxEntries*2; i++) {
			Logger.getInstance().log(logMessage, Logger.LOGLEVEL_INFO);
		}

		/* Lese komplettes log */
		String logString = Logger.getInstance().getLog();

		/* Split - 1 weil immer mehr Elemente als Trenner */
		int count = logString.split(logMessage).length-1;
		
		/* Zähle Vorkommen der Testnachricht im gesamten Log und vergleiche */
		assertEquals(count, maxEntries);
		
		
	}


	@Test
	public void testLogLevels() {
		
		final String testMessage = "Testmessage_Level";
		
		/* Resette maximale Log-Größe */
		Logger.MAX_LOG_ENTRIES = 100;
		
		/* Setze Loglevel NONE. Es dürfen nur noch Errors geloggt werden */
		Logger.getInstance().setLoglevel(Logger.LOGLEVEL_NONE);
		
		
		Logger.getInstance().log(testMessage, Logger.LOGLEVEL_INFO);
		Logger.getInstance().log(testMessage, Logger.LOGLEVEL_DEBUG);
		Logger.getInstance().log(testMessage, Logger.LOGLEVEL_NONE);
		Logger.getInstance().log(testMessage, Logger.LOGLEVEL_ERROR);
		
		int count = Logger.getInstance().getLog().split(testMessage).length-1;
		
		/* Nur INFO und ERROR dürfen durchgekommen sein */
		assertEquals(count, 2);
		
		
		
	}
}
