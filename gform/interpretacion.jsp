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
		SELECT * FROM interpretacion WHERE id_orden=?
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
            $('#interpretacion').ajaxForm({ 
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
            	document.location = "interpretacion_form.jsp?id=" + data.id;
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
	
    <h2>Interpretación</h2>
    
    <form table="interpretacion" xmlns:c="http://java.sun.com/jsp/jstl/core">
    
	    <div class="botones" align="right">
	    	<input type="submit" class="boton" value="Guardar"/>
	        <c:url var="u" value=""/>
	        <input type="button" class="boton" value="Cancelar" onclick="document.location='${u}'"/>
	    </div>
	    
	     <div class="tabs">
	        <ul>
	            <li id="li_tab1" onmousedown="clickTab(this, 'div_tab1')" class="current">
	                <a href="javascript:void(null);">Datos Interpretación</a>
	            </li>
	            <li id="li_tab2" onmousedown="clickTab(this, 'div_tab2')">
	                <a href="javascript:void(null);">Presupuesto</a>
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
						<br/>
						
						<element>
							<label>Tipo Int.</label>
							<name>tipo_interpretacion</name>
							<type>select</type>
							<query>
								SELECT 'Simultanea' AS 'option', 1 AS 'value'
								UNION SELECT 'Consecutiva',2
								UNION SELECT 'Acompañante',3
							</query>
						</element>
						
						<element>
							<label>Nombre Evento</label>
							<name>nombre_evento</name>
							<type>VARCHAR</type>
						</element>
						<br/>
						
						<element>
							<label>Tema Evento</label>
							<name>tema_tema</name>
							<type>VARCHAR</type>
						</element>
						<br/>
												
						<element>
							<label>Fecha Inicio</label>
							<name>fecha_inicio</name>
							<type>DATETIME</type>
						</element>
						
						<element>
							<label>Fecha Fin</label>
							<name>fecha_fin</name>
							<type>DATETIME</type>
						</element>
						
						<element>
							<label>Horario</label>
							<name>horario</name>
							<type>VARCHAR</type>
						</element>
						
						<element>
							<label>Total Días</label>
							<name>total_dias</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Total Horas</label>
							<name>total_horas</name>
							<type>VARCHAR</type>
						</element>
						
						<element>
							<label>Jornada Completa</label>
							<name>jornada_completa</name>
							<type>INT</type>
						</element>
						
						<element>
							<label>Media Jornada</label>
							<name>media_jornada</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Nº Asistentes</label>
							<name>n_asistentes</name>
							<type>INT</type>
						</element>
						
						<element>
							<label>Lugar</label>
							<name>lugar</name>
							<type>VARCHAR</type>
						</element>
						<br/>
						
						<element>
							<label>Comentario</label>
							<name>comentario</name>
							<type>textarea</type>
						</element>
						<br/>
						
						<element>
							<label>Equipo Técnico</label>
							<name>equipo_tecnico</name>
							<type>CHECKBOX</type>
						</element>
						<br/>
						
						<element>
							<label>Nº Cabinas</label>
							<name>n_cabinas</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Tipo Cabinas</label>
							<name>tipo_cabinas</name>
							<type>select</type>
							<query>
								SELECT 'Individual' AS 'option', 1 AS 'value'
								UNION SELECT 'Doble',2
							</query>
						</element>
						<br/>
						
						<element>
							<label>Grabación</label>
							<name>grabación</name>
							<type>CHECKBOX</type>
						</element>
						<br/>
						
						<element>
							<label>Auriculares</label>
							<name>auriculares</name>
							<type>select</type>
							<query>
								SELECT 'Radio' AS 'option', 1 AS 'value'
								UNION SELECT 'Infrarrojos',2
							</query>
						</element>
						<br/>
						
						<element>
							<label>Micros</label>
							<name>micros</name>
							<type>select</type>
							<query>
								SELECT 'Inalámbricos' AS 'option', 1 AS 'value'
								UNION SELECT 'De mesa',2
								UNION SELECT 'Solapa',3
							</query>
						</element>
						<br/>
						
					</div>
				</div>
			</div>
			
			<div id="div_tab2" class="div_cont">
				<div class="formulario">
			
					<div class="columna_izq">
					
					Interpretes
					
						<element>
							<label>Media Jornada</label>
							<name>media_jornada_presup</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Nº Intérpretes</label>
							<name>mj_interpretes</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Coste</label>
							<name>mj_coste</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>

						<element>
							<label>Precio</label>
							<name>mj_precio</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Subtotal Coste</label>
							<name>mj_subtotal_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Subtotal Precio</label>
							<name>mj_subtotal_precio</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Jornada Completa</label>
							<name>jornada_completa_presup</name>
							<type>INT</type>
						</element>
						<br/>

						<element>
							<label>Nº Intérpretes</label>
							<name>jc_interpretes</name>
							<type>INT</type>
						</element>
						<br/>

						<element>
							<label>Coste</label>
							<name>jc_coste</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Precio</label>
							<name>jc_precio</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Subtotal Coste</label>
							<name>jc_subtotal_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Subtotal Precio</label>
							<name>jc_subtotal_precio</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Horas</label>
							<name>horas_presup</name>
							<type>INT</type>
						</element>
						<br/>

						<element>
							<label>Nº Intérpretes</label>
							<name>h_interpretes</name>
							<type>INT</type>
						</element>
						<br/>

						<element>
							<label>Coste</label>
							<name>h_coste</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Precio</label>
							<name>h_precio</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Subtotal Coste</label>
							<name>h_subtotal_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Subtotal Precio</label>
							<name>h_subtotal_precio</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
					</div>
						
					<div>
						
						Equipo Técnico
						
						<element>
							<label>Coste ET</label>
							<name>coste_et</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Precio ET</label>
							<name>precio_et</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Coste Grabación</label>
							<name>coste_grab</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Precio Grabación</label>
							<name>precio_grab</name>
							<type>DECIMAL</type>
						</element>
						<br/>

						<element>
							<label>Subtotal Coste ET</label>
							<name>subtotal_coste_et</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Subtotal Precio ET</label>
							<name>subtotal_precio_et</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						Dietas
						
						<element>
							<label>Km</label>
							<name>km</name>
							<type>INT</type>
						</element>
						<br/>
						
						<element>
							<label>Tarifa</label>
							<name>tarifa</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Subtotal Km</label>
							<name>subtotal_km</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Comidas</label>
							<name>comidas</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>

						<element>
							<label>Estancia</label>
							<name>estancia</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>

						<element>
							<label>Subtotal Dietas</label>
							<name>subtotal_dietas</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						Otros gastos 

						<element>
							<label>Precio Gastos Gestión</label>
							<name>precio_gg</name>
							<type>DECIMAL</type>
							<defvalue>50</defvalue>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Coste Otros</label>
							<name>coste_otros</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>

						<element>
							<label>Precio Otros</label>
							<name>precio_otros</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
						
						<element>
							<label>Plus Urgencia</label>
							<name>plus_urgencia</name>
							<type>DECIMAL</type>
							<onchange>recalc()</onchange>
						</element>
						<br/>
					</div>	
					
					<div class="columna_drcha">
						
						Totales
						
						<element>
							<label>Subtotal Coste Intérpretes</label>
							<name>total_subtotal_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Subtotal Precio Intérpretes</label>
							<name>total_subtotal_precio</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>
						
						<element>
							<label>Total Coste</label>
							<name>total_coste</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
						</element>
						<br/>

						<element>
							<label>Total Precio</label>
							<name>total_precio</name>
							<type>DECIMAL</type>
							<readonly>true</readonly>
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
			
			<div id="div_tab3" class="div_cont">
			
				<div class="formulario">
				<div class="columna_izq">
					<br/>
					<element>
						<label>Facturado Traductor</label>
						<name>facturado_traductor</name>
						<type>CHECKBOX</type>
					</element>
					<br/>
					
					<element>
						<label>Fecha</label>
						<name>fecha_factura_traductor</name>
						<type>DATETIME</type>
					</element>
					<br/>
					
					<element>
						<label>Nº factura</label>
						<name>num_factura_traductor</name>
						<type>VARCHAR</type>
					</element>
					<br/>
					
					<element>
						<label>Coste Intérpretes</label>
						<name>coste_interpretes</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>	
						<label>Coste Equipo Técnico</label>
						<name>coste_equipo_tecnico</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Coste Dietas</label>
						<name>coste_dietas</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Coste Gestión</label>
						<name>coste_gestion</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Coste Otros Gastos</label>
						<name>coste_otros_gastos</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Coste Final</label>
						<name>coste_final</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Comentario</label>
						<name>comentario_facturacion</name>
						<type>TEXTAREA</type>
					</element>
					
				</div>
				
				<div class="columna_drcha">
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
						<label>Palabras</label>
						<name>fpalabras</name>
						<type>INT</type>
					</element>
					<br/>
					
					<element>
						<label>Gastos gestión</label>
						<name>fgastos_gestion</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>Gastos envio</label>
						<name>fgastos_envio</name>
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
					
					<element>
						<label>MB</label>
						<name>fmb</name>
						<type>DECIMAL</type>
					</element>
					<br/>
					
					<element>
						<label>MB%</label>
						<name>fmb100</name>
						<type>DECIMAL</type>
					</element>
				</div>
				</div>
			</div>
			
		</div>
	</form>

<c:import url="/pie.jsp"/>