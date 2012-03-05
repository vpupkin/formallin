package eu.blky.wdb;

import java.util.List;

import com.thoughtworks.xstream.XStream;

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
//		System.out.println(book);
		Wdb  book2 = new Wdb ("Book");
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		assertEquals( book2.getProperty("Author").getProperty("First name")._() , "Cervantes" );
		assertEquals( book .getProperty("Title")._(), "Don Quijote" );

		
		Wdb  rack = new Wdb ("Shelf");
//		System.out.println(rack);
		rack.setProperty("book", book);
//		System.out.println(rack);
		rack.setProperty("book", book2);
//		System.out.println(rack);
		rack.setProperty("Color", "red"); 
//		System.out.println(rack);
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		assertEquals( books.size(),2 );	 
		Wdb titles = rack.getProperty("Title");// here must be no titles!
//		System.out.println(titles);
		assertEquals( titles , null );	 
	}
	
	
	
	public void test2ndCategory() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");
		
		Category bookCat = new Category("book");
		Category proseCat = new Category(bookCat, "prose");
		
		Wdb  book = new Wdb ("Book");
		book.addCategory(proseCat);
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author); 
		Wdb  book2 = new Wdb ("Book");
		book2.addCategory(bookCat);
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		assertEquals( book2.getProperty("Author").getProperty("First name")._(), "Cervantes" );
		assertEquals( book .getProperty("Title")._(), "Don Quijote" );
		
		Wdb  rack = new Wdb ("Shelf"); 
		rack.setProperty("book", book); 
		rack.setProperty("book", book2); 
		rack.setProperty("Color", "red");  
		System.out.println(rack);
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		Wdb titles = books.getProperty("Title");// here must be two titles! 
		System.out.println(titles);
		assertEquals( titles.size(),2 );
		titles = books.getProperty("Title");// here must be two titles! 
		
		assertEquals( titles.size(),3 ); // TODO WTF!?
		 
	}
	
	
	
	
	public void test2ndCategoryXStream() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");
		
		Category bookCat = new Category("book");
		Category proseCat = new Category(bookCat, "prose");
		
		Wdb  book = new Wdb ("Book");
		book.addCategory(proseCat);
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author); 
		Wdb  book2 = new Wdb ("Book");
		book2.addCategory(bookCat);
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		Wdb propertyFN = book2.getProperty("Author").getProperty("First name");
		assertEquals( propertyFN._(), "Cervantes" );
		assertEquals( book .getProperty("Title")._(), "Don Quijote" );
		
		Wdb  rack = new Wdb ("Shelf"); 
		rack.setProperty("book", book); 
		rack.setProperty("book", book2); 
		rack.setProperty("Color", "red");
		XStream x=new XStream();
		System.out.println(x.toXML(rack));
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		Wdb titles = books.getProperty("Title");// here must be two titles! 
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println(x.toXML(titles));
		assertEquals( titles.size(),2 );
		 
	}
	
	
	

	public void testDDBO(){
	 	WDBOService ddboService = WDBOService.getInstance();
		
		Category category1 = ddboService.createCategory("Author");
		
		List  categories = ddboService.getCategories();
		assertEquals(""+categories, 1, categories.size());
		
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
				
		
		for (Wdb o :ddboService.getObjects()){
			ddboService.remove(o);
		}
		
		ddboService.flush(translator);		
		translator.setProperty("oneMoreProp", "value");
		ddboService.flush(translator);
		
		assertEquals(2, ddboService.getObjects().size());
		assertEquals(2, ddboService.getObjects("Author").size());
 
	}	
	/**
	 * 
	 */

	public void testCreateWdbo(){
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		System.out.println(translator); 
	 	
		Wdb propertiesTmp = translator.getProperties("First name");
		String expected = propertiesTmp._();//propertiesTmp.get(0);
		assertEquals(expected   , "Ivanov");
		//translator.changeProperty("First name", 0, "Petrov");
		translator.setProperty("First name",   "Petrov"); 
		System.out.println(translator);
		
		Wdb  book = new Wdb ("Book");//new Category ("Book"));
		book.addProperty("Title", "Don Quijote");
		book.addProperty("CDU", "821.134.2-31\"16");
		book.addProperty("CDU", "821.134.2-31\"17");
		
		assertEquals(book.getProperties("Title")._(), "Don Quijote");
		assertEquals(book.getProperties("CDU").size(), 2);
		assertEquals(book.getProperties("CDU")._(), "821.134.2-31\"16");
		assertEquals(book.getProperties("CDU").get(0)._(), "821.134.2-31\"17");
		
		//book.addProperty("Author", author);
		book.setProperty("Author", translator);
		
		System.out.println(book);
		
		Wdb translatorCopy = (Wdb) book.getProperties("Author");
		//translatorCopy.changeProperty("role", 0, "ilustrator");
		
		System.out.println(translatorCopy); 
 

	
	}

	public void testDDBO_100ms(){
	 	WDBOService ddboService = WDBOService.getInstance();
		
		Category category1 = ddboService.createCategory("Author");
		
		List  categories = ddboService.getCategories();
		assertEquals(""+categories, 1, categories.size());
		
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
				
		
		for (Wdb o :ddboService.getObjects()){
			ddboService.remove(o);
		}
		int oCounter = 0;
		for (long start=System.currentTimeMillis();System.currentTimeMillis()<(start+100);){
			translator.setProperty("storeId", "#"+oCounter );
			ddboService.flush(translator);
			oCounter ++;
			
		}
		
		assertEquals(oCounter , ddboService.getObjects().size());
		assertEquals(oCounter , ddboService.getObjects("Author").size());
 
	}

	public void testDDBO_asProps_100ms(){
	 	WDBOService ddboService = WDBOService.getInstance();
		
		for (Wdb o :ddboService.getObjects()){
			ddboService.remove(o);
		}
		Category category1 = ddboService.createCategory("Author");
		
		List  categories = ddboService.getCategories();
		assertEquals(""+categories, 1, categories.size());
		
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
		int oCounter = 0;
		for (long start=System.currentTimeMillis();System.currentTimeMillis()<(start+100);){
			translator.setProperty("storeId", "#"+oCounter );
			ddboService.flush(translator);
			oCounter ++; 
		}
		
		assertEquals(oCounter , ddboService.getObjects().size());
		assertEquals(oCounter , ddboService.getObjects("Author").size());
	
	}
}


 