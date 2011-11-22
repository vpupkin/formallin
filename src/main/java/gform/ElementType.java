package gform;

public enum ElementType {

	INT, DECIMAL, SELECT, CHECKBOX, AJAXSELECT, DATETIME, VARCHAR, TEXTAREA;
	
	public static ElementType elementType(String typeName) {
		
		for (ElementType key : values()) {
			if (key.toString().equalsIgnoreCase(typeName)){
				return key;
			}
		}
		throw new IllegalArgumentException ("Not a valid type name: " + typeName);
	}
}
