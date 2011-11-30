<sql:query var="rs">
	$query
</sql:query>

<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>

<select name="$table__$name" id="$table__$name">
	<option value=""></option>
	<c:forEach items="${rs.rows}" var="item">
		<c:set var="selected"><c:if test="${data.$name==item.value}">selected</c:if></c:set>
		<option value="${item.value}" ${selected}><c:out value="${item.option}"/></option>
	</c:forEach>
</select>

<c:remove var="rs"/>

