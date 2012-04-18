<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>

<h3>Category list</h3>
<table> 
<% 
WDBOService ddboService = WDBOService.getInstance();  
int count = 0;
for (Category catTmp :ddboService.getCategories()){
%>
<tr>
	<td><%=count++%></td><td><%= catTmp._()%></td>
</tr>
<%
}
%>
</table>
<%
String toAdd = null;
try{ 
	toAdd = request.getParameter("toAdd").toString(); 
	Category libCat =ddboService.createCategory(toAdd); 
	// persist libriries.... 
	ddboService.flush(libCat); 
}catch(Exception e){e.printStackTrace();}
  
%> 

<h3> Add Category form:</h3>
<form method="post" id="addCatForm">
	<input type="text" id="toAdd" name="toAdd" class="toAdd" value="">
<input type="submit" value="addCategory">
</form>
 