<%@page	contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="/dataSource.jsp" %>

<c:import url="/cabecera.jsp"/>

<%--
<fmt:setBundle basename="gform"/>
<fmt:message key="$key.$name"/>
--%>
<c:set var="oper" value="add"/>
<c:if test="${!empty param.id}">
	<sql:query var="rs">
		SELECT * FROM tipo_servicios_traduccion WHERE id=?
		<sql:param>${param.id}</sql:param>
	</sql:query>
	<c:if test="${rs.rowCount>0}">
		<c:set var="data" value="${rs.rows[0]}"/>
		<c:set var="oper" value="edit"/>
	</c:if>
	<c:remove var="rs"/>
</c:if>


<c:if test="${not empty param.mensaje}">
	<script type="text/javascript">
		$(document).ready(function() {
  			alert('${param.mensaje}');
		});
	</script>
</c:if>

    <script type="text/javascript">
    	// TABS -----------------------
        var li_sel_id='li_tab1';
        var div_sel_id='div_tab1';
	
        function clickTab(li, div_id){
            var li_id = li.id;
            document.getElementById(li_sel_id).className = "div_cont";
            document.getElementById(li_id).className = "current";
            li_sel_id = li_id;
            document.getElementById(div_sel_id).className="div_cont";
            document.getElementById(div_id).className="current";
            div_sel_id = div_id;
        }
        // END TABS -------------------
        
        $(document).ready(function() { 
            // bind form using ajaxForm 
            $('#tipo_servicios_traduccion').ajaxForm({ 
                // dataType identifies the expected content type of the server response 
                dataType:  'json', 
         
                // success identifies the function to invoke when the server response 
                // has been received 
                success:   processJson 
            }); 
        });
        
	</script>
	
	<div class="migas">
	    <br/>
	    <strong>Servicio de Traducci&oacute;n::Tipos de servicios</strong>
	    <c:choose>
	        <c:when test="${oper == 'edit'}">->Modificando datos</c:when>
	        <c:otherwise>->Insertando nuevo</c:otherwise>
	    </c:choose>
	</div>
	
	
    <form table="tipo_servicios_traduccion" xmlns:c="http://java.sun.com/jsp/jstl/core">
    	<br/>
	    <div class="botones" align="right">
	        <input type="submit" class="boton" value="Guardar"/>
	        <c:url var="u" value=""/>
	        <input type="button" class="boton" value="Cancelar" onclick="document.location='${u}'"/>
	    </div>
	    
	     <div class="tabs">
	        <ul>
	            <li id="li_tab1" onmousedown="clickTab(this,'div_tab1')" class="current">
	                <a href="javascript:void(null);">Servicio de traducción</a>
	            </li>
	        </ul>
	    </div>
    
    
    	<input type="hidden" id="oper" name="oper" value="${oper}"/>
    	<input type="hidden" id="json" name="json" value="true"/>
    
		<!-- 
			xmlns:g="http://www.w3.org/2001/XMLSchema-instance"
    		g:schemaLocation="http://www.example.com/ file:///D:/ProyectosE/SRI/gform/gform.xsd">
		-->
		
    	<div class="tabs_cont" style="height:100%">
    
			<div id="div_tab1" class="current">
			
			<br/>
			<br/>
			
			<div class="formulario_t">

				<div class="columna_drcha">
				
					<element>
						<name>id</name>
						<type>INT</type>
						<size>11</size>
						<key>true</key>
						<hidden>true</hidden>
					</element>
					
					<element>
						<label>Tipo servicio</label>
						<name>nombre</name>
						<type>varchar</type>
						<size>50</size>
					</element>
					
					<element>
						<label>Proyecto</label>
						<name>idProyecto</name>
						<type>select</type>
						<query>SELECT nombre AS 'option', idProyecto AS 'value' FROM proyectos</query>
						<references>proyectos(idProyecto)</references>
					</element>
				
					<element>
						<label>Tipo I.V.A.</label>
						<name>tipo_iva</name>
						<type>select</type>
						<query>SELECT CONCAT_WS('% - ', valor, descripcion) AS 'option', id AS 'value' FROM tipo_iva</query>
						<references>tipo_iva(id)</references>
					</element>
				
					<element>
						<label>Subcuenta</label>
						<name>codSubcuenta</name>
						<type>select</type>
						<query>SELECT CONCAT_WS(' - ', codSubcuenta, nombre) AS 'option', codSubcuenta AS 'value' FROM subcuentas</query>
						<references>subcuentas(codSubcuenta)</references>
					</element>
				
				</div>	
			</div>
			</div>
		</div>
	</form>
	
<c:import url="/pie.jsp"/>