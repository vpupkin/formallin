package gform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Procesa páginas xml y recupera valores con sentencias XPath.
 * 
 * @author sergi
 */
public class XmlDoc {

	private Logger log = Logger.getLogger(XmlDoc.class.getName());
	
	private Document doc;

	private NamespaceContext namespaceContext;

	public XmlDoc(){}
	
	/**
	 * El constructor de la clase.
	 * Parsea la pagina en formato xml desde url y la guarda en un objeto doc.
	 * 
	 * @param url- Url de la página xml completo y CODIFICADO!!
	 * @exception ParserConfigurationException, SAXException, IOException
	 */
	public XmlDoc(URL url) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);		// never forget this!
		domFactory.setCoalescing(true);			// para convertir CDATA a texto
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse(url.openStream());
	}
	
	/**
	 * El constructor de la clase.
	 * Parsea la pagina en formato xml desde un fichero y la guarda en un objeto doc.
	 * 
	 * @param is - xml file insput stream
	 * @exception ParserConfigurationException, SAXException, IOException
	 */
	public XmlDoc(InputStream is) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		domFactory.setCoalescing(true);			// para convertir CDATA a texto
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse(is);
	}
	
	public void setNamespaceContext(NamespaceContext context){
		this.namespaceContext = context;
	}
	
	
	
	/**
	 * Devuelve valores de los campos xml segun la sentencia XPath.
	 * 
	 * @param xpathStr - sentencia XPath
	 * @exception XPathExpressionException
	 */
	public ArrayList<String> getValues(String xpathStr) throws XPathExpressionException {
		
		return getValues(doc, xpathStr);
	}

	/**
	 * @param xpathStr
	 * @return
	 * @throws XPathExpressionException 
	 */
	public NodeList getNodes(String xpathStr) throws XPathExpressionException {
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		if(namespaceContext!=null){
			xpath.setNamespaceContext(namespaceContext);
		}
		XPathExpression expr = xpath.compile(xpathStr);
		
		return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	}

	public ArrayList<String> getNodeValues(Node node, String xpathStr) throws XPathExpressionException {

		return getValues(node, xpathStr);
	}
	
	private ArrayList<String> getValues(Object doc, String xpathStr) throws XPathExpressionException {
		
		ArrayList<String> values = new ArrayList<String>();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		if(namespaceContext!=null){
			xpath.setNamespaceContext(namespaceContext);
		}
		XPathExpression expr = xpath.compile(xpathStr);

		//log.log(Level.INFO, "xpathStr=" + xpathStr);
		//log.log(Level.INFO, "expr=" + expr);

		NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		
		for (int i = 0; i < nodeList.getLength(); i++) {
		
			values.add(nodeList.item(i).getNodeValue());
		}
		return values;
	}
}
