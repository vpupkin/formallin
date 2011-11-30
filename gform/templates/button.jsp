<c:if test="${$button_if}">
	<button id="bDatos_$name" type="button">Datos</button>
	<script type="text/javascript">
		document.getElementById('bDatos_$name').onclick = 
			function(){
				var w = 1152;
				var h = 720;
				var winl = (screen.width - w) / 2;
				var wint = (screen.height - h) / 2;
			
				var selectElement = document.getElementById('$table__$name');
				var selectValue = selectElement[selectElement.selectedIndex].value;

				if(selectValue != null && selectValue != ''){

					var url = '${baseUrl}/'+'$button_url'+selectValue;
				
					window.open(url, "Datos",
						"dependent=yes,toolbar=no,resizable=yes,scrollbars=yes,status=no,width="
						+ w + ",height=" + h + ",left=" + winl + ",top=" + wint);
				}

		};
	</script>
</c:if>