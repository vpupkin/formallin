<%@page contentType="text/html;charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@include file="/dataSource.jsp"%>

<c:if test="${!empty param.idCliente}">
	<sql:query var="rs">
		SELECT * FROM cliente_traduccion WHERE id=?
		<sql:param>${param.idCliente}</sql:param>
	</sql:query>
	<c:set var="data" value="${rs.rows[0]}" />
	<c:remove var="rs" />
</c:if>


<form table="cliente_traduccion" xmlns:c="http://java.sun.com/jsp/jstl/core">

<div class="formulario_t">
	<br/>
	<div class="columna_drcha">
	
		<element>
			<label>id</label>
			<name>id</name>
			<type>INT</type>
			<hidden>true</hidden>
			<key>true</key>
			<references>cliente(idCliente)</references>
		</element>
		
		<element>
			<label>Tipo</label>
			<name>id_tipo</name>
			<type>select</type>
			<query>
				SELECT 'Estudiante' AS 'option', 1 AS 'value'
				UNION SELECT 'Externo',2
				UNION SELECT 'Interno',3
			</query>
		</element>
		
		<element>
			<label>Página web</label>
			<name>web</name>
			<type>VARCHAR</type>
			<size>256</size>
		</element>
		
	</div>
	
	<div class="columna_izq">
	
		
		<element>
			<label>Particular</label>
			<name>particular</name>
			<type>CHECKBOX</type>
		</element>
		
		<element>
			<label>Moroso</label>
			<name>moroso</name>
			<type>CHECKBOX</type>
		</element>
		
		<element>
			<label>Bolsa</label>
			<name>bolsa</name>
			<type>CHECKBOX</type>
		</element>
	
		
	
	</div>
	
	<div class="antifloat"></div>
	
	<c:if test="${empty param.idCliente}">
		<p class="msg_alert">Debe guardar los datos para añadir personas de contacto.</p>
	</c:if>
	<c:if test="${!empty param.idCliente}">				
		<div class="columna_drcha">
			<grid>
				<title>Personas de contacto</title>
				<table_model>cliente_traduccion_contactos</table_model>
				<name>grid_contactos</name>
				<columns>id,nombre,mail,telefono</columns>
				<url>/traducciones/grid_contactos_xml.jsp?id=${param.idCliente}</url>
				<query>
					SELECT id, nombre, mail, telefono
					FROM cliente_traduccion_contactos
					WHERE fk_idClienteTraduccion=${param.id}
				</query>
				<procesa_url>/traducciones/cliente_traduccion_contactos_procesa.jsp?cliente_traduccion_contactos__fk_idClienteTraduccion=${param.idCliente}</procesa_url>
			</grid>
		</div>
	</c:if>
	

	<c:if test="${empty param.idCliente}">
		<p class="msg_alert">Debe guardar los datos para añadir tarifas.</p>
	</c:if>
	<c:if test="${!empty param.idCliente}">				
		<div class="columna_izq">
			<grid>
				<title>Tarifas</title>
				<table_model>cliente_traduccion_tarifas</table_model>
				<name>grid_tarifas</name>
				<columns>id, id_idioma_origen, id_idioma_trad, tarifa</columns>
				<url>/traducciones/grid_tarifas_xml.jsp?id=${param.idCliente}</url>
				<query>
					SELECT ctt.id, i1.idioma AS idioma_origen, i2.idioma AS idioma_trad, ctt.tarifa
					FROM cliente_traduccion_tarifas ctt
						INNER JOIN idioma i1 ON i1.id=ctt.id_idioma_origen
						INNER JOIN idioma i2 ON i2.id=ctt.id_idioma_trad
					WHERE ctt.fk_idClienteTraduccion=${param.id}
				</query>
				<procesa_url>/traducciones/cliente_traduccion_tarifas_procesa.jsp?cliente_traduccion_tarifas__fk_idClienteTraduccion=${param.idCliente}</procesa_url>
			</grid>
		</div>
	</c:if>
					
</div>
</form>
