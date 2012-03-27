<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<table> 
<%
WDBOService ddboService = WDBOService.getInstance(); 
String toSearch = ""; // default search will produse all DB-items
try{ 
	toSearch = request.getParameter("toSearch").toString();
}catch(Exception e){e.printStackTrace();}	
// search over full scan ....
int count = 0;

for ( Wdb o:ddboService.getObjects()){
	// here "like" is most effective search! :)
	if (o._().indexOf(toSearch)  == -1 ) continue; 	
%>
<tr>
	<td><%=count++%></td><td><%=o%> </td><td><%=o.uid%> </td><td><%=o.getUID()%> </td><td><%=o.getCategories()%> </td>
</tr>
<% 			
}		 
%>
<table>
<form method="post" id="searcgForm">
	<input type="text" id="toSearch" name="toSearch" class="toSearch" value="">
<input type="submit" value="search">
</form>

