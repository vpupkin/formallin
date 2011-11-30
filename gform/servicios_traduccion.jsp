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
	
    <form table="servicios_traduccion" xmlns:c="http://java.sun.com/jsp/jstl/core">
    
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
				
					<element>
						<name>id_orden</name>
						<type>int</type>
						<key>true</key>
						<hidden>true</hidden>
					</element>
				
					<element>
						<label>Nº Formulario</label>
						<name>num_formulario</name>
						<type>int</type>
					</element>
					<br/>
				
					<element>
						<label>Cliente</label>
						<name>id_cliente</name>
						<type>select</type>
						<references>cliente_traduccion(id)</references>
						<query>
							SELECT CONCAT_WS(',', c.apellidos, c.nombre) AS 'option', ct.id AS 'value'
							FROM cliente_traduccion ct
								INNER JOIN cliente c ON c.idCliente=ct.id
						</query>
						<button>true</button>
						<button_url>registro_clientes/cliente_form.jsp?menu=false&amp;filtro_idTipoCli=10&amp;idCliente=</button_url>
					</element>
					<br/>
					
					<element>
						<label>Nuevo Cliente</label>
						<name>nuevo_cliente</name>
						<type>checkbox</type>
					</element>
					<br/>
					
					<element>
						<label>Persona Contacto</label>
						<name>persona_contacto</name>
						<type>ajaxSelect</type>
						<query>
							SELECT nombre AS 'option', id AS 'value'
							FROM cliente_traduccion_contactos 
							WHERE fk_idClienteTraduccion=?
						</query>
						<refelement>id_cliente</refelement>
					</element>
					<br/>
					<!-- 
					Mail
					-->
					
					<element>
						<label>Tipo Servicio</label>
						<name>tipo_servicio</name>
						<type>select</type>
						<references>tipo_servicios_traduccion(id)</references>
						<query>
							SELECT nombre AS 'option', id AS 'value'
							FROM tipo_servicios_traduccion
						</query>
						<button>true</button>
						<button_url>
							traducciones/tipo_servicio.jsp?id=${param.id}&amp;tipo=
						</button_url>
						<button_if>!empty param.id</button_if>
					</element>
					<br/>
					
					<element>
						<label>Fecha Entrada</label>
						<name>fecha_entrada</name>
						<type>datetime</type>
					</element>	
					<br/>
					
					<element>
						<label>Fecha Límite</label>
						<name>fecha_limita</name>
						<type>datetime</type>
					</element>	
					<br/>
					
					<element>
						<label>Fecha Salida</label>
						<name>fecha_salida</name>
						<type>datetime</type>
					</element>
					
					<element>
						<label>Trados</label>
						<name>trados</name>
						<type>checkbox</type>
					</element>
					<br/>
					
					<element>
						<label>Alineador</label>
						<name>alineador</name>
						<type>select</type>
						<references>empleado(id)</references>
						<query>
							SELECT CONCAT_WS(',', c.apellidos, c.nombre) AS 'option', e.id AS 'value'
							FROM empleado e
								INNER JOIN cliente c ON c.idCliente=e.fk_idPersona
							WHERE e.fk_idDpto=3
						</query>
					</element>
					<br/>
		
				</div>
	
				<div class="columna_drcha">
	
					<element>
						<label>Idioma Original</label>
						<name>ididiomaoriginal</name>
						<type>select</type>
						<references>idioma(idIdioma)</references>
						<query>
							SELECT idioma AS 'option', id AS 'value'
							FROM idioma2
						</query>
					</element>
					<br/>
					
					<element>
						<label>Idioma Solicitado</label>
						<name>ididiomasolicita</name>
						<type>select</type>
						<references>idioma(idIdioma)</references>
						<query>
							SELECT idioma AS 'option', id AS 'value'
							FROM idioma2
						</query>
					</element>
					<br/>
					
					<element>
						<label>Enviado Pto</label>
						<name>enviado_pto</name>
						<type>checkbox</type>
					</element>
					<br/>
					
					<element>
						<label>Aprobado Pto</label>
						<name>aprobado_pto</name>
						<type>checkbox</type>
					</element>
					<br/>
					
					<element>
						<label>Entregado</label>
						<name>entregado</name>
						<type>checkbox</type>
					</element>
					<br/>
					
					<element>
						<label>Cancelado</label>
						<name>cancelado</name>
						<type>checkbox</type>
					</element>
					
					<element>
						<name>id_cancelacion</name>
						<type>select</type>
						<query>
							SELECT 'Plazo' AS 'option', 1 AS 'value'
							UNION SELECT 'Precio',2
							UNION SELECT 'No lo necesita',3
							UNION SELECT 'Sin respuesta',4
							UNION SELECT 'Otros',5
						</query>
					</element>
					
					<br/>
					
					<element>
						<label>Facturado</label>
						<name>facturado</name>
						<type>checkbox</type>
					</element>
				
					<element>
						<label>Comentario</label>
						<name>comentario</name>
						<type>textarea</type>
						<cols>60</cols>
						<rows>2</rows>
					</element>
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