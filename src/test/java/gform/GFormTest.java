package gform;
 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException; 

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
 
import org.xml.sax.SAXException;
 

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  30.11.2011::14:16:29<br> 
 */
public class GFormTest extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */ 
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */ 
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */ 
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */ 
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link gform.DFUtil#parseTable(java.lang.String)}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @throws FileNotFoundException 
	 */ 
	public void testParceTableName() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		System.out.println("parceTableName");
		
		File f = new File("gform/servicios_traduccion.jsp");
		String gform = GForm.readFileAsString(f);
		
		int beginIndex = gform.indexOf("<form");
		int endIndex = gform.indexOf("</form>");
		
		String form = gform.substring(beginIndex, endIndex+7);
		String table = new GForm().parceTableName(form);
		
		assertEquals("servicios_traduccion", table);
	}
	
	
	public void testSetTemplate() {
		fail("Not yet implemented");
	}

	public void testGenerateFormJsp() {
		fail("Not yet implemented");
	}

	public void testGenerateProcesaJsp() {
		fail("Not yet implemented");
	}

	public void testUpdateDBSchema() {
		fail("Not yet implemented");
	}

}


 