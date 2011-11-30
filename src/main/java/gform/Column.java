package gform;

import java.util.logging.Logger;

public class Column {

	private static Logger log = Logger.getLogger(Column.class.getName());
	
	private String name;
	private SQLType sqlType;
	private String size;
	
	private boolean foringKey;
	private String pKeyTable;
	private String pKeyName;
	
	private boolean key;
	private boolean auto;
	
	private boolean notNull;
	
	private String onDelete = "RESTRICT";
	
	/**
	 * @return the auto
	 */
	public boolean isAuto() {
		return auto;
	}

	/**
	 * @param auto the auto to set
	 */
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public Column(String columnName){
		this.name = columnName;
	}
	
	public Column(String columnName, SQLType sqlType, String columnSize) {
		this.name = columnName;
		this.sqlType = sqlType;
		this.size = columnSize;
		
		log.info("size="+size);
	}

	/**
	 * @return the key
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(boolean key) {
		this.key = key;
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

	/**
	 * @return the typeName
	 */
	public SQLType getSqlType() {
		return sqlType;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setSqlType(SQLType sqlType) {
		
		this.sqlType = sqlType;
		
		// set default size
		if(size == null){
			switch (sqlType) {
			case INT:
				size = "10";
				break;
			case VARCHAR:
				size = "50";
				break;
			case DECIMAL:
				size = "19,4";
				break;
			}
		}
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String columnSize) {
		this.size = columnSize;
	}

	/**
	 * @return the pKeyTable
	 */
	public String getpKeyTable() {
		return pKeyTable;
	}

	/**
	 * @param pKeyTable the pKeyTable to set
	 */
	public void setpKeyTable(String pKeyTable) {
		this.pKeyTable = pKeyTable;
	}

	/**
	 * @return the pKeyName
	 */
	public String getpKeyName() {
		return pKeyName;
	}

	/**
	 * @param pKeyName the pKeyName to set
	 */
	public void setpKeyName(String pKeyName) {
		this.pKeyName = pKeyName;
	}

	/**
	 * @return the foringKey
	 */
	public boolean isForingKey() {
		return foringKey;
	}

	/**
	 * @param foringKey the foringKey to set
	 */
	public void setForingKey(boolean foringKey) {
		this.foringKey = foringKey;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}

	public String getOnDelete() {
		return onDelete;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean isNotNull() {
		return notNull;
	}


}
