<c:if test="${!empty data.$refelement}">
	<sql:query var="rs">
		$query
		<sql:param>${data.$refelement}</sql:param>
	</sql:query>
</c:if>

<c:if test="${!empty '$label'}">
<label for="$table__$name">$label</label>
</c:if>
<select name="$table__$name" id="$table__$name">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.$name==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>


<script type="text/javascript">
	document.getElementById('$table__$refelement').onchange = 
		function(){
			var paramValue = this[this.selectedIndex].value;
			//var params = new Array();
			//params['$refelement']=paramValue;
			reloadSelect('$table__$name', '$ajaxUrl', {$refelement:paramValue});
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