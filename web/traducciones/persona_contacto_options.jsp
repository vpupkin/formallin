<%@page	contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="../dataSource.jsp" %>

<sql:query var="rs">
	
							SELECT nombre AS 'option', id AS 'value'
							FROM cliente_traduccion_contactos 
							WHERE fk_idClienteTraduccion=?
						
	<sql:param>${param.id_cliente}</sql:param>
</sql:query>

<c:forEach items="${rs.rows}" var="item">
	<option value="${item.value}"><c:out value="${item.option}"/></option>
</c:forEach>

<c:remove var="rs"/>
