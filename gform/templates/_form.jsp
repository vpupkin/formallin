<%@page	contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="../dataSource.jsp" %>
<%@include file="../cabecera_scripts.jsp"%>
<c:import url="../comun/util.jsp"/>

<%--
<fmt:setBundle basename="gform"/>
<fmt:message key="$key.$name"/>
--%>

<c:if test="${!empty param.$key}">
	<sql:query var="rs">
		SELECT * FROM $table WHERE $key=?
		<sql:param>${param.$key}</sql:param>
	</sql:query>
	<c:set var="data" value="${rs.rows[0]}"/>
	<c:remove var="rs"/>
</c:if>

<head>
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    
    <jsp:useBean id="prop" class="sri.Properties"/>
	<c:set target="${prop}" property="request" value="${pageContext.request}"/>
	<base href="${prop.base}/" />
    
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
			   
        function inicia(){
        <c:if test="${not empty param.mensaje}">
                alert('${param.mensaje}');
        </c:if>
        }
	</script>
</head>

<body onload="inicia();">
<%@include file="../cabecera.jsp"%>
	<div class="migas">
        <br/>
        <c:choose>
            <c:when test="${param.filtro_idTipoCli==1}"><strong>Alumnos::Gesti&oacute;n de Alumnos</strong></c:when>
            <c:when test="${param.filtro_idTipoCli==7}"><strong>Servicio de Traducci&oacute;n::Traductores</strong></c:when>
            <c:when test="${param.filtro_idTipoCli==8}"><strong>Empresa::Gesti&oacute;n de Personal</strong></c:when>
            <c:when test="${param.filtro_idTipoCli==9}"><strong>Gesti&oacute;n Acad&eacute;mica::Profesores</strong></c:when>
            <c:when test="${param.filtro_idTipoCli==10}"><strong>Servicio de Traducci&oacute;n::Clientes</strong></c:when>
            <c:otherwise><strong>Alumnos::Gesti&oacute;n de Alumnos</strong></c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${modificar}">
                ->Modificando datos del ${nomTipoCli}
                <%--[<span style="font-size:80%;"><fmt:formatNumber value="${idCliente}" pattern="000000"/></span>]--%>
                [<span style="font-size:80%;">${cliente.apellidos}, ${cliente.nombre}</span>]
            </c:when>
            <c:otherwise>
                ->Insertando nuevo ${nomTipoCli}
            </c:otherwise>
        </c:choose>
    </div>
    
    <%-- <div class="botones"> --%>
    <div id="btnsAlumno" class="btnsAlumno" align="right">
        <input type="button" class="boton" onclick="enviaFormulario()" value="${submitValue}"/>
        <c:url var="u" value="cliente_listado.jsp?filtro_nacionalidad=${param.filtro_nacionalidad}&filtro_idTipoCli=${param.filtro_idTipoCli}"/>
        <input type="button" class="boton" value="Cancelar" onclick="document.location='${u}'"/>
    </div>
    
     <div class="tabs">
        <ul>
            <li id="li_tab1" onmousedown="clickTab(this,'div_tab1')" class="current">
                <a href="javascript:void(null);">Datos persona</a>
            </li>
        </ul>
    </div>
    <div class="tabs_cont" style="height:100%">
		<form id="$form.name" name="$form.name" action="$form.action">
		<br/>
		$form.fields
		
		</form>
	</div>
</body>