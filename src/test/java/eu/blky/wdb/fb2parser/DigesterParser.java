package eu.blky.wdb.fb2parser;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import eu.blky.wdb.Wdb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**  
	 * Parses the contents of fb2-book XML file.  The name of the file to
	 * parse must be specified as the first command line argument. 
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  10.05.2012::15:07:36<br> 
 */
public class DigesterParser {

	public DigesterParser(  Properties digPairs, InputStream in) throws IOException, SAXException
	{
	        // instantiate Digester and disable XML validation
	        Digester digester = new Digester();
	        digester.setValidating(false);
	        String attributeName = "xxx";
	        Class clazz = WdbCreator.class;
			
	        // instantiate AddressBookParser class
	        digester.addFactoryCreate("FictionBook", clazz, attributeName+1); 
	        digester.addFactoryCreate("FictionBook/description", clazz, attributeName+2); 
	        digester.addFactoryCreate("FictionBook/description/title-info", clazz, attributeName+3  ); 
			digester.addFactoryCreate("FictionBook/description/title-info/genre", clazz,  attributeName+4 );

	        digester.addSetNext("FictionBook/description",              			 		"setProperty" );
	        digester.addSetNext("FictionBook/description/title-info",               		"setProperty" );
	        digester.addCallMethod("FictionBook/description/title-info/genre",       "setProperty", 0);
	        //digester.addSetNext("FictionBook/description/title-info/genre",               	"setProperty" );
	        

	        // now that rules and actions are configured, start the parsing process
	        Object abp = digester.parse(in);
	        System.out.println(abp); 
	}	
	public DigesterParser(  InputStream in) throws IOException, SAXException
	{
	        // instantiate Digester and disable XML validation
	        Digester digester = new Digester();
	        digester.setValidating(false);

	        // instantiate AddressBookParser class
	        digester.addObjectCreate("address-book", Fb2Book.class );
	        // instantiate Contact class
	        digester.addObjectCreate("address-book/contact", Fb2Book.class );

	        // set type property of Contact instance when 'type' attribute is found
	        digester.addSetProperties("address-book/contact",         "type", "type" );

	        // set different properties of Contact instance using specified methods
	        digester.addCallMethod("address-book/contact/name",       "setName", 0);
	        digester.addCallMethod("address-book/contact/address",    "setAddress", 0);
	        digester.addCallMethod("address-book/contact/city",       "setCity", 0);
	        digester.addCallMethod("address-book/contact/province",   "setProvince", 0);
	        digester.addCallMethod("address-book/contact/postalcode", "setPostalcode", 0);
	        digester.addCallMethod("address-book/contact/country",    "setCountry", 0);
	        digester.addCallMethod("address-book/contact/telephone",  "setTelephone", 0);

	        // call 'addContact' method when the next 'address-book/contact' pattern is seen
	        digester.addSetNext("address-book/contact",               "addContact" );

	        // now that rules and actions are configured, start the parsing process
	        Object abp = digester.parse(in);
	        System.out.println(abp);
 		
	}

}