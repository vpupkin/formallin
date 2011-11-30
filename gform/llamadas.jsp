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
		SELECT * FROM llamadas WHERE id_orden=?
		<sql:param>${param.id}</sql:param>
	</sql:query>
	<c:if test="${rs.rowCount>0}">
		<c:set var="data" value="${rs.rows[0]}"/>
		<c:set var="oper" value="edit"/>
	</c:if>
	<c:remove var="rs"/>
</c:if>

<c:if test="${oper eq 'add'}">
	<%-- buscar tarifa --%>
	<sql:query var="rsTarifa">
		SELECT tarifa
			FROM tarifas_traduccion
		WHERE id_tipo_servicio=? 
			AND id_idioma_origen=(SELECT ididiomaoriginal FROM servicios_traduccion WHERE id_orden=?)
			AND id_idioma_trad=(SELECT ididiomasolicita FROM servicios_traduccion WHERE id_orden=?)
		ORDER BY fecha DESC
		LIMIT 1
		<sql:param>${param.tipo}</sql:param>
		<sql:param>${param.id}</sql:param>
		<sql:param>${param.id}</sql:param>
	</sql:query>
	<fmt:formatNumber var="tarifaCSI" type="number" maxFractionDigits="4" value="${rsTarifa.rows[0].tarifa}" />
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
            $('#llamadas').ajaxForm({ 
                // dataType identifies the expected content type of the server response 
                dataType:  'json', 
         
                // success identifies the function to invoke when the server response 
                // has been received 
                success:   processJson 
            }); 
        });

        /**
         * Recalucular resto de campos del formumlario despues de cambir un campo.
         * La funci�n se ejecuta cundo hay un evento onChange.
         */
        function recalc(){

            var duracion = currencyToNumber($('input#llamadas__duracion').val());
			var tarifaCSI = currencyToNumber($('input#llamadas__tarifa_csi').val());
			var totalCSI = duracion * tarifaCSI;
			
			$('input#llamadas__total_csi').val(totalCSI);
			$('input#llamadas__total_csi').formatCurrency();
			
			var totalTraductor = currencyToNumber($('input#llamadas__total_traductor').val());
			var costeGG = currencyToNumber($('input#llamadas__coste_gg').val());
			var otrosGastos = currencyToNumber($('input#llamadas__otros_gastos').val());
			var totalCoste = totalTraductor + costeGG + otrosGastos;
			
			$('input#llamadas__total_coste').val(totalCoste);
			$('input#llamadas__total_coste').formatCurrency();

			var precioGG = currencyToNumber($('input#llamadas__Precio_GG').val());
			var gastosEnvio = currencyToNumber($('input#llamadas__gastos_envio').val());

			var plusUrgencia = percentageToNumber($('input#llamadas__plus_urgencia').val());

			var totalAux = totalCSI + precioGG + otrosGastos + gastosEnvio;

			var total = totalAux + totalAux * plusUrgencia;

			$('input#llamadas__total').val(total);
			$('input#llamadas__total').formatCurrency();

			var iva = currencyToNumber($('input#llamadas__iva').val());
			$('input#llamadas__total_iva').val(total + total * iva);	
			$('input#llamadas__total_iva').formatCurrency();
			
			var mb = total - totalCoste;
			$('input#llamadas__mb').val(mb);
			$('input#llamadas__mb').formatCurrency();	
			if(total != 0){
				var mb100 = Math.round((mb/total)*100);
				$('input#llamadas__mb100').val(mb100);
			}
        }

        /**
         * Copiar valores de campos desde pesta�a Presupuesto a Facturaci�n.
         * La funci�n se ejecuta con boton "Pasar a facturaci�n".
         */
        function facturar(){

        	var totalCSI = $('input#llamadas__total_csi').asNumber();
			var otrosGastos = $('input#llamadas__otros_gastos').asNumber();
			var palabras = $('input#llamadas__palabras').asNumber();
			var gastosGestion =	$('input#llamadas__Precio_GG').asNumber();
			var gastosEnvio = $('input#llamadas__gastos_envio').asNumber();
			var total = $('input#llamadas__total').asNumber();
			var iva = $('input#llamadas__total_iva').asNumber();
			var mb = $('input#llamadas__mb').asNumber();
			var mb100 = $('input#llamadas__mb100').asNumber();
			
			
        	$('input#llamadas__ftotal_csi').val(totalCSI);
        	$('input#llamadas__ftotal_csi').formatCurrency();
        	
        	$('input#llamadas__fotros_gastos').val(otrosGastos);
        	$('input#llamadas__fotros_gastos').formatCurrency();
        	
        	$('input#llamadas__fpalabras').val(palabras);

        	$('input#llamadas__fgastos_gestion').val(gastosGestion);
        	$('input#llamadas__fgastos_gestion').formatCurrency();
        	
        	$('input#llamadas__fgastos_envio').val(gastosEnvio);
        	$('input#llamadas__fgastos_envio').formatCurrency();

        	$('input#llamadas__ftotal').val(total);
        	$('input#llamadas__ftotal').formatCurrency();

        	$('input#llamadas__ftotal_iva').val(iva);
        	$('input#llamadas__ftotal_iva').formatCurrency();

        	$('input#llamadas__fmb').val(mb);
        	$('input#llamadas__fmb').formatCurrency();

        	$('input#llamadas__fmb100').val(mb100);
        	$('input#llamadas__fmb100').formatCurrency();
        }


        /**
         * Recalucular resto de campos del subformumlario Traductores despues de cambir un campo.
         * La funci�n se ejecuta cundo hay un evento onChange en jGrid-form.
         */
        function recalc_subform_traductores(){

        	var iva = $('input#servicios_traduccion_traductor__iva').asNumber();
        	var irpf = $('input#servicios_traduccion_traductor__irpf').asNumber();

        	var minutos = $('input#servicios_traduccion_traductor__minutos').asNumber();
			var tarifa = $('input#servicios_traduccion_traductor__tarifa_traductor').asNumber();
			var totalTraductor = minutos * tarifa;

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

        /**
         * Evento onChange select 'servicios_traduccion_traductor__id_traductor'
         * del formulario "Agregar/Modificar registro" del jGrid.
         * Sirve para poner valores por defecto en los campos.
         *
         */
		function set_def_tarifa(){
			mySelect = document.getElementById('servicios_traduccion_traductor__id_traductor');
			var paramValue = mySelect[mySelect.selectedIndex].value;

			$.post("def_tarifa.jsp", {id:paramValue},
				function(data){
				/*
					$('input#servicios_traduccion_traductor__minimo').val(data.minimo);
					$('input#servicios_traduccion_traductor__minimo').formatCurrency();
				*/
					$('input#servicios_traduccion_traductor__tarifa_traductor').val(data.tarifa);
					$('input#servicios_traduccion_traductor__tarifa_traductor').formatCurrency();

					$('input#servicios_traduccion_traductor__irpf').val(data.irpf);
					$('input#servicios_traduccion_traductor__irpf').formatCurrency();

					$('input#servicios_traduccion_traductor__iva').val(data.iva);
					$('input#servicios_traduccion_traductor__iva').formatCurrency();

				}, "json"
			)
        }
         
	</script>
	
    <h2>Llamadas telef�nicas</h2>
    
    <form table="llamadas" xmlns:c="http://java.sun.com/jsp/jstl/core">
    
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
	                <a href="javascript:void(null);">Facturaci�n</a>
	            </li>
	        </ul>
	    </div>
    
    	<input type="hidden" id="oper" name="oper" value="${oper}"/>
    	<input type="hidden" id="tipo" name="tipo" value="${param.tipo}"/>
    	<input type="hidden" id="json" name="json" value="true"/>
    
    	<div class="tabs_cont" style="height:100%">

			<div id="div_tab1" class="current">
				<br/>
				<div class="formulario">
				
					<div class="columna_drcha">
						
						<c:set var="id_orden" value="${param.id}"/>
						
						<element>
							<name>id_orden</name>
							<type>int</type>
							<key>true</key>
							<references>servicios_traduccion(id_orden)</references>
							<hidden>true</hidden>
							<ondelete>CASCADE</ondelete>
						</element>
						
						<element>
							<label>Tema</label>
							<name>id_tema</name>
							<type>select</type>
							<references>tema(id)</references>
							<query>
								SELECT tema AS 'option', id AS 'value' FROM tema ORDER BY tema
							</query>
						</element>
						<br/>
						
						<element>
							<label>Fecha</label>
							<name>fecha</name>
							<type>datetime</type>
						</element>
						<br/>
						
						<element>
							<label>Comentario</label>
							<name>comentario</name>
							<type>textarea</type>
							<cols>60</cols>
							<rows>4</rows>
						</element>
						
					</div>

					<div class="columna_izq">
					
						<element>
							<label>Destinatario</label>
							<name>destinatario</name>
							<type>varchar</type>
						</element>
						<br/>
						
						<element>
							<label>Horario</label>
							<name>horario</name>
							<type>int</type>
						</element>
						<br/>
						
						<element>
							<label>N�m.contacto</label>
							<name>contacto</name>
							<type>int</type>
						</element>
						<br/>
						
						<element>
							<label>Lugar</label>
							<name>lugar</name>
							<type>varchar</type>
						</element>
						
					</div>
					
					<div class="antifloat"></div>
					<hr/>
					<br/>
					
					<div class="columna_drcha">
						<element>
							<label>Minutos</label>
							<name>duracion</name>
							<type>INT</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Total traductor/es</label>
							<name>total_traductor</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Tarifa CSI</label>
							<name>tarifa_csi</name>
							<type>DECIMAL</type>
							<defvalue>${tarifaCSI}</defvalue>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Total CSI</label>
							<name>total_csi</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Otros gastos</label>
							<name>otros_gastos</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Gastos desplazamiento</label>
							<name>gastos_despl</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						
					</div>
					
					<div class="columna_izq">
						
						<element>
							<label>Plus urgencia, %</label>
							<name>plus_urgencia</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Total coste</label>
							<name>total_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Total precio</label>
							<name>total</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>IVA</label>
							<name>iva</name>
							<type>DECIMAL</type>
							<defvalue>0,16</defvalue>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Total+IVA</label>
							<name>total_iva</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
												
						<element>
							<label>MB</label>
							<name>mb</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
												
						<element>
							<label>MB, %</label>
							<name>mb100</name>
							<type>INT</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<label> </label>
						<button type="button" onclick="facturar()">Pasar a facturaci�n</button>
						
					</div>
				</div>
			</div>
			
			<div id="div_tab2" class="div_cont">
				<div class="formulario">
					<div>
						<grid>
							<title>Traductores</title>
							<name>grid_traductores_llamadas</name>
							<table_model>servicios_traduccion_traductor</table_model>
							<columns>id, id_traductor, minutos, tarifa_traductor, gastos_desplazamiento, fecha_factura_trad, num_factura_trad, total_traductor, iva, irpf, total_iva, total_irpf, coste_final, facturado_traductor</columns>
							<url>/traducciones/grid_traductores_llamadas_xml.jsp?id=${param.id}</url>
							<query>
								SELECT id, CONCAT_WS(',', apellidos, nombre) AS traductor,
									minutos, tarifa_traductor, gastos_desplazamiento, fecha_factura_trad, num_factura_trad,
									total_traductor, iva, irpf, total_iva, total_irpf, coste_final, facturado_traductor																	
								FROM servicios_traduccion_traductor
									INNER JOIN cliente ON cliente.idCliente=id_traductor
								WHERE id_orden=${param.id}
							</query>
							<procesa_url>/traducciones/servicios_traduccion_traductor_procesa.jsp?json=true&amp;servicios_traduccion_traductor__id_orden=${param.id}</procesa_url>
							<gridComplete>
								<![CDATA[
									var columns = jQuery("#listado").getCol("servicios_traduccion_traductor__minutos");
		        					var summa =0;
					    			for(i=0; i<columns.length; i++){
					    				summa = summa + parseInt(columns[i]);
					        		}
					    			if(!isNaN(summa)){
					    				$("input#llamadas__duracion").val(summa);
					    			}
					    			
					    			columns = jQuery("#listado").getCol("servicios_traduccion_traductor__gastos_desplazamiento");
					    			summa =0;
					    			for(i=0; i<columns.length; i++){
					    				summa = summa + parseInt(columns[i]);
					        		}
					    			if(!isNaN(summa)){
					    				$("input#llamadas__gastos_despl").val(summa);
					    			}
					    			
					    			columns = jQuery("#listado").getCol("servicios_traduccion_traductor__total_traductor");
					    			summa =0;
					    			for(i=0; i<columns.length; i++){
					    				var columna = replaceAll(columns[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				summa = summa + parseInt(columns[i]);
					    			}
					    			$("input#llamadas__total_traductor").val(summa);
					    			$("input#llamadas__total_traductor").formatCurrency();
					    		
					    			recalc();
				    			]]>
							</gridComplete>
						</grid>
					</div>
				</div>
			</div>
			
			<div id="div_tab3" class="div_cont">
				
				<br/>
				<br/>
				
				<div class="formulario">
				<div class="columna_drcha">
					
					<element>
						<label>Facturado cliente</label>
						<name>facturado_cliente</name>
						<type>CHECKBOX</type>
					</element>
					<br/>
					
					<element>
						<label>Fecha factura</label>
						<name>fecha_factura_cliente</name>
						<type>DATETIME</type>
					</element>
					<br/>
					
					<element>
						<label>N� factura</label>
						<name>num_factura_cliente</name>
						<type>VARCHAR</type>
					</element>
					<br/>
					
					<element>
						<label>Total CSI</label>
						<name>ftotal_csi</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>	
						<label>Otros gastos</label>
						<name>fotros_gastos</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Total Final</label>
						<name>ftotal</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Total+IVA</label>
						<name>ftotal_iva</name>
						<type>DECIMAL</type>
					</element>

				</div>
				</div>
			</div>
			
		</div>
	</form>

<c:import url="/pie.jsp"/>