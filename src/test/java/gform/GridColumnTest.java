/**
 * 
 */
package gform;
   

import junit.framework.TestCase; 

/**
 * @author Administrador
 * 
 */
public class GridColumnTest extends TestCase {
	public static void setUpBeforeClass() throws Exception {
	} 
	public static void tearDownAfterClass() throws Exception {
	} 
	public void setUp() throws Exception {
	} 
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link gform.GridColumn}.
	 */ 
	public void testToString(){
		System.out.println("test GridColumn.toString()");
		Element element = new Element();
		element.setName("my_column");
		GridColumn col = new GridColumn(element);
		assertEquals("{name:'my_column', index:'my_column', width:85, align:'right', search:true, editable:true, hidden:false}",col.toString());
	}
}
