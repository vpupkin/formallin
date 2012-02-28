package eu.blky.wdb;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.co.llabor.cache.Manager;

import net.sf.jsr107cache.Cache;

/** 
 * <b>Description:TODO</b>
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

	public Category createCategory(String key) { 
		
		Category retval = (Category) getCategoryCache().get(key );
		if (retval ==null){
			retval = new Category(key);
			getCategoryCache().put(key, retval);
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
		Set<String> keys = getCategoryCache().keySet();
		for (String key:keys ){
			Category catTmp = (Category) getCategoryCache().get(key);
			retval.add(catTmp );
		}
		return retval ; 
	}

	public LinkedList<Wdb> getObjects() { 
		Set<String> keySet = getCache().keySet();
		LinkedList<Wdb> retval = new LinkedList( );
		for (String key:keySet){
			Wdb e = (Wdb) getCache().get(key);
			e.setId(key);
			retval .add(e );
		}
		return retval ;
	}

	public LinkedList<Wdb> getObjects(String categoryPar) { 
		LinkedList<Wdb> retval = new LinkedList<Wdb>();
		for (Wdb next: getObjects()){
			Category catTmp = this.getCategory(categoryPar);
			if (next.getCategories().contains(catTmp )){
				retval.add(next);
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

	public void flush(Wdb translator) {
		Cache cTmp = getCache()  ;
		UID uid = new UID();
		cTmp.put( uid.toString()  , translator );
	}

	public void remove(Wdb oPar) {
		Cache cTmp = getCache()  ;
		cTmp.remove(oPar.getId());
	}

}


 