
<form table="servicios_traduccion_traductor" alias="stt">
	
	
	<element>
		<name>id</name>
		<type>int</type>
		<key>true</key>
		<hidden>true</hidden>
		<auto>true</auto>
	</element>
	
	<element>
		<name>id_orden</name>
		<type>int</type>
		<hidden>true</hidden>
		<references>servicios_traduccion(id_orden)</references>
		<ondelete>CASCADE</ondelete>
		<notnull>true</notnull>
	</element>


	<element>
		<name>tipo_servicio</name>
		<type>int</type>
		<hidden>true</hidden>
		<references>tipo_servicios_traduccion(id)</references>
		<notnull>true</notnull>
	</element>

	<element>
		<label>Traductor</label>
		<name>id_traductor</name>
		<type>select</type>
		<references>traductor(id)</references>
		<query>
			SELECT CONCAT_WS(',', apellidos, nombre) AS 'option', id AS 'value'
			FROM traductor
				INNER JOIN cliente ON idCliente=id
		</query>
		<notnull>true</notnull>
		<onchange>set_def_tarifa()</onchange>
	</element>

	<element>
		<label>Palabras/Horas</label>
		<name>palabras</name>
		<type>INT</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>

	<element>
		<label>Minutos</label>
		<name>minutos</name>
		<type>INT</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>

	<element>
		<label>Mínimo</label>
		<name>minimo</name>
		<type>DECIMAL</type>
		<size>19,2</size>
		<defvalue>0</defvalue>
		<onchange>recalc_subform_traductores()</onchange>
		
	</element>

	<element>
		<label>Tarifa traductor</label>
		<name>tarifa_traductor</name>
		<type>DECIMAL</type>
		<size>19,4</size>
		<defvalue>0</defvalue>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	
	<element>
		<label>Tarifa hora</label>
		<name>tarifa_hora</name>
		<type>DECIMAL</type>
		<size>19,4</size>
		<defvalue>0</defvalue>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	
	<element>
		<label>Km</label>
		<name>km</name>
		<type>int</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	<br/>
	
	<element>
		<label>Precio/Km</label>
		<name>precio_km</name>
		<type>DECIMAL</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>

	<element>
		<label>Gastos Desplazamiento</label>
		<name>gastos_desplazamiento</name>
		<type>DECIMAL</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	<br/>
	
	<element>
		<label>Dietas</label>
		<name>dietas</name>
		<type>DECIMAL</type>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	
	<element>
		<label>Fecha frac</label>
		<name>fecha_factura_trad</name>
		<type>DATETIME</type>
	</element>
	
	<element>
		<label>Nº frac</label>
		<name>num_factura_trad</name>
		<type>VARCHAR</type>
	</element>
	
	<element>
		<label>Total traductor</label>
		<name>total_traductor</name>
		<type>DECIMAL</type>
		<size>19,2</size>
		<readonly>true</readonly>
	</element>
	
	<element>
		<label>IVA</label>
		<name>iva</name>
		<type>DECIMAL</type>
		<size>16,4</size>
		<defvalue>0</defvalue>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	
	<element>
		<label>IRPF</label>
		<name>irpf</name>
		<type>DECIMAL</type>
		<size>16,4</size>
		<defvalue>0</defvalue>
		<onchange>recalc_subform_traductores()</onchange>
	</element>
	
	<element>
		<label>Total IVA, &#8364;</label>
		<name>total_iva</name>
		<type>DECIMAL</type>
		<size>19,2</size>
	</element>
	
	<element>
		<label>Total IRPF, &#8364;</label>
		<name>total_irpf</name>
		<type>DECIMAL</type>
		<size>19,2</size>
	</element>
	
	<element>
		<label>Coste final</label>
		<name>coste_final</name>
		<type>DECIMAL</type>
		<size>19,2</size>
	</element>
	
	<element>
		<label>Facturado Traductor</label>
		<name>facturado_traductor</name>
		<type>CHECKBOX</type>
	</element>
	
	<element>
		<label>Comentario</label>
		<name>fcomentario</name>
		<type>textarea</type>
		<cols>50</cols>
		<rows>10</rows>
	</element>
	
</form>