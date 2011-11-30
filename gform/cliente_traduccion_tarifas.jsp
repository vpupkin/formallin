<form table="cliente_traduccion_tarifas">

	<element>
		<name>id</name>
		<type>INT</type>
		<key>true</key>
		<auto>true</auto>
		<hidden>true</hidden>
	</element>

	<element>
		<name>fk_idClienteTraduccion</name>
		<type>INT</type>
		<size>11</size>
		<references>cliente_traduccion(id)</references>
		<hidden>true</hidden>
	</element>
	
	<element>
		<label>Idioma Origen</label>
		<name>id_idioma_origen</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma ORDER BY idioma</query>
		<references>idioma(id)</references>
	</element>
	
	<element>
		<label>Idioma Traducción</label>
		<name>id_idioma_trad</name>
		<type>select</type>
		<query>SELECT idioma AS 'option', id AS 'value' FROM idioma ORDER BY idioma</query>
		<references>idioma(id)</references>
	</element>
	
	<element>
		<label>Tarifa</label>
		<name>tarifa</name>
		<type>DECIMAL</type>
		<size>10,2</size>
	</element>

</form>