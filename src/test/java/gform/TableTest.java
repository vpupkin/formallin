/**
 * 
 */
package gform;

import java.util.ArrayList;

import junit.framework.TestCase;
 

/**
 * @author Administrador
 * 
 */
public class TableTest extends TestCase {
 

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
	 */ 
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
