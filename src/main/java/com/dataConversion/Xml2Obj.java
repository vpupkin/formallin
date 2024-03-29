package com.dataConversion;

import java.io.File;  
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Xml2Obj {
	public List<com.objects.Property> getAddressList(String xmlFilePath) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.objects");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		com.objects.Properties properties = null;
		try{
			properties = (com.objects.Properties)unmarshaller.unmarshal(new File(xmlFilePath));
		}catch(Exception e){
			InputStream classpathXMLReasource = this.getClass().getClassLoader().getResourceAsStream("com/dataConversion/nrhp.xml");
			properties = (com.objects.Properties)unmarshaller.unmarshal(classpathXMLReasource);
		}
		return properties.getProperty();
	}
}
