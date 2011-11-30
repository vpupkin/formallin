package gform;

import junit.framework.TestCase;

public class JSONTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToString() {
		
		JSON json = new JSON();
		JSON menu = new JSON();
		JSON popup = new JSON();
		
		JSON[] menuitem = new JSON[3];
		
		menuitem[0] = new JSON();
		menuitem[0].put("value", "'New'");
		menuitem[0].put("onclick", "CreateNewDoc()");
		
		menuitem[1] = new JSON();
		menuitem[1].put("value", "'Open'");
		menuitem[1].put("onclick", "OpenDoc()");
		
		menuitem[2] = new JSON();
		menuitem[2].put("value", "'Close'");
		menuitem[2].put("onclick", "CloseDoc()");
		
		popup.put("menuitem", menuitem);
		
		menu.put("id", null);
		menu.put("value", "'File'");
		menu.put("popup", popup);
		
		json.put("menu", menu);

		String expected = "{menu:{id:null,value:'File',popup:{menuitem:[{value:'New',onclick:CreateNewDoc()},{value:'Open',onclick:OpenDoc()},{value:'Close',onclick:CloseDoc()}]}}}";
		
		System.out.println(expected);
		System.out.println(json.toString());
		
		assertEquals(expected, json.toString());
		
	}
/*
  {"menu": {
   "id": "file",
   "value": "File",
   "popup": {
     "menuitem": [
       {"value": "New", "onclick": "CreateNewDoc()"},
       {"value": "Open", "onclick": "OpenDoc()"},
       {"value": "Close", "onclick": "CloseDoc()"}
     ]
   }
 }}
 */
}
