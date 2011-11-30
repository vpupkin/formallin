
<script type="text/javascript">

	jQuery(document).ready(function(){

		var mygrid = jQuery("#listado").jqGrid({
	        url:$url,
	        datatype: "xml",
	        colNames:[$colNames],
	        colModel:[$colModel],
	        autowidth: false,
	        rownumbers: false,
	        pager: jQuery('#paginacion'),
	        rowNum:5,
	        rowList:[5,15,20,50],
	        height:120,
	        width:800,
	        sortname: 'orden',
	        mtype: "POST",
	        viewrecords: true,
	        sortorder: "asc",
	        gridview : true,
	        caption: "$title",
	        multiselect: false,
	        id: "id",
	        imgpath: "../scripts/themes/steel/images/",
	       	        
	        ondblClickRow: function(rowid){
	            jQuery("#listado").editGridRow( rowid, null);
	        },
	        gridComplete: function(data){
				$gridComplete
		    },
	        editurl:$procesa_url
	    })
	    .navGrid('#paginacion',{edit:true,add:true,del:true,search:false,refresh:true});

		
		$("#b_add").click(function(){
			jQuery("#listado").editGridRow("new",{reloadAfterSubmit:false});
		});
/*
		$("#b_del").click(function(){
			var gr = jQuery("#listado").getGridParam('selrow');
			if( gr != undefined && gr != null) {
				var row = jQuery("#listado").getRowData(gr);
				jQuery("#listado").delGridRow(row.id,{reloadAfterSubmit:false});
			}else{
                alert("Por favor, seleccione un registro.");
            }
		});*/
	});

</script>


<div style="margin-left:160px;">
	<table id="listado"></table>
	<div id="paginacion"></div>
	<%--
	<button type="button" id="b_add">Nuevo</button>
	<button type="button" id="b_del">Borrar</button> --%>
</div>
 