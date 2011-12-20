package eu.blky.wdb;

import java.util.Set;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  19.12.2011::21:35:16<br> 
 */
public class Category extends Wdb{

	

	public Category(String oName) {
		super(oName);  
	}

	// n-to-m relation
	private Set<Wdb> instances;
	
	
	public void addInstance(Wdb wdb) {
		this.instances.add(wdb);
	}

}


 