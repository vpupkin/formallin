package eu.blky.wdb;
 
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map; 
import java.util.Properties;
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

	protected String oName;
	private static long cluniqueId=0;
	protected String id = ""+cluniqueId++;
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
			String RRR = "]";// � end   }}} ] >  9  :) 
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
		//String catAsStr = this.categories.size()>0?":"+this.categories.toString():"";
		String sbAsString = sb.toString().length()>0?"={"+sb+"}":"";
		return oName+/*catAsStr+*/sbAsString;
	}

	public Wdb(String oName) {
		this.oName = oName;
	}

	public Wdb(Properties o) {
		init(o);
	}

	protected void init(Properties o) {
		this.oName = o.getProperty("oName");
		this.id=  o.getProperty("id") ;
		String catsTmp = o.getProperty("categories");
		if (null!= catsTmp)
		for (String cat :catsTmp .split(",")){
			WDBOService ddboService = WDBOService.getInstance();
			Category catTmp = ddboService.createCategory(cat);
			pushCategory( catTmp );
		} 
	}

	private void pushCategory(Category catTmp) {
		if (this.categories ==null){
			this.categories  =  catTmp ;
		}else{
			this.categories  .add(catTmp);
		}
	}

	Map<String,Wdb> props = new HashMap<String, Wdb>();
	private Category  categories = null;
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
		Set<Wdb> categoriesA = a.getCategories();
		Set<Wdb> categoriesB = b.getCategories();
		for (Wdb cA:categoriesA){
			if(categoriesB.contains(cA))retval++;
		}
		return retval;
	}
	
	public Set<Wdb> getCategories() {
		Set<Wdb> retval = this.categories.toSet();
		return retval ;
	}
	
	protected Set<Wdb> toSet() {
		Set<Wdb> retval = new HashSet<Wdb>();
		retval .add(this);
		if (this.size()>1){
			Iterator<Wdb> i = this.iterator();
			for (;i.hasNext();){
				retval.add(i.next());
			}
		}
		
		return retval ;
	}

	public void addCategory(Category theC) { 
		this.pushCategory( theC );
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

	public Properties toProperties() {		 
			Properties retval = new Properties();
			//retval.putAll(this.props);
			for (String key:props.keySet()){
				retval.put(key, props.get(key)._());
			}
			String categoriesStr = "";
			String prefix = "";
			if (categories!=null)
			for (Wdb cat:categories ){
				categoriesStr  +=  prefix;
				categoriesStr  +=  cat._();
				prefix =", ";
			}			
			retval.put("categories", categoriesStr  );
			retval.put("oName", oName);
			retval.put("id", ""+id);
			return retval; 		
	}
	
 
}


 