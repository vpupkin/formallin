<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>
<input type="text" name="$table__$name" id="$table__$name" value="${data.$name}" size="$size" maxlength="$maxlength" onchange="$onchange" $readonly/>
