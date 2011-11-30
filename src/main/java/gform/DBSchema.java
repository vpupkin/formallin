package gform;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * Metodos para crear o modificar tablas en la base de datos.
 * 
 * @author sergi
 *
 */
public class DBSchema {

	private static Logger log = Logger.getLogger(DBSchema.class.getName());

	private DatabaseMetaData md;
	private Connection c;
	
	public DBSchema(String dbName) throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,"root","root");
		md = c.getMetaData();
	}
	
	public boolean isTableExists(String name) throws SQLException{
		
		ResultSet rs1 = md.getColumns("gform", "gform", name, null);
		return (rs1.next());
	}

	public void createTable(Table table) throws SQLException {
		
		StringBuffer query = new StringBuffer();
		query.append(String.format("CREATE TABLE %1$s (",table.getName()));
		
		ArrayList<Column> columns = table.getColumns();
		
		for(int i=0; i<columns.size(); i++){
			
			Column col = columns.get(i);
			
			if(col.getSize()!=null){
				query.append(String.format("%1$s %2$s(%3$s)", col.getName(), col.getSqlType(), col.getSize()));
			}
			else {
				query.append(String.format("%1$s %2$s", col.getName(), col.getSqlType()));
			}
			
			if(col.isAuto()){
				query.append(" AUTO_INCREMENT");
			}
			
			if(col.isNotNull()){
				query.append(" NOT NULL");
			}
			
			query.append(",");
		}

		ArrayList<String> keys = table.getKeys();
		query.append("PRIMARY KEY (");
		
		for(int i=0; i<keys.size(); i++){
			
			String key = keys.get(i);
			
			query.append(key);
			
			if(i < keys.size()-1){
				query.append(",");
			}
		}
		query.append(")"); // close key
		query.append(")");
		
		log.info(query.toString());
		
		Statement statement = c.createStatement();
		
		statement.executeUpdate(query.toString());
		
		// add foring keys
		int fk = 0;
		int indx =0;
		
		for(int i=0; i<columns.size(); i++){
			
			Column col = columns.get(i);
			if(col.isForingKey()){
				
				String tableName = table.getName();
				String keyColumn = col.getName();
				String refKeyName = col.getpKeyName();
				String refTableName = col.getpKeyTable();
				String onDelete = col.getOnDelete();
				
				if(!table.getKeys().get(0).equals(keyColumn)){
					// first primary key don't need be indexed
					String q = String.format("CREATE INDEX INDX_%1$s_%3$d ON %1$s (%2$s ASC)", tableName, keyColumn, ++indx);
					log.info(q);
					statement.executeUpdate(q);
				}
				String q = String.format("ALTER TABLE %1$s ADD CONSTRAINT FK_%1$s_%5$d FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s) ON DELETE %6$s ON UPDATE CASCADE", tableName, keyColumn, refTableName, refKeyName, ++fk, onDelete);
				log.info(q);
				statement.executeUpdate(q);
			}
		}
		statement.close();
	}

	
	public void changeTable(Table table) throws SQLException {
		// TODO Auto-generated method stub
		dropTable(table);
		createTable(table);
	}
	
	
	public void dropTable(Table table) throws SQLException{
		
		String q = String.format("DROP TABLE %1$s", table.getName());
		log.info(q);
		
		Statement statement = c.createStatement();
		statement.executeUpdate(q);
		statement.close();
	}
	
	public Table parseTable(String name){
		
		Table table = new Table(name);
		try {
			ResultSet rs1 = md.getColumns("gescsidiomas", "gescsidiomas", name, null);
			
			ResultSetMetaData rsmd = rs1.getMetaData();
			for(int i=1; i<=rsmd.getColumnCount(); i++){
				System.out.println(rsmd.getColumnLabel(i));
			}
			
			while(rs1.next()){
				String columnName = rs1.getString("COLUMN_NAME");
				SQLType typeName = SQLType.valueOf(rs1.getString("TYPE_NAME").toUpperCase());
				String columnSize = rs1.getString("COLUMN_SIZE");
				
				String decDig = rs1.getString("DECIMAL_DIGITS");
				if(decDig != null && Integer.parseInt(decDig)>0){
					columnSize = columnSize.concat(",");
					columnSize = columnSize.concat(decDig);
				}
				
				Column col = new Column(columnName, typeName, columnSize);
				table.addColumn(col);
				
			}
			
			ResultSet rs2 = md.getImportedKeys("gescsidiomas", "gescsidiomas", name);
			ResultSetMetaData rsmd2 = rs2.getMetaData();
			for(int i=1; i<=rsmd2.getColumnCount(); i++){
				System.out.println(rsmd2.getColumnLabel(i));
			}
			
			
			while(rs2.next()){
				String pkTable = rs2.getString("PKTABLE_NAME");
				String pkColumn = rs2.getString("PKCOLUMN_NAME");
				String fkColumn = rs2.getString("FKCOLUMN_NAME");

				Column fColumn = table.getColumn(fkColumn);
				fColumn.setForingKey(true);
				fColumn.setpKeyName(pkColumn);
				fColumn.setpKeyTable(pkTable);
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return table;
	}
}
