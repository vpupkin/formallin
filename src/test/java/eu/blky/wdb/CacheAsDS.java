package eu.blky.wdb;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.jsr107cache.Cache;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  06.06.2012::16:01:58<br> 
 */
public class CacheAsDS implements DataSource {

	private Cache cache;

	public CacheAsDS(Cache cacheTmp) {
		this.cache = cacheTmp;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return null;
		}
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
		}
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 06.06.2012");
		else {
			return 0;
		}
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
	public Connection getConnection() throws SQLException {
		return new Connection2Cache(this.cache);
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return getConnection();
	}

}


 