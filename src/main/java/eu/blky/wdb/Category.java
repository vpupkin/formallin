package eu.blky.wdb;
 
import gform.GForm;

import java.util.Properties; 
import java.util.logging.Logger;

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
	private static Logger log = Logger.getLogger(Category.class.getName());


	public Category(String oName) {
		this(null, oName);
	}

	public Category(Category parentCategory, String oName) { 
		//super(oName); 
		this.oName = oName;
		if (this.parent==null){
			this.parent=parentCategory; 
		}else{
			this.parent.add(parentCategory);
		}
	}

	public Category(Properties catTmp) {
		super(catTmp);
	}
  
	/**
	 * 
	 * @author vipup
	 * @param o
	 */
	@Override
	protected void init(Properties o) {
		this.oName = o.getProperty("oName");
		this.id=  o.getProperty("id") ;
//		String catsTmp = o.getProperty("categories");
//		if (null!= catsTmp)
//		for (String cat :catsTmp .split(",")){
//			WDBOService ddboService = WDBOService.getInstance();
//			Category catTmp = ddboService.createCategory(cat);
//			this.categories.add(catTmp);
//		} 
	}

}


 