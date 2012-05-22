package com.dataConversion;

import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Obj2Xml {
	public void persistAddressList(com.objects.Properties prop, Writer writer) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance("com.objects");
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(prop, writer);
	}
}
