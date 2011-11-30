<?xml version="1.0" encoding="iso-8859-1"?>
<%
        response.setHeader("Content-Type", "text/xml");
        response.setHeader("Expires", "Mon, 01 Jan 2009 01:00:00 GMT");
        response.setHeader("Cache-Control", "must-revalidate");
        response.setHeader("Cache-Control", "no-cache");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt' %>

<%@include file="../dataSource.jsp"%>


<sql:query var="consultaNumeroResultados">
	SELECT COUNT(*) as numeroResultados FROM (
		
					SELECT id, nombre, mail
					FROM cliente_traduccion_contactos
					WHERE fk_idClienteTraduccion=${param.id}
				
	) aux
</sql:query>

<sql:query var="consultaResultados">
	
					SELECT id, nombre, mail
					FROM cliente_traduccion_contactos
					WHERE fk_idClienteTraduccion=${param.id}
				
</sql:query>

<%-- NUMERO DE PAGINAS --%>
<c:set var="resultInDouble" value="${(consultaNumeroResultados.rows[0].numeroResultados-1)/param.rows +1}" />
<c:set var="resultInDoubleZero" value="${resultInDouble - resultInDouble %1}" />
<fmt:formatNumber value="${resultInDoubleZero}" var="numPaginas" pattern="0" />

<rows>
    <page>${param.page}</page>
    <total>${numPaginas}</total>
    <records>${consultaNumeroResultados.rows[0].numeroResultados}</records>

    <c:set var="edit"><img src="../graf/edit.gif" border="0" title="Editar" alt="Editar" /></c:set>
    <c:set var="see"><img src="../graf/preview.gif" border="0" title="Ver" alt="Ver" /></c:set>
    <c:set var="del"><img src="../graf/delete.gif" border="0" title="Borrar" alt="Borrar" /></c:set>

    <c:forEach var="registro" items="${consultaResultados.rows}" varStatus="s" begin="${(param.page-1)*param.rows}" end="${(param.page)*param.rows-1}">
        <row id="${registro.id}">
        	<c:forEach var="colName" items="${consultaResultados.columnNames}">
        		<c:set var="cellValue">
	            	<c:choose>
	            		<c:when test="${registro[colName]=='true'}">Sí</c:when>
	            		<c:when test="${registro[colName]=='false'}">No</c:when>
	            		<c:otherwise>${registro[colName]}</c:otherwise>
	            	</c:choose>
            	</c:set>
            	<cell>${fn:trim(cellValue)}</cell>
            </c:forEach>
        </row>
    </c:forEach>
</rows>
