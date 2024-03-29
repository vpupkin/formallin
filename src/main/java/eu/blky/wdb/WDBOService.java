package eu.blky.wdb;

import gform.GForm;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Date;
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
	private static final long MAX_CACHED_AGE = 60 * 10 * 1000; // 10 minutes

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
		key = key.replace("\\","").trim();
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
			//  validate the new Object
			if (!Wdb.LAZY && key.replace("=..=", ":").indexOf(e.getUID().toString())!=0){ 
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
		Cache searchCache = getCache("SearchCache");
		LinkedList<Wdb> retval = new LinkedList<Wdb>();
		Properties cachedSearch = (Properties) searchCache.get(categoryPar+".properties");
		do {
			if (cachedSearch != null){
				Date timestampTmp = new Date(Long.parseLong(cachedSearch.getProperty("timestamp")));
				if ((System.currentTimeMillis() - timestampTmp.getTime() )>MAX_CACHED_AGE) break;  // this entry will be autoexpired after 10 minues
				String ids = cachedSearch.getProperty( categoryPar);
				for (String idTmp :ids.split(",")){
					Object oTmp = getByUID(idTmp);
					if (oTmp instanceof Properties){
						oTmp = new Wdb((Properties)oTmp);
						retval.add((Wdb) oTmp);
					}					
				}
				return retval;
			}
		}while (1==2);
		cachedSearch  = new Properties();
		
		 
		LinkedList<Wdb> objects = getObjects();
		Category catTmp = this.getCategory(categoryPar);
		for (Wdb next: objects){			
			List<Wdb> categoriesTmp = next.getCategoriesAsList();
			// Workaround for contains!!!! 8-EEE
			if (categoriesTmp.contains(catTmp) && !retval.contains(next)){
				retval.add(next);
			}
		}
		 
		String indexTmp = "";
		String prefix = "";
		for (Wdb theNext:retval){
			indexTmp  +=prefix; 
			indexTmp +=theNext.getId();
			prefix = ", ";
		}
		cachedSearch .setProperty(categoryPar, indexTmp);
		cachedSearch .setProperty("timestamp", ""+System.currentTimeMillis());
		searchCache.put(categoryPar+".properties", cachedSearch );
		
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
			Object idTMP1 = oPar.getId();
			String newidTMP2 = oParAsProperties.getProperty("id");
			System.out.println("idTMP(1):"+idTMP1 +" ====>>>> "+newidTMP2);
			oPar.setId(newidTMP2 );
			oPar.injectProperties(oParAsProperties);
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
			Properties oParAsProperties = oPar.toProperties();
			cTmp.put(key,  oParAsProperties);
			Object idTMP1 = oPar.getId();
			String newidTMP2 = oParAsProperties.getProperty("id");
			System.out.println("idTMP[1]:::"+idTMP1 +" ====>>>> "+newidTMP2);
			oPar.setId(newidTMP2 );
		}
	}

	

	public void remove(Wdb oPar) {
		Cache cTmp = getCache()  ;
		Object id2 = oPar.getId();
		Object id = id2+".properties";
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
		keyTmp  = keyTmp .replace("\\", "");
		keyTmp  = keyTmp .replace(":", "=..=");
		keyTmp = keyTmp.trim();
		Object retval = cacheTmp.get(keyTmp);
		return retval ;
	}
	
	public void resetCategories() {
		 Cache cTmp = getCategoryCache();
		 cTmp.clear();
	} 
	public void resetSearchCache() {
		 Cache cTmp = getCache("SearchCache");
		 cTmp.clear();
	} 
	
}


 