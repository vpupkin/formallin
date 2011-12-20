<%@page contentType="text/html;charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@include file="/dataSource.jsp"%>

<c:if test="${!empty param.idCliente}">
	<sql:query var="rs">
		SELECT * FROM tarifas_traduccion WHERE id=?
		<sql:param>${param.id}</sql:param>
	</sql:query>
	<c:set var="data" value="${rs.rows[0]}" />
	<c:remove var="rs" />
</c:if>


<form table="tarifas_traduccion" xmlns:c="http://java.sun.com/jsp/jstl/core">

<div class="formulario_t">
	<br/>
	<div class="columna_drcha">
	
	<element>
		<name>id</name>
		<type>INT</type>
		<size>11</size>
		<key>true</key>
		<auto>true</auto>
		<hidden>true</hidden>
	</element>
	
	<element>
		<label>Fecha inicio</label>
		<name>fecha</name>
		<type>DATETIME</type>
	</element>
	
	<element>
		<label>Tipo servicio</label>
		<name>id_tipo_servicio</name>
		<type>select</type>
		<query>SELECT nombre AS 'option', id AS 'value' FROM tipo_servicios_traduccion ORDER BY nombre</query>
		<references>tipo_servicios_traduccion(id)</references>
	</element>
	
	<element>
		<label>Idioma</label>
		<name>id_idioma</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma ORDER BY idioma</query>
		<references>idioma(id)</references>
	</element>

	<element>
		<label>Tarifa</label>
		<name>tarifa</name>
		<type>DECIMAL</type>
		<size>10,2</size>
	</element>
	</div>

</div>
</form>
