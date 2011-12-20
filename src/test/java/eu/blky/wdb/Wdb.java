package eu.blky.wdb;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map; 
import java.util.Set;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  19.12.2011::14:31:05<br> 
 */
public class Wdb extends LinkedList<Wdb>{

	private String oName;
	public String toString(){
		StringBuffer sb = new StringBuffer();
		String EOP  = "";
		for (String nameOfProp:this.props.keySet()){
			Wdb nextProp = this.props.get(nameOfProp);
			List<Wdb> toPrint = new ArrayList<Wdb>();  
			toPrint .add(nextProp);
			for (int i=0;i<nextProp.size();i++){
				toPrint.add(nextProp.get(i));
			}
			
			sb .append(EOP);
			sb .append("\n\t");
			sb.append(nameOfProp); 
			String prefix = "=\u0060";
			String EOV = "";
			for(Wdb nextWdb:toPrint){
				sb.append(EOV);
				sb.append(prefix);
				String propsAsString = nextWdb.toString();
				for (String s:propsAsString .split("\n")){  
					sb .append(s); 
					EOV = "\u00B4\n\t";
					sb.append(EOV);
				} 
				prefix =  ";\n\t\u0060";
				
			} 
			EOP = (",");
		}
		String catAsStr = this.categories.size()>0?":"+this.categories.toString():"";
		String sbAsString = sb.toString().length()>0?"={"+sb+"}":"";
		return oName+catAsStr+sbAsString;
	}

	public Wdb(String oName) {
		this.oName = oName;
	}

	Map<String,Wdb> props = new HashMap<String, Wdb>();
	private Set<Category> categories = new HashSet<Category>();
	
	public void setProperty(String propertyName, String valuePar) {
		props.put( propertyName, new Wdb(valuePar));
	}
	public void setProperty(String propertyName, Object valuePar) {
		if (valuePar == null )return; // TODO
		if (this.toString().hashCode() == valuePar.toString().hashCode()){
			throw new WdbException("Wrong Parent - (itself)_!");
		}
		if (valuePar instanceof  Wdb) {
			// compare own categories with valuePar
			Wdb toAssignValue = (Wdb)valuePar;
			if (1==2 && diffCategory(this,toAssignValue)==0){
				throw new WdbException("identicall category-Set !");
			}else{
				Wdb oTmp = props.get(propertyName);
				if (oTmp == null){
					oTmp = toAssignValue;
				}else{
					oTmp.addLast(toAssignValue);
				}
				props.put( propertyName, oTmp); 
			}
				
		}
		this.setProperty(propertyName,""+ valuePar);
	}
	private int diffCategory(Wdb a, Wdb b) {
		int retval = 0;
		Set<Category> categoriesA = a.getCategories();
		Set<Category> categoriesB = b.getCategories();
		for (Category cA:categoriesA)
			if(categoriesB.contains(cA))retval++;
			
		return retval;
	}
	
	public Set<Category> getCategories() {
		return this.categories;
	}
	
	public void addCategory(Category theC) {
		theC.addInstance(this);
		this.categories.add(theC);
	} 
	
	public void addProperty(String propertyName, String valuePar) {
		this.setProperty(propertyName, valuePar );
	}

}


 