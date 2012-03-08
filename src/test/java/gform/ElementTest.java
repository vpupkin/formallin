/**
 * 
 */
package gform;
 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException; 

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import junit.framework.TestCase;  
import org.xml.sax.SAXException;

/**
 * @author Administrador
 * 
 */
public class ElementTest  extends TestCase {

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
	 * Test method for {@link gform.Element}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @throws FileNotFoundException 
	 */ 
	public void testElement() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		System.out.println("Element");
		
		File f = new File("test/gform/test.jsp");
		String gform = GForm.readFileAsString(f);
		
		int beginIndex = gform.indexOf("<element>");
		int endIndex = gform.indexOf("</element>");
		
		String gformElement = gform.substring(beginIndex, endIndex+10);
		Element element = Element.parceGFormElement(gformElement);
		
		assertEquals("id_orden", element.getName());
		assertEquals(ElementType.INT, element.getType());
		assertTrue(element.isKey());
		assertTrue(element.isAuto());
		assertTrue(element.isHidden());
		
	}
}
