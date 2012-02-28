package eu.blky.wdb;

import java.util.HashSet;
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

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 6581281116649476813L;
	private Category parent;


	public Category(String oName) {
		super(oName);  
	}

	public Category(Category parentCategory, String oName) { 
		super(oName); 
		if (this.parent==null){
			this.parent=parentCategory; 
		}else{
			this.parent.add(parentCategory);
		}
	}
  
 

}


 