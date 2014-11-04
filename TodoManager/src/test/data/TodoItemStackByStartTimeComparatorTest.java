package test.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import data.TodoItemStack;
import data.TodoItemStackByStartTimeComparator;
import data.TodoList;
import data.TodoProject;

public class TodoItemStackByStartTimeComparatorTest {

	@Test
	public void test() {

		/* Generate different start dates */
		Date startDate1 = new Date(500000000);
		Date startDate2 = new Date(200000000);
		Date startDate3 = new Date(300000000);
		Date reminder4  = new Date(400000000);

		TodoList tl = new TodoList("name", new TodoProject());
		
		/* Generate test objects */
		TodoItemStack tis1 = new TodoItemStack("TIS 1", 1, null, true, 10, startDate1, new Date(), "");
		TodoItemStack tis2 = new TodoItemStack("TIS 2", 2, null, true, 12, startDate2, new Date(), "");
		TodoItemStack tis3 = new TodoItemStack("TIS 3", 3, null, true, 18, startDate3, new Date(), "");
		TodoItemStack tis4 = new TodoItemStack("TIS 4", 4, tl, true, 5, null, null, "");
		
		
		tis4.setReminderDate(reminder4);
		tis4.setReminderActivationStatus(true);
		
		/* Generate new List and add test objects */
		ArrayList<TodoItemStack> tisList = new ArrayList<TodoItemStack>();
		tisList.add(tis1);
		tisList.add(tis2);
		tisList.add(tis3);
		tisList.add(tis4);
		
		/* Initial state: List is in order of incoming */
		assertEquals(tisList.indexOf(tis1), 0);
		assertEquals(tisList.indexOf(tis2), 1);
		assertEquals(tisList.indexOf(tis3), 2);
		assertEquals(tisList.indexOf(tis4), 3);
			
		/* Sort the collection by its start Date with the Comparator */
		Collections.sort(tisList, new TodoItemStackByStartTimeComparator());
		
		/* Target state: List is in order by start Date */
		assertEquals(tisList.indexOf(tis1), 3);
		assertEquals(tisList.indexOf(tis2), 0);
		assertEquals(tisList.indexOf(tis3), 1);
		assertEquals(tisList.indexOf(tis4), 2);
		
		
		
		
	}

}
