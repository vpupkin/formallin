<%@page import="java.util.Properties"%>
<%@page import="eu.blky.wdb.WDBOService"%>
<%@page import="eu.blky.wdb.Wdb"%>
<%@page import="eu.blky.wdb.Category"%>
<%
String errmessage = "";
String uid = null;
Wdb oTmp = null;
WDBOService ddboService = WDBOService.getInstance();
// Add new property
try{ 	 
	uid = request.getParameter("uid").toString();
 	oTmp  = new Wdb((Properties)ddboService.getByUID(uid)); 
	String newPropertyName =  request.getParameter("newPropertyName");
	String newPropertyVal =  request.getParameter("newPropertyVal");
	oTmp.addProperty(newPropertyName, newPropertyVal);
	ddboService.flush(oTmp);
}catch(Exception e){e.printStackTrace();}

//add new Category
//System.out.println( ""+request.getParameter("addCategory")  );
do{ try{ 	 
	uid = request.getParameter("uid").toString();
	oTmp  = new Wdb((Properties)ddboService.getByUID(uid));
	String addCategory = request.getParameter("addCategory");
	if (addCategory == null)break;
	Category catTTT = ddboService.createCategory(addCategory, oTmp); 
	oTmp.addCategory(catTTT);
	ddboService.flush(oTmp);
}catch(Exception e){e.printStackTrace();}
}while(1==2);


//delete Category

do{ try{ 	 
	uid = request.getParameter("uid").toString();
	oTmp  = new Wdb((Properties)ddboService.getByUID(uid));
	String delCategory = request.getParameter("delCategory");
	if (delCategory == null)break;
	Category catTTT = ddboService.createCategory(delCategory, oTmp); 
	oTmp.delCategory(catTTT);
	ddboService.flush(oTmp);
}catch(Exception e){e.printStackTrace();}
}while(1==2);

%>
<h1><%=errmessage%></h1> 
<%=uid==null?"":uid%>
<br>
<%=oTmp==null?"":oTmp%>
<h4>categories</h4>
<table><tr>
<%
try{ 
	uid = request.getParameter("uid").toString();
	oTmp  = new Wdb((Properties)ddboService.getByUID(uid)); 
	for (Wdb catTmp :oTmp.getCategoriesAsList()){
		%><!-- <%=catTmp %> -->
 <td>		
 <form method="get" id="delCategoryForm" >
	-<input type="submit"   value="<%=catTmp %>"  name="delCategory"  id="<%=catTmp.hashCode()%>">
	<input type="hidden"   value="<%=uid%>"  name="uid"  id="<%=uid%>">
	</input>
  </form>
  </td>
		<% 
	}
}catch(Exception e){e.printStackTrace();}
%>
</tr></table>
<h4>props</h4>
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

<h4>Categories to add:</h4>
<table><tr>
<%

//WDBOService ddboService = WDBOService.getInstance();
for (Category catTmp :ddboService.getCategories()){
	//TODO if (oTmp!=null && oTmp.getCategories()!=null && oTmp.getCategoriesAsList().contains(catTmp ))continue;
%><td><form method="get" id="addCategoryForm" >
	+<input type="submit"   value="<%=catTmp %>"  name="addCategory"  id="<%=catTmp.hashCode()%>">
	<input type="hidden"   value="<%=uid%>"  name="uid"  id="<%=uid%>">
	</input>
  </form></td>
  
<%
}
%>
</tr></table>
