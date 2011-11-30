package gform;

public class Text {
	static public final String EOL = "\r\n";
	
	private StringBuffer context;
	
	public Text(){
		context = new StringBuffer();
	}
	
	public void append(String arg){
		context.append(arg);
	}
	
	public void appendLine(String arg){
		context.append(arg);
		context.append(EOL);
	}
	
	public String toString(){
		return context.toString();
	}
	
}
