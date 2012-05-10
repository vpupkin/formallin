package eu.blky.wdb.fb2parser;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  10.05.2012::15:18:03<br> 
 */
public class DigesterParserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDigesterParser() throws IOException, SAXException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("fb2/addrbook.xml");
		DigesterParser dp = new DigesterParser(in );
		
		System.out.println(dp);
	}

}


 