/**
 * 
 */
package gform;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Administrador
 * 
 */
public class TableTest {

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
	 */
	@Test
	public void testParseTable() {

		Table table = new Table("servicios_traduccion");

		ArrayList<Column> columns = table.getColumns();
		for (Column column : columns) {
			System.out.println(column.getName());
			System.out.println(column.getSqlType());
			System.out.println(column.getSize());
			System.out.println(column.isForingKey());
			System.out.println(column.getpKeyTable());
			System.out.println(column.getpKeyName());
			System.out.println();
		}

	}

}
