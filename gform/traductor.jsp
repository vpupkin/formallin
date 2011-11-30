<%@page contentType="text/html;charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@include file="/dataSource.jsp"%>

<c:if test="${!empty param.idCliente}">
	<sql:query var="rs">
		SELECT * FROM traductor WHERE id=?
		<sql:param>${param.idCliente}</sql:param>
	</sql:query>
	<c:set var="data" value="${rs.rows[0]}" />
	<c:remove var="rs" />
</c:if>


<form table="traductor" xmlns:c="http://java.sun.com/jsp/jstl/core">

<div class="formulario">
	<br/>
	<div class="columna_izq">
	
	<element>
		<name>id</name>
		<type>INT</type>
		<key>true</key>
		<references>cliente(idCliente)</references>
		<hidden>true</hidden>
	</element>
	
	<element>
		<name>fecha_insercion</name>
		<type>DATETIME</type>
		<hidden>true</hidden>
	</element>

	<element>
		<label>Tipo</label>
		<name>id_tipo_traductor</name>
		<type>select</type>
		<query>
			SELECT 'Estudiante' AS 'option', 1 AS 'value'
			UNION SELECT 'Externo',2
			UNION SELECT 'Interno',3
		</query>
	</element>
	
	<element>
		<label>Valoracion</label>
		<name>valoracion</name>
		<type>select</type>
		<query>
			SELECT '1' AS 'option', 1 AS 'value'
			UNION SELECT '2',2
			UNION SELECT '3',3
			UNION SELECT '4',4
			UNION SELECT '5',5
			UNION SELECT '6',6
			UNION SELECT '7',7
			UNION SELECT '8',8
			UNION SELECT '9',9
			UNION SELECT '10',10
		</query>
	</element>
	</div>
	
	<div class="columna_drcha">
	<element>
		<label>Licenciado</label>
		<name>licenciado</name>
		<type>checkbox</type>
	</element>
	
	<element>
		<label>Jurado</label>
		<name>jurado</name>
		<type>checkbox</type>
	</element>
	
	<element>
		<label>Interpretación</label>
		<name>interpretacion</name>
		<type>checkbox</type>
	</element>
	</div>
	
	<div class="columna_izq">
	<element>
		<label>Idioma materno 1</label>
		<name>idioma_1</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Idioma materno 2</label>
		<name>idioma_2</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Secundario</label>
		<name>idioma_3</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Otro idioma 1</label>
		<name>idioma_4</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Otro idioma 2</label>
		<name>idioma_5</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Otro idioma 3</label>
		<name>idioma_6</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	
	<element>
		<label>Otro idioma 4</label>
		<name>idioma_7</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma2 ORDER BY idioma</query>
		<references>idioma2(id)</references>
	</element>
	</div>
	
	<div class="columna_drcha">
		<element>
			<label>Especialidad traducción</label>
			<name>especialidad</name>
			<type>TEXTAREA</type>
			<cols>60</cols>
			<rows>2</rows>
		</element>
		
		<element>
			<label>Otras cualificaciones</label>
			<name>cualificaciones</name>
			<type>TEXTAREA</type>
			<cols>60</cols>
			<rows>2</rows>
		</element>
		
		<element>
			<label>Comentario</label>
			<name>observaciones</name>
			<type>TEXTAREA</type>
			<cols>60</cols>
			<rows>2</rows>
		</element>
	</div>
	
	<div class="antifloat"></div>
	
	<div class="columna_izq">
	<br/>
	<element>
		<label>Tarifas</label>
		<name>tarifa_1</name>
		<type>TEXTAREA</type>
	</element>

	<element>
		<name>tarifa_2</name>
		<type>TEXTAREA</type>
	</element>

	<element>
		<label>Mínimo</label>
		<name>minimo</name>
		<type>DECIMAL</type>
		<size>10,2</size>
	</element>

	<element>
		<label>Tarifa base</label>
		<name>tarifa_base</name>
		<type>DECIMAL</type>
		<size>10,2</size>
	</element>
	</div>
	
	<div class="columna_drcha">
	<element>
		<label>Nº Cuenta</label>
		<name>banco</name>
		<type>varchar</type>
		<size>4</size>
	</element>
	
	<element>
		<name>sucursal</name>
		<type>varchar</type>
		<size>4</size>
	</element>
	
	<element>
		<name>dc</name>
		<type>varchar</type>
		<size>2</size>
	</element>
	
	<element>
		<name>cuenta</name>
		<type>varchar</type>
		<size>10</size>
	</element>

	<INPUT TYPE="button" VALUE="Validar" onClick="validarCCC(this.form)"/>

	
	<element>
		<label>IBAN</label>
		<name>iban</name>
		<type>VARCHAR</type>
		<size>34</size>
	</element>
	
	<element>
		<label>SWIFT</label>
		<name>swift</name>
		<type>VARCHAR</type>
		<size>12</size>
	</element>
	
	<element>
		<label>C. Fiscal</label>
		<name>certificado_fiscal</name>
		<type>checkbox</type>
	</element>

	<element>
		<name>fecha_certificado_fiscal</name>
		<type>datetime</type>
	</element>
	
	<element>
		<label>IRPF</label>
		<name>irpf</name>
		<type>decimal</type>
		<size>16,4</size>
	</element>

	<element>
		<label>IVA</label>
		<name>iva</name>
		<type>decimal</type>
		<size>16,4</size>
	</element>
	
	</div>

</div>
</form>
