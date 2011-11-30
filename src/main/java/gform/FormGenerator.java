package gform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormGenerator {
	
	private static Logger log = Logger.getLogger(FormGenerator.class.getName());
	static private final String EOL = "\r\n";
	
	public FormGenerator() throws IOException {
		
		InputStream is = new FileInputStream("resources/gform_es.properties"); 
		Properties prop = new Properties();
		prop.load(is);
		is.close();
		
		String template;
		StringBuffer context = new StringBuffer();
		
		String tableName = "servicios_traduccion";
		
		Table table = new Table(tableName);
		ArrayList<Column> columns = table.getColumns();
		
		for(Column column: columns){
			
			if(column.isForingKey()){
				template = readFileAsString(new File("gform/templates/select.jsp"));
				template = template.replaceAll("\\$name", column.getName());
				template = template.replaceAll("\\$key", tableName);
				
				String propSelect = tableName+"."+column.getName()+".select";
				
				String select = (String)prop.get(propSelect);
				if(select == null){
					log.warning("property " + propSelect + "is not found.");
					throw new java.lang.NullPointerException();
				}
				template = template.replaceAll("\\$select", select);
				template = template.replaceAll("\\$fkTable", column.getpKeyTable());
				template = template.replaceAll("\\$fkName", column.getpKeyName());
				
				context.append(template);
				context.append(EOL);
			}
			else{
				switch (column.getSqlType()) {
					case BIT:
						template = readFileAsString(new File("gform/templates/checkbox.jsp"));
						template = template.replaceAll("\\$name", column.getName());
						template = template.replaceAll("\\$key", tableName);
						context.append(template);
						context.append(EOL);
						break;
						
					case INT:
					case VARCHAR:
					case DATETIME:
						template = readFileAsString(new File("gform/templates/input.jsp"));
						template = template.replaceAll("\\$name", column.getName());
						template = template.replaceAll("\\$key", tableName);
						
						context.append(template);
						context.append(EOL);
						break;
						
					case TEXT:
						
						break;
						
				}
			}
		}

		String formTemplate = readFileAsString(new File("gform/templates/form.jsp"));
		
		formTemplate = formTemplate.replaceAll(Pattern.quote("$form.name"), tableName);
		String aux =context.toString();
		formTemplate = formTemplate.replaceFirst(Pattern.quote("$form.fields"), Matcher.quoteReplacement(aux));

		
		FileOutputStream fos = new FileOutputStream("web/traducciones/servicios_traduccion_gform.jsp");
		OutputStreamWriter osw = new OutputStreamWriter(fos/*, "UTF8"*/);
		Writer out = new BufferedWriter(osw);
		out.write(formTemplate);
		out.close();
	}
	
	private String readFileAsString(File file) throws IOException {
		return readFileAsString(file, null);
	}
	
	private String readFileAsString(File file, String codif) throws IOException {
		
		BufferedReader br;
		if (codif != null) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), codif));
		} else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
		}
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			buffer.append(line);
			buffer.append(EOL);
		}
		br.close();
		return buffer.toString();
	}
	
	public static void main(String[] args) throws IOException{
		new FormGenerator();
	}
}
