package eu.blky.wdb;

import gform.GForm;

import java.util.Map;
import java.util.logging.Logger;

import cc.co.llabor.cache.BasicCacheFactory;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  05.03.2012::11:59:43<br> 
 */
public class WdbCacheFactory extends BasicCacheFactory implements CacheFactory{

	private static Logger log = Logger.getLogger(WdbCacheFactory.class.getName());

	@Override
	public Cache createCache(Map env) throws CacheException {
		return super.createCache(env);
	}

}


 