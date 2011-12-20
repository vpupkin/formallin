package eu.blky.wdb;
 
import java.util.HashMap;
import java.util.HashSet;
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
public class Wdb {

	private String oName;
	public String toString(){
		return oName+":"+this.categories+"={"+props+"}";
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
		if (valuePar.equals(this))throw new WdbException("Wrong Parent - (itself)_!");
		if (valuePar instanceof  Wdb) {
			// compare own categories with valuePar
			Wdb toAssignValue = (Wdb)valuePar;
			if (1==2 && diffCategory(this,toAssignValue)==0){
				throw new WdbException("identicall category-Set !");
			}else{
				props.put( propertyName, toAssignValue);
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


 