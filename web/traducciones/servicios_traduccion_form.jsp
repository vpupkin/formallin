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
		SELECT * FROM servicios_traduccion WHERE id_orden=?
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
            $('#servicios_traduccion').ajaxForm({ 
                // dataType identifies the expected content type of the server response 
                dataType:  'json', 
         
                // success identifies the function to invoke when the server response 
                // has been received 
                success:   processJson 
            }); 
        });
        
        //function enviaFormulario(){
	    //	document.getElementById('servicios_traduccion').submit();
	    //}

        function processJson(data){
        	alert(data.mensaje);
        	if(!data.error)
            	document.location = "servicios_traduccion_form.jsp?id=" + data.id;
        }
	</script>
	
    <form name="servicios_traduccion" id="servicios_traduccion" action="servicios_traduccion_procesa.jsp">
    
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

			<div class="formulario">

				<div class="columna_izq">
				
					<c:if test="${!empty data}">
	<input type="hidden" name="servicios_traduccion__id_orden"  value="${data.id_orden}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="servicios_traduccion__id_orden"  value="${id_orden}"  />
</c:if>

				
					<c:if test="${!empty 'Nº Formulario'}"><label for="servicios_traduccion__num_formulario">Nº Formulario</label></c:if>
<input type="text" name="servicios_traduccion__num_formulario" id="servicios_traduccion__num_formulario" value="${data.num_formulario}" size="10" maxlength="10" onchange="" />

					<br/>
				
					<sql:query var="rs">
	
							SELECT CONCAT_WS(',', c.apellidos, c.nombre) AS 'option', ct.id AS 'value'
							FROM cliente_traduccion ct
								INNER JOIN cliente c ON c.idCliente=ct.id
						
</sql:query>

<c:if test="${!empty 'Cliente'}"><label for="servicios_traduccion__id_cliente">Cliente</label></c:if>

<select name="servicios_traduccion__id_cliente" id="servicios_traduccion__id_cliente">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.id_cliente==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>

<c:if test="${true}">
	<button id="bDatos_id_cliente" type="button">Datos</button>
	<script type="text/javascript">
		document.getElementById('bDatos_id_cliente').onclick = 
			function(){
				var w = 1152;
				var h = 720;
				var winl = (screen.width - w) / 2;
				var wint = (screen.height - h) / 2;
			
				var selectElement = document.getElementById('servicios_traduccion__id_cliente');
				var selectValue = selectElement[selectElement.selectedIndex].value;

				if(selectValue != null && selectValue != ''){

					var url = '${baseUrl}/'+'registro_clientes/cliente_form.jsp?menu=false&filtro_idTipoCli=10&idCliente='+selectValue;
				
					window.open(url, "Datos",
						"dependent=yes,toolbar=no,resizable=yes,scrollbars=yes,status=no,width="
						+ w + ",height=" + h + ",left=" + winl + ",top=" + wint);
				}

		};
	</script>
</c:if>

					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.nuevo_cliente}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Nuevo Cliente'}"><label for="servicios_traduccion__nuevo_cliente">Nuevo Cliente</label></c:if>
<input type="checkbox" name="servicios_traduccion__nuevo_cliente" ${checked} />

					<br/>
					
					<c:if test="${!empty data.id_cliente}">
	<sql:query var="rs">
		
							SELECT nombre AS 'option', id AS 'value'
							FROM cliente_traduccion_contactos 
							WHERE fk_idClienteTraduccion=?
						
		<sql:param>${data.id_cliente}</sql:param>
	</sql:query>
</c:if>

<c:if test="${!empty 'Persona Contacto'}">
<label for="servicios_traduccion__persona_contacto">Persona Contacto</label>
</c:if>
<select name="servicios_traduccion__persona_contacto" id="servicios_traduccion__persona_contacto">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.persona_contacto==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>


