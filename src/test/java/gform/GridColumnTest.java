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
public class GridColumnTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link gform.GridColumn}.
	 */
	@Test
	public void testToString(){
		System.out.println("test GridColumn.toString()");
		Element element = new Element();
		element.setName("my_column");
		GridColumn col = new GridColumn(element);
		assertEquals("{name:'my_column', index:'my_column', width:85, align:'right', search:true, editable:true, hidden:false}",col.toString());
	}
}
