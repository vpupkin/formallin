package eu.blky.wdb;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
		System.out.println(uid);
		
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
		Cache cacheTmp = getCache();
		for (String key:keySet){
			Properties  o = (Properties) cacheTmp.get(key);
			Wdb e = new Wdb (o); 
			String id = key;
			int endIndex = id .indexOf(".properties");
			id = id .indexOf(".properties")>0?id .substring(0, endIndex ):id;
			e.setId(id);
			retval .add(e );
		}
		return retval ;
	}

	public LinkedList<Wdb> getObjects(String categoryPar) { 
		LinkedList<Wdb> retval = new LinkedList<Wdb>();
		LinkedList<Wdb> objects = getObjects();
		Category catTmp = this.getCategory(categoryPar);
		for (Wdb next: objects){			
			Set<Wdb> categoriesTmp = next.getCategories();
			// workaroud for contains!!!! 8-EEE
			for (Wdb cToCheck:categoriesTmp){
				if (cToCheck._().equals(  catTmp._())  || cToCheck._() == catTmp._() || 1==2){
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
			if (cTmp._().equals(categoryPar)){
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
		Object o = cTmp.get(uid.toString());
		if (o==null){ // very 1st store
			Properties oParAsProperties = oPar.toProperties();
			String key = uid.toString() +".properties";
			cTmp.put( key , oParAsProperties  );
		}else{
			// this object is already in store - update by storing the same obj with new uid, if some change occur 
			// TODO +++ ??
			if (!oPar.equals(o)){
				synchronized (UID.class) {
					oPar.uid = new UID();
					flush(oPar);
				}
			}	
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
	
}


 