<script type="text/javascript">
	document.getElementById('servicios_traduccion__id_cliente').onchange = 
		function(){
			var paramValue = this[this.selectedIndex].value;
			//var params = new Array();
			//params['id_cliente']=paramValue;
			reloadSelect('servicios_traduccion__persona_contacto', '${baseUrl}/traducciones/persona_contacto_options.jsp', {id_cliente:paramValue});
		};

	/**
	 * Recargar un Select con datos recibidos por ajax.
	 * 
	 * @param id_select - 
	 * @param url - url para ajax
	 * @param params - lista de parametros para añadir a url
	 * 
	 * @author sergi 
	 */
	function reloadSelect(select_id, url, params){
		
		selectElement = document.getElementById(select_id);

		$.post(url, params,
			function(data){
				selectElement.innerHTML = data;
			}
		);
	}
	
</script>

<c:remove var="rs"/>

					<br/>
					<!-- 
					Mail
					-->
					
					<sql:query var="rs">
	
							SELECT nombre AS 'option', id AS 'value'
							FROM tipo_servicios_traduccion
						
</sql:query>

<c:if test="${!empty 'Tipo Servicio'}"><label for="servicios_traduccion__tipo_servicio">Tipo Servicio</label></c:if>

<select name="servicios_traduccion__tipo_servicio" id="servicios_traduccion__tipo_servicio">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.tipo_servicio==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>

<c:if test="${!empty param.id}">
	<button id="bDatos_tipo_servicio" type="button">Datos</button>
	<script type="text/javascript">
		document.getElementById('bDatos_tipo_servicio').onclick = 
			function(){
				var w = 1152;
				var h = 720;
				var winl = (screen.width - w) / 2;
				var wint = (screen.height - h) / 2;
			
				var selectElement = document.getElementById('servicios_traduccion__tipo_servicio');
				var selectValue = selectElement[selectElement.selectedIndex].value;

				if(selectValue != null && selectValue != ''){

					var url = '${baseUrl}/'+'traducciones/tipo_servicio.jsp?id=${param.id}&tipo='+selectValue;
				
					window.open(url, "Datos",
						"dependent=yes,toolbar=no,resizable=yes,scrollbars=yes,status=no,width="
						+ w + ",height=" + h + ",left=" + winl + ",top=" + wint);
				}

		};
	</script>
</c:if>

					<br/>
					
					<c:if test="${!empty 'Fecha Entrada'}"><label for="servicios_traduccion__fecha_entrada">Fecha Entrada</label></c:if>
<input type="text" name="servicios_traduccion__fecha_entrada" id="servicios_traduccion__fecha_entrada" value="<fmt:formatDate value="${data.fecha_entrada}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />
	
					<br/>
					
					<c:if test="${!empty 'Fecha Límite'}"><label for="servicios_traduccion__fecha_limita">Fecha Límite</label></c:if>
<input type="text" name="servicios_traduccion__fecha_limita" id="servicios_traduccion__fecha_limita" value="<fmt:formatDate value="${data.fecha_limita}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />
	
					<br/>
					
					<c:if test="${!empty 'Fecha Salida'}"><label for="servicios_traduccion__fecha_salida">Fecha Salida</label></c:if>
<input type="text" name="servicios_traduccion__fecha_salida" id="servicios_traduccion__fecha_salida" value="<fmt:formatDate value="${data.fecha_salida}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />

					
					
<c:set var="checked" value=""/>
<c:if test="${data.trados}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Trados'}"><label for="servicios_traduccion__trados">Trados</label></c:if>
<input type="checkbox" name="servicios_traduccion__trados" ${checked} />

					<br/>
					
					<sql:query var="rs">
	
							SELECT CONCAT_WS(',', c.apellidos, c.nombre) AS 'option', e.id AS 'value'
							FROM empleado e
								INNER JOIN cliente c ON c.idCliente=e.fk_idPersona
							WHERE e.fk_idDpto=3
						
