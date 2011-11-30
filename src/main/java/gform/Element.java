package gform;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Element {
	
	private String label = "";
	private String name;
	private ElementType type;
	private String query;
	private String references;
	private String refElement;
	private boolean hidden;
	private boolean key;
	private boolean auto;
	private boolean readonly = false;
	
	private boolean button;
	private String buttonUrl;
	private String buttonIf;
	
	private String size;
	private String cols;
	private String rows;
	private int decimalPlaces;
	private String onChange;
	private String onDelete;
	
	private boolean notNull;
	private String defValue;
	
	public int getDecimalPlaces(){
		if(size != null && size.indexOf(",")>0){
			decimalPlaces = Integer.parseInt(size.substring(size.indexOf(",")+1));
		}
		return decimalPlaces;
	}
	
	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}
	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(boolean key) {
		this.key = key;
	}
	
	/**
	 * @return the references
	 */
	public String getReferences() {
		return references;
	}
	/**
	 * @param references the references to set
	 */
	public void setReferences(String references) {
		this.references = references;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public ElementType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(ElementType type) {
		this.type = type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = ElementType.elementType(type);
	}
	
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * @return the refElement
	 */
	public String getRefElement() {
		return refElement;
	}
	
	/**
	 * @param refElement the refElement to set
	 */
	public void setRefElement(String refElement) {
		this.refElement = refElement;
	}
	
	public boolean isKey() {
		return key;
	}
	
	public void setButton(boolean button) {
		this.button = button;
	}
	public boolean isButton() {
		return button;
	}
	
	public void setButtonUrl(String url) {
		this.buttonUrl = url;
	}
	
	public String getButtonUrl() {
		return buttonUrl;
	}
	
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	public boolean isReadonly() {
		return readonly;
	}
	
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public String toXml(){
		
		Text text = new Text();
		
		text.appendLine("<element>");
		
		if(label != null){
			text.appendLine(String.format("<label>%1$s</label>", label));
		}
				
		text.appendLine(String.format("<name>%1$s</name>", name));
		text.appendLine(String.format("<type>%1$s</type>", type.toString()));
		if(size != null){
			text.appendLine(String.format("<size>%1$s</size>", size));
		}
		
		if(key){
			text.appendLine("<key>true</key>");
		}
		
		if(auto){
			text.appendLine("<auto>true</auto>");
		}
		
		if(references!=null){
			text.appendLine(String.format("<references>%1$s</references>",references));
		}
		
		text.appendLine("</element>");
		
		return text.toString();
	}
	

	public static Element parceGFormElement(String xmlElement) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		
		xmlElement = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + xmlElement;
		
		XmlDoc xml = new XmlDoc(new ByteArrayInputStream(xmlElement.getBytes()));
		NodeList nodeList =  xml.getNodes("//element");

		
		Element element = new Element();
		
		Node node = nodeList.item(0);
		
		ArrayList<String> aux = xml.getNodeValues(node, "label/text()");
		if(!aux.isEmpty()){
			element.setLabel(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "name/text()");
		element.setName(aux.get(0));
		
		aux = xml.getNodeValues(node, "type/text()");
		element.setType(aux.get(0));
		
		aux = xml.getNodeValues(node, "references/text()");
		if(!aux.isEmpty()){
			element.setReferences(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "size/text()");
		if(!aux.isEmpty()){
			
			element.setSize(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "cols/text()");
		if(!aux.isEmpty()){
			
			element.setCols(aux.get(0));
		}

		aux = xml.getNodeValues(node, "rows/text()");
		if(!aux.isEmpty()){
			
			element.setRows(aux.get(0));
		}

		
		
		aux = xml.getNodeValues(node, "query/text()");
		if(!aux.isEmpty()){
			element.setQuery(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "refelement/text()");
		if(!aux.isEmpty()){
			element.setRefElement(aux.get(0));
		}
		
		aux = xml.getNodeValues(node, "hidden/text()");
		if(!aux.isEmpty()){
			element.setHidden(Boolean.parseBoolean(aux.get(0)));
		}
		
		aux = xml.getNodeValues(node, "key/text()");
		if(!aux.isEmpty()){
			element.setKey(Boolean.parseBoolean(aux.get(0)));
		}
		
		aux = xml.getNodeValues(node, "auto/text()");
		if(!aux.isEmpty()){
			element.setAuto(Boolean.parseBoolean(aux.get(0)));
		}
		
		aux = xml.getNodeValues(node, "readonly/text()");
		if(!aux.isEmpty()){
			element.setReadonly(Boolean.parseBoolean(aux.get(0)));
		}
		
		aux = xml.getNodeValues(node, "notnull/text()");
		if(!aux.isEmpty()){
			element.setNotNull(Boolean.parseBoolean(aux.get(0)));
		}
		
		
		aux = xml.getNodeValues(node, "button/text()");
		if(!aux.isEmpty()){
			element.setButton(Boolean.parseBoolean(aux.get(0)));
		}
		
		aux = xml.getNodeValues(node, "button_url/text()");
		if(!aux.isEmpty()){
			element.setButtonUrl(aux.get(0).trim());
		}
		
		aux = xml.getNodeValues(node, "button_if/text()");
		if(!aux.isEmpty()){
			element.setButtonIf(aux.get(0).trim());
		}
		
		aux = xml.getNodeValues(node, "onchange/text()");
		if(!aux.isEmpty()){
			element.setOnChange(aux.get(0).trim());
		}
		
		aux = xml.getNodeValues(node, "ondelete/text()");
		if(!aux.isEmpty()){
			element.setOnDelete(aux.get(0).trim());
		}
		
		aux = xml.getNodeValues(node, "defvalue/text()");
		if(!aux.isEmpty()){
			element.setDefValue(aux.get(0).trim());
		}
		
		return element;
			
	}
	
	public void setCols(String cols) {
		this.cols = cols;
	}
	
	public String getCols() {
		return cols;
	}
	
	public void setRows(String rows) {
		this.rows = rows;
	}
	
	public String getRows() {
		return rows;
	}
	public void setButtonIf(String buttonIf) {
		this.buttonIf = buttonIf;
	}
	public String getButtonIf() {
		return buttonIf;
	}

	public String getOnChange() {
		return onChange;
	}
	
	public void setOnChange(String script){
		onChange = script;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}

	public String getOnDelete() {
		return onDelete;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getDefValue() {
		return defValue;
	}

}
