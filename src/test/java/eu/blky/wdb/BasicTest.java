package eu.blky.wdb;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.server.UID;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

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
	private int aCount;
	private int bCount;
	private int cCount;


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
		
		// kill categories
		WDBOService.getInstance().resetCategories();
		
		// kill caches
		//SearchCache\
		WDBOService.getInstance().resetSearchCache();
		
		long execTime = System.currentTimeMillis()-startTime ;
		collectStatistics("deleteAll", objects.size(), execTime ); 
		for (Wdb o :ddboService.getCategories() ){
			ddboService.removeCategory(o);
		}	
		
		
		// clean Indexes
		creanDirToIndex();
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
		assertEquals(book.getProperty("Author").get(1 )._(),author._());
		 
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

	

	public void testCategoryAddRemove() {
		Wdb author = new Wdb ("Author");
		author.setProperty("First name", "Cervantes");
		author.addProperty("Second name", "Miguel");
		
		Category bookCat = new Category("book");
		Category proseCat = new Category(bookCat, "prose");
		
		Wdb  book = new Wdb ("Book");
		book.addCategory(proseCat); 
		
		WDBOService ddboService = WDBOService.getInstance();
		// initial add
		ddboService .flush(book);
		assertEquals(  6, ddboService.getObjects().size());
		assertEquals(  ddboService.getObjects("Book").size(),1);
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories(). size(),2);
		// del category
		book  =  ddboService.getObjects("Book").get(  0 );
		book  .delCategory(proseCat);
		// before persistence the state is the same  
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories(). size(),2);
		
		
		// persist
		ddboService .flush(book);		
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories(). size(),1);
		assertEquals( "{{{"+ ddboService.getObjects("Book").get(  0 ) .getCategories()._()+"}}}",  ddboService.getObjects("Book").get(  0 ) .getCategories() ._() ,"Book");
		
	}
	

	
	public void testCategoryAddRemoveAddRemove() {
		testCategoryAddRemove();
		WDBOService ddboService = WDBOService.getInstance();
		Wdb book = ddboService.getObjects("Book").get(  0 );
		assertEquals( ""+ book  .getCategories(), book  .getCategories() ._() ,"Book");
		
		Category bookCat = new Category("Book");
		Category proseCat = new Category(bookCat, "prose");
		Category scifiCat = new Category(bookCat, "scifi");
		
		// no persistence
		book.addCategory(proseCat);
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories() ._() ,"Book");
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories().size() ,1);
		Wdb  categories = book  .getCategories();
		assertTrue( ""+ categories, categories.contains(proseCat) );
		assertTrue( ""+ categories, categories.contains(bookCat) );
		assertFalse ( ""+ categories, categories.contains(scifiCat) );
		
		book.addCategory(scifiCat);
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories() ._() ,"Book");
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories().size() ,1);
		//assertEquals( ""+ book  .getCategories(),"[Book, prose, scifi]", ""+book  .getCategories() );
		assertTrue( ""+ categories, categories.contains(proseCat) );
		assertTrue( ""+ categories, categories.contains(bookCat) );
		assertTrue(  ""+ categories, categories.contains(scifiCat) );
		
		
		// persist all
		ddboService.flush(book);
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories() ._() ,"Book");
		assertEquals( ""+ ddboService.getObjects("Book").get(  0 ) .getCategories(), ddboService.getObjects("Book").get(  0 ) .getCategories().size() ,3);
		//assertEquals( ""+ book  .getCategories(),"[Book, prose, scifi]", ""+book  .getCategories() );
		assertTrue( ""+ categories, categories.contains(proseCat) );
		assertTrue( ""+ categories, categories.contains(bookCat) );
		assertTrue(  ""+ categories, categories.contains(scifiCat) );
		
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
		UID expected = translator.getUID();
		assertEquals(1, ddboService.getObjects("Author").size());
		translator.setProperty("oneMoreProp", "value");
		ddboService.flush(translator); 
		UID actual = translator.getUID();
		assertEquals(expected, actual);
		assertEquals(1, ddboService.getObjects("Author").size());
		// TODO TO BE OR NOT TO BE THAT ISSSSSSSSSSSSSSssssssssssssssssssssssssssssssssssssssss.................................... _THE Q!!!!!!!!!_!_!_!__!_______!_!??!?!?!?
		assertEquals(1, ddboService.getObjects("Author").size());
		//TODO assertEquals(6, ddboService.getObjects().size());
		//TODO assertEquals(6, ddboService.getObjects().size());
		assertEquals(1, ddboService.getObjects("Author").size()); 
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
		
		assertEquals(oCounter /oCounter /* the object should be overwrite */ , ddboService.getObjects("Author").size());
		LinkedList<Wdb> objects = ddboService.getObjects();
		int y = oCounter+5;// now +6
		assertEquals(oCounter+" !=!="+objects+"["+y+"]", y , objects.size());
		assertEquals(oCounter /oCounter /* the object should be overwrite */ , ddboService.getObjects("Author").size());

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
		int y = oCounter+10;//TODO +  new Forme1
		assertEquals(oCounter /* ! ! */ /oCounter, ddboService.getObjects("Author").size()); 
		LinkedList<Wdb> objects = ddboService.getObjects();
		assertEquals("::"+objects+":["+objects.size()+"]"+y+"::::"+oCounter, y , objects.size());
		
		long searchStart = System.currentTimeMillis();
		int searchCount=22;
		for (int i=0;i<searchCount;i++){
			assertEquals(oCounter /* ! ! */ /oCounter , ddboService.getObjects("Author").size());
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

		int toCreate = (int) (11 + System.nanoTime()%100);
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
		assertEquals( toCreate/toCreate /* the object should be overwrite */, ddboService.getObjects("Author").size() );
		assertEquals( ddboService.getObjects("Book").size(), toCreate/toCreate /* the object should be overwrite */);
		
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
		
		 
		int toCreate = (int) (20 + System.nanoTime()%20);
		long start = System.currentTimeMillis();
		WDBOService ddboService = createSomeObjects(toCreate); 
		
		long l = System.currentTimeMillis() - start;
		System.out.println("#"+toCreate+"items created in "+l+" ms");
		assertEquals( ddboService.getObjects("Author").size(), toCreate/toCreate /* the object should be overwrite */);
		assertEquals( ddboService.getObjects("Book").size(), toCreate/toCreate /* the object should be overwrite */);
		
		collectStatistics("createGala", toCreate, l); 
	}

	public WDBOService createSomeObjects(int toCreate) {
		WDBOService ddboService = WDBOService.getInstance(); 
		Category categoryA = ddboService.createCategory("Author");
		Category categoryB = ddboService.createCategory("Book");
		Category libCat = new Category("Library");
		
		// assumes we have 5 libraries
		Wdb libA  = new Wdb ("LibraryAthen", libCat );
		cCount++;
		Wdb libB  = new Wdb ("LibraryBerlin", libCat);
		cCount++;
		Wdb libC  = new Wdb ("LibraryCologne", libCat );
		cCount++;
		
		
		for (int i = 0; i < toCreate; i++) {
			// author 
			Wdb aTmp = new Wdb(getFSName(), categoryA);
			aCount++;
			for (int j=0;j< System.currentTimeMillis()%10;j++){
				Wdb aAdress = getAddress(); 
				aTmp.setProperty("adress", aAdress );
			}
			Wdb birthAdress = getAddress();
			aTmp.setProperty("birthAdress", birthAdress );
			
			// CreateThe Book
			Wdb bTmp = new Wdb(getTitle(), categoryB);
			bCount++;
			 

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
		return ddboService;
	}
	
	
	public void testSearch() throws CorruptIndexException, IOException, ParseException, InterruptedException, URISyntaxException{
		// creating some objects...
		try{
			 createSomeObjects(5);
		}catch(Throwable e){}
		
		// Creating IndexWriter object and specifying the path where Indexed
		//files are to be stored.
		MaxFieldLength mfl = MaxFieldLength .UNLIMITED ;

		StandardAnalyzer analyzerTmp = new StandardAnalyzer(Version.LUCENE_30);

		Directory dirTmp = new SimpleFSDirectory(getDirToIndex() );
		IndexWriter indexWriter = new IndexWriter(dirTmp , analyzerTmp, mfl  );
		            
		WDBOService ddboService = WDBOService.getInstance();      
		
		// Reading each line present in the file.
		for (Wdb o:ddboService.getObjects() )
		{
			
			// store the Obj into Hadoop
			FSDataOutputStream outTmp = getHadoopOut(""+o.getId());
			Properties pTmp = o.toProperties();
			String comments = ""+System.currentTimeMillis();
			pTmp.store(outTmp, comments );
			outTmp.close();
			
			// Getting each field present in an Obj 
			                
			// For each row, creating a document and adding data to the document with the associated fields.
			org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
			String scUri = "";
			for (String key :o.getPropertyNames()){
				Wdb propVal = o.getProperty(key);
				try{
					//document.add(new Field(key, propVal._(),Field.Store.YES,Field.Index.ANALYZED));
					document.add(new Field("propertyName", key,Field.Store.NO ,Field.Index.NOT_ANALYZED_NO_NORMS));
					//scUri+=key;
					scUri+=", ";
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
			for (Wdb cat :o.getCategoriesAsList()  ){
				String valTmp = cat._();
				try{
					document.add(new Field("category", valTmp,Field.Store.YES,Field.Index.ANALYZED));
					scUri+=valTmp;
					scUri+=", ";
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
			// store full path as "cs-uri"
			document.add(new Field ("cs-uri",scUri,Field.Store.YES,Field.Index.NOT_ANALYZED ));
			document.add(new Field ("uid",""+o.getId(),Field.Store.YES,Field.Index.NOT_ANALYZED));
			
			                 
			// Adding document to the index file.
			indexWriter.addDocument(document);
		}        
		indexWriter.optimize();
		indexWriter.close();
		
		search_1("  Book  AND Country "); //search_1(" Author   "); search_1(" Book  or  Author");search_1(" B*") search_1(" C*") search_1("Ho*")
		assertEquals(bCount,search_1("Book", true)); //search_1(" category (Book )") assertEquals(1,assertEquals(1,
		assertEquals(aCount, search_1("Author", true)); //search_1(" category (Book )") assertEquals(1,
		assertEquals(cCount, search_1("Library", true)); //search_1(" category (Book )") assertEquals(1,assertEquals(1,
		search_1("category(Author)", true); //search_1(" category (Book )") assertEquals(1,assertEquals(1,
		search_1(" category (Planet )");
		
	}
	
	/**
	 * // Creating FSDataInputStream object, for reading the data from "Test.txt" file residing on HDFS.
FSDataInputStream filereader = dfs.open(new Path(dfs.getWorkingDirectory()+ File_DIR));

	 * @author vipup
	 * @return
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws URISyntaxException 
	 */
	FSDataOutputStream getHadoopOut(String idPar) throws IOException, InterruptedException, URISyntaxException{
		// Path where the index files will be stored.
		String Index_DIR="/IndexFiles/";
		// Path where the data file is stored.
		String File_DIR="/DataFile/"+idPar;//o.getId() 
		// Creating FileSystem object, to be able to work with HDFS
		File_DIR = File_DIR.replace(":", "_.._");
		Configuration config = new Configuration();
		config.set("fs.default.name","hdfs://127.0.0.1:9000/");
		URI uriTMp = new URI("hdfs://127.0.0.1:9000/");
		FileSystem dfs = FileSystem.get(uriTMp ,config, "tom");
		Path pathToOut = new Path(dfs.getWorkingDirectory()+ File_DIR);
		FSDataOutputStream dfsOut = dfs.create( pathToOut );
		return dfsOut;
		
	}
	
	IndexWriter getIndexWriter() throws IOException{

		// Creating a RAMDirectory (memory) object, to be able to create index in memory.
		RAMDirectory rdir = new RAMDirectory(); 
		// Creating IndexWriter object for the Ram Directory
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_30);
		MaxFieldLength mfl = MaxFieldLength .UNLIMITED ;
		IndexWriter indexWriter = new IndexWriter (rdir, standardAnalyzer, mfl);		
		
		return indexWriter;
	}

	private File getDirToIndex() {
		String pathTmp ="./.indexfile";
		File dirToIndex = new File(pathTmp);
		dirToIndex .deleteOnExit();
		return dirToIndex;
	}
	

	private void creanDirToIndex() {
		try{
			for (String indexTmp:getDirToIndex() .list()){
				 new File(getDirToIndex(),indexTmp).delete();
			}
		}catch(Exception e){}
	}

	int search_1(String queryTmp) throws IOException, ParseException{
		return search_1(queryTmp, false);
	}
	private int search_1(String queryTmp, boolean distinct) throws IOException, ParseException {
		// ##2 - search
		int retval = -1;
		{
			String pathTmp ="./.indexfile";
			Directory dirTmp = new SimpleFSDirectory(new File(pathTmp));
			
			// Creating Searcher object and specifying the path where Indexed files are stored.
			Searcher searcher = new IndexSearcher(dirTmp);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

			// Printing the total number of documents or entries present in the index file.
			System.out.println("Total Documents = "+searcher.maxDoc()) ;
			            
			// Creating the QueryParser object and specifying the field name on 
			//which search has to be done.
			QueryParser parser = //new QueryParser(Version.LUCENE_30, "cs-uri", analyzer);
				new QueryParser(Version.LUCENE_30, "category", analyzer);
			            
			// Creating the Query object and specifying the text for which search has to be done.
			Query query = parser.parse(queryTmp);
			            
			// Below line performs the search on the index file and
			WdbCollector hits = new WdbCollector(distinct);
			searcher.search(query, hits);
			            
			// Printing the number of documents or entries that match the search query.
			System.out.println("Number of matching documents = "+ hits.length());

			retval = hits.length();
			// Printing documents (or rows of file) that matched the search criteria.
			WDBOService ddboService = WDBOService.getInstance();   
			for (int i = 0; i < retval  ; i++)
			{
			    Document doc = hits.doc(i); 
			    Object uidTmp = doc.get("uid");
				String _ = null;
				try{
					_ = ((Wdb)ddboService .getByUID(uidTmp))._();
				}catch(Exception e){}
				System.out.println(uidTmp + " = "+ doc.get("cs-uri")+ " "  +"["+ _+"]");
			}			
		}
		
		return retval;
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


 