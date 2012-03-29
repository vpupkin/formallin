<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<%
String errmessage = "";
String uid = null;
Wdb oTmp = null;
WDBOService ddboService = WDBOService.getInstance();  
try{ 	 
	uid = request.getParameter("uid").toString();
	// brute search
	for (Wdb theNextTmp :ddboService.getObjects()){
		if (uid .indexOf(theNextTmp.getUID().toString()) == 1){
			System.out.println(" brute search miss FOUND UID#"+uid);
			oTmp  = theNextTmp;
			break;
		}else{
			System.out.println(" brute search miss ["+uid+"]=====!  {"+theNextTmp.getUID().toString()+"}");
		}
	}
	
	String newPropertyName =  request.getParameter("newPropertyName");
	String newPropertyVal =  request.getParameter("newPropertyVal");
	oTmp.addProperty(newPropertyName, newPropertyVal);
	ddboService.flush(oTmp);
}catch(Exception e){e.printStackTrace();}	
%>
<h1><%=errmessage%></h1> 
<%=uid==null?"":uid%>
<br>
<%=oTmp==null?"":oTmp%>
<ul>
<%
try{
if (oTmp!=null){
		for (String pKey:oTmp.getPropertyNames()){
			String p1 = oTmp.getProperty(pKey)._();
%>
<li>
<%=pKey %>::<%=p1 %>
</li>
<%
		}
	}
}catch(Exception e){}
%>
<ul>
<form method="post" id="editForm">
	<input type="text" id="newPropertyUID" name="newPropertyUID" class="newPropertyUID" disabled value="<%=uid%>">
	<input type="text" id="newPropertyName" name="newPropertyName" class="newPropertyName" value="">
	<input type="text" id="newPropertyVal" name="newPropertyVal" class="newPropertyVal" value="">
	<input type="submit" value="addNewProperty">
</form>

