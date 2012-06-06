package eu.blky.wdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import net.sf.jsr107cache.Cache;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  06.06.2012::16:05:45<br> 
 */
public class Statement2Cache implements Statement {
 
	private Connection2Cache connection;
	private boolean closed;

	public Statement2Cache(Connection2Cache connection) {
		this.connection= connection; 
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
	public ResultSet executeQuery(String sql) throws SQLException {
		return new ResultSet4Cache(this, sql);
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void close() throws SQLException {
		this.closed = true;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
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
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public Connection getConnection() throws SQLException { 
		return this.connection;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
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
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return false;
		}
	}

	public Cache getCache() {
		return this.connection.getCache();
	}

}


 