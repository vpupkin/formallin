<%@page	contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="/dataSource.jsp" %>

<c:import url="/cabecera.jsp">
	<c:param name="menu" value="false"/>
</c:import>

<c:set var="oper" value="add"/>
<c:if test="${!empty param.id}">
	<sql:query var="rs">
		SELECT * FROM revision WHERE id_orden=?
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
        
        //function enviaFormulario(){
	    //	document.getElementById('servicios_traduccion').submit();
	    //}
		
		$(document).ready(function() { 
            // bind form using ajaxForm 
            $('#revision').ajaxForm({ 
                // dataType identifies the expected content type of the server response 
                dataType:  'json', 
         
                // success identifies the function to invoke when the server response 
                // has been received 
                success:   processJson 
            }); 
        });
        
        function processJson(data){
        	alert(data.mensaje);
        	if(!data.error)
            	document.location = "revision_form.jsp?id=" + data.id;
        }


        /**
         * Recalucular resto de campos del formumlario despues de cambir un campo.
         * La función se ejecuta cundo hay un evento onChange.
         */
        function recalc(){

            var palabras = currencyToNumber($('input#revision__palabras').val());
			var tarifaCSI = currencyToNumber($('input#revision__tarifa_csi').val());
			var totalCSI = palabras * tarifaCSI;
			
			$('input#revision__total_csi').val(totalCSI);
			$('input#revision__total_csi').formatCurrency();
			
			var totalTraductor = currencyToNumber($('input#revision__total_traductor').val());
			var costeGG = currencyToNumber($('input#revision__coste_gg').val());
			var otrosGastos = currencyToNumber($('input#revision__otros_gastos').val());
			var totalCoste = totalTraductor + costeGG + otrosGastos;
			
			$('input#revision__total_coste').val(totalCoste);
			$('input#revision__total_coste').formatCurrency();

			var precioGG = currencyToNumber($('input#revision__Precio_GG').val());
			var gastosEnvio = currencyToNumber($('input#revision__gastos_envio').val());

			var plusUrgencia = percentageToNumber($('input#revision__plus_urgencia').val());

			var totalAux = totalCSI + precioGG + otrosGastos + gastosEnvio;

			var total = totalAux + totalAux * plusUrgencia;

			$('input#revision__total').val(total);
			$('input#revision__total').formatCurrency();

			var iva = currencyToNumber($('input#revision__iva').val());
			$('input#revision__total_iva').val(total + total * iva);	
			$('input#revision__total_iva').formatCurrency();
			
			var mb = total - totalCoste;
			$('input#revision__mb').val(mb);
			$('input#revision__mb').formatCurrency();	

			var mb100 = Math.round((mb/total)*100);
			$('input#revision__mb100').val(mb100);
        }

        /**
         * Copiar valores de campos desde pestaña Presupuesto a Facturación.
         * La función se ejecuta con boton "Pasar a facturación".
         */
        function facturar(){

        	var palabras = $('input#revision__palabras').asNumber();
        	
			var totalCSI = $('input#revision__total_csi').asNumber();
			var otrosGastos = $('input#revision__otros_gastos').asNumber();
			var gastosGestion =	$('input#revision__Precio_GG').asNumber();
			var gastosEnvio = $('input#revision__gastos_envio').asNumber();
			var iva = $('input#revision__iva').asNumber();
			var totalTraductor = $('input#revision__total_traductor').asNumber();
				
        	$('input#revision__ftotal_csi').val(totalCSI);
        	$('input#revision__ftotal_csi').formatCurrency();
        	
        	$('input#revision__fotros_gastos').val(otrosGastos);
        	$('input#revision__fotros_gastos').formatCurrency();
        	
        	$('input#revision__fpalabras').val(palabras);

        	$('input#revision__fgastos_gestion').val(gastosGestion);
        	$('input#revision__fgastos_gestion').formatCurrency();
        	
        	$('input#revision__fgastos_envio').val(gastosEnvio);
        	$('input#revision__fgastos_envio').formatCurrency();

        	var total = totalCSI + otrosGastos + gastosGestion + gastosEnvio;
        	
        	$('input#revision__ftotal').val(total);
        	$('input#revision__ftotal').formatCurrency();

        	$('input#revision__ftotal_iva').val(total + total * iva);
        	$('input#revision__ftotal_iva').formatCurrency();

        	var fmb = total - totalTraductor;
        	$('input#revision__fmb').val(fmb);
        	$('input#revision__fmb').formatCurrency();

        	$('input#revision__fmb100').val((fmb / total) * 100);
        	$('input#revision__fmb100').formatCurrency();
        }


        /**
         * Recalucular resto de campos del subformumlario Traductores despues de cambir un campo.
         * La función se ejecuta cundo hay un evento onChange en jGrid-form.
         */
        function recalc_subform_traductores(){

        	var minimo = $('input#servicios_traduccion_traductor__minimo').asNumber();
        	var iva = $('input#servicios_traduccion_traductor__iva').asNumber();
        	var irpf = $('input#servicios_traduccion_traductor__irpf').asNumber();

        	var totalTraductor = 0;
        	
			if(minimo != 0){
				totalTraductor = minimo;
			}
			else {
				var palabras = $('input#servicios_traduccion_traductor__palabras').asNumber();
				var tarifa = $('input#servicios_traduccion_traductor__tarifa_traductor').asNumber();
				totalTraductor = palabras * tarifa;
			}

			var totalIVA = totalTraductor * iva;
			var totalIRPF = totalTraductor * irpf;
			var coste = totalTraductor + totalIVA - totalIRPF;
			                                  			
			$('input#servicios_traduccion_traductor__total_traductor').val(totalTraductor);
			$('input#servicios_traduccion_traductor__total_traductor').formatCurrency();

			$('input#servicios_traduccion_traductor__total_iva').val(totalIVA);
			$('input#servicios_traduccion_traductor__total_iva').formatCurrency();
			
			$('input#servicios_traduccion_traductor__total_irpf').val(totalIRPF);
			$('input#servicios_traduccion_traductor__total_irpf').formatCurrency();

			$('input#servicios_traduccion_traductor__coste_final').val(coste);
			$('input#servicios_traduccion_traductor__coste_final').formatCurrency();
			
        }
	</script>
	
    <h2>Revisión</h2>
    
    <form name="revision" id="revision" action="revision_procesa.jsp">
    
	    <div class="botones" align="right">
	    	<input type="submit" class="boton" value="Guardar"/>
	        <c:url var="u" value=""/>
	        <input type="button" class="boton" value="Cancelar" onclick="document.location='${u}'"/>
	    </div>
	    
	     <div class="tabs">
	        <ul>
	            <li id="li_tab1" onmousedown="clickTab(this, 'div_tab1')" class="current">
	                <a href="javascript:void(null);">Presupuesto</a>
	            </li>
	            <li id="li_tab2" onmousedown="clickTab(this, 'div_tab2')">
	                <a href="javascript:void(null);">Traductores</a>
	            </li>
	            <li id="li_tab3" onmousedown="clickTab(this, 'div_tab3')">
	                <a href="javascript:void(null);">Facturación</a>
	            </li>
	        </ul>
	    </div>
    
    	<input type="hidden" id="oper" name="oper" value="${oper}"/>
    	<input type="hidden" id="tipo" name="tipo" value="${param.tipo}"/>
    	<input type="hidden" id="json" name="json" value="true"/>
    
    	<div class="tabs_cont" style="height:100%">

			<div id="div_tab1" class="current">
				<div class="formulario">
					<br/>
					<div class="columna_izq">
						
						<c:set var="id_orden" value="${param.id}"/>
						
						<c:if test="${!empty data}">
	<input type="hidden" name="revision__id_orden"  value="${data.id_orden}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="revision__id_orden"  value="${id_orden}"  />
