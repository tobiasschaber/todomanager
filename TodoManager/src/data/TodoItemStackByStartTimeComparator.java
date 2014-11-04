package data;

import java.util.Comparator;
import java.util.Date;


/**
 * Dieser Comparator vergleicht zwei TodoItemStacks auf Basis ihres
 * Start-Zeitpunkts, und falls dieser nicht gesetzt ist, auf Basis
 * ihres Reminder-Datums. Dies wird z.B. verwendet, um im Time-Panel
 * eine Sortierung der Anzeige nach Start-Datum zu erreichen.
 * @author Tobias Schaber
 */
public class TodoItemStackByStartTimeComparator implements Comparator<TodoItemStack> {

	
	@Override
	public int compare(TodoItemStack tis1, TodoItemStack tis2) {
		
		Date date1;
		Date date2;
		
		if(tis1.getPlanStart() != null) {
			date1 = tis1.getPlanStart();
		} else {
			if(tis1.getReminderActivationStatus() && tis1.getReminderDate() != null) {
				date1 = tis1.getReminderDate();
			} else {
				/* Kein Vergleich möglich, gleich abbrechen */
				return 0;
			}
		}
		
		
		if(tis2.getPlanStart() != null) {
			date2 = tis2.getPlanStart();
		} else {
			if(tis2.getReminderActivationStatus() && tis2.getReminderDate() != null) {
				date2 = tis2.getReminderDate();
			} else {
				/* Kein Vergleich möglich, gleich abbrechen */
				return 0;
			}
		}
		
		return date1.compareTo(date2);
	}
		
}
