package eu.blky.wdb;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import net.sf.jsr107cache.Cache;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  06.06.2012::16:04:15<br> 
 */
public class Connection2Cache implements Connection {

	private Cache cache;
	private boolean closed;

	public Connection2Cache(Cache cache) {
		this.cache = cache;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new Statement2Cache(this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void close() throws SQLException {
		this.closed = true;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	public Cache getCache() {
		return this.cache;
	}

}


 