</c:if>

						<br/>
						
						<sql:query var="rs">
	
								SELECT 'Con cuño' AS 'option', 1 AS 'value'
								UNION SELECT 'Jurada',2
								UNION SELECT 'Normal',3
							
</sql:query>

<c:if test="${!empty 'Tipo Revisión'}"><label for="revision__tipo_revision">Tipo Revisión</label></c:if>

<select name="revision__tipo_revision" id="revision__tipo_revision">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.tipo_revision==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


						<br/>
						
						<sql:query var="rs">
	
								SELECT tipo AS 'option', id AS 'value' FROM tipoDocumento ORDER BY tipo
							
</sql:query>

<c:if test="${!empty 'Tipo Doc'}"><label for="revision__tipo_doc">Tipo Doc</label></c:if>

<select name="revision__tipo_doc" id="revision__tipo_doc">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.tipo_doc==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


						
						<sql:query var="rs">
	
								SELECT tema AS 'option', id AS 'value' FROM tema ORDER BY tema
							
</sql:query>

<c:if test="${!empty 'Tema'}"><label for="revision__id_tema">Tema</label></c:if>

<select name="revision__id_tema" id="revision__id_tema">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.id_tema==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


						<br/>
												
						<c:if test="${!empty 'Título'}"><label for="revision__nombre_doc">Título</label></c:if>
