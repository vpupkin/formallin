<sql:query var="rs">
	$query
</sql:query>
<c:set var="itemValues" value=""/>
<c:forEach items="${rs.rows}" var="item" varStatus="s">
	<c:set var="itemValues">${itemValues}${item.value}:${item.option}<c:if test="${!s.last}">;</c:if></c:set>
</c:forEach>

<c:remove var="rs"/>

editoptions:{value:"${itemValues}"}