<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix='sql' uri='http://java.sun.com/jsp/jstl/sql' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	response.setHeader("Expires", "Mon, 01 Jan 2009 01:00:00 GMT");
	response.setHeader("Cache-Control", "must-revalidate");
	response.setHeader("Cache-Control", "no-cache");
%>

<%--<%@page contentType="application/json;charset=iso-8859-1" pageEncoding="iso-8859-1"%>--%>

<%@ include file="../dataSource.jsp"%>

<c:if test="${!empty param.id}"><c:set var="id" value="${param.id}"/></c:if>
<c:if test="${id == '_empty'}"><c:set var="id" value=""/></c:if>

<c:if test="${!empty param.datos_economicos__id}"><c:set var="id" value="${param.datos_economicos__id}"/></c:if>

<c:if test="${!empty param.datos_economicos__fecha_certificado_fiscal}">
<fmt:parseDate var="datos_economicos__fecha_certificado_fiscal" value="${param.datos_economicos__fecha_certificado_fiscal}" pattern="dd/MM/yyyy" />
</c:if>


<c:catch var="errorTransaccion">
    <sql:transaction>
        	<%-- DELETE --%>
            <c:if test="${param.oper eq 'del'}">
            	
            	<sql:update var="result">
					DELETE FROM datos_economicos WHERE id=?
<sql:param>${id}</sql:param>

				</sql:update>
				<c:if test="${result==1}">
					<c:set var="mensaje">Registro borrado correctamente.</c:set>
				</c:if>
			</c:if>				
            
            <%-- INSERT --%>
            <c:if test="${param.oper eq 'add'}">
                <sql:update var="result">
                	INSERT INTO datos_economicos(id,tipo_cuenta,banco,sucursal,dc,cuenta,cuenta_ext,iban,swift,certificado_fiscal,fecha_certificado_fiscal,irpf,iva,forma_pago)
VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
<sql:param>${param.datos_economicos__id}</sql:param>
<sql:param>${param.datos_economicos__tipo_cuenta}</sql:param>
<sql:param>${param.datos_economicos__banco}</sql:param>
<sql:param>${param.datos_economicos__sucursal}</sql:param>
<sql:param>${param.datos_economicos__dc}</sql:param>
<sql:param>${param.datos_economicos__cuenta}</sql:param>
<sql:param>${param.datos_economicos__cuenta_ext}</sql:param>
<sql:param>${param.datos_economicos__iban}</sql:param>
<sql:param>${param.datos_economicos__swift}</sql:param>
<sql:param value="${!empty param.datos_economicos__certificado_fiscal}"/>
<sql:dateParam value="${datos_economicos__fecha_certificado_fiscal}"/>
<sql:param><fmt:parseNumber type="number"  value="${param.datos_economicos__irpf}" /></sql:param>
<sql:param><fmt:parseNumber type="number"  value="${param.datos_economicos__iva}" /></sql:param>
<sql:param>${param.datos_economicos__forma_pago}</sql:param>

                </sql:update>
                <sql:query var="rs">
					SELECT LAST_INSERT_ID() AS ultimoID
				</sql:query>
				<c:set var="id" value="${rs.rows[0].ultimoID}"/>
                <c:if test="${result==1}">
					<c:set var="mensaje">Registro insertado correctamente.</c:set>
				</c:if>
            </c:if>

            <%-- UPDATE --%>
            <c:if test="${param.oper eq 'edit'}">
                <sql:update var="result">
                	UPDATE datos_economicos SET
tipo_cuenta=?,
banco=?,
sucursal=?,
dc=?,
cuenta=?,
cuenta_ext=?,
iban=?,
swift=?,
certificado_fiscal=?,
fecha_certificado_fiscal=?,
irpf=?,
iva=?,
forma_pago=?
WHERE id = ?
<sql:param>${param.datos_economicos__tipo_cuenta}</sql:param>
<sql:param>${param.datos_economicos__banco}</sql:param>
<sql:param>${param.datos_economicos__sucursal}</sql:param>
<sql:param>${param.datos_economicos__dc}</sql:param>
<sql:param>${param.datos_economicos__cuenta}</sql:param>
<sql:param>${param.datos_economicos__cuenta_ext}</sql:param>
<sql:param>${param.datos_economicos__iban}</sql:param>
<sql:param>${param.datos_economicos__swift}</sql:param>
<sql:param value="${!empty param.datos_economicos__certificado_fiscal}"/>
<sql:dateParam value="${datos_economicos__fecha_certificado_fiscal}"/>
<sql:param><fmt:parseNumber type="number"  value="${param.datos_economicos__irpf}" /></sql:param>
<sql:param><fmt:parseNumber type="number"  value="${param.datos_economicos__iva}" /></sql:param>
<sql:param>${param.datos_economicos__forma_pago}</sql:param>
<sql:param>${id}</sql:param>

                </sql:update>
                <c:if test="${result==1}">
					<c:set var="mensaje">Registro modificado correctamente.</c:set>
				</c:if>
            </c:if>
        
    </sql:transaction>
</c:catch>

<c:set var="error">false</c:set>
<c:if test="${!empty errorTransaccion || empty result}">
	<%
		//pageContext.setAttribute("eol","\r\n");
		//pageContext.setAttribute("br","\\r");
		System.err.println(pageContext.getAttribute("errorTransaccion"));
	%>
	<c:set var="mensaje">Error!</c:set>
	<c:set var="error">true</c:set>
</c:if>

<c:if test="${param.json==true}">{"mensaje":"${mensaje}", "error":"${error}", "id":"${id}" <c:if test="${!empty param.tipo}">, "tipo":"${param.tipo}"</c:if> }</c:if>
<c:if test="${empty param.json}">
	<%
		String ref = request.getHeader("referer");
		java.net.URL refUrl = new java.net.URL(ref);
		pageContext.setAttribute("referer", refUrl.getPath());
	%>
	<c:redirect url="${referer}" context="/">
		<c:param name="id" value="${id}"/>
		<c:param name="mensaje" value="${mensaje}"/>
		<c:param name="tipo" value="${param.tipo}"/>
	</c:redirect>
</c:if>
