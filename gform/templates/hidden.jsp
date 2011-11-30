<c:if test="${!empty data}">
	<input type="hidden" name="$table__$name"  value="${data.$name}"  />
</c:if>
<c:if test="${empty data}">
	<input type="hidden" name="$table__$name"  value="${$name}"  />
</c:if>