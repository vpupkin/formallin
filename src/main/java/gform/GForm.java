package gform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class GForm {
	
	private static Logger log = Logger.getLogger(GForm.class.getName());
	
	private String gform;
	private Table table;
	private String webAppPath;
	private String subDir;
	
	public GForm(){
	}
	
	public GForm(String gformTemplate, String webAppPath, String subDir) throws IOException{
		gform = GForm.readFileAsString(new File(gformTemplate));
		this.webAppPath = webAppPath;
		this.subDir = subDir;
	}
	
	
	// TODO quitar este método, recibir tableName como parámetro del constructor
	public String parceTableName(String xmlForm) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		xmlForm = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + xmlForm;
		XmlDoc xml = new XmlDoc(new ByteArrayInputStream(xmlForm.getBytes()));
		return xml.getValues("//form/@table").get(0);
	}
	
	public void setTemplate(String template) throws IOException{
		gform = GForm.readFileAsString(new File(template));
		table = null;
	}

	private void setSubDir(String arg) {
		subDir = arg;
		
	}

	private void setWebAppPath(String arg) {
		webAppPath = arg;
		
	}

	public void generateFormJsp() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, SQLException, ClassNotFoundException{
		
		int beginIndex = gform.indexOf("<form");
		int endIndex = gform.indexOf("</form>");
		
		String form = gform.substring(beginIndex, endIndex+7);
		String tableName = parceTableName(form);
		
		endIndex = gform.indexOf(">", beginIndex);
		
		StringBuffer jspContext = new StringBuffer();
		
		jspContext.append(gform.substring(0,beginIndex));
		jspContext.append(String.format("<form name=\"%1$s\" id=\"%1$s\" action=\"%1$s_procesa.jsp\">",tableName));
		jspContext.append(gform.substring(endIndex+1, gform.length()));
		
		table = new Table(tableName);
		log.info(tableName);
		
		/* Procesar ELEMENTs */
		while((beginIndex = jspContext.indexOf("<element>")) >= 0){
			endIndex = jspContext.indexOf("</element>", beginIndex);
			String xmlElement = jspContext.substring(beginIndex, endIndex+10);
			Element element = Element.parceGFormElement(xmlElement);
			
			String template = "";
			
			String elementName = element.getName();
			ElementType elementType = element.getType();
			
			Column column = new Column(elementName);

			if(element.isKey()){
				column.setKey(true);
				if(element.isAuto()){
					column.setAuto(true);
				}
			}
			
			if(element.getSize() != null){
				column.setSize(element.getSize());
			}
			
			if(element.getOnDelete() != null){
				column.setOnDelete(element.getOnDelete());
			}
			
			if(element.isNotNull()){
				column.setNotNull(true);
			}
			
			String references = element.getReferences();
			if(references != null){
				column.setForingKey(true);
				
				String pKeyTable = references.substring(0,references.indexOf('('));
				String pKeyName = references.substring(references.indexOf('(')+1,references.indexOf(')'));
				
				column.setpKeyName(pKeyName);
				column.setpKeyTable(pKeyTable);
			}
			
			if(element.isHidden()){
				column.setSqlType(SQLType.typeName(element.getType().toString()));
				template = readFileAsString(new File("gform/templates/hidden.jsp"));
				template = template.replaceAll("\\$name", elementName);
				template = template.replaceAll("\\$table", tableName);
			}
			else {
				switch (elementType){
				case VARCHAR:
					column.setSqlType(SQLType.VARCHAR);
					int size = Integer.parseInt(column.getSize());
					template = readFileAsString(new File("gform/templates/input.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$maxlength", column.getSize());
					
					if(element.isReadonly()){
						template = template.replaceAll("\\$readonly", "readonly");
					}
					else{
						template = template.replaceAll("\\$readonly", "");
					}
					
					if(size < 50){
						template = template.replaceAll("\\$size", column.getSize());
					}
					else {
						template = template.replaceAll("\\$size", ""+50);
					}
					break;
				
				case DATETIME:
					column.setSqlType(SQLType.DATETIME);
					template = readFileAsString(new File("gform/templates/datetime.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$size", ""+10);
					template = template.replaceAll("\\$maxlength", ""+10);
					if(element.isReadonly()){
						template = template.replaceAll("\\$readonly", "readonly");
					}
					else{
						template = template.replaceAll("\\$readonly", "");
					}
					break;
				
				case DECIMAL:
					column.setSqlType(SQLType.DECIMAL);
					template = readFileAsString(new File("gform/templates/decimal.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$size", ""+10);
					template = template.replaceAll("\\$maxlength", ""+10);
					
					if(element.isReadonly()){
						template = template.replaceAll("\\$readonly", "readonly");
					}
					else{
						template = template.replaceAll("\\$readonly", "");
					}
					
					if(element.getDefValue() != null){
						template = template.replaceAll("\\$defvalue", element.getDefValue());
					}
					else {
						template = template.replaceAll("\\$defvalue", "");
					}
					
					if(element.getOnChange() != null){
						template = template.replaceAll("\\$onchange", Matcher.quoteReplacement(element.getOnChange()));
					}
					else {
						template = template.replaceAll("\\$onchange", "");
					}
					
					break;
					
				case INT:
					column.setSqlType(SQLType.INT);
					template = readFileAsString(new File("gform/templates/int.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$size", ""+10);
					template = template.replaceAll("\\$maxlength", ""+10);
					
					if(element.isReadonly()){
						template = template.replaceAll("\\$readonly", "readonly");
					}
					else{
						template = template.replaceAll("\\$readonly", "");
					}
					
					if(element.getOnChange() != null){
						template = template.replaceAll("\\$onchange", Matcher.quoteReplacement(element.getOnChange()));
					}
					else {
						template = template.replaceAll("\\$onchange", "");
					}
					break;
					
				case SELECT:
					column.setSqlType(SQLType.INT);
					
					template = readFileAsString(new File("gform/templates/select.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$query", element.getQuery());
					if(element.isButton()){
						String buttonTemplate = readFileAsString(new File("gform/templates/button.jsp"));
						buttonTemplate = buttonTemplate.replaceAll("\\$button_url", Matcher.quoteReplacement(element.getButtonUrl()));
						
						buttonTemplate = buttonTemplate.replaceAll("\\$table", tableName);
						buttonTemplate = buttonTemplate.replaceAll("\\$name", elementName);
						
						if(element.getButtonIf()!=null){
							buttonTemplate = buttonTemplate.replaceAll("\\$button_if", Matcher.quoteReplacement(element.getButtonIf()));
						}
						else {
							buttonTemplate = buttonTemplate.replaceAll("\\$button_if", "true");
						}
						template = template.concat(buttonTemplate);
					}
					break;
				
				case AJAXSELECT:
					column.setSqlType(SQLType.INT);
					template = readFileAsString(new File("gform/templates/ajaxselect.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					template = template.replaceAll("\\$query", element.getQuery());
					template = template.replaceAll("\\$refelement", element.getRefElement());
					
					// crear jsp para consulta por ajax
					String jspOpt = element.getName() + "_options.jsp";
					generateOptionsJsp(new File(webAppPath + "/" + subDir, jspOpt), element.getQuery(), element.getRefElement());
					template = template.replaceAll("\\$ajaxUrl", Matcher.quoteReplacement("${baseUrl}/" + subDir + "/" + jspOpt));
					break;
				
				case TEXTAREA:
					column.setSqlType(SQLType.VARCHAR);
					
					if(element.getSize() == null){
						column.setSize("1000");
					}
					
					template = readFileAsString(new File("gform/templates/textarea.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					
					if(element.getCols()!=null){
						template = template.replaceAll("\\$cols", element.getCols());
					}
					else {
						template = template.replaceAll("\\$cols", ""+20);
					}
					
					if(element.getRows()!=null){
						template = template.replaceAll("\\$rows", element.getRows());
					}
					else {
						template = template.replaceAll("\\$rows", ""+2);
					}
					break;
					
				case CHECKBOX:
					column.setSqlType(SQLType.BIT);
					template = readFileAsString(new File("gform/templates/checkbox.jsp"));
					template = template.replaceAll("\\$table", tableName);
					template = template.replaceAll("\\$name", elementName);
					template = template.replaceAll("\\$label", element.getLabel());
					break;
					
				default:
					throw new RuntimeException("No case for " + elementType);
				}
			}
			table.addColumn(column);
			jspContext.replace(beginIndex, endIndex+10, template);
		}
		
		/* Procesar GRIDs */
		
		String beginTag = "<grid>";
		String endTag = "</grid>";
		
		while((beginIndex = jspContext.indexOf(beginTag)) >= 0){
			endIndex = jspContext.indexOf(endTag, beginIndex);
			String xmlFragmen = jspContext.substring(beginIndex, endIndex + endTag.length());
			Grid grid = Grid.parceGFormGrid(xmlFragmen);
			
			String template = readFileAsString(new File("gform/templates/grid.jsp"));
			template = template.replaceAll("\\$title", Matcher.quoteReplacement(grid.getTitle()));
			template = template.replaceAll("\\$url", Matcher.quoteReplacement("\"${baseUrl}\"+" + grid.getUrl()));
			template = template.replaceAll("\\$procesa_url", Matcher.quoteReplacement("\"${baseUrl}\"+" + grid.getProcesaUrl()));
			template = template.replaceAll("\\$gridComplete", Matcher.quoteReplacement(grid.getGridComplete()));
			
			String tableModelFileName = grid.getTableModel() + ".jsp";
			
			String tableModel = readFileAsString(new File("gform/" + tableModelFileName));
			
			/* Procesar GRID ELEMENTs */
			GridColumnModel colModel = new GridColumnModel();
			int endIndex2 = 0;
			int beginIndex2 =0;
			while((beginIndex2 = tableModel.indexOf("<element>", endIndex2)) >= 0){
				endIndex2 = tableModel.indexOf("</element>", beginIndex2);
				String xmlElement = tableModel.substring(beginIndex2, endIndex2 + 10);
				Element element = Element.parceGFormElement(xmlElement);
				String label = element.getLabel();
				String name = element.getName();
				
				if(grid.getColumns().indexOf(name)>=0){
				
					GridColumn gridColumn = new GridColumn(element);
					if(label != null && !label.isEmpty()){
						gridColumn.setColName(label);
					}
					else {
						gridColumn.setColName(name);
					}
					gridColumn.setHidden(element.isHidden());
					gridColumn.setElement(element);
					
					gridColumn.setName(grid.getTableModel()+"__"+gridColumn.getName());
					
					colModel.addColumn(gridColumn);
				}
			}
			
			template = template.replaceAll("\\$colNames", Matcher.quoteReplacement(colModel.getColumnNames()));
			
			Text colModelText = new Text();
			ArrayList<GridColumn> gridColumns = colModel.getColumns();
			int count = 0;
			for(int i=0; i< gridColumns.size(); i++){
				GridColumn column = gridColumns.get(i);
				if(!column.isHidden()||(column.isHidden() && column.getElement().isKey())){
					if(i > 0){
						colModelText.appendLine(",");
					}
					colModelText.append(column.toString());
				}
				count++;
			}
			
			template = template.replaceAll("\\$colModel", Matcher.quoteReplacement(colModelText.toString()));
			
			
			jspContext.replace(beginIndex, endIndex + endTag.length(), template);
			
			// crear jsp para consulta por ajax
			String xmlJsp = grid.getName().concat("_xml.jsp");
			generateXmlJsp(new File(webAppPath + "/" + subDir, xmlJsp), grid.getQuery());
		}
				
		/* Guardar JSP */
		FileOutputStream fos = new FileOutputStream(new File(webAppPath + "/" + subDir, tableName + "_form.jsp"));
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		Writer out = new BufferedWriter(osw);
		out.write(jspContext.toString());
		out.close();
		
	}
	
	public void generateProcesaJsp() throws IOException{
		
		String template = readFileAsString(new File("gform/templates/procesa.jsp"));
		template = template.replaceAll("\\$id", table.getName()+"__"+table.getKeys().get(0));
		template = template.replaceAll("\\$deleteQuery", Matcher.quoteReplacement(table.getDeleteQuery()));
		template = template.replaceAll("\\$insertQuery", Matcher.quoteReplacement(table.getInsertQuery()));
		template = template.replaceAll("\\$updateQuery", Matcher.quoteReplacement(table.getUpdateQuery()));
		template = template.replaceAll("\\$convertDates", Matcher.quoteReplacement(table.getConvertDates()));
		
		File file = new File(webAppPath + "/" + subDir, table.getName() + "_procesa.jsp");
		
		log.info(file.getPath());
		
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos/*, "UTF8"*/);
		Writer out = new BufferedWriter(osw);
		out.write(template);
		out.close();
	}
	
	
	private void generateOptionsJsp(File jsp, String query, String refelement) throws IOException{
		
		String template = readFileAsString(new File("gform/templates/options.jsp"));
		template = template.replaceAll("\\$query", query);
		template = template.replaceAll("\\$refelement", refelement);
		
		FileOutputStream fos = new FileOutputStream(jsp);
		OutputStreamWriter osw = new OutputStreamWriter(fos/*, "UTF8"*/);
		Writer out = new BufferedWriter(osw);
		out.write(template);
		out.close();
	}
	
	private void generateXmlJsp(File jsp, String query) throws IOException{
		
		String template = readFileAsString(new File("gform/templates/grid_xml.jsp"));
		template = template.replaceAll("\\$query", Matcher.quoteReplacement(query));
		
		FileOutputStream fos = new FileOutputStream(jsp);
		OutputStreamWriter osw = new OutputStreamWriter(fos/*, "UTF8"*/);
		Writer out = new BufferedWriter(osw);
		out.write(template);
		out.close();
	}
	
	public void updateDBSchema() throws SQLException, ClassNotFoundException{
		
		DBSchema db = new DBSchema("gescsidiomas");
		
		if(db.isTableExists(table.getName())){
			// actualizar columnas, prime key, external keys
			db.changeTable(table);
		}
		else{
			// create table, prime keys, external keys
			db.createTable(table);
		}

	}
	
	
	
	public static String readFileAsString(File file) throws IOException {
		return readFileAsString(file, null);
	}
	
	public static String readFileAsString(File file, String codif) throws IOException {
		
		BufferedReader br;
		if (codif != null) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), codif));
		} else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
		}
		
		Text text = new Text();
		String line;
		while ((line = br.readLine()) != null) {
			text.appendLine(line);
		}
		br.close();
		return text.toString();
	}
	
	
	public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, SQLException, ClassNotFoundException{
		
		//DBSchema db = new DBSchema("gescsidiomas");
		
		GForm gform = new GForm();
		gform.setWebAppPath("web");
		gform.setSubDir("traducciones");
		
		gform.setTemplate("gform/servicios_traduccion.jsp");
		gform.generateFormJsp();
		//gform.updateDBSchema();
		//gform.generateProcesaJsp();
		
		gform.setTemplate("gform/traduccion.jsp");
		gform.generateFormJsp();
		//gform.updateDBSchema();
		//gform.generateProcesaJsp();
		
		gform.setTemplate("gform/revision.jsp");
		gform.generateFormJsp();
		//gform.updateDBSchema();
		//gform.generateProcesaJsp();
		
		//gform.setTemplate("gform/traductor.jsp");
		//gform.generateFormJsp();
		//formClientes.updateDBSchema();
		//gform.generateProcesaJsp();
		
		gform.setTemplate("gform/cliente_traduccion.jsp");
		gform.generateFormJsp();
		//gform.generateProcesaJsp();
		
		//Table contactos = db.parseTable("contactos");
		//System.out.println(contactos.exportToXml());
		
		//gform.setTemplate("gform/cliente_traduccion_contactos.jsp");
		//gform.generateFormJsp();
		//gform.generateProcesaJsp();
		//gform.updateDBSchema();
		
		//gform.setTemplate("gform/servicios_traduccion_traductor.jsp");
		//gform.generateFormJsp(); // no tiene, pero hay que generar para DBSchema
		//gform.updateDBSchema();
		//gform.generateProcesaJsp();
		
	}
}
