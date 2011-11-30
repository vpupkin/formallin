<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>
<textarea name="$table__$name" cols="$cols" rows="$rows"><c:out value="${data.$name}"/></textarea>
