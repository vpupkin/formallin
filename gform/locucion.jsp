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
		SELECT * FROM locucion WHERE id_orden=?
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
            $('#locucion').ajaxForm({ 
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
            	document.location = "locucion_form.jsp?id=" + data.id;
        }


        /**
         * Recalucular resto de campos del formumlario despues de cambir un campo.
         * La función se ejecuta cundo hay un evento onChange.
         */
        function recalc(){

            var costeHoras = currencyToNumber($('input#locucion__coste_horas').val());
			var precioHoras = currencyToNumber($('input#locucion__precio_horas').val());
			var gastosDesplazamiento = currencyToNumber($('input#locucion__gastos_desplazamiento').val());
			var dietas = currencyToNumber($('input#locucion__dietas').val());
			var costeGG= currencyToNumber($('input#locucion__coste_gg').val());
			var precioGG= currencyToNumber($('input#locucion__Precio_GG').val());
			var otrosGastos= currencyToNumber($('input#locucion__otros_gastos').val());
			var plusUrgencia= currencyToNumber($('input#locucion__plus_urgencia').val());
			
			
			var totalCoste = costeHoras + gastosDesplazamiento + dietas + costeGG + otrosGastos;
			$('input#locucion__total_coste').val(totalCoste);
			$('input#locucion__total_coste').formatCurrency();
			
			var total = precioHoras + gastosDesplazamiento + dietas + precioGG + otrosGastos;
			total = total + total * plusUrgencia;
			
			$('input#locucion__total').val(total);
			$('input#locucion__total').formatCurrency();
			

			var iva = currencyToNumber($('input#locucion__iva').val());
			$('input#locucion__total_iva').val(total + total * iva);	
			$('input#locucion__total_iva').formatCurrency();
			
			var mb = total - totalCoste;
			$('input#locucion__mb').val(mb);
			$('input#locucion__mb').formatCurrency();	

			var mb100 = Math.round((mb/total)*100);
			$('input#locucion__mb100').val(mb100);
        }

        /**
         * Copiar valores de campos desde pestaña Presupuesto a Facturación.
         * La función se ejecuta con boton "Pasar a facturación".
         */
        function facturar(){

			var gastosGestion =	$('input#locucion__Precio_GG').asNumber();
			var totalCSI = $('input#locucion__precio_horas').asNumber();
			var otrosGastos = $('input#locucion__otros_gastos').asNumber();
			var total = $('input#locucion__total').asNumber();
			var totalIVA = $('input#locucion__total_iva').asNumber();
			
        	$('input#locucion__fgastos_gestion').val(gastosGestion);
        	$('input#locucion__fgastos_gestion').formatCurrency();
        	
        	$('input#locucion__ftotal_csi').val(totalCSI);
        	$('input#locucion__ftotal_csi').formatCurrency();
        	
        	$('input#locucion__fotros_gastos').val(otrosGastos);
        	$('input#locucion__fotros_gastos').formatCurrency();
        	
        	$('input#locucion__ftotal').val(total);
        	$('input#locucion__ftotal').formatCurrency();
        	
        	$('input#locucion__ftotal_iva').val(totalIVA);
        	$('input#locucion__ftotal_iva').formatCurrency();
        }


        /**
         * Recalucular resto de campos del subformumlario Traductores despues de cambir un campo.
         * La función se ejecuta cundo hay un evento onChange en jGrid-form.
         */
        function recalc_subform_traductores(){

			var horas = $('input#servicios_traduccion_traductor__horas').asNumber();
			var km = $('input#servicios_traduccion_traductor__km').asNumber();
			var precio_km = $('input#servicios_traduccion_traductor__precio_km').asNumber();
			var dietas = $('input#servicios_traduccion_traductor__dietas').asNumber();
			var tarifa = $('input#servicios_traduccion_traductor__tarifa_hora').asNumber();
			
			var iva = $('input#servicios_traduccion_traductor__iva').asNumber();
        	var irpf = $('input#servicios_traduccion_traductor__irpf').asNumber();

			var coste_gg = $('input#servicios_traduccion_traductor__coste_gg').asNumber();
			var otros_gastos = $('input#servicios_traduccion_traductor__irpf').asNumber();
        	
			var gastos_desplazamiento = km * precio_km;
			var total_traductor = tarifa * horas;
			var totalIVA = total_traductor * iva;
			var totalIRPF = total_traductor * irpf;
			var coste = total_traductor + totalIVA - totalIRPF + gastos_desplazamiento;

			$('input#servicios_traduccion_traductor__gastos_desplazamiento').val(gastos_desplazamiento);
			$('input#servicios_traduccion_traductor__gastos_desplazamiento').formatCurrency();
			                               			
			$('input#servicios_traduccion_traductor__total_traductor').val(total_traductor);
			$('input#servicios_traduccion_traductor__total_traductor').formatCurrency();

			$('input#servicios_traduccion_traductor__total_iva').val(totalIVA);
			$('input#servicios_traduccion_traductor__total_iva').formatCurrency();
			
			$('input#servicios_traduccion_traductor__total_irpf').val(totalIRPF);
			$('input#servicios_traduccion_traductor__total_irpf').formatCurrency();

			$('input#servicios_traduccion_traductor__coste_final').val(coste);
			$('input#servicios_traduccion_traductor__coste_final').formatCurrency();
			
        }
	</script>
	
    <h2>Locución</h2>
    
    <form table="locucion" xmlns:c="http://java.sun.com/jsp/jstl/core">
    
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
						
						<element>
							<name>id_orden</name>
							<type>int</type>
							<key>true</key>
							<references>servicios_traduccion(id_orden)</references>
							<hidden>true</hidden>
							<ondelete>CASCADE</ondelete>
						</element>
						
						<element>
							<label>Título</label>
							<name>nombre_doc</name>
							<type>VARCHAR</type>
						</element>
						<br/>
						
						<element>
							<label>Tema</label>
							<name>id_tema</name>
							<type>select</type>
							<references>tema(id)</references>
							<query>
								SELECT tema AS 'option', id AS 'value' FROM tema ORDER BY tema
							</query>
						</element>
						
						<element>
							<label>Comentario</label>
							<name>comentario</name>
							<type>textarea</type>
						</element>
					</div>
					
					<div class="columna_drcha">								
						
						<element>
							<label>Traducción</label>
							<name>traduccion</name>
							<type>checkbox</type>
						</element>
						<br/>
						
						<element>
							<label>Lugar</label>
							<name>lugar</name>
							<type>VARCHAR</type>
						</element>
						
						<element>
							<label>Fecha</label>
							<name>fecha</name>
							<type>datetime</type>
						</element>
						
						<element>
							<label>Horario</label>
							<name>horario</name>
							<type>int</type>
						</element>
						<br/>
						
					
					</div>	
					
					<div class="antifloat"></div>
						<hr/>
						<br/>
					
					<div class="columna_izq">
						<element>
							<label>Horas</label>
							<name>horas</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Coste Horas</label>
							<name>coste_horas</name>
							<type>DECIMAL</type>
						</element>
						<br/>
						
						<element>
							<label>Precio Horas</label>
							<name>precio_horas</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Gastos Desplazamiento</label>
							<name>gastos_desplazamiento</name>
							<type>DECIMAL</type>
						</element>
						<br/>
						
						<element>
							<label>Dietas</label>
							<name>dietas</name>
							<type>DECIMAL</type>
						</element>
						<br/>
						
						<element>
							<label>Coste gastos gestión</label>
							<name>coste_gg</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Precio gastos gestión</label>
							<name>Precio_GG</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
					</div>
					
					<div class="columna_drcha">

						<element>
							<label>Otros gastos</label>
							<name>otros_gastos</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>

						
						<element>
							<label>Plus urgencia</label>
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
							<label>Total</label>
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
							<defvalue>0,18</defvalue>
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
						<button type="button" onclick="facturar()">Pasar a facturación</button>
						
					</div>
				</div>
			</div>
			
			<div id="div_tab2" class="div_cont">
				<div class="formulario">
					<div>
						<grid>
							<title>Traductores</title>
							<name>grid_traductores_locucion</name>
							<table_model>servicios_traduccion_traductor</table_model>
							<columns>id, id_traductor, horas, tarifa_hora, km, precio_km, gastos_desplazamiento, dietas, fecha_factura_trad, num_factura_trad, total_traductor, iva, irpf, total_iva, total_irpf, coste_final, facturado_traductor</columns>
							<url>"/traducciones/grid_traductores_locucion_xml.jsp?id=${param.id}&amp;tipo=${param.tipo}"</url>
							<query>
								SELECT id, CONCAT_WS(',', apellidos, nombre) AS traductor,
									horas, tarifa_hora, km, precio_km, gastos_desplazamiento, dietas, fecha_factura_trad, num_factura_trad, total_traductor,
									iva, irpf, total_iva, total_irpf, coste_final, facturado_traductor																	
								FROM servicios_traduccion_traductor
									INNER JOIN cliente ON cliente.idCliente=id_traductor
								WHERE id_orden=${param.id} AND tipo_servicio=${param.tipo}
							</query>
							<procesa_url>"/traducciones/servicios_traduccion_traductor_procesa.jsp?json=true&amp;servicios_traduccion_traductor__id_orden=${param.id}&amp;servicios_traduccion_traductor__tipo_servicio=${param.tipo}"</procesa_url>
							<gridComplete>
								<![CDATA[
									// calcular 'Horas'
									var colHoras = jQuery("#listado").getCol("servicios_traduccion_traductor__horas");
		        					var sumaHoras =0;
					    			for(i=0; i<colHoras.length; i++){
					    				sumaHoras = sumaHoras + parseInt(colHoras[i]);
					        		}
					    			if(!isNaN(sumaHoras)){
					    				$("input#locucion__horas").val(sumaHoras);
					    			}
					    			
					    			// calcular 'Coste Horas'
					    			var colTotalTraductor = jQuery("#listado").getCol("servicios_traduccion_traductor__total_traductor");
					    			var sumaTotalTraductor = 0;
					    			for(i=0; i<colTotalTraductor.length; i++){
					    				var columna = replaceAll(colTotalTraductor[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				sumaTotalTraductor = sumaTotalTraductor + parseFloat(columna);
					    			}
					    			$("input#locucion__coste_horas").val(sumaTotalTraductor);
					    			$("input#locucion__coste_horas").formatCurrency();
					    			
					    			// calcular 'Gastos Desplazamiento'
					    			var colGastosDespl = jQuery("#listado").getCol("servicios_traduccion_traductor__gastos_desplazamiento");
					    			var sumaGastosDespl =0;
					    			for(i=0; i<colGastosDespl.length; i++){
					    				var columna = replaceAll(colGastosDespl[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				sumaGastosDespl = sumaGastosDespl + parseFloat(columna);
					    			}
					    			$("input#locucion__gastos_desplazamiento").val(sumaGastosDespl);
					    			$("input#locucion__gastos_desplazamiento").formatCurrency();
					    			
					    			// calucular 'Dietas'
					    			var colDietas = jQuery("#listado").getCol("servicios_traduccion_traductor__dietas");
					    			var sumaDietas =0;
					    			for(i=0; i<colDietas.length; i++){
					    				var columna = replaceAll(colDietas[i], ".", "");
					    				columna = replaceAll(columna, ",", ".");
					    				sumaDietas = sumaDietas + parseFloat(columna);
					    			}
					    			$("input#locucion__dietas").val(sumaDietas);
					    			$("input#locucion__dietas").formatCurrency();
					    			
					    			recalc();
				    			]]>
							</gridComplete>
						</grid>
					</div>
				</div>
			</div>
			
			<div id="div_tab3" class="div_cont">
			
				<div class="formulario">
				<div class="columna_izq">
					<br/>
					
					<element>
						<label>Facturado cliente</label>
						<name>facturado_cliente</name>
						<type>CHECKBOX</type>
					</element>
					<br/>
					
					<element>
						<label>Fecha frac</label>
						<name>fecha_factura_cliente</name>
						<type>DATETIME</type>
					</element>
					<br/>
					
					<element>
						<label>Nº frac</label>
						<name>num_factura_cliente</name>
						<type>VARCHAR</type>
					</element>
					<br/>
					
					<element>
						<label>Gastos Gestión</label>
						<name>fgastos_gestion</name>
						<type>DECIMAL</type>
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
					<br/>
					
					
				</div>
				</div>
			</div>
			
		</div>
	</form>

<c:import url="/pie.jsp"/>