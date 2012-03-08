package eu.blky.wdb;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import junit.framework.AssertionFailedError;
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
	 	WDBOService ddboService = WDBOService.getInstance();
		for (Wdb o :ddboService.getObjects()){
			ddboService.remove(o);
		}
		for (Wdb o :ddboService.getCategories() ){
			ddboService.removeCategory(o);
		}	
	} 
	
	public void test0() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel"); 
		assertEquals(author.getProperty("Second name")._(), "Miguel");
	}
	
	public void test1st() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");
		assertEquals(author.getProperty("First name")._(), "Cervantes");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		assertEquals(book.getProperty("Author").getProperty("First name")._(), "Cervantes");
		
	}
	
	public void test2nd() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		 
		Wdb  book2 = new Wdb ("Book");
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		 
		
		Wdb  rack = new Wdb ("Shelf");
		 
		assertEquals(rack.getProperty("book") , null);
		rack.setProperty("book", book);
		assertEquals(rack.getProperty("book") , book);
		 
		rack.setProperty("book", book2);
		assertEquals(rack.getProperty("book") , book2);
		 
		rack.setProperty("Color", "red"); 
		 
		assertEquals(rack.getProperty("Color")._() , "red");
		try{
			assertEquals(rack.getProperty("Color") , "red");
			fail("Have to be not equals with String::"+rack.getProperty("Color"));
		}catch(AssertionFailedError e){
			// ok!
		}
		
		
		
	}

	
	
	public void test2ndAsserts() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
//		
		Wdb  book2 = new Wdb ("Book");
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		assertEquals( book2.getProperty("Author").getProperty("First name")._() , "Cervantes" );
		assertEquals( book .getProperty("Title")._(), "Don Quijote" );

		
		Wdb  rack = new Wdb ("Shelf");
//		
		rack.setProperty("book", book);
//		
		rack.setProperty("book", book2);
//		
		rack.setProperty("Color", "red"); 
//		
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		assertEquals( books.size(),2 );	 
		Wdb titles = rack.getProperty("Title");// here must be no titles!
