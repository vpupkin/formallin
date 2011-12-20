<form table="cliente_traduccion_contactos">

	<element>
		<name>id</name>
		<type>INT</type>
		<size>11</size>
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
		<label>Nombre</label>
		<name>nombre</name>
		<type>VARCHAR</type>
		<size>100</size>
	</element>
	
	<element>
		<label>E-Mail</label>
		<name>mail</name>
		<type>VARCHAR</type>
		<size>100</size>
	</element>
	
	<element>
		<label>Teléfono</label>
		<name>telefono</name>
		<type>VARCHAR</type>
		<size>100</size>
	</element>
</form>