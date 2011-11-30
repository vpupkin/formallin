/**
 * 
 */
package gform;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Administrador
 * 
 */
public class GFormTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
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
	@Test
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

}
