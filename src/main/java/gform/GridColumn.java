package gform;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class GridColumn {

	private static Logger log = Logger.getLogger(GridColumn.class.getName());
	
	private String colName;
	private String name;
	//private String index;
	private int defaultWidth = 85;
	private int width;
	private String align ="right";
	private boolean search=true;
	private boolean editable=true;
	private boolean hidden=false;
	private Element element;

	public GridColumn(Element element){
		this.element = element;
		this.name = element.getName();
	}

	public void setElement(Element element){
		this.element = element;
	}
	
	public Element getElement(){
		return element;
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
	public void setName(String columnName) {
		this.name = columnName;
	}


	public void setColName(String colName) {
		this.colName = colName;
	}


	public String getColName() {
		return colName;
	}

/*
	public void setIndex(String index) {
		this.index = index;
	}


	public String getIndex() {
		return index;
	}
*/

	public void setWidth(int width) {
		this.width = width;
	}


	public int getWidth() {
		if(width != 0){
			return width;
		}
		else {
			
			// TODO en lugar de usar defaultWith calcular tamaño de la columna
			// según el timpo y size del element
			
			return defaultWidth;
		}
		
	}


	public void setAlign(String align) {
		this.align = align;
	}


	public String getAlign() {
		return align;
	}


	public void setSearch(boolean search) {
		this.search = search;
	}


	public boolean isSearch() {
		return search;
	}


	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	public boolean isHidden() {
		return hidden;
	}

	public String toString(){
		
		// edittype:"select", editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}
		String editoptions = "";
		ElementType eType = element.getType();
		if(eType != null && eType == ElementType.SELECT){
			editoptions = editoptions.concat(", edittype:'select'");
			//recuperar values
			String template;
			try {
				template = GForm.readFileAsString(new File("gform/templates/editoptions.jsp"));
				template = template.replaceAll("\\$query", Matcher.quoteReplacement(element.getQuery()));
				
				editoptions = editoptions.concat(",");
				editoptions = editoptions.concat(Text.EOL);
				editoptions = editoptions.concat(template);
				
			} catch (IOException e) {
				e.printStackTrace();
				log.severe(e.getMessage());
			}
		}
		if(eType != null && eType == ElementType.CHECKBOX){
			editoptions = editoptions.concat(", edittype:'checkbox', editoptions: {value:'Sí:'}");
		}
		
		// formatter:'currency', formatoptions:{decimalSeparator:",", thousandsSeparator: ".", decimalPlaces: 4, prefix: ""}
		String formater = "";
		if(eType != null && eType == ElementType.DECIMAL){
			int decimalPlaces = element.getDecimalPlaces();
			formater = String.format(", formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:%1$d, prefix:''}", decimalPlaces);
		}
		
		//editoptions: {readonly: 'readonly'}
		String readonly = "";
		if(element.isReadonly()){
			readonly = ", editoptions: {readonly: 'readonly'}";
		}
		
		//editoptions:{dataEvents: [ { type:'change', fn: function(e) {...}}]}
		String dataevents = "";
		if(element.getOnChange() != null){
			dataevents = String.format(", editoptions:{dataEvents: [ { type:'change', fn: function(e) {%1$s}}]}", element.getOnChange());
		}
		
		
		// {name:"id", index:"id", width:85, align:"right", search:false, editable:false, hidden:true}
		return String.format(
				"{name:'%1$s', index:'%1$s', width:%2$d, align:'%3$s', search:%4$b, editable:%5$b, hidden:%6$b%7$s%8$s%9$s%10$s}",
				name, width, align, search, editable, hidden, editoptions, formater, readonly, dataevents);
	}

}
