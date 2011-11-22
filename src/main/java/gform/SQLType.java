package gform;

public enum SQLType {

	INT, BIT, TEXT, VARCHAR, DATETIME, DECIMAL;
	
	public static SQLType typeName(String typeName) {
		for (SQLType key : values()) {
			if (key.toString().equalsIgnoreCase(typeName)){
				return key;
			}
		}
		throw new IllegalArgumentException ("Not a valid type name: " + typeName);
	}
}
