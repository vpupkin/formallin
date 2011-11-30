package gform;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Grid {
	
	private static Logger log = Logger.getLogger(Grid.class.getName());
	
	private String title;
	private String tableModel;
	private String url;
	private String query;
	private String procesaUrl;
	private String name; // este nombre se utiliza como nombre del fichero name_xml.jsp para consulta por ajax
	private String columns; // comma separated columns list
	private String gridComplete = "";
	

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTableModel(String tableModel) {
		this.tableModel = tableModel;
	}

	public String getTableModel() {
		return tableModel;
	}


	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getColumns() {
		return columns;
	}

	public static Grid parceGFormGrid(String xmlText) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		
		xmlText = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>".concat(xmlText);
		
		XmlDoc xml = new XmlDoc(new ByteArrayInputStream(xmlText.getBytes()));
		NodeList nodeList =  xml.getNodes("//grid");
		
		Grid grid = new Grid();
		
		Node node = nodeList.item(0);
		
		ArrayList<String> aux = xml.getNodeValues(node, "title/text()");
		if(!aux.isEmpty()){
			grid.setTitle(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "table_model/text()");
		if(!aux.isEmpty()){
			grid.setTableModel(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "url/text()");
		if(!aux.isEmpty()){
			grid.setUrl(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "query/text()");
		if(!aux.isEmpty()){
			grid.setQuery(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "procesa_url/text()");
		if(!aux.isEmpty()){
			grid.setProcesaUrl(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "name/text()");
		if(!aux.isEmpty()){
			grid.setName(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "columns/text()");
		if(!aux.isEmpty()){
			grid.setColumns(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "gridComplete/text()");
		if(!aux.isEmpty()){
			grid.setGridComplete(aux.get(0));
		}
		
		return grid;
			
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setProcesaUrl(String url) {
		procesaUrl = url;
	}

	public String getProcesaUrl() {
		return procesaUrl;
	}

	public void setGridComplete(String gridComplete) {
		this.gridComplete = gridComplete;
	}

	public String getGridComplete() {
		return gridComplete;
	}


}