<input type="text" name="revision__nombre_doc" id="revision__nombre_doc" value="${data.nombre_doc}" size="50" maxlength="50" />

					</div>
					
					<div class="columna_drcha">	
						<c:if test="${!empty 'Núm Páginas'}"><label for="revision__num_paginas">Núm Páginas</label></c:if>
<input type="text" name="revision__num_paginas" id="revision__num_paginas" value="${data.num_paginas}" size="10" maxlength="10" onchange="" />

						<br/>
						
						<c:if test="${!empty 'Comentario'}"><label for="revision__comentario">Comentario</label></c:if>
<textarea name="revision__comentario" cols="20" rows="2"><c:out value="${data.comentario}"/></textarea>

						<br/>
					
					</div>	
					
					<div class="antifloat"></div>
						<hr/>
						<br/>
					
					<div class="columna_izq">
						<c:if test="${!empty 'Horas/Palabras'}"><label for="revision__palabras">Horas/Palabras</label></c:if>
<input type="text" name="revision__palabras" id="revision__palabras" value="${data.palabras}" size="10" maxlength="10" onchange="" readonly/>

						<br/>
						
						<c:if test="${!empty 'Total traductor/es'}"><label for="revision__total_traductor">Total traductor/es</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__total_traductor"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_traductor}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__total_traductor"></c:set>
</c:if>

<input type="text" name="revision__total_traductor" id="revision__total_traductor" value="${revision__total_traductor}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Tarifa CSI'}"><label for="revision__tarifa_csi">Tarifa CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__tarifa_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.tarifa_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__tarifa_csi">0,084</c:set>
</c:if>

<input type="text" name="revision__tarifa_csi" id="revision__tarifa_csi" value="${revision__tarifa_csi}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total CSI'}"><label for="revision__total_csi">Total CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__total_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__total_csi"></c:set>
</c:if>

<input type="text" name="revision__total_csi" id="revision__total_csi" value="${revision__total_csi}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Gastos de envio'}"><label for="revision__gastos_envio">Gastos de envio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__gastos_envio"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.gastos_envio}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__gastos_envio"></c:set>
</c:if>

<input type="text" name="revision__gastos_envio" id="revision__gastos_envio" value="${revision__gastos_envio}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Coste gastos gestión'}"><label for="revision__coste_gg">Coste gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__coste_gg"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.coste_gg}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__coste_gg"></c:set>
</c:if>

<input type="text" name="revision__coste_gg" id="revision__coste_gg" value="${revision__coste_gg}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Precio gastos gestión'}"><label for="revision__Precio_GG">Precio gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__Precio_GG"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.Precio_GG}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__Precio_GG"></c:set>
</c:if>

<input type="text" name="revision__Precio_GG" id="revision__Precio_GG" value="${revision__Precio_GG}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Otros gastos'}"><label for="revision__otros_gastos">Otros gastos</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__otros_gastos"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.otros_gastos}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__otros_gastos"></c:set>
</c:if>

