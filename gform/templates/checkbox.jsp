
<c:set var="checked" value=""/>
<c:if test="${data.$name}"><c:set var="checked" value="checked"/></c:if>
<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>
<input type="checkbox" name="$table__$name" ${checked} />
