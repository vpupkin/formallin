package eu.blky.wdb;

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  19.12.2011::14:22:57<br> 
 */
public class BasicTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	} 
	
	public void testIsTableExists() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		
		System.out.println(book);
	}
}


 