<input type="text" name="revision__otros_gastos" id="revision__otros_gastos" value="${revision__otros_gastos}" size="10" maxlength="10"  onchange="recalc()"/>

						
					</div>
					
					<div class="columna_drcha">
						
						<c:if test="${!empty 'Plus urgencia, %'}"><label for="revision__plus_urgencia">Plus urgencia, %</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__plus_urgencia"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.plus_urgencia}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__plus_urgencia"></c:set>
</c:if>

<input type="text" name="revision__plus_urgencia" id="revision__plus_urgencia" value="${revision__plus_urgencia}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total coste'}"><label for="revision__total_coste">Total coste</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__total_coste"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_coste}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__total_coste"></c:set>
</c:if>

<input type="text" name="revision__total_coste" id="revision__total_coste" value="${revision__total_coste}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Total precio'}"><label for="revision__total">Total precio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__total"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__total"></c:set>
</c:if>

<input type="text" name="revision__total" id="revision__total" value="${revision__total}" size="10" maxlength="10" readonly onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'IVA'}"><label for="revision__iva">IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__iva">0,16</c:set>
</c:if>

<input type="text" name="revision__iva" id="revision__iva" value="${revision__iva}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total+IVA'}"><label for="revision__total_iva">Total+IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__total_iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__total_iva"></c:set>
</c:if>

<input type="text" name="revision__total_iva" id="revision__total_iva" value="${revision__total_iva}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
												
						<c:if test="${!empty 'MB'}"><label for="revision__mb">MB</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__mb"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.mb}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__mb"></c:set>
</c:if>

<input type="text" name="revision__mb" id="revision__mb" value="${revision__mb}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
												
						<c:if test="${!empty 'MB, %'}"><label for="revision__mb100">MB, %</label></c:if>
<input type="text" name="revision__mb100" id="revision__mb100" value="${data.mb100}" size="10" maxlength="10" onchange="" readonly/>

						<br/>

						<label> </label>
						<button type="button" onclick="facturar()">Pasar a facturación</button>
						
					</div>
				</div>
			</div>
			
			<div id="div_tab2" class="div_cont">
				<div class="formulario">
					<div>
						
