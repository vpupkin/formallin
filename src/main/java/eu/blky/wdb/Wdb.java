package eu.blky.wdb;
 
import gform.GForm;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.logging.Logger;

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

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 453774634348463145L;
	private static final Set<Wdb> EMPTY_SET = Collections.unmodifiableSet( new HashSet<Wdb>());
	private static final boolean TRACE = false;
	private static final List<Wdb> EMPTY_LIST = Collections.unmodifiableList ( new ArrayList<Wdb>());
	public static final boolean LAZY = true;
	protected String oName;
	private static long cluniqueId=0;
	protected String id = ""+cluniqueId++;
	private volatile Properties tempToIni = null;
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
		//String catAsStr = this.categories.size()>0?":"+this.categories.toString():"";
		String sbAsString = sb.toString().length()>0?"={"+sb+"}":"";
		return oName+/*catAsStr+*/sbAsString;
	}

	protected Wdb(){
		// 
	}
	
	@Override
	public boolean contains(Object o){
		if (o == this) return true;
		int index = indexOf( o);
		// ala compare with non-persisted category LIKE this.equals(o)
		if (this instanceof Category && o instanceof Category && this.oName.equals( ((Category )o).oName)){
			index = 0;
		}
		return index>=0;
	}	
	
	/**
	 * @deprecated
	 * @author vipup
	 * @param oName
	 */
	public Wdb(String oName) {
		this.oName = oName;
		this.pushCategory(oName);
	}

	
	
	
	public Wdb(Properties o) {
		if (!LAZY){
			init(o);
		}else{
			// store the Init-info to temporary...
			tempToIni  = o;
		}
	}

	public Wdb(String valuePar, Category theC) {
		this.oName = valuePar;
		this.pushCategory(theC); 
	}

	protected void init(Properties o) {
		//init categories
		String catsTmp = o.getProperty("categories");
		if (null!= catsTmp) {
			String[] split = catsTmp .split(",");
			for (String cat :split){ 
				pushCategory( cat  );
			}
		} 
		//init properties
		for (Object pkey:o.keySet()){
			// SKIP reserved
			if ("categories".equals(pkey))continue;
			if ("id".equals(pkey))continue;
			if ("oName".equals(pkey))continue;
			// fill the Obj
			Object value = o.get(pkey);
			if (value instanceof Wdb){
				if (TRACE)this.props.put(""+pkey, (Wdb) value);
			}else if (value instanceof String){
				if (TRACE)System.out.println(" loadind...");
				for (String uid:((String) value).split(",")){
					try{
						if (TRACE)System.out.println(" :"+uid);
						WDBOService ddboService = WDBOService.getInstance();
						String key = uid.substring(1,uid.length()-1 );
						Object toPushProps =  ddboService.getByUID(key); // "[" "]"
						Wdb toPush = new Wdb((Properties)toPushProps);
						this.setProperty(""+pkey, toPush);//!!  WRONG is :: this.props.put(""+pkey, toPush);
					}catch(Exception e){
						if (TRACE)e.printStackTrace();
					}
				}
				 
			}
		}
	}

	public void initId(Properties o) {
		this.oName = o.getProperty("oName");
		this.id=  o.getProperty("id") ;
		this.setId(id); // synch with uid
	}

	private void pushCategory(String catPar) {
		WDBOService ddboService = WDBOService.getInstance();
		Category catTmp = ddboService.createCategory(catPar, this);
		pushCategory(catTmp);
	}
	private void pushCategory(Category catPar) { 
		//checkLazyInit();
		getId();
		if (this.categories ==null){
			this.categories  =  catPar ;
		}else{
			if (this.categories.indexOf(catPar)>=0){//(this.categories  .contains(catPar )){
				// DO nothing
			}else
				this.categories.add(catPar);
		} 
	}
	
	@Override
	public boolean add(Wdb e) {
		return super.add(e);
	}
	
	@Override
	public int indexOf(Object oPar) {
		int retval = -1;
		if (oPar instanceof Wdb && oPar != null)
			for (int i = 0; i < this.size(); i++) {
				Wdb oTmp = this.get(i);
				if (oTmp != null && oPar != null)
					if (oTmp._() != null)
						if (((Wdb) oPar)._() != null)
							if (oTmp._().equals(((Wdb) oPar)._()))
								if (oTmp.equals(oPar)) {
									retval = i;
									break;
								}
			}
		return retval;
	}

	Map<String,Wdb> props = new HashMap<String, Wdb>();
	private Category  categories = null;
	public UID uid = null;
	private static Logger log = Logger.getLogger(Wdb.class.getName());
	
	public void setProperty(String propertyName, String valuePar) {
		if (valuePar == null) return; // nothing to do
		this.uid = null; // reset uid for any Object-change  
		WDBOService ddboService = WDBOService.getInstance();
		Category theC = ddboService.createCategory(propertyName, this);
		Wdb wdbTmp = new Wdb(valuePar,theC );		
		setProperty(propertyName, wdbTmp);
	}
	
	@Override
	public int size(){
		return super.size() +1;
	}
	
	public List<String> getPropertyNames(){
		checkLazyInit(); 
		// TODO toooooo dirty impl...!
		return Arrays.asList(  props.keySet().toArray(new String[]{}));
		
	}
	
	public void setProperty(String key, Wdb valuePar) {
			
		if (valuePar == null )return; // TODO
		if (this.toString().hashCode() == valuePar.toString().hashCode()){
			throw new WdbException("Wrong Parent - (itself)_!"); // TODO
		} 
		//try{// try //// - shifting
			Wdb oldTmp = props.put(key, valuePar);
			if (oldTmp!=null){
				valuePar.add(oldTmp); 
			}
//		}	catch(Exception e){
//			e.printStackTrace();
//		}
		 
	}
	private int diffCategory(Wdb a, Wdb b) {
		int retval = 0;
		Wdb  categoriesA = a.getCategories();
		Wdb  categoriesB = b.getCategories();
		for (Wdb cA:categoriesA){
			if(categoriesB.contains(cA))retval++;
		}
		return retval;
	}
	
	public Wdb getCategories() { 
		checkLazyInit(); 
		return this.categories  ;
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
	
	
	public void delCategory(Category theC) { 
		checkLazyInit();
		if (this.categories ==null){
			return; // ignore
		}else{
			if (this.categories.indexOf(theC)>=0){//(this.categories  .contains(catPar )){
				this.categories.remove(theC);				
			}else if ( 	this.categories.equals( theC ) ){
				this.categories = null;
			}
		} 		
	} 
	
	public void addProperty(String propertyName, String valuePar) {
		this.setProperty(propertyName, valuePar );
	}

	/**
	 * return all objects with the same Category-Set with existing property with name == ${key}
	 * 
	 * @author vipup
	 * @param key
	 * @return
	 */
	public Wdb getProperty(String key) {
		checkLazyInit(); 
		Category catTmp = new Category(key);
		Wdb retval = null;
		try{
			retval =  this.element().getProperty(key);
			Properties properties = retval.toProperties(); 
			Wdb returnContainer = new Wdb(properties);	
			//"X-Get" - pushing requested propertyName into Temp-Obj
			//returnContainer.setProperty("X-Get", key) ;
			Wdb myProp = this.props.get(key);
			returnContainer .add(myProp );			
			retval  =returnContainer ; 
		}catch(Exception e){
			retval = this.props.get(key);
		} 
		return retval;

	}

	/**
	 * gives back always inited value (in cmp with toString() for ex. )
	 * @author vipup
	 * @return
	 */
	public String _() { 
		checkLazyInit();
		if (this.props.containsKey("X-Get")){
			return _(0);
		}
		return oName;
		
	}

	private String _(int i) {  
		String xGet = this.props.get("X-Get")._();
		Wdb wdb = this.get(i);
		String retval =  (wdb == null) ?oName : wdb.props.get(xGet)._() ;
		return retval;  
	}

	public  Wdb  getProperties(String key) { 
		Wdb retval = getProperty(key);
		return retval ;
		
	}

	void setId(String key) {
		this.id = key; 
		try {    
			char[] charArray = key.replace("\\=", "=").toCharArray();
			byte[] decodedTmp = Base64Coder.decode(charArray);
			InputStream in64 = new ByteArrayInputStream(  decodedTmp);
			DataInput stored64 = new DataInputStream(in64 );
			this.uid =  UID.read(stored64 ); 
			this.id = this.uid.toString();
			if (TRACE)System.out.println("key:"+key+"->"+uid+"-->> keyID--|"+ this.id );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public Wdb get(int index) {
		if (index == 0) return this;
		else return super.get(index-1);
	}
	
	
	private void checkLazyInit(){
		getId();
		if (LAZY   && tempToIni != null && tempToIni.getProperty("initedAt") == null) {
			init(tempToIni);
			//initId(tempToIni);
			tempToIni.setProperty("initedAt", ""+System.currentTimeMillis());//tempToIni = null;			
		}
	}
	
	public Object getId() {
		if (LAZY   && tempToIni != null) {
			//init(tempToIni);
			initId(tempToIni);
			//tempToIni = null;
		}
		return this.id ;
	}

	public UID getUID() {
		checkLazyInit();
		synchronized (UID.class) {
			if (this.uid == null){
				synchronized (UID.class) {
					this.uid =  new UID();
					WDBOService ddboService = WDBOService.getInstance();				
					ddboService .flush(this);
				}
			} 
		}
		return this.uid; 
	}

	public Properties toProperties() {	
			checkLazyInit();
			Properties retval = new Properties();
			//retval.putAll(this.props);
			for (String key:props.keySet()){
				Wdb the1st = props.get(key);				
				String val = "["+the1st.getUID()+"]";//"["+the1st._()+"]";
				String prefix = ",";
				if (the1st .size()>1){
					for (int i=1;i<the1st .size();i++){
						Wdb theNext = the1st .get(i-1);
						String idAsStr = "["+theNext.getUID()+"]";
						if (val.indexOf(idAsStr)>=0)continue; // DISTINCT!
						String toConcat = prefix + idAsStr;
						val+=toConcat;//"["+theNext._()+"]";
					}
				}
				retval.put(key, val );
			}
			String categoriesStr = "";
			String prefix = "";
			if (categories!=null){
				if (categories.size()>0)	{
					categoriesStr +=categories._();
					prefix =", ";
					for (Wdb cat:categories ){
						categoriesStr  +=  prefix;
						categoriesStr  +=  cat._();
						prefix =", ";
					}	 
				}
				if (categoriesStr.length()>0){
					retval.put("categories", categoriesStr  );
				}
			}
			if (null != this.oName){
				retval.put("oName", this.oName);
			}
			retval.put("id", toBase64 () );
			return retval; 		
	}

	private String  toBase64() {
		//this.uid  =  new UID( );// UID.read(in);//
		String retval = null; 
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutput out = new DataOutputStream(bout );
		try {
			this.uid .write(out );
			bout.flush();
			byte[] data =bout.toByteArray();
			String uid64 = new String( Base64Coder.encode(data) );
			retval = uid64 ;
		} catch (NullPointerException e) {
			this.uid = getUID();
			retval = toBase64() ;
		} catch (IOException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retval;
	}

	public List<Wdb> getCategoriesAsList() {
		checkLazyInit();
		if ( this.categories == null) return EMPTY_LIST;
		List<Wdb> retval = //this.categories.toSet();
			new ArrayList<Wdb>();
		int size = this.categories.size();
		for (int i=0;i<size;i++){
			Wdb e = this.categories.get(i);
			retval .add(e );
		}
		return retval;
	}
	
 
}


 