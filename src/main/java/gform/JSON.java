package gform;

import java.util.LinkedHashMap;

public class JSON extends LinkedHashMap<String, Object>{
	
	private static final long serialVersionUID = 1371915928436381030L;

	public String toString(){
		String aux = "{";
		Object[] keys = keySet().toArray();
		for(int k=0; k<keys.length; k++){
			Object value = get(keys[k]);
			aux = aux.concat("" + keys[k] + ":");
			if(value == null){
				aux = aux.concat("null");
			}
			else if(value instanceof JSON[]){
				aux = aux.concat("[");
				JSON[] jsonArray = (JSON[]) value;
				for(int i=0; i<jsonArray.length; i++){
					aux = aux.concat(jsonArray[i].toString());
					if(i<jsonArray.length-1){
						aux = aux.concat(",");
					}
				}
				aux = aux.concat("]");
			}
			else {
				aux = aux.concat(value.toString());
			}
			if(k<keys.length-1){
				aux = aux.concat(",");
			}
		}
		aux = aux.concat("}");
		return aux;
	}
	
	public static void main(String[] args) throws Exception {
		
		String s1 = "uno";
		String s2 = "dos";
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append("Hi ");
			sb.append(s1);
			sb.append("; Hi to you ");
			sb.append(s2);
			String s = sb.toString();
		}
		long end = System.currentTimeMillis();
		System.out.println("StringBuffer = " + ((end - start))
				+ " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String s = "Hi ".concat(s1);
			s = s.concat("; Hi to you ");
			s = s.concat(s2);
		}
		end = System.currentTimeMillis();
		System.out.println("Concat = " + ((end - start))
				+ " millisecond");
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String s = "Hi ".concat(s1).concat("; Hi to you ").concat(s2);
		}
		end = System.currentTimeMillis();
		System.out.println("Concat en line = " + ((end - start))
				+ " millisecond");
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String s = "Hi "+s1+"; Hi to you "+s2;
		}
		end = System.currentTimeMillis();
		System.out.println("Concatenation = " + ((end - start))
				+ " millisecond");
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			String s3 = String.format("Hi %s; Hi to you %s", s1, s2);
		}
		end = System.currentTimeMillis();
		System.out.println("Format = " + ((end - start)) + " millisecond");
	}
}
