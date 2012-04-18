<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.WDBOService"%>
<html><body>

<h4>object comparison by categories</h4>
<%
String toSearch = ""; 
String prefixTmp = ",";
try{ 
	toSearch = session.getAttribute("toSearch").toString();
}catch(Exception e){e.printStackTrace();}	
if (request.getParameter("toSearch") !=null) // addSEARCH
try{
	String sTmp = "";
	for (String searchStr:(toSearch+","+request.getParameter("toSearch")).replace(",,",",").toString().split(",")){
		if (searchStr == null) continue;
		if ((","+sTmp+",").indexOf(","+searchStr+",") >=0) continue;
		sTmp +=prefixTmp;
		sTmp += searchStr;
		sTmp = sTmp.indexOf(",")==0?sTmp.substring(1):sTmp;
		session.setAttribute("toSearch", ""+sTmp);
		toSearch = sTmp;
		prefixTmp = ",";
	} 
}catch(Exception e){e.printStackTrace();}
%>

<%
String toDel =  request.getParameter("toDel");

String sTmp = "";
for (String searchStr:(toSearch).toString().split(",")){ 
	if (searchStr ==null ) {
		continue;
	} 
	if (toDel !=null && toDel.equals(searchStr) ) {
		System.out.println(" DELETE>["+toDel+"]"); 
		continue;
	}else{
		sTmp +=prefixTmp;
		sTmp += searchStr;
		sTmp = sTmp.indexOf(",")==0?sTmp.substring(1):sTmp;
	}
%>
<form method="post" id="deLETEFORM<%=searchStr.hashCode()%>">
<input type="text"  id="toDel" name="toDel" class="toDel" value="<%=searchStr%>">
<input type="submit" value="delete this category from comparison">
</form>
<%
}
if (toDel !=null) // DELETE
	session.setAttribute("toSearch", sTmp);
%>


<h4><%=("".equals(toSearch)?"enter one or more categories (separated by comma) for compare...":session.getAttribute("toSearch").toString())%></h4>
<form method="post" id="ADDFORM">
	<input type="text" id="toSearch" name="toSearch" class="toSearch" value="">
<input type="submit"  value="add Category (es) to compare..">
</form>

<%
WDBOService ddboService = WDBOService.getInstance(); 
for (String catTmp  :toSearch.split(",")){
	if ("".equals(catTmp.trim()))continue;
	%><h3><%=catTmp%></h3><%
	try{
		for (Wdb o:ddboService.getObjects(catTmp)){
		%><%=o %><br><% 	
		}// oWdbs
	}catch(Throwable e){e.printStackTrace(); } 
}// Cats
%>


<h4>links</h4>
<a href="add.jsp">add.jsp</a>
<a href="edit.jsp">edit.jsp</a>
<a href="search.jsp">search.jsp</a>
<a href="addCategory.jsp">addCategory.jsp</a>



</body></html>