</sql:query>

<c:if test="${!empty 'Alineador'}"><label for="servicios_traduccion__alineador">Alineador</label></c:if>

<select name="servicios_traduccion__alineador" id="servicios_traduccion__alineador">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.alineador==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


					<br/>
		
				</div>
	
				<div class="columna_drcha">
	
					<sql:query var="rs">
	
							SELECT idioma AS 'option', id AS 'value'
							FROM idioma2
						
</sql:query>

<c:if test="${!empty 'Idioma Original'}"><label for="servicios_traduccion__ididiomaoriginal">Idioma Original</label></c:if>

<select name="servicios_traduccion__ididiomaoriginal" id="servicios_traduccion__ididiomaoriginal">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.ididiomaoriginal==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


					<br/>
					
					<sql:query var="rs">
	
							SELECT idioma AS 'option', id AS 'value'
							FROM idioma2
						
</sql:query>

<c:if test="${!empty 'Idioma Solicitado'}"><label for="servicios_traduccion__ididiomasolicita">Idioma Solicitado</label></c:if>

<select name="servicios_traduccion__ididiomasolicita" id="servicios_traduccion__ididiomasolicita">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.ididiomasolicita==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.enviado_pto}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Enviado Pto'}"><label for="servicios_traduccion__enviado_pto">Enviado Pto</label></c:if>
<input type="checkbox" name="servicios_traduccion__enviado_pto" ${checked} />

					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.aprobado_pto}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Aprobado Pto'}"><label for="servicios_traduccion__aprobado_pto">Aprobado Pto</label></c:if>
<input type="checkbox" name="servicios_traduccion__aprobado_pto" ${checked} />

					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.entregado}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Entregado'}"><label for="servicios_traduccion__entregado">Entregado</label></c:if>
<input type="checkbox" name="servicios_traduccion__entregado" ${checked} />

					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.cancelado}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Cancelado'}"><label for="servicios_traduccion__cancelado">Cancelado</label></c:if>
<input type="checkbox" name="servicios_traduccion__cancelado" ${checked} />

					
					<sql:query var="rs">
	
							SELECT 'Plazo' AS 'option', 1 AS 'value'
							UNION SELECT 'Precio',2
							UNION SELECT 'No lo necesita',3
							UNION SELECT 'Sin respuesta',4
							UNION SELECT 'Otros',5
						
</sql:query>

<c:if test="${!empty ''}"><label for="servicios_traduccion__id_cancelacion"></label></c:if>

<select name="servicios_traduccion__id_cancelacion" id="servicios_traduccion__id_cancelacion">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.id_cancelacion==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


					
					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.facturado}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Facturado'}"><label for="servicios_traduccion__facturado">Facturado</label></c:if>
<input type="checkbox" name="servicios_traduccion__facturado" ${checked} />

				
					<c:if test="${!empty 'Comentario'}"><label for="servicios_traduccion__comentario">Comentario</label></c:if>
<textarea name="servicios_traduccion__comentario" cols="60" rows="2"><c:out value="${data.comentario}"/></textarea>

					<br/>
					</div>
					
				</div>
				
				<div class="antifloat"></div>
					
				<c:url var="u" value="/report">
					<c:param name="jrxml">traducciones/FormServicios.jrxml</c:param>
					<c:param name="format">pdf</c:param>
					<c:param name="id_orden">${data.id_orden}</c:param>
				</c:url>
				<a href="${u}">Presupuesto en PDF</a>
				<br/>
				
				<c:url var="u" value="/report">
					<c:param name="jrxml">traducciones/FormServicios.jrxml</c:param>
					<c:param name="format">odt</c:param>
					<c:param name="id_orden">${data.id_orden}</c:param>
				</c:url>
				<a href="${u}">Presupuesto en ODT</a>
				<br/>
			</div>
		</div>

	</form>
	
<c:import url="/pie.jsp"/>
