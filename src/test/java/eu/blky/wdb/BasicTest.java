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
	
	public void test0() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		 
		
		System.out.println(author);
	}
	
	public void test1st() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		
		System.out.println(book);
	}
	
	public void test2nd() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		System.out.println(book);
		Wdb  book2 = new Wdb ("Book");
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		System.out.println(book2);
		
		Wdb  rack = new Wdb ("Shelf");
		System.out.println(rack);
		rack.setProperty("book", book);
		System.out.println(rack);
		rack.setProperty("book", book2);
		System.out.println(rack);
		rack.setProperty("Color", "red"); 
		System.out.println(rack);
	}
	
	
	
	public void test2ndAsserts() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		System.out.println(book);
		Wdb  book2 = new Wdb ("Book");
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		assertEquals( book2.getProperty("Author").getProperty("First name").toString(), "Cervantes" );
		assertEquals( book .getProperty("Title").toString(), "Don Quijote" );
		
		Wdb  rack = new Wdb ("Shelf");
//		System.out.println(rack);
		rack.setProperty("book", book);
//		System.out.println(rack);
		rack.setProperty("book", book2);
//		System.out.println(rack);
		rack.setProperty("Color", "red"); 
//		System.out.println(rack);
		
		assertEquals( rack .getProperty("Color").toString(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		Wdb titles = books.getProperty("Title");// here must be two titles!
//		System.out.println(titles);
		assertEquals( titles.size(),2 );
		 
	}
}


 