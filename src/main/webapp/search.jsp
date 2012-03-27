<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<table>

<%
WDBOService ddboService = WDBOService.getInstance(); 
String toSearch = "Library";
try{ 
	toSearch = request.getParameter("toSearch").toString();
}catch(Exception e){e.printStackTrace();}	
// search over full scan ....
int count = 0;

for ( Wdb   o:ddboService.getObjects()){
	// exact search only!
	if (o._().indexOf(toSearch)  == -1 ) continue; 	
%>
<tr>
	<td><%=count++%></td><td><%=o%> </td>
</tr>
<% 			
}		 
%>
<table>
<form method="post" id="searcgForm">
	<input type="text" id="toSearch" name="toSearch" class="toSearch" value="">
<input type="submit" value="search">
</form>

