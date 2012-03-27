<html><body>
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
<input type="text" id="toDel" name="toDel" class="toDel" value="<%=searchStr%>">
<input type="submit" value="del">
</form>
<%
}
if (toDel !=null) // DELETE
	session.setAttribute("toSearch", sTmp);
%>


<h4><%=("".equals(toSearch)?"ender one or more categories (separated by comma) for compare...":session.getAttribute("toSearch").toString())%></h4>
<form method="post" id="ADDFORM">
	<input type="text" id="toSearch" name="toSearch" class="toSearch" value="">
<input type="submit" >
</form>
</body></html>