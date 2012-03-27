<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<%
String errmessage = "";
WDBOService ddboService = WDBOService.getInstance();  
try{ 	
	String oName = null;
	String catName = null;
	oName = request.getParameter("oName").toString(); 
 	catName = request.getParameter("catName").toString(); 
	
	Wdb oTmp  = new Wdb (oName,ddboService.createCategory(  catName ) ); 
	// persist libriries.... 
	ddboService.flush(oTmp);
}catch(Exception e){e.printStackTrace();}	
%>
<h1><%=errmessage%></h1> 


<h3>Category list</h3>
<table> 
<%
int count = 0;
for (Category catTmp :ddboService.getCategories()){
%>
<tr>  	<td><%=count++%></td><td><%= catTmp%></td>   </tr>
<%
}
%>
</table>
 
<h3> Add Object form:</h3> 
<form method="post" id="addObjForm">
	<input type="text" id="oName" name="oName" class="oName" value="">
	<input type="text" id="catName" name="catName" class="catName" value="">
	<input type="submit" value="createObject">
</form>
 