<script type="text/javascript">

	jQuery(document).ready(function(){

		var mygrid = jQuery("#listado").jqGrid({
	        url:"${baseUrl}"+"/traducciones/grid_traductores_2_xml.jsp?id=${param.id}&tipo=${param.tipo}",
	        datatype: "xml",
	        colNames:["id","Traductor","Palabras","Mínimo","Tarifa traductor","Fecha frac","Nº frac","Total traductor","IVA","IRPF","Total IVA","Total IRPF","Coste final","Facturado Traductor"],
	        colModel:[{name:'servicios_traduccion_traductor__id', index:'servicios_traduccion_traductor__id', width:0, align:'right', search:true, editable:true, hidden:true},
{name:'servicios_traduccion_traductor__id_traductor', index:'servicios_traduccion_traductor__id_traductor', width:0, align:'right', search:true, editable:true, hidden:false, edittype:'select',
<sql:query var="rs">
	
			SELECT CONCAT_WS(',', apellidos, nombre) AS 'option', id AS 'value'
			FROM traductor
				INNER JOIN cliente ON idCliente=id
		
</sql:query>
<c:set var="itemValues" value=""/>
<c:forEach items="${rs.rows}" var="item" varStatus="s">
	<c:set var="itemValues">${itemValues}${item.value}:${item.option}<c:if test="${!s.last}">;</c:if></c:set>
</c:forEach>

<c:remove var="rs"/>

editoptions:{value:"${itemValues}"}
},
{name:'servicios_traduccion_traductor__palabras', index:'servicios_traduccion_traductor__palabras', width:0, align:'right', search:true, editable:true, hidden:false, editoptions:{dataEvents: [ { type:'change', fn: function(e) {recalc_subform_traductores()}}]}},
{name:'servicios_traduccion_traductor__minimo', index:'servicios_traduccion_traductor__minimo', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:2, prefix:''}, editoptions:{dataEvents: [ { type:'change', fn: function(e) {recalc_subform_traductores()}}]}},
{name:'servicios_traduccion_traductor__tarifa_traductor', index:'servicios_traduccion_traductor__tarifa_traductor', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:4, prefix:''}, editoptions:{dataEvents: [ { type:'change', fn: function(e) {recalc_subform_traductores()}}]}},
{name:'servicios_traduccion_traductor__fecha_factura_trad', index:'servicios_traduccion_traductor__fecha_factura_trad', width:0, align:'right', search:true, editable:true, hidden:false},
{name:'servicios_traduccion_traductor__num_factura_trad', index:'servicios_traduccion_traductor__num_factura_trad', width:0, align:'right', search:true, editable:true, hidden:false},
{name:'servicios_traduccion_traductor__total_traductor', index:'servicios_traduccion_traductor__total_traductor', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:2, prefix:''}, editoptions: {readonly: 'readonly'}},
{name:'servicios_traduccion_traductor__iva', index:'servicios_traduccion_traductor__iva', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:4, prefix:''}, editoptions:{dataEvents: [ { type:'change', fn: function(e) {recalc_subform_traductores()}}]}},
{name:'servicios_traduccion_traductor__irpf', index:'servicios_traduccion_traductor__irpf', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:4, prefix:''}, editoptions:{dataEvents: [ { type:'change', fn: function(e) {recalc_subform_traductores()}}]}},
{name:'servicios_traduccion_traductor__total_iva', index:'servicios_traduccion_traductor__total_iva', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:2, prefix:''}},
{name:'servicios_traduccion_traductor__total_irpf', index:'servicios_traduccion_traductor__total_irpf', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:2, prefix:''}},
{name:'servicios_traduccion_traductor__coste_final', index:'servicios_traduccion_traductor__coste_final', width:0, align:'right', search:true, editable:true, hidden:false, formatter:'currency', formatoptions:{decimalSeparator:',', thousandsSeparator:'.', decimalPlaces:2, prefix:''}},
{name:'servicios_traduccion_traductor__facturado_traductor', index:'servicios_traduccion_traductor__facturado_traductor', width:0, align:'right', search:true, editable:true, hidden:false, edittype:'checkbox', editoptions: {value:'Sí:'}}],
	        autowidth: false,
	        rownumbers: false,
	        pager: jQuery('#paginacion'),
	        rowNum:5,
	        rowList:[5,15,20,50],
	        height:120,
	        width:800,
	        sortname: 'orden',
	        mtype: "POST",
	        viewrecords: true,
	        sortorder: "asc",
	        gridview : true,
	        caption: "Traductores",
	        multiselect: false,
	        id: "id",
	        imgpath: "../scripts/themes/steel/images/",
	       	        
	        ondblClickRow: function(rowid){
	            jQuery("#listado").editGridRow( rowid, null);
	        },
	        gridComplete: function(data){
				
								
									var columns = jQuery("#listado").getCol("servicios_traduccion_traductor__palabras");
		        					var summa =0;
					    			for(i=0; i<columns.length; i++){
					    				summa = summa + parseInt(columns[i]);
					        		}
					    			if(!isNaN(summa)){
					    				$("input#revision__palabras").val(summa);
					    			}
					    			
					    			columns = jQuery("#listado").getCol("servicios_traduccion_traductor__total_traductor");
					    			summa =0;
					    			for(i=0; i<columns.length; i++){
					    				var columna = replaceAll(columns[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				summa = summa + parseInt(columns[i]);
					    			}
					    			$("input#revision__total_traductor").val(summa);
					    			$("input#revision__total_traductor").formatCurrency();
					    			recalc();
				    			
							
		    },
	        editurl:"${baseUrl}"+"/traducciones/servicios_traduccion_traductor_procesa.jsp?json=true&servicios_traduccion_traductor__id_orden=${param.id}&servicios_traduccion_traductor__tipo_servicio=${param.tipo}"
	    })
	    .navGrid('#paginacion',{edit:true,add:true,del:true,search:false,refresh:true});

		
		$("#b_add").click(function(){
			jQuery("#listado").editGridRow("new",{reloadAfterSubmit:false});
		});
