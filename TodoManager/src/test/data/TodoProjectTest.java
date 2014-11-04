package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.TodoProject;

public class TodoProjectTest {

	@Test
	public void testInitialState() {
		
		TodoProject tp = new TodoProject();
		
		assertEquals(tp.unsavedChanges, false);
		assertNull(tp.getSavedUnder());
		assertEquals(tp.countObservers(), 0);
	}
	
	
	

}
