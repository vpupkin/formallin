<c:if test="${!empty '$label'}"><label for="$table__$name">$label</label></c:if>
<input type="text" name="$table__$name" id="$table__$name" value="<fmt:formatDate value="${data.$name}" pattern="dd/MM/yyyy"/>" size="$size" maxlength="$maxlength" $readonly/>
