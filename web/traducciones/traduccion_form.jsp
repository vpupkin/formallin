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
		SELECT * FROM traduccion WHERE id_orden=?
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
            $('#traduccion').ajaxForm({ 
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
            	document.location = "traduccion_form.jsp?id=" + data.id;
        }


        /**
         * Recalucular resto de campos del formumlario despues de cambir un campo.
         * La función se ejecuta cundo hay un evento onChange.
         */
        function recalc(){

            var palabras = currencyToNumber($('input#traduccion__palabras').val());
			var tarifaCSI = currencyToNumber($('input#traduccion__tarifa_csi').val());
			var totalCSI = palabras * tarifaCSI;
			
			$('input#traduccion__total_csi').val(totalCSI);
			$('input#traduccion__total_csi').formatCurrency();
			
			var totalTraductor = currencyToNumber($('input#traduccion__total_traductor').val());
			var costeGG = currencyToNumber($('input#traduccion__coste_gg').val());
			var otrosGastos = currencyToNumber($('input#traduccion__otros_gastos').val());
			var totalCoste = totalTraductor + costeGG + otrosGastos;
			
			$('input#traduccion__total_coste').val(totalCoste);
			$('input#traduccion__total_coste').formatCurrency();

			var precioGG = currencyToNumber($('input#traduccion__Precio_GG').val());
			var gastosEnvio = currencyToNumber($('input#traduccion__gastos_envio').val());

			var plusUrgencia = percentageToNumber($('input#traduccion__plus_urgencia').val());

			var totalAux = totalCSI + precioGG + otrosGastos + gastosEnvio;

			var total = totalAux + totalAux * plusUrgencia;

			$('input#traduccion__total').val(total);
			$('input#traduccion__total').formatCurrency();

			var iva = currencyToNumber($('input#traduccion__iva').val());
			$('input#traduccion__total_iva').val(total + total * iva);	
			$('input#traduccion__total_iva').formatCurrency();
			
			var mb = total - totalCoste;
			$('input#traduccion__mb').val(mb);
			$('input#traduccion__mb').formatCurrency();	

			var mb100 = Math.round((mb/total)*100);
			$('input#traduccion__mb100').val(mb100);
        }

        /**
         * Copiar valores de campos desde pestaña Presupuesto a Facturación.
         * La función se ejecuta con boton "Pasar a facturación".
         */
        function facturar(){

        	var palabras = $('input#traduccion__palabras').asNumber();
        	
			var totalCSI = $('input#traduccion__total_csi').asNumber();
			var otrosGastos = $('input#traduccion__otros_gastos').asNumber();
			var gastosGestion =	$('input#traduccion__Precio_GG').asNumber();
			var gastosEnvio = $('input#traduccion__gastos_envio').asNumber();
			var iva = $('input#traduccion__iva').asNumber();
			var totalTraductor = $('input#traduccion__total_traductor').asNumber();
				
        	$('input#traduccion__ftotal_csi').val(totalCSI);
        	$('input#traduccion__ftotal_csi').formatCurrency();
        	
        	$('input#traduccion__fotros_gastos').val(otrosGastos);
        	$('input#traduccion__fotros_gastos').formatCurrency();
        	
        	$('input#traduccion__fpalabras').val(palabras);

        	$('input#traduccion__fgastos_gestion').val(gastosGestion);
        	$('input#traduccion__fgastos_gestion').formatCurrency();
        	
        	$('input#traduccion__fgastos_envio').val(gastosEnvio);
        	$('input#traduccion__fgastos_envio').formatCurrency();

        	var total = totalCSI + otrosGastos + gastosGestion + gastosEnvio;
        	
        	$('input#traduccion__ftotal').val(total);
        	$('input#traduccion__ftotal').formatCurrency();

        	$('input#traduccion__ftotal_iva').val(total + total * iva);
        	$('input#traduccion__ftotal_iva').formatCurrency();

        	var fmb = total - totalTraductor;
        	$('input#traduccion__fmb').val(fmb);
        	$('input#traduccion__fmb').formatCurrency();

        	$('input#traduccion__fmb100').val((fmb / total) * 100);
        	$('input#traduccion__fmb100').formatCurrency();
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
	
    <h2>Traducción</h2>
    
    <form name="traduccion" id="traduccion" action="traduccion_procesa.jsp">
    
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
	<input type="hidden" name="traduccion__id_orden"  value="${data.id_orden}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="traduccion__id_orden"  value="${id_orden}"  />
</c:if>

						<br/>
						
						<sql:query var="rs">
	
								SELECT 'Con cuño' AS 'option', 1 AS 'value'
								UNION SELECT 'Jurada',2
								UNION SELECT 'Normal',3
							
</sql:query>

<c:if test="${!empty 'Tipo Traducción'}"><label for="traduccion__tipo_traduccion">Tipo Traducción</label></c:if>

<select name="traduccion__tipo_traduccion" id="traduccion__tipo_traduccion">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.tipo_traduccion==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


						<br/>
						
						<sql:query var="rs">
	
								SELECT tipo AS 'option', id AS 'value' FROM tipoDocumento ORDER BY tipo
							
</sql:query>

<c:if test="${!empty 'Tipo Doc'}"><label for="traduccion__tipo_doc">Tipo Doc</label></c:if>

<select name="traduccion__tipo_doc" id="traduccion__tipo_doc">
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

<c:if test="${!empty 'Tema'}"><label for="traduccion__id_tema">Tema</label></c:if>

<select name="traduccion__id_tema" id="traduccion__id_tema">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.id_tema==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


						<br/>
												
						<c:if test="${!empty 'Título'}"><label for="traduccion__nombre_doc">Título</label></c:if>
<input type="text" name="traduccion__nombre_doc" id="traduccion__nombre_doc" value="${data.nombre_doc}" size="50" maxlength="50" />

					</div>
					
					<div class="columna_drcha">	
						<c:if test="${!empty 'Núm Páginas'}"><label for="traduccion__num_paginas">Núm Páginas</label></c:if>
<input type="text" name="traduccion__num_paginas" id="traduccion__num_paginas" value="${data.num_paginas}" size="10" maxlength="10" onchange="" />

						
						<c:if test="${!empty 'Palabras R'}"><label for="traduccion__num_palabras">Palabras R</label></c:if>
<input type="text" name="traduccion__num_palabras" id="traduccion__num_palabras" value="${data.num_palabras}" size="10" maxlength="10" onchange="" />

						
						<br/>
						
						<c:if test="${!empty 'Comentario'}"><label for="traduccion__comentario">Comentario</label></c:if>
<textarea name="traduccion__comentario" cols="20" rows="2"><c:out value="${data.comentario}"/></textarea>

						<br/>
					
					</div>	
					
					<div class="antifloat"></div>
						<hr/>
						<br/>
					
					<div class="columna_izq">
						<c:if test="${!empty 'Palabras'}"><label for="traduccion__palabras">Palabras</label></c:if>
<input type="text" name="traduccion__palabras" id="traduccion__palabras" value="${data.palabras}" size="10" maxlength="10" onchange="" readonly/>

						<br/>
						
						<c:if test="${!empty 'Total traductor/es'}"><label for="traduccion__total_traductor">Total traductor/es</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__total_traductor"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_traductor}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__total_traductor"></c:set>
</c:if>

<input type="text" name="traduccion__total_traductor" id="traduccion__total_traductor" value="${traduccion__total_traductor}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Tarifa CSI'}"><label for="traduccion__tarifa_csi">Tarifa CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__tarifa_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.tarifa_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__tarifa_csi">0,084</c:set>
</c:if>

<input type="text" name="traduccion__tarifa_csi" id="traduccion__tarifa_csi" value="${traduccion__tarifa_csi}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total CSI'}"><label for="traduccion__total_csi">Total CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__total_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__total_csi"></c:set>
</c:if>

<input type="text" name="traduccion__total_csi" id="traduccion__total_csi" value="${traduccion__total_csi}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Gastos de envio'}"><label for="traduccion__gastos_envio">Gastos de envio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__gastos_envio"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.gastos_envio}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__gastos_envio"></c:set>
</c:if>

<input type="text" name="traduccion__gastos_envio" id="traduccion__gastos_envio" value="${traduccion__gastos_envio}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Coste gastos gestión'}"><label for="traduccion__coste_gg">Coste gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__coste_gg"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.coste_gg}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__coste_gg"></c:set>
</c:if>

<input type="text" name="traduccion__coste_gg" id="traduccion__coste_gg" value="${traduccion__coste_gg}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Precio gastos gestión'}"><label for="traduccion__Precio_GG">Precio gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__Precio_GG"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.Precio_GG}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__Precio_GG"></c:set>
</c:if>

<input type="text" name="traduccion__Precio_GG" id="traduccion__Precio_GG" value="${traduccion__Precio_GG}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Otros gastos'}"><label for="traduccion__otros_gastos">Otros gastos</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__otros_gastos"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.otros_gastos}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__otros_gastos"></c:set>
</c:if>

<input type="text" name="traduccion__otros_gastos" id="traduccion__otros_gastos" value="${traduccion__otros_gastos}" size="10" maxlength="10"  onchange="recalc()"/>

						
					</div>
					
					<div class="columna_drcha">
						
						<c:if test="${!empty 'Plus urgencia, %'}"><label for="traduccion__plus_urgencia">Plus urgencia, %</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__plus_urgencia"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.plus_urgencia}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__plus_urgencia"></c:set>
</c:if>

<input type="text" name="traduccion__plus_urgencia" id="traduccion__plus_urgencia" value="${traduccion__plus_urgencia}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total coste'}"><label for="traduccion__total_coste">Total coste</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__total_coste"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_coste}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__total_coste"></c:set>
</c:if>

<input type="text" name="traduccion__total_coste" id="traduccion__total_coste" value="${traduccion__total_coste}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
						
						<c:if test="${!empty 'Total precio'}"><label for="traduccion__total">Total precio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__total"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__total"></c:set>
</c:if>

<input type="text" name="traduccion__total" id="traduccion__total" value="${traduccion__total}" size="10" maxlength="10" readonly onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'IVA'}"><label for="traduccion__iva">IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__iva">0,16</c:set>
</c:if>

<input type="text" name="traduccion__iva" id="traduccion__iva" value="${traduccion__iva}" size="10" maxlength="10"  onchange="recalc()"/>

						<br/>
						
						<c:if test="${!empty 'Total+IVA'}"><label for="traduccion__total_iva">Total+IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__total_iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.total_iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__total_iva"></c:set>
</c:if>

<input type="text" name="traduccion__total_iva" id="traduccion__total_iva" value="${traduccion__total_iva}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
												
						<c:if test="${!empty 'MB'}"><label for="traduccion__mb">MB</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__mb"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.mb}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__mb"></c:set>
</c:if>

<input type="text" name="traduccion__mb" id="traduccion__mb" value="${traduccion__mb}" size="10" maxlength="10" readonly onchange=""/>

						<br/>
												
						<c:if test="${!empty 'MB, %'}"><label for="traduccion__mb100">MB, %</label></c:if>
<input type="text" name="traduccion__mb100" id="traduccion__mb100" value="${data.mb100}" size="10" maxlength="10" onchange="" readonly/>

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
					    				$("input#traduccion__palabras").val(summa);
					    			}
					    			
					    			columns = jQuery("#listado").getCol("servicios_traduccion_traductor__total_traductor");
					    			summa =0;
					    			for(i=0; i<columns.length; i++){
					    				var columna = replaceAll(columns[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				summa = summa + parseInt(columns[i]);
					    			}
					    			$("input#traduccion__total_traductor").val(summa);
					    			$("input#traduccion__total_traductor").formatCurrency();
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
<c:if test="${!empty 'Facturado cliente'}"><label for="traduccion__facturado_cliente">Facturado cliente</label></c:if>
<input type="checkbox" name="traduccion__facturado_cliente" ${checked} />

					<br/>
					
					<c:if test="${!empty 'Fecha frac'}"><label for="traduccion__fecha_factura_cliente">Fecha frac</label></c:if>
<input type="text" name="traduccion__fecha_factura_cliente" id="traduccion__fecha_factura_cliente" value="<fmt:formatDate value="${data.fecha_factura_cliente}" pattern="dd/MM/yyyy"/>" size="10" maxlength="10" />

					<br/>
					
					<c:if test="${!empty 'Nº frac'}"><label for="traduccion__num_factura_cliente">Nº frac</label></c:if>
<input type="text" name="traduccion__num_factura_cliente" id="traduccion__num_factura_cliente" value="${data.num_factura_cliente}" size="50" maxlength="50" />

					<br/>
					
					<c:if test="${!empty 'Total CSI'}"><label for="traduccion__ftotal_csi">Total CSI</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__ftotal_csi"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal_csi}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__ftotal_csi"></c:set>
</c:if>

<input type="text" name="traduccion__ftotal_csi" id="traduccion__ftotal_csi" value="${traduccion__ftotal_csi}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Otros gastos'}"><label for="traduccion__fotros_gastos">Otros gastos</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__fotros_gastos"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fotros_gastos}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__fotros_gastos"></c:set>
</c:if>

<input type="text" name="traduccion__fotros_gastos" id="traduccion__fotros_gastos" value="${traduccion__fotros_gastos}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Palabras'}"><label for="traduccion__fpalabras">Palabras</label></c:if>
<input type="text" name="traduccion__fpalabras" id="traduccion__fpalabras" value="${data.fpalabras}" size="10" maxlength="10" onchange="" />

					<br/>
					
					<c:if test="${!empty 'Gastos gestión'}"><label for="traduccion__fgastos_gestion">Gastos gestión</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__fgastos_gestion"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fgastos_gestion}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__fgastos_gestion"></c:set>
</c:if>

<input type="text" name="traduccion__fgastos_gestion" id="traduccion__fgastos_gestion" value="${traduccion__fgastos_gestion}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Gastos envio'}"><label for="traduccion__fgastos_envio">Gastos envio</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__fgastos_envio"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fgastos_envio}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__fgastos_envio"></c:set>
</c:if>

<input type="text" name="traduccion__fgastos_envio" id="traduccion__fgastos_envio" value="${traduccion__fgastos_envio}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Total Final'}"><label for="traduccion__ftotal">Total Final</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__ftotal"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__ftotal"></c:set>
</c:if>

<input type="text" name="traduccion__ftotal" id="traduccion__ftotal" value="${traduccion__ftotal}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'Total+IVA'}"><label for="traduccion__ftotal_iva">Total+IVA</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__ftotal_iva"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.ftotal_iva}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__ftotal_iva"></c:set>
</c:if>

<input type="text" name="traduccion__ftotal_iva" id="traduccion__ftotal_iva" value="${traduccion__ftotal_iva}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'MB'}"><label for="traduccion__fmb">MB</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__fmb"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fmb}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__fmb"></c:set>
</c:if>

<input type="text" name="traduccion__fmb" id="traduccion__fmb" value="${traduccion__fmb}" size="10" maxlength="10"  onchange=""/>

					<br/>
					
					<c:if test="${!empty 'MB%'}"><label for="traduccion__fmb100">MB%</label></c:if>
<c:if test="${!empty data}">
	<c:set var="traduccion__fmb100"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.fmb100}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="traduccion__fmb100"></c:set>
</c:if>

<input type="text" name="traduccion__fmb100" id="traduccion__fmb100" value="${traduccion__fmb100}" size="10" maxlength="10"  onchange=""/>

				</div>
				</div>
			</div>
			
		</div>
	</form>

<c:import url="/pie.jsp"/>
