package test.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

import utils.DateUtils;

public class DateUtilsTest {
	
	
	@Test
	public void removeTimeFromDateTest() {
		
		/* Test date = 26.11.2013, 08:08:04 */
		Date d = new Date();
		
		/* Apply filter */
		Date dNeu = DateUtils.removeTimeFromDate(d);
		
		/* get seconds from reduzed date */
		long dNeuSec = dNeu.getTime();
		
		/* the reduzed time is based on seconds (*1000),
		 * and does not contain minuts (*60) and hours (*60)
		 */
		assertEquals((dNeuSec % (60*60*1000)), 0L);		
	}

	@Test
	public void getPreMontagTest() {
		
		/* atuelle Zeit */
		Date now = new Date();
		
		/* 10000 Tage in die Zukunft prüfen sollte reichen */
		for(int i=0; i<10000; i++) {
			
			/* Zu prüfenden Tag berechnen */
			Date d = new Date(now.getTime() + (i*1000*60*60*24));

			/* Montag der Woche berechnen*/
			Date dMod = DateUtils.getPreMontag(d);
			
			Calendar cMod = new GregorianCalendar(Locale.GERMANY);
			cMod.setTime(dMod);
			
			Calendar cInit = new GregorianCalendar(Locale.GERMANY);
			cInit.setTime(d);
			
			/* Es muss sich um einen Montag handeln (=2) */
			assertEquals(cMod.get(Calendar.DAY_OF_WEEK), 2);
			
			/* Der Tag muss in der gleichen Woche des Jahres liegen wie das Original */
			assertEquals(cMod.get(Calendar.WEEK_OF_YEAR), cInit.get(Calendar.WEEK_OF_YEAR));
			
		}
		
	}
}
