package test.data;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import data.Attachment;

public class AttachmentTest {
	
	
	@Test
	public void testTemporaryFileName() {
		
		/* Generate two attachments */
		Attachment att1 = new Attachment(Attachment.STORE_MODE_NOSELECTION, new File("test1.txt"));
		Attachment att2 = new Attachment(Attachment.STORE_MODE_NOSELECTION, new File("test2.txt"));
		
		/* Get temporary name two times to ensure that they stay the same */
		String name1_1 = att1.getTemporaryFileName();
		String name1_2 = att1.getTemporaryFileName();
		
		System.out.println(name1_1);
		
		/* Get temporary name two times to ensure that they stay the same */
		String name2_1 = att2.getTemporaryFileName();
		String name2_2 = att2.getTemporaryFileName();
		
		/* Ensure that the temporary names are the same */
		assertEquals(name1_1, name1_2);
		assertEquals(name2_1, name2_2);
		
		
	}

}
