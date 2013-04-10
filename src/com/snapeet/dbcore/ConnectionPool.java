/**
 * Copyright (c) 2008 / 2013  Snapeet Inc (rezamirz@gmail.com).
 *
 * dbcore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.snapeet.dbcore;

import java.sql.*;
import org.apache.log4j.Logger;
import com.snapeet.common.PoolManager;
import com.snapeet.common.EmptyPoolException;

public class ConnectionPool
{
	private static Logger _logger = Logger.getLogger(ConnectionPool.class);
	private static PoolManager<Connection2> _pool = new PoolManager<Connection2>();
	private static boolean _connected = false;

	private ConnectionPool() {}

	public static synchronized void init(int number, String dburl, String dbdriver, String user, String password, boolean autocommit)
		throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if ( _connected )
			return;

		_logger.debug("init() URL:" + dburl + ", driver:" + dbdriver + ", user:" + user + ", password:" + password);

		Class.forName(dbdriver).newInstance();
		for (int i=0; i<number; i++)
		{
			Connection con = DriverManager.getConnection(dburl, user, password);
			con.setAutoCommit(autocommit);
			Connection2 con2 = new Connection2(con);
			_pool.add(con2);
		}

		_connected = true;
		_logger.debug("init() completed loading db driver");
	}

	public static Connection2 getConnection() throws EmptyPoolException
	{
		synchronized (_pool)
		{
			return (Connection2) _pool.get();
		}
	}

	public static void releaseConnection(Connection2 c)
	{
		if ( c != null )
		{
			synchronized ( _pool )
			{
				_pool.release(c);
			}
		}
	}

	public static boolean isConnected()
	{
		return _connected;
	}

	public static void close()
	{
		if ( !_connected )
			return;

		for (int i=0; i<_pool.poolSize(); i++)
		{
			try
			{
				Connection2 con2 = getConnection();
				con2.close();
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				_logger.debug("close() in used connections:" + (_pool.poolSize()-i));
				break;
			}
		}
		_pool.clear();
		_connected = false;
	}
}
