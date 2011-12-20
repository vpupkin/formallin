<%@page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@include file="/dataSource.jsp"%>

<c:set var="oper" value="add"/>
<c:if test="${!empty param.id}">
	<sql:query var="rs">
		SELECT * FROM datos_economicos WHERE id=?
		<sql:param>${param.idCliente}</sql:param>
	</sql:query>
	<c:if test="${rs.rowCount>0}">
		<c:set var="data" value="${rs.rows[0]}"/>
		<c:set var="oper" value="edit"/>
	</c:if>
	<c:remove var="rs"/>
</c:if>

<form table="datos_economicos" xmlns:c="http://java.sun.com/jsp/jstl/core">
	
	<input type="hidden" id="oper" name="oper" value="${oper}"/>
	
	<div class="formulario_t">
		<br/>
		<div class="columna_drcha">
	 
			<element>
				<name>id</name>
				<type>INT</type>
				<key>true</key>
				<references>cliente(idCliente)</references>
				<hidden>true</hidden>
			</element>
			
			<element>
				<label>Tipo cuenta</label>
				<name>tipo_cuenta</name>
				<type>select</type>
				<query>
					SELECT 'Cuenta de España' AS 'option', 1 AS 'value'
					UNION SELECT 'Cuenta en el Extranjero',2
				</query>
			</element>
		
			<div id="cnacional">
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
			</div>
			
			<div id="cextranjero">
				<element>
					<label>Nº Cuenta</label>
					<name>cuenta_ext</name>
					<type>varchar</type>
					<size>45</size>
				</element>
			</div>
			
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
				<label>Certificado fiscal</label>
				<name>certificado_fiscal</name>
				<type>checkbox</type>
			</element>
		
			<element>
				<label>Fecha certificado</label>
				<name>fecha_certificado_fiscal</name>
				<type>datetime</type>
			</element>
			
			<element>
				<label>IRPF, %</label>
				<name>irpf</name>
				<type>decimal</type>
				<size>16,4</size>
			</element>
		
			<element>
				<label>IVA, %</label>
				<name>iva</name>
				<type>decimal</type>
				<size>16,4</size>
			</element>
			
			<element>
				<label>Forma de pago</label>
				<name>forma_pago</name>
				<type>select</type>
				<query>
					SELECT '30 días' AS 'option', 1 AS 'value'
				</query>
			</element>
		
		</div>
	
	</div>
</form>