/*
		$("#b_del").click(function(){
			var gr = jQuery("#listado").getGridParam('selrow');
			if( gr != undefined && gr != null) {
				var row = jQuery("#listado").getRowData(gr);
				jQuery("#listado").delGridRow(row.id,{reloadAfterSubmit:false});
			}else{
                alert("Por favor, seleccione un registro.");
            }
		});*/
	});

</script>


<div style="margin-left:160px;">
	<table id="listado"></table>
	<div id="paginacion"></div>
	<%--
	<button type="button" id="b_add">Nuevo</button>
	<button type="button" id="b_del">Borrar</button> --%>
</div>
 

					</div>
				</div>
			</div>
			
			<div id="div_tab3" class="div_cont">
			
				<div class="formulario">
				<div class="columna_izq">
					<br/>
					
					
<c:set var="checked" value=""/>
<c:if test="${data.facturado_cliente}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Facturado cliente'}"><label for="revision__facturado_cliente">Facturado cliente</label></c:if>
<input type="checkbox" name="revision__facturado_cliente" ${checked} />

					<br/>
					
					<c:if test="${!empty 'Fecha frac'}"><label for="revision__fecha_factura_cliente">Fecha frac</label></c:if>
<input type="text" name="revision__fecha_factura_cliente" id="revision__fecha_factura_cliente" value="<fmt:formatDate value="${data.fecha_factura_cliente}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />

					<br/>
					
					<c:if test="${!empty 'Nº frac'}"><label for="revision__num_factura_cliente">Nº frac</label></c:if>
<input type="text" name="revision__num_factura_cliente" id="revision__num_factura_cliente" value="${data.num_factura_cliente}" size="50" maxlength="50" />

					<br/>
					
					<c:if test="${!empty 'Total CSI'}"><label for="revision__ftotal_csi">Total CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__ftotal_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__ftotal_csi"></c:set>
</c:if>

<input type="text" name="revision__ftotal_csi" id="revision__ftotal_csi" value="${revision__ftotal_csi}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Otros gastos'}"><label for="revision__fotros_gastos">Otros gastos</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__fotros_gastos"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fotros_gastos}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__fotros_gastos"></c:set>
</c:if>

<input type="text" name="revision__fotros_gastos" id="revision__fotros_gastos" value="${revision__fotros_gastos}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
			
					<c:if test="${!empty 'Gastos gestión'}"><label for="revision__fgastos_gestion">Gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__fgastos_gestion"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fgastos_gestion}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__fgastos_gestion"></c:set>
</c:if>

<input type="text" name="revision__fgastos_gestion" id="revision__fgastos_gestion" value="${revision__fgastos_gestion}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Gastos envio'}"><label for="revision__fgastos_envio">Gastos envio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__fgastos_envio"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fgastos_envio}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__fgastos_envio"></c:set>
</c:if>

<input type="text" name="revision__fgastos_envio" id="revision__fgastos_envio" value="${revision__fgastos_envio}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Total Final'}"><label for="revision__ftotal">Total Final</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__ftotal"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__ftotal"></c:set>
</c:if>

<input type="text" name="revision__ftotal" id="revision__ftotal" value="${revision__ftotal}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Total+IVA'}"><label for="revision__ftotal_iva">Total+IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__ftotal_iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal_iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__ftotal_iva"></c:set>
</c:if>

<input type="text" name="revision__ftotal_iva" id="revision__ftotal_iva" value="${revision__ftotal_iva}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'MB'}"><label for="revision__fmb">MB</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__fmb"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fmb}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__fmb"></c:set>
</c:if>

<input type="text" name="revision__fmb" id="revision__fmb" value="${revision__fmb}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'MB, %'}"><label for="revision__fmb100">MB, %</label></c:if>
<c:if test="${!empty data}">
	<c:set var="revision__fmb100"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fmb100}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="revision__fmb100"></c:set>
</c:if>

<input type="text" name="revision__fmb100" id="revision__fmb100" value="${revision__fmb100}" size="10" maxlength="10"  onchange=""/>

				</div>
				</div>
			</div>
			
		</div>
	</form>

<c:import url="/pie.jsp"/>
