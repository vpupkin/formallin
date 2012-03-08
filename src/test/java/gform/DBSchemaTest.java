/**
 * 
 */
package gform;
 

import java.sql.SQLException;
import java.util.logging.Logger;

import junit.framework.TestCase;
 

/**
 * @author Administrador
 * 
 */
public class DBSchemaTest  extends TestCase {
	
	private static Logger log = Logger.getLogger(DBSchemaTest.class.getName());
	DBSchema db;
	Table testTable;
	
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
		log.info("setUp");
		db = new DBSchema("gescsidiomas");
		testTable = new Table("testTable");
	}

	/**
	 * @throws java.lang.Exception
	 */ 
	public void tearDown() throws Exception {
		log.info("tearDown");
		if(db.isTableExists(testTable.getName())){
			db.dropTable(testTable);
		}
	}

	
	/**
	 * Test method for {@link gform.DBSchema#createTable(java.lang.String)}.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */ 
	public void testCreateTable() throws ClassNotFoundException, SQLException {
		log.info("createTable");
		
		assertFalse(db.isTableExists(testTable.getName()));
		
		Column idColumn = new Column("id", SQLType.INT, "10");
		idColumn.setKey(true);
		idColumn.setAuto(true);
		idColumn.setForingKey(true);
		idColumn.setpKeyName("idCliente");
		idColumn.setpKeyTable("cliente");
		
		testTable.addColumn(idColumn);
		
		
		db.createTable(testTable);
		
		assertTrue(db.isTableExists("testTable"));

	}
}
