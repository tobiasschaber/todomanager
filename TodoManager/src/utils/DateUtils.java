package utils;

import java.text.SimpleDateFormat;
import java.util.*;

import controller.ConfigurationHandlerImpl;


/**
 * Controller für diverse Funktionen die Programmübergreifend benötigt werden.
 * @author Tobias Schaber
 *
 */
public class DateUtils {
	
	/**
	 * Entfernt von einem Zeitstempel (Date) alle Stunden, Minuten, Sekunden und Millisekunden,
	 * so dass nur noch der Tag um 00:00:00.000 Uhr übrig bleibt
	 * @param d Das Eingangs-Datum mit Uhrzeit-Informationen
	 * @return Das bearbeitete Datum ohne Uhrzeit-Informationen
	 */
	public static Date removeTimeFromDate(Date d) {
		
		if(d == null) return null;
		
		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(d);
		gc.set(GregorianCalendar.HOUR, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
    
		/* manchmal liefert der JDateChooser Nachmittag, 12 Uhr zurück. das muss auf 0 gesetzt werden */
		if(gc.get(GregorianCalendar.HOUR_OF_DAY) == 12) {
			gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		}
		
		
		
		return gc.getTime();
		
	}
	
	
	/**
	 * Liefert ein formatiertes Datum nach Standard-Formatierung
	 * @param d Das zu formatierende Datum
	 * @return Das formatierte Datum als String
	 */
	public static String getFormattedDate(Date d) {

		return new SimpleDateFormat(
				ConfigurationHandlerImpl
				.getInstance()
				.getProperty("DefaultDateFormat"))
				.format(d);
		
		
	}

	
	
	/**
	 * Nimmt ein Date entgegen und liefert aus der Woche, in der sich das Date befindet,
	 * den Montag zurück.
	 * @param d Ein beliebiger Tag in Form eines Date
	 * @return Der Montag der dazugehörigen Woche
	 */
	public static Date getPreMontag(Date d) {
		Calendar c = Calendar.getInstance(Locale.GERMANY);
		c.setTime(d);
		
		c.set(Calendar.DAY_OF_WEEK, 2);
		
		return c.getTime();
	}	
	
	

}
