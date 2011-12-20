package gform;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author sergi
 *
 */
public class GridColumnModel {
	
	private static Logger log = Logger.getLogger(GridColumnModel.class.getName());
	
	private ArrayList<GridColumn> columns;
	
	
	public GridColumnModel(){
		columns = new ArrayList<GridColumn>();
	}
	
	public void addColumn(GridColumn column){
		columns.add(column);
	}
	
	public String getColumnNames(){
		String names = "";
		int count = 0;
		for(int i=0; i< columns.size(); i++){
			GridColumn column = columns.get(i);
			if(!column.isHidden() || (column.isHidden() && column.getElement().isKey())){
				if(i>0){
					names = names.concat(",");
				}
				names = names.concat(String.format("\"%1$s\"", column.getColName()));
				count++;
			}
		}
		return names;
	}
	
	public ArrayList<GridColumn> getColumns() {

		return columns;
	}
}
