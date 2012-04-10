package eu.blky.wdb;

import gform.GForm;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import cc.co.llabor.cache.Manager;

import net.sf.jsr107cache.Cache;

/** 
 * <b>primary interface for interaction with CacheFS-WDB persistence.</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  25.02.2012::21:55:50<br> 
 */
public class WDBOService {
	
	static final WDBOService me = new WDBOService();

	public static WDBOService getInstance() {
		return me;
	}
	
	private Wdb nullObject = null;
	private static Logger log = Logger.getLogger(GForm.class.getName());
	private Wdb getNullObject(){
		nullObject = nullObject ==null?new Wdb("nullObject"):nullObject ;
		return nullObject ;
	}
	/**
	 * @deprecated use Constructor .OR. Wbd-target object as one more parm
	 * 
	 * 
	 * @author vipup
	 * @param key
	 * @return
	 */
	public Category createCategory(String key ) {  
		Wdb nullObject2 = getNullObject();
		UID uid = nullObject2.getUID();
		log.fine(""+uid);
		
		return createCategory(key, nullObject2);
	}
	public Category createCategory(String key, Wdb forObject) { 
		Cache categoryCache = getCategoryCache();
		Category retval  = null;
		synchronized(Cache.class){
			Properties catTmp = (Properties) categoryCache.get(key+".properties" );
			retval = catTmp==null? new Category(key):new Category(catTmp);
			if (retval ==null||catTmp==null){
				retval = new Category(key);
				Properties properties = retval.toProperties();
				try{
					String newUID = forObject.uid.toString();
					String oldO = (String) properties.put("o", newUID);
					String bakKey = "~o" ;
					String suffix = ", ";
					while(oldO!=null){
							properties .put("o", oldO  + suffix  + newUID);
							
							oldO = (String) properties.put(bakKey, oldO);
							bakKey = "~"+bakKey;
					}
					 
					
				}catch(NullPointerException e){
					//e.printStackTrace();
				}
				String key2 = key+".properties";
				categoryCache.put(key2, properties);
			}
		}
		return retval ; 
	}

	private Cache getCategoryCache() { 
		return  getCache("Category"); 
	}
	private Cache getCache() { 
		return  getCache("Obj"); 
	}	
	

	private Cache getCache(String cacheNS) {  
		return Manager.getCache(WDBOService.class.getName()+"/"+cacheNS); 
	}

	public List<Category> getCategories() { 
		List<Category> retval = new ArrayList<Category>();
		Cache categoryCache = getCategoryCache();
		Set<String> keys = categoryCache.keySet();
		for (String key:keys ){
			if ("nullObject.properties".equals(key))continue; // ignore NULL
			Properties o = (Properties) categoryCache.get(key);
			Category catTmp = new  Category(o);
			retval.add(catTmp );
		}
		return retval ; 
	}

	public LinkedList<Wdb> getObjects() { 
		Set<String> keySet = getCache().keySet();
		LinkedList<Wdb> retval = new LinkedList( );
		
		for (String key:keySet){
			Properties  o = (Properties) getByUID(key);//Cache cacheTmp = getCache(); cacheTmp.get(key);
			if (o == null) {
				System.out.println("NULL@"+key);
				continue;
			}
			Wdb e = new Wdb (o); 
			if (key.replace("=..=", ":").indexOf(e.getUID().toString())!=0){ // base64->UID check
//				String id = key;
//				int endIndex = id .indexOf(".properties");
//				id = id .indexOf(".properties")>0?id .substring(0, endIndex ):id;
//				id = o.getProperty("id");
//				e.setId(id);
				throw new WdbException("for Object#"+key+"# expected UID:"+ (e.getUID().toString())  );
			}
			retval .add(e );
		}
		return retval ;
	}

	/**
	 * seach all Objects by Category
	 * 
	 * @author vipup
	 * @param categoryPar
	 * @return
	 */
	public LinkedList<Wdb> getObjects(String categoryPar) { 
		LinkedList<Wdb> retval = new LinkedList<Wdb>();
		LinkedList<Wdb> objects = getObjects();
		Category catTmp = this.getCategory(categoryPar);
		for (Wdb next: objects){			
			Set<Wdb> categoriesTmp = next.getCategories();
			// workaroud for contains!!!! 8-EEE
			for (Wdb cToCheck:categoriesTmp){
				if  (cToCheck == null) continue;
				if  (catTmp == null) continue;
				String _1 = cToCheck._();
				String _2 = catTmp._();
				if  (_1 == null) continue;
				if  (_2 == null) continue;				
				if (_1.equals(  _2)  || cToCheck._() == catTmp._() || 1==2){
					retval.add(next);
					break;
				}
			}
			
		}
		return retval;
		
	}

	private Category getCategory(String categoryPar) {
		Category retval = null;
		for (Category cTmp :getCategories()){
			if (cTmp == null) continue;
			String catNameTmp = cTmp._();
			if (catNameTmp  == null) continue;
			if (catNameTmp.equals(categoryPar)){
				retval = cTmp;
				break;
			}
		}
		return retval;
	}

	public Wdb getObject(int i) {
		return (Wdb) getObjects() . toArray()[i];
	}

	/**
	 * make synch the Object with cache-persistence.
	 * 
	 * @author vipup
	 * @param oPar
	 */
	public void flush(Wdb oPar) {
		Cache cTmp = getCache()  ;
		UID uid = oPar.getUID();
		String key = uid.toString() +".properties";
		Object o = cTmp.get(key);
		// check for old instances
		String oldKey = oPar.getId()+".proper"+"ties";
		Object oldTmp = cTmp.get(oldKey);
				
		//  0 0 - persist all dependencies also
		if (o==null && oldTmp == null){ // very 1st store
			Properties oParAsProperties = oPar.toProperties(); 
			cTmp.put( key , oParAsProperties  );
		// 0 1	|| // 1 1  	-> merge
		}else if (o==null && oldTmp != null && oldTmp instanceof Properties){ // MERGE: replace Old + New = New ==>> Old
			Wdb toMerge = new Wdb( (Properties)oldTmp );
			for (String propName:oPar.getPropertyNames()){
				toMerge.setProperty( propName, oPar.getProperty(propName));
			}
			for (String propName:toMerge.getPropertyNames()){
				toMerge.setProperty( propName, toMerge.getProperty(propName));
			} 
			Properties mergedProperties = toMerge.toProperties();
			cTmp.put( oldKey , mergedProperties  ); 
		// 1 0 -- fully new Object 	
		}else{ // SAME as 0 0 
			Properties properties = oPar.toProperties();
			cTmp.put(key,  properties);
		}
	}

	

	public void remove(Wdb oPar) {
		Cache cTmp = getCache()  ;
		Object id = oPar.getId()+".properties";
		cTmp.remove(id);
	}
	public void removeCategory(Wdb oPar) {
		Cache cTmp = getCategoryCache()  ;
		Object id = oPar._()+".properties";
		cTmp.remove(id);
	}
	public Object  getByUID(Object key) {
		Cache cacheTmp = getCache(); 
		String keyTmp = (""+key).replace("\\", "");
		keyTmp = keyTmp.indexOf(".properties")>0?keyTmp:(keyTmp+".properties");
		keyTmp  = keyTmp .replace("\'", "");
		Object retval = cacheTmp.get(keyTmp);
		return retval ;
	} 
	
}


 