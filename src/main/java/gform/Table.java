package gform;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * 
 * @author sergi
 *
 */
public class Table {
	
	private static Logger log = Logger.getLogger(Table.class.getName());
	
	private String name;
	private ArrayList<Column> columns;
	private ArrayList<String> keys;
	
	
	public Table(String name){
		this.name = name;
		columns = new ArrayList<Column>();
		keys = new ArrayList<String>();
	}
	
	/**
	 * @return the key
	 */
	public ArrayList<String> getKeys() {
		return keys;
	}
	
	public String getName(){
		return name;
	}
	
	public void addColumn(Column column){
		columns.add(column);
		if(column.isKey()){
			keys.add(column.getName());
		}
	}
	
	public Column getColumn(String name){
		Column column = null;
		
		for(Column col: columns){
			if(col.getName().equalsIgnoreCase(name)){
				column = col;
				break;
			}
		}
		return column;
	}

	public ArrayList<Column> getColumns() {

		return columns;
	}
	
	public String getDeleteQuery() {
		
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM " + name + " WHERE ");
		
		for(int i=0; i< keys.size(); i++){
			String key = keys.get(i);
			query.append(key + "=?");
			if(i < keys.size()-1){
				query.append(" AND ");
			}
		}
		query.append(Text.EOL);
		
		for(int i=0; i< keys.size(); i++){
			String key = keys.get(i);
			query.append("<sql:param>${id}</sql:param>");
			query.append(Text.EOL);
		}
		
		return query.toString();
	}
	
	
	
	public String getConvertDates(){
		
		Text convertDates = new Text();
		for(int i=0; i< columns.size(); i++){
			Column column = columns.get(i);
			switch (column.getSqlType()) {
			case DATETIME:
				String paramName = name + "__" + column.getName();
				convertDates.appendLine(String.format("<c:if test=\"${!empty param.%1$s}\">",paramName));
				convertDates.appendLine(String.format("<fmt:parseDate var=\"%1$s\" value=\"${param.%1$s}\" pattern=\"dd/MM/yyyy\" />",paramName));
				convertDates.appendLine("</c:if>");
				break;
			}
		}
		return convertDates.toString();
	}
	
	
	public String getInsertQuery() {
		
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO " + name + "(");

		int count = 0;
		for(int i=0; i< columns.size(); i++){
			Column column = columns.get(i);
			if(!column.isAuto()){
				query.append(column.getName());
				if(i < columns.size()-1){
					query.append(",");
				}
				count++;
			}
		}
		
		query.append(")");
		query.append(Text.EOL);
		
		query.append("VALUES (");
		for(int i=0; i<count; i++){
			query.append("?");
			if(i < count-1){
				query.append(",");
			}
		}
		
		query.append(")");
		query.append(Text.EOL);
		
		for(int i=0; i< columns.size(); i++){
			Column column = columns.get(i);
			if(!column.isAuto()){
				log.info(column.getName());
				switch (column.getSqlType()) {
				case BIT:
					query.append("<sql:param value=\"${!empty param." + name + "__" + column.getName()+"}\"/>");	
					break;
					
				case DECIMAL:
					query.append("<sql:param><fmt:parseNumber type=\"number\"  value=\"${param."+name+"__"+column.getName()+"}\" /></sql:param>");
					break;
				
				case DATETIME:
					query.append("<sql:dateParam value=\"${" + name + "__" + column.getName() + "}\"/>");
					break;
					
				default:
					query.append("<sql:param>${param."+name+"__"+column.getName()+"}</sql:param>");
					break;
				}
				query.append(Text.EOL);
			}
		}
		return query.toString();
	}
	
	public String getUpdateQuery() {
		
		StringBuffer query = new StringBuffer();
		
		query.append("UPDATE " + name + " SET");
		query.append(Text.EOL);
		
		int count = 0;
		for(int i=0; i< columns.size(); i++){
			Column column = columns.get(i);
			if(!column.isKey()){
				query.append(column.getName()+"=?");
				if(i < columns.size()-1){
					query.append(",");
				}
				query.append(Text.EOL);
				count++;
			}
		}
		
		query.append("WHERE " + keys.get(0) + " = ?");
		query.append(Text.EOL);
		
		for(int i=0; i< columns.size(); i++){
			Column column = columns.get(i);
			if(!column.isKey()){
				
				switch (column.getSqlType()) {
				case BIT:
					query.append("<sql:param value=\"${!empty param."+name+"__"+column.getName()+"}\"/>");	
					break;
				
				case DECIMAL:
					query.append("<sql:param><fmt:parseNumber type=\"number\"  value=\"${param."+name+"__"+column.getName()+"}\" /></sql:param>");
					break;
				
				case DATETIME:
					query.append("<sql:dateParam value=\"${" + name + "__" + column.getName() + "}\"/>");
					break;
					
				default:
					query.append("<sql:param>${param."+name+"__"+column.getName()+"}</sql:param>");
					break;
				}
				query.append(Text.EOL);
			}
		}
		
		query.append("<sql:param>${id}</sql:param>");
		query.append(Text.EOL);
		
		return query.toString();
	}
	
	public String exportToXml(){
		
		Text text = new Text();
		
		for(int i=0; i< columns.size(); i++){
			
			Column column = columns.get(i);
			Element element = new Element();
			
			element.setName(column.getName());
			element.setLabel(column.getName());
			element.setKey(column.isKey());
			element.setAuto(column.isAuto());
			
			
			switch (column.getSqlType()) {
			case BIT:
				element.setType(ElementType.CHECKBOX);
				break;
			case TEXT:
				element.setType(ElementType.TEXTAREA);
				break;
			default:
				element.setType(column.getSqlType().toString());
				element.setSize(column.getSize());
				break;
			}
			
			if(column.isForingKey()){
				element.setReferences(column.getpKeyTable()+"("+column.getpKeyName()+")");
			}
			
			text.append(element.toXml());
		}
		return text.toString();
	}

}
