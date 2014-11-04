package test.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import utils.TodoProjectToXmlConverter;

public class TodoProjectToXmlConverterTest {

	@Test
	public void test() {

		/* Test-String mit allen Sonderzeichen die gefiltert werden. Letztes Zeichen muss ein & sein */
		final String unescaped = "<>>><\"><<<>\"\"&&";
		
		/* Convertierter String ohne Sonderzeichen */
		final String escaped = TodoProjectToXmlConverter.escapeXmlString(unescaped);
		
		/* Das "&" ist auch in der bereinigten Version enthalten. Es darf
		 * jedoch nicht als letztes Zeichen übrig sein */
		assertNotEquals(escaped.lastIndexOf("&"), escaped.length());
		 
		/* alle anderen Sonderzeichen dürfen gar nicht mehr enthalten sein */
		assertEquals(escaped.indexOf("<"), -1);
		assertEquals(escaped.indexOf(">"), -1);
		assertEquals(escaped.indexOf("\""), -1);
		
		
		
	
	}

}
