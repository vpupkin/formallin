package eu.blky.wdb.fb2parser;

import org.apache.commons.digester.Digester;
import org.xml.sax.Attributes;

import eu.blky.wdb.Wdb;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  10.05.2012::19:15:11<br> 
 */
public class WdbCreator implements org.apache.commons.digester.ObjectCreationFactory{

	private Digester digester;

	@Override
	public Object createObject(Attributes attributes) throws Exception {
		for (int i=0;i<attributes.getLength();i++){
			String qname = attributes.getQName(i);
			System.out.println(qname );
			String type = attributes.getType(i);
			System.out.println(type );
			String uri= attributes.getURI(i);
			System.out.println(uri);
			String val = attributes.getValue(i);
			System.out.println(val);
			String lname= attributes.getLocalName(i);
			System.out.println(lname);
		}
		System.out.println(attributes);
		 return new Wdb("DEFAUltRoooot");
	}

	@Override
	public Digester getDigester() { 
		return this.digester  ; 
	}

	@Override
	public void setDigester(Digester digester) {
		this.digester =  digester ;
	}

}


 