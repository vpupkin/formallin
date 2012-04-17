package eu.blky.wdb;

import gform.GForm;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

import cc.co.llabor.cache.Manager;

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

	private static Logger log = Logger.getLogger(BasicTest.class.getName());


	protected void setUp() throws Exception {
		super.setUp();


	}

	protected void tearDown() throws Exception {
		super.tearDown();
	 	WDBOService ddboService = WDBOService.getInstance();
	 	long startTime = System.currentTimeMillis();
	 	// kill all objects
	 	LinkedList<Wdb> objects = ddboService.getObjects();
		for (Wdb o :objects){
			ddboService.remove(o);
		}
		// kill caches
		//SearchCache\
		WDBOService.getInstance().resetSearchCache();
		
		long execTime = System.currentTimeMillis()-startTime ;
		collectStatistics("deleteAll", objects.size(), execTime ); 
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
	
	public void testProps() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Miguel");
		author.addProperty("Second name", "Cervantes");
		assertEquals(author.getProperty("First name")._(), "Miguel");

		Wdb  book = new Wdb ("Book");
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author);
		assertEquals(book.getProperty("Author").getProperty("First name")._(), "Miguel");
		Wdb secondAuthor = new Wdb ("Author");
		author.setProperty("First name", "Vasja");
		author.setProperty("Second name", "Pupkin");
		book.setProperty("Author", secondAuthor); 
		WDBOService ddboService = WDBOService.getInstance(); 
		ddboService.flush(book);
		assertEquals(book.getProperty("Author").getProperty("First name")._(), "Vasja");
		assertEquals(book.getProperty("Author").getProperty("First name")._(), "Vasja");
		assertEquals(book.getProperty("Author").getProperty("First name").size(),2);
		assertEquals(book.getProperty("Author").size( ),2);
		assertEquals(book.getProperty("Author").get(1 ),author);
		 
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
		
		assertEquals( titles.size(),2 ); //  
		WDBOService ddboService = WDBOService.getInstance();
		
		ddboService .flush(rack);
		assertEquals(7 , ddboService.getCategories().size());//[Shelf, First name, Author, Second name, Book, Color, Title]
	}
	
 
	
	public void test2ndCategoryXStream() {
		Wdb author = new Wdb ("Author");  // Object author = new Author();
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");
		
		Category bookCat = new Category("book");
		Category proseCat = new Category(bookCat, "prose");
		
		Wdb  book = new Wdb ("Book");// Object book = new Book();
		book.addCategory(proseCat);
		book.setProperty("Title", "Don Quijote");
		book.setProperty("Author", author); 
		Wdb  book2 = new Wdb ("Book");// Object book2 = new Book();
		book2.addCategory(bookCat);
		book2.setProperty("Title", "Kornelia");
		book2.setProperty("Author", author);
		Wdb propertyFN = book2.getProperty("Author").getProperty("First name");
		assertEquals( propertyFN._(), "Cervantes" );
		assertEquals( book .getProperty("Title")._(), "Don Quijote" );
		
		Wdb  rack = new Wdb ("Shelf"); // Object rack = new Shelf();
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
		
		
		
		List<Category> categories = ddboService.getCategories();
		assertEquals(""+categories, 0, categories.size());// by default the category-list is empty
		
		Wdb  translator = new Wdb (("Author"));//new Wdb (new Category2("Author"));
		Category category1 = ddboService.createCategory("Author", translator);
		translator.addProperty("First name", "Ivanov");
		translator.addProperty("Second name", "Sergey");
		translator.addProperty("role", "translator"); 
		translator.addCategory(category1) ; 
				
		 
		ddboService.flush(translator);		
		translator.setProperty("oneMoreProp", "value");
		ddboService.flush(translator);
		
		assertEquals(2, ddboService.getObjects("Author").size());
		assertEquals(2, ddboService.getObjects("Author").size());
		//TODO assertEquals(6, ddboService.getObjects().size());
		//TODO assertEquals(6, ddboService.getObjects().size());
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
		assertEquals(book.getProperties("CDU").get(1)._(), "821.134.2-31\"16");// this property is versioned and shifted into prev. position
		
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
		
		//Wdb  translator = new Wdb (("Sergei Ivanoff"), category1);// new "Sergei Ivanoff":Author //new Wdb (new Category2("Author"));
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
		int y = oCounter*2+5 -1;// !+!+!+5???? !!! x*2+3; -nullObject
		assertEquals(oCounter+" !=!="+objects, y , objects.size());
		assertEquals(oCounter , ddboService.getObjects("Author").size());

		assertEquals(y , ddboService.getObjects().size());
		
		collectStatistics("updateProp", oCounter, ttlTmp-System.currentTimeMillis());
 
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
		int y = oCounter*2+9-1;//TODO +  ??? x*2+3; - nullObject
		assertEquals(oCounter , ddboService.getObjects("Author").size()); 
		LinkedList<Wdb> objects = ddboService.getObjects();
		assertEquals(""+objects, y , objects.size());
		
		long searchStart = System.currentTimeMillis();
		int searchCount=22;
		for (int i=0;i<searchCount;i++){
			assertEquals(oCounter , ddboService.getObjects("Author").size());
		}
		long searchEnd = System.currentTimeMillis();
		long msPerSearch = ((searchEnd -searchStart)/searchCount);
		System.out.println( "for #"+y+":::"+ (1000* searchCount)/(searchEnd -searchStart) +" sps !! "+msPerSearch+" ms-per-search ");
 
		collectStatistics("search", searchCount, msPerSearch);
	}
	
	
	
	private String getTitle(){
		String in []=new String[]{"From", "In"  , "To"};
		String city []=new String[]{"New York", "Moscow", "Berlin", "Tokio"};
		String at []=new String[]{"At", "In" , "The"};
		String year []=new String[]{"1810", "1984", "1917", "2000", "2050"};
		String me []=new String[]{"I", "we", "they", "you", "she", "Bob", "Alise", "Rabbit"};
		String found []=new String[]{"loose", "found", "throw", "escape"};
		String her []=new String[]{"his", "her" , "its" , "they"};
		String flowers []=new String[]{"flowers", "life" , "" , "nature"};
		String punkt []=new String[]{".", "..." , "!" , "?"};
		String retval =
			in[(int) ((Math.random()*10000)%in.length)]+" "+  
			city[(int) ((Math.random()*10000)%city.length)]+" "+  
			at[(int) ((Math.random()*10000)%at.length)]+" "+  
			year[(int) ((Math.random()*10000)%year.length)]+" "+  
			me[(int) ((Math.random()*10000)%me.length)]+" "+  
			found[(int) ((Math.random()*10000)%found.length)]+" "+  
			her[(int) ((Math.random()*10000)%her.length)]+" "+  
			punkt[(int) ((Math.random()*10000)%punkt.length)]+" "+  
			""
			;
		return retval ;
	}
	private String getFSName(){
		String fn []=new String[]{"Ivan","Elena","Tom","Jim","Anton","Lev" , "Taras" , "Dalaj" };
		String sn []=new String[]{"Pushkin", "Oruel", "Twen", "Sharapov", "Nikamoto", "Shevchenko", "Lama" , "Down" };
		String retval =
			fn[(int) ((Math.random()*10000)%fn.length)]+" "+  
			sn[(int) ((Math.random()*10000)%sn.length)]+" "+    
			""
			;
		return retval ;
	}	
	public void testDDBO_BibCollection(){  
		
		WDBOService ddboService = WDBOService.getInstance();

		int toCreate = (int) (101 + System.nanoTime()%100);
		long start = System.currentTimeMillis();
		for (int i = 0; i < toCreate; i++) {
			Category categoryA = ddboService.createCategory("Author");
			Wdb aTmp = new Wdb(getFSName(), categoryA);
			Category categoryB = ddboService.createCategory("Book");
			Wdb bTmp = new Wdb(getTitle(), categoryB);

			bTmp.setProperty("author", aTmp);
			bTmp.setProperty("published", "" + new Date());

			ddboService.flush(bTmp); 
			//System.out.println(bTmp);
		} 
		long l = System.currentTimeMillis() - start;
		System.out.println("#"+toCreate+"items created in "+l+" ms");
		assertEquals( ddboService.getObjects("Author").size(), toCreate);
		assertEquals( ddboService.getObjects("Book").size(), toCreate);
		
		collectStatistics("create", toCreate, l); 
	}

	private Wdb getAddress(){
		WDBOService ddboService = WDBOService.getInstance(); 
		Category categoryAddrress = ddboService.createCategory("Addrress");
		Category categoryC = ddboService.createCategory("Country");
		Category categoryD = new Category("House");
		Category categoryE = ddboService.createCategory("Street");
		Category categoryF = new Category("Floor");
		Category categoryG = ddboService.createCategory("Town");
		Category categoryP = new Category("Planet");

		String land []=new String[]{"Russia", "USA"  , "Germany", "Spain", "Vietnam" , "Canada", "Japan", "China", "Germany", "France"};
		String city []=new String[]{"New York", "Moscow", "Berlin", "Tokio"};
		String planet []=new String[]{"Moon", "Mars" , "Earth" , "Venera"};
		String street []=new String[]{"a", "b" , "c" , "d"};
		Wdb retval = new Wdb ();
		retval .addCategory(categoryAddrress);
		retval .setProperty("land", new Wdb(land[(int) ((Math.random()*10000)%land.length)] , categoryC));
		retval .setProperty("city", new Wdb(city[(int) ((Math.random()*10000)%city.length)] , categoryG));
		retval .setProperty("house", new Wdb( ""+(int) ((Math.random()*10000)%land.length)  , categoryD));
		retval .setProperty("street", new Wdb(street[(int) ((Math.random()*10000)%street.length)] , categoryE));
		retval .setProperty("floor", new Wdb(""+(int) ((Math.random()*10000)%1111) , categoryF));
		retval .setProperty("planet", new Wdb(planet[(int) ((Math.random()*10000)%planet.length)] , categoryP));
 		return retval ;
	}
	
	
	public void testGalaLib(){  
		
		WDBOService ddboService = WDBOService.getInstance(); 
		int toCreate = (int) (200 + System.nanoTime()%20);
		long start = System.currentTimeMillis();
		Category categoryA = ddboService.createCategory("Author");
		Category categoryB = ddboService.createCategory("Book");
		Category libCat = new Category("Library");
		// assumes we habe 5 librarie
		Wdb libA  = new Wdb ("LibraryAthen", libCat );
		Wdb libB  = new Wdb ("LibraryBerlin", libCat);
		Wdb libC  = new Wdb ("LibraryCologne", libCat );
		
		
		for (int i = 0; i < toCreate; i++) {
			// author 
			Wdb aTmp = new Wdb(getFSName(), categoryA);
			for (int j=0;j< System.currentTimeMillis()%10;j++){
				Wdb aAdress = getAddress();
				aTmp.setProperty("adress", aAdress );
			}
			Wdb birthAdress = getAddress();
			aTmp.setProperty("birthAdress", birthAdress );
			Wdb bTmp = new Wdb(getTitle(), categoryB);

			bTmp.setProperty("author", aTmp);
			bTmp.setProperty("published", "" + new Date());
			// register book
			ddboService.flush(bTmp); 
			
			// put the book into store
			if (i%2==0) libA.setProperty("book", bTmp);
			if (i%3==0) libB.setProperty("book", bTmp);
			if (i%5==0) libC.setProperty("book", bTmp);
			//System.out.println(bTmp);
		} 
		// persist libriries....
		ddboService.flush(libA); 
		ddboService.flush(libB); 
		ddboService.flush(libC); 
		
		long l = System.currentTimeMillis() - start;
		System.out.println("#"+toCreate+"items created in "+l+" ms");
		assertEquals( ddboService.getObjects("Author").size(), toCreate);
		assertEquals( ddboService.getObjects("Book").size(), toCreate);
		
		collectStatistics("createGala", toCreate, l); 
	}
	

	private void collectStatistics(String statName, int toCreate, long l) {
		Cache c = Manager.getCache("WBDstat");
		Properties cachedStat = (Properties) c.get(statName+".properties");
		cachedStat = cachedStat == null?new Properties():cachedStat ;
		String  key = statName +"_"+toCreate;
		String value = ""+l;
		Object oldVal = cachedStat .put(key , value);
		String newKey = key;
		while(oldVal!=null){
			newKey = "~"+newKey;
			oldVal = cachedStat .put(newKey  , oldVal);
		}		
		c.put(statName+".properties", cachedStat);

	}
}


 