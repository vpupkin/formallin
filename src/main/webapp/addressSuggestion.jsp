<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>Search - National Register of Historic Places</title>
	</head>
	<script language='JavaScript'>
		//For Ajax 
		var req;
		
		function init() {
			if (window.XMLHttpRequest) 
				req = new XMLHttpRequest;
			else if (window.ActiveXObject)
				req = new ActiveXObject("Microsoft.XMLHTTP");
			var url = 'http://' + location.host + '/GForm/GetAddress';
			req.open("POST", url, true);
			req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		}
		
		function initiateSearch() {
			var searchString = document.getElementById('address').value;
			if ((searchString == null) || (searchString.length < 3)) {
				return;
			}
			init();
			req.onreadystatechange = displayResults;
			var request="searchString="+searchString;
			req.send(request);
		}
		
		function displayResults() {
			if (req.readyState == 4) {
				if (req.status == 200) {
					var propertyTags = req.responseXML.getElementsByTagName('property');
					for (var i=0; i< propertyTags.length; i++) {
						var full_address = "<table width='100%' cellspacing='0' cellpadding='1'><tr style='background-color: #CCDEF7;'><td><b>";
						for (var j=0; j< propertyTags[i].getElementsByTagName('name').length; j++)
						full_address = full_address + 
										propertyTags[i].getElementsByTagName('name')[j].firstChild.nodeValue;
						full_address =  full_address + "</b></td></tr><tr style='background-color: #DDE9FB;'><td>" + 
								propertyTags[i].getElementsByTagName('address')[0].firstChild.nodeValue
								+ ' ' + propertyTags[i].getElementsByTagName('city')[0].firstChild.nodeValue
								+ ' ' + propertyTags[i].getElementsByTagName('state')[0].firstChild.nodeValue
								+ '</td></tr></table>';
						var row = document.getElementById('table001').insertRow(-1);
						var cell = row.insertCell(-1);
						cell.innerHTML = full_address;
					}	
				}
			}	
		}
		
		function removeResults() {
			var resultsTable = document.getElementById('table001');
			var records = resultsTable.rows.length;
			for (var i=(records-1); i> 0; i--)
				resultsTable.deleteRow(i);
		}
	</script>
	<body>
	<font face='Verdana' size='2'>
		<table id='table001' width='50%' cellspacing='2'>
			<tr style='background-color: #C6F9D2;'>
				<td>
					&nbsp;&nbsp;Search &nbsp;&nbsp;&nbsp;
					<input type='text' id='address' value='' onkeyup='removeResults();initiateSearch()' width=30>
					&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</font>
	</body>
</html>
