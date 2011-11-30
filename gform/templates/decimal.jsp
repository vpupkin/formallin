<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>
<c:if test="${!empty data}">
	<c:set var="$table__$name"><fmt:formatNumber type="number" maxFractionDigits="4" value="${data.$name}" /></c:set>
</c:if>
<c:if test="${empty data}">
	<c:set var="$table__$name">$defvalue</c:set>
</c:if>

<input type="text" name="$table__$name" id="$table__$name" value="${$table__$name}" size="$size" maxlength="$maxlength" $readonly onchange="$onchange"/>
