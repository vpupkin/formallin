<%@page contentType="text/html;charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@include file="/dataSource.jsp"%>

<c:if test="${!empty param.idCliente}">
	<sql:query var="rs">
		SELECT * FROM cliente_traduccion WHERE id=?
		<sql:param>${param.idCliente}</sql:param>
	</sql:query>
	<c:set var="data" value="${rs.rows[0]}" />
	<c:remove var="rs" />
</c:if>


<form name="cliente_traduccion" id="cliente_traduccion" action="cliente_traduccion_procesa.jsp">

<div class="formulario">
	<br/>
	<div class="columna_izq">
	
		<c:if test="${!empty data}">
	<input type="hidden" name="cliente_traduccion__id"  value="${data.id}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="cliente_traduccion__id"  value="${id}"  />
</c:if>

		
		<sql:query var="rs">
	
				SELECT 'Estudiante' AS 'option', 1 AS 'value'
				UNION SELECT 'Externo',2
				UNION SELECT 'Interno',3
			
</sql:query>

<c:if test="${!empty 'Tipo'}"><label for="cliente_traduccion__id_tipo">Tipo</label></c:if>

<select name="cliente_traduccion__id_tipo" id="cliente_traduccion__id_tipo">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.id_tipo==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>


		
		<c:if test="${!empty 'Página web'}"><label for="cliente_traduccion__web">Página web</label></c:if>
<input type="text" name="cliente_traduccion__web" id="cliente_traduccion__web" value="${data.web}" size="50" maxlength="256" />

		
		
<c:set var="checked" value=""/>
<c:if test="${data.particular}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Particular'}"><label for="cliente_traduccion__particular">Particular</label></c:if>
<input type="checkbox" name="cliente_traduccion__particular" ${checked} />

		
		
<c:set var="checked" value=""/>
<c:if test="${data.moroso}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Moroso'}"><label for="cliente_traduccion__moroso">Moroso</label></c:if>
<input type="checkbox" name="cliente_traduccion__moroso" ${checked} />

		
		
<c:set var="checked" value=""/>
<c:if test="${data.bolsa}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty 'Bolsa'}"><label for="cliente_traduccion__bolsa">Bolsa</label></c:if>
<input type="checkbox" name="cliente_traduccion__bolsa" ${checked} />

	
	</div>
	
	<div class="columna_drcha">
	
			<label><b>Tarifas</b></label>
		
			<c:if test="${!empty 'esp -> ing'}"><label for="cliente_traduccion__tarifa_esp_ing">esp -> ing</label></c:if>
<c:if test="${!empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_ing"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.tarifa_esp_ing}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_ing"></c:set>
</c:if>

<input type="text" name="cliente_traduccion__tarifa_esp_ing" id="cliente_traduccion__tarifa_esp_ing" value="${cliente_traduccion__tarifa_esp_ing}" size="10" maxlength="10"  onchange=""/>

			
			<c:if test="${!empty 'esp -> fr'}"><label for="cliente_traduccion__tarifa_esp_fr">esp -> fr</label></c:if>
<c:if test="${!empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_fr"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.tarifa_esp_fr}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_fr"></c:set>
</c:if>

<input type="text" name="cliente_traduccion__tarifa_esp_fr" id="cliente_traduccion__tarifa_esp_fr" value="${cliente_traduccion__tarifa_esp_fr}" size="10" maxlength="10"  onchange=""/>

			
			<c:if test="${!empty 'esp -> al'}"><label for="cliente_traduccion__tarifa_esp_al">esp -> al</label></c:if>
<c:if test="${!empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_al"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.tarifa_esp_al}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="cliente_traduccion__tarifa_esp_al"></c:set>
</c:if>

<input type="text" name="cliente_traduccion__tarifa_esp_al" id="cliente_traduccion__tarifa_esp_al" value="${cliente_traduccion__tarifa_esp_al}" size="10" maxlength="10"  onchange=""/>

			
		
	
	</div>
	
	<div class="antifloat"></div>
	<c:if test="${empty param.idCliente}">
		<p class="msg_alert">Debe guardar los datos para añadir personas de contacto.</p>
	</c:if>
	<c:if test="${!empty param.idCliente}">				
		<div>
			
<script type="text/javascript">

	jQuery(document).ready(function(){

		var mygrid = jQuery("#listado").jqGrid({
	        url:"${baseUrl}"+/traducciones/cliente_traduccion_contactos_xml.jsp?id=${param.idCliente},
	        datatype: "xml",
	        colNames:["id","Nombre","E-Mail"],
	        colModel:[{name:'cliente_traduccion_contactos__id', index:'cliente_traduccion_contactos__id', width:0, align:'right', search:true, editable:true, hidden:true},
{name:'cliente_traduccion_contactos__nombre', index:'cliente_traduccion_contactos__nombre', width:0, align:'right', search:true, editable:true, hidden:false},
{name:'cliente_traduccion_contactos__mail', index:'cliente_traduccion_contactos__mail', width:0, align:'right', search:true, editable:true, hidden:false}],
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
	        caption: "Personas de contacto",
	        multiselect: false,
	        id: "id",
	        imgpath: "../scripts/themes/steel/images/",
	       	        
	        ondblClickRow: function(rowid){
	            jQuery("#listado").editGridRow( rowid, null);
	        },
	        gridComplete: function(data){
				
		    },
	        editurl:"${baseUrl}"+/traducciones/cliente_traduccion_contactos_procesa.jsp?cliente_traduccion_contactos__fk_idClienteTraduccion=${param.idCliente}
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
	</c:if>
					
</div>
</form>
