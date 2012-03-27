<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<%
String toAdd = null;
try{ 
	toAdd = request.getParameter("toAdd").toString();
	WDBOService ddboService = WDBOService.getInstance(); 
	Category libCat =ddboService.createCategory(toAdd); 
	// persist libriries.... 
	ddboService.flush(libCat); 
}catch(Exception e){e.printStackTrace();}
  
%> 
<form method="post" id="addCatForm">
	<input type="text" id="toAdd" name="toAdd" class="toAdd" value="">
<input type="submit" value="addCategory">
</form>
 