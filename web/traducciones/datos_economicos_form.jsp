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

<form name="datos_economicos" id="datos_economicos" action="datos_economicos_procesa.jsp">
	
	<input type="hidden" id="oper" name="oper" value="${oper}"/>
	
	<div class="formulario_t">
		<br/>
		<div class="columna_drcha">
	 
			<c:if test="${!empty data}">
	<input type="hidden" name="datos_economicos__id"  value="${data.id}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="datos_economicos__id"  value="${id}"  />
</c:if>

			
			<sql:query var="rs">
	
					SELECT 'Cuenta de España' AS 'option', 1 AS 'value'
					UNION SELECT 'Cuenta en el Extranjero',2
				
</sql:query>

<c:if test="${!empty 'Tipo cuenta'}"><label for="datos_economicos__tipo_cuenta">Tipo cuenta</label></c:if>

<select name="datos_economicos__tipo_cuenta" id="datos_economicos__tipo_cuenta">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.tipo_cuenta==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


		
			<div id="cnacional">
				<c:if test="${!empty 'Nº Cuenta'}"><label for="datos_economicos__banco">Nº Cuenta</label></c:if>
<input type="text" name="datos_economicos__banco" id="datos_economicos__banco" value="${data.banco}" size="4" maxlength="4" />

				
				<c:if test="${!empty ''}"><label for="datos_economicos__sucursal"></label></c:if>
<input type="text" name="datos_economicos__sucursal" id="datos_economicos__sucursal" value="${data.sucursal}" size="4" maxlength="4" />

				
				<c:if test="${!empty ''}"><label for="datos_economicos__dc"></label></c:if>
<input type="text" name="datos_economicos__dc" id="datos_economicos__dc" value="${data.dc}" size="2" maxlength="2" />

				
				<c:if test="${!empty ''}"><label for="datos_economicos__cuenta"></label></c:if>
<input type="text" name="datos_economicos__cuenta" id="datos_economicos__cuenta" value="${data.cuenta}" size="10" maxlength="10" />

			
				<INPUT TYPE="button" VALUE="Validar" onClick="validarCCC(this.form)"/>
			</div>
			
			<div id="cextranjero">
				<c:if test="${!empty 'Nº Cuenta'}"><label for="datos_economicos__cuenta_ext">Nº Cuenta</label></c:if>
<input type="text" name="datos_economicos__cuenta_ext" id="datos_economicos__cuenta_ext" value="${data.cuenta_ext}" size="45" maxlength="45" />

			</div>
			
			<c:if test="${!empty 'IBAN'}"><label for="datos_economicos__iban">IBAN</label></c:if>
<input type="text" name="datos_economicos__iban" id="datos_economicos__iban" value="${data.iban}" size="34" maxlength="34" />

			
			<c:if test="${!empty 'SWIFT'}"><label for="datos_economicos__swift">SWIFT</label></c:if>
<input type="text" name="datos_economicos__swift" id="datos_economicos__swift" value="${data.swift}" size="12" maxlength="12" />

			
			
<c:set var="checked" value=""/>
<c:if test="${data.certificado_fiscal}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Certificado fiscal'}"><label for="datos_economicos__certificado_fiscal">Certificado fiscal</label></c:if>
<input type="checkbox" name="datos_economicos__certificado_fiscal" ${checked} />

		
			<c:if test="${!empty 'Fecha certificado'}"><label for="datos_economicos__fecha_certificado_fiscal">Fecha certificado</label></c:if>
<input type="text" name="datos_economicos__fecha_certificado_fiscal" id="datos_economicos__fecha_certificado_fiscal" value="<fmt:formatDate value="${data.fecha_certificado_fiscal}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />

			
			<c:if test="${!empty 'IRPF, %'}"><label for="datos_economicos__irpf">IRPF, %</label></c:if>
<c:if test="${!empty data}">
	<c:set var="datos_economicos__irpf"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.irpf}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="datos_economicos__irpf"></c:set>
</c:if>

<input type="text" name="datos_economicos__irpf" id="datos_economicos__irpf" value="${datos_economicos__irpf}" size="10" maxlength="10"   onchange=""/>

		
			<c:if test="${!empty 'IVA, %'}"><label for="datos_economicos__iva">IVA, %</label></c:if>
<c:if test="${!empty data}">
	<c:set var="datos_economicos__iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="datos_economicos__iva"></c:set>
</c:if>

<input type="text" name="datos_economicos__iva" id="datos_economicos__iva" value="${datos_economicos__iva}" size="10" maxlength="10"   onchange=""/>

			
			<sql:query var="rs">
	
					SELECT '30 días' AS 'option', 1 AS 'value'
				
</sql:query>

<c:if test="${!empty 'Forma de pago'}"><label for="datos_economicos__forma_pago">Forma de pago</label></c:if>

<select name="datos_economicos__forma_pago" id="datos_economicos__forma_pago">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.forma_pago==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


		
		</div>
	
	</div>
</form>