//		
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
//		
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		assertEquals( books.size(),2 );
		Wdb titles = books.getProperty("Title");// here must be two titles! 
 		assertEquals( titles.size(),2 );
		
		titles = books.getProperty("Title");// here must be two titles! 
		
		assertEquals( titles.size(),2 ); // TODO WTF!?
		 
	}
	
	
	
	public void test2ndCategoryFlush() {
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
//		
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		Wdb titles = books.getProperty("Title");// here must be two titles! 
//		
		assertEquals( titles.size(),2 );
		titles = books.getProperty("Title");// here must be two titles! 
		
		assertEquals( titles.size(),2 ); // TODO WTF!? 
		WDBOService ddboService = WDBOService.getInstance();
		
		ddboService .flush(rack);
		assertEquals(7 , ddboService.getCategories().size());
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
		String xmlTmp = x.toXML(rack);
		// 
		assertTrue( xmlTmp.indexOf("Cervantes")>0  );
		assertTrue( xmlTmp.indexOf("Author")>0  );
		assertTrue( xmlTmp.indexOf("Kornelia")>0  );
		assertTrue( xmlTmp.indexOf("Don Quijote")>0  );
		
		assertEquals( rack .getProperty("Color")._(), "red" );
		Wdb books = rack .getProperty("book");// here should be two books! 
		Wdb titles = books.getProperty("Title");// here must be two titles! 
//		
//		
		assertEquals( titles.size(),2 );
		 
	}
	
	
	

	public void testDDBO(){
	 	WDBOService ddboService = WDBOService.getInstance();
		
		Category category1 = ddboService.createCategory("Author");
		
		List<Category> categories = ddboService.getCategories();
		assertEquals(""+categories, 1, categories.size());
		
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
				
		 
		ddboService.flush(translator);		
		translator.setProperty("oneMoreProp", "value");
		ddboService.flush(translator);
		
		assertEquals(6, ddboService.getObjects().size());
		assertEquals(2, ddboService.getObjects("Author").size());
		assertEquals(6, ddboService.getObjects().size());
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
		 
	 	
		Wdb propertiesTmp = translator.getProperties("First name");
		String expected = propertiesTmp._();//propertiesTmp.get(0);
		assertEquals(expected   , "Ivanov");
		//translator.changeProperty("First name", 0, "Petrov");
		translator.setProperty("First name",   "Petrov"); 
		
		
		Wdb  book = new Wdb ("Book");//new Category ("Book"));
		book.addProperty("Title", "Don Quijote");
		book.addProperty("CDU", "821.134.2-31\"16");
		book.addProperty("CDU", "821.134.2-31\"17");
		
		assertEquals(book.getProperties("Title")._(), "Don Quijote");
		assertEquals(book.getProperties("CDU").size(), 2);
		assertEquals(book.getProperties("CDU")._(), "821.134.2-31\"17");
		assertEquals(book.getProperties("CDU").get(0)._(), "821.134.2-31\"16");// this property is versioned and shifted into prev. position
		
		//book.addProperty("Author", author);
		book.setProperty("Author", translator);
		
//		System.out.println(book);
		
		Wdb translatorCopy = (Wdb) book.getProperties("Author");
		//translatorCopy.changeProperty("role", 0, "ilustrator");
		
//		System.out.println(translatorCopy); 
 

	
	}

	public void testDDBO_XXXms(){
	 	WDBOService ddboService = WDBOService.getInstance();
		
		Category category1 = ddboService.createCategory("Author");
		
		List  categories = ddboService.getCategories();
		assertEquals(""+categories, 1, categories.size());
		
		Wdb  translator = new Wdb (("Sergei Ivanoff"), category1);// new "Sergei Ivanoff":Author //new Wdb (new Category2("Author"));
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
				
		
		for (Wdb o :ddboService.getObjects()){
			ddboService.remove(o);
		}
		int oCounter = 0;
		long start=System.currentTimeMillis();
		long ttlTmp =(start+100) +start%200;
		// here will be stored modified version of the Obj -> all prev. versions have to be shifted. 
		for (;System.currentTimeMillis()<ttlTmp ;){
			translator.setProperty("storeId", "#"+oCounter );
			ddboService.flush(translator);
			oCounter ++; 
		}
		
		assertEquals(oCounter , ddboService.getObjects("Author").size());
		LinkedList<Wdb> objects = ddboService.getObjects();
		int y = oCounter*2+3;//x*2+3;
		assertEquals(oCounter+" !=!="+objects, y , objects.size());
		assertEquals(oCounter , ddboService.getObjects("Author").size());

		assertEquals(y , ddboService.getObjects().size());

 
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
		long start=System.currentTimeMillis();
		long ttlTmp =(start+100) +start%111;
		// here will be stored modified version of the Obj -> all prev. versions have to be shifted. 
		for (;System.currentTimeMillis()<ttlTmp ;){
			translator.setProperty("storeId", "#"+oCounter );
			ddboService.flush(translator);
			oCounter ++; 
		} 
		int y = oCounter*2+3;//x*2+3;
		assertEquals(oCounter , ddboService.getObjects("Author").size()); 
		LinkedList<Wdb> objects = ddboService.getObjects();
		assertEquals(""+objects, y , objects.size());
		
		long searchStart = System.currentTimeMillis();
		int searchCount=22;
		for (int i=0;i<searchCount;i++){
			assertEquals(oCounter , ddboService.getObjects("Author").size());
		}
		long searchEnd = System.currentTimeMillis();
		System.out.println( "for #"+y+":::"+ (1000* searchCount)/(searchEnd -searchStart) +" sps !! "+((searchEnd -searchStart)/searchCount)+" ms-per-search ");
				 

	}
}


 