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
public class GridTest extends TestCase {

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
	public void testGrid() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		System.out.println("test Grid");
		
		File f = new File("test/gform/test.jsp");
		String gform = GForm.readFileAsString(f);
		
		int beginIndex = gform.indexOf("<grid>");
		int endIndex = gform.indexOf("</grid>");
		
		String gformElement = gform.substring(beginIndex, endIndex+7);
		Grid element = Grid.parceGFormGrid(gformElement);
		
		assertEquals("titulo", element.getTitle());
		
	}
}
