package eu.blky.wdb;
 
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private static long cluniqueId=0;
	private String id = ""+cluniqueId++;
	public String toString(){
		StringBuffer sb = new StringBuffer();
		String EOP  = "";
		
		for (String nameOfProp:this.props.keySet()){
			List<Wdb> toPrint = new ArrayList<Wdb>();  
			Wdb nextProp = this.props.get(nameOfProp); 
			toPrint .add(nextProp);
			for (int i=0;i<nextProp.size()-1;i++){
				Wdb e = nextProp.get(i);
				toPrint.add(e);
			}
//			System.out.println("<toprint hash="+this.id+" id=\'"+this.oName+"\'>");
//			System.out.println(toPrint);
//			System.out.println("</toprint>");
			sb .append(EOP);
			sb .append("\n\t");
			sb.append(nameOfProp);
			String LLL = "[";// ` BEGIN {{{ [ <  6  (:
			String RRR = "]";// ´ end   }}} ] >  9  :) 
			String prefix = "==\n\t"+LLL;
			String EOV = "";
			for(Wdb nextWdb:toPrint){
				sb.append(EOV);
				sb.append(prefix);
				if (nextWdb ==null) continue;
				String propsAsString = nextWdb.toString();
				for (String s:propsAsString .split("\n")){  
					sb .append(s); 
					EOV =  "\n\t";
					sb.append(EOV);
				} 

				EOV = RRR+"";
				sb.append(EOV);
				prefix =  ";\n\t"+LLL;
				
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
	public UID uid = null;
	
	public void setProperty(String propertyName, String valuePar) {
		this.uid = null; // reset uid for any Object-change
		Wdb wdbTmp = new Wdb(valuePar);
		setProperty(propertyName, wdbTmp);
	}
	
	@Override
	public int size(){
		return super.size() +1;
	}
	
	public void setProperty(String propertyName, Wdb valuePar) {
//		Wdb oldTmp  = props.put( propertyName, wdbTmp );
//		if (oldTmp!=null && !oldTmp.equals( wdbTmp )){
//			wdbTmp  .add(oldTmp  );
//		}		
		if (valuePar == null )return; // TODO
		if (this.toString().hashCode() == valuePar.toString().hashCode()){
			throw new WdbException("Wrong Parent - (itself)_!"); // TODO
		}
		{ //if (valuePar instanceof  Wdb)
			// compare own categories with valuePar
			Wdb toAssignValue = (Wdb)valuePar;
			if (1==2 && diffCategory(this,toAssignValue)==0){
				throw new WdbException("identicall category-Set !");
			}else{
				Wdb oTmp = props.get(propertyName);
				if (oTmp == null){
					oTmp = toAssignValue;
				}else{
					oTmp.add(toAssignValue);
				}
				props.put( propertyName, oTmp); 
			}
				
		} 
	}
	private int diffCategory(Wdb a, Wdb b) {
		int retval = 0;
		Set<Category> categoriesA = a.getCategories();
		Set<Category> categoriesB = b.getCategories();
		for (Category cA:categoriesA){
			if(categoriesB.contains(cA))retval++;
		}
		return retval;
	}
	
	public Set<Category> getCategories() {
		return this.categories;
	}
	
	public void addCategory(Category theC) { 
		this.categories.add(theC);
	} 
	
	public void addProperty(String propertyName, String valuePar) {
		this.setProperty(propertyName, valuePar );
	}

	/**
	 * return all objects which has property with name == "key"
	 * 
	 * @author vipup
	 * @param key
	 * @return
	 */
	public Wdb getProperty(String key) {
		Wdb retval = this.props.get(key);      
		Iterator<Wdb> i = this.iterator();
		for (;i.hasNext();){
			Wdb next = i.next();
			if (next.getProperty(key)!=null){
				retval.add(next);
			}
		}
		return retval;

	}

	public String _() { 
		if (this.props.containsKey("X-Get")){
			return _(0);
		}
		return oName;
		
	}

	private String _(int i) {  
		String xGet = this.props.get("X-Get")._();
		Wdb retval = this.get(i).props.get(xGet) ;
		return retval._();  
	}

	public  Wdb  getProperties(String key) { 
		Wdb retval = getProperty(key);
		return retval ;
		
	}

	void setId(String key) {
		this.id = key;
	}

	public Object getId() {
		return this.id ;
	}

	public UID getUID() {
		synchronized (UID.class) {
			this.uid = this.uid == null? new UID():this.uid ;
		}
		return this.uid; 
	}
	
 
}


 