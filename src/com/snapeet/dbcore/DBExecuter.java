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

import java.util.Collection;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import com.snapeet.common.EmptyPoolException;

/**
 * A simple framework to execut some transactions on the database.
 *
 * The class parameter of the DBExecuter methods (class T if there is any) has to have the default 
 * constructor in order to be able to create an instance of the class in memory and then map it to 
 * the record in DB during get()/getAll() methods.
 *
 */
public class DBExecuter
{
	private static Logger _logger = Logger.getLogger(DBExecuter.class);

	public static int create(IDBCreatorMapper creator, IQueryParamsQueue params) throws SQLException, EmptyPoolException
	{
		Connection2 con = null;
		int key = 0;

		try
		{
			con = ConnectionPool.getConnection();
			key = create(con, creator, params, true);
		}
		catch (SQLException e)
		{
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}
		return key;
	}

	public static int create(Connection2 con, IDBCreatorMapper creator, IQueryParamsQueue params, boolean commit) throws SQLException
	{
		PreparedStatement2 stmt = null;
		PreparedStatement2 stmt2 = null;
		ResultSet rs = null;
		int key = 0;

		try
		{
			stmt = con.prepareStatement(creator.getStatement());
			creator.setStatement(stmt, params);
	
			key = stmt.executeUpdate();

			if ( creator.getSequence() != null )
			{
				stmt2 = con.prepareStatement(creator.getSequence());
				rs = stmt2.executeQuery();
				if ( rs.next() )
					key = rs.getInt(1);
			}

			if ( commit )
				con.commit();

		}
		catch (SQLException e)
		{
			con.rollback();
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( stmt != null )
				con.releaseStatement(stmt);

			if ( rs != null )
				rs.close();

			if ( stmt2 != null )
				con.releaseStatement(stmt2);
		}

		return key;
	}

	public static <T extends IDBMapper> T get(Class<T> clazz, IDBQueryMapper selector, IQueryParamsQueue params) 
		throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException
	{
		Connection2 con = null;
		PreparedStatement2 stmt = null;
		ResultSet rs = null;
		T obj = null;

		try
		{
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(selector.getStatement());
			selector.setStatement(stmt, params);

			rs = stmt.executeQuery();

			if ( rs.next() )
			{
				obj = clazz.newInstance();
				obj.map(rs);
			}
		}
		catch (SQLException e)
		{
			con.rollback();
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( rs != null )
				rs.close();

			if ( stmt != null )
				con.releaseStatement(stmt);

			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}

		return obj;

	}

        public static <K, T extends IDBMapper> void getCached(Class<T> clazz, IDBQueryMapper selector, IQueryParamsQueue params, IDBCache<K, T> cache) 
		throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException
	{
		Connection2 con = null;
		PreparedStatement2 stmt = null;
		ResultSet rs = null;

		try
		{
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(selector.getStatement());
			if ( params != null )
				selector.setStatement(stmt, params);
			rs = stmt.executeQuery();

			while ( rs.next() )
			{
				T obj = clazz.newInstance();
				obj.map(rs);
				try
				{
					cache.put(obj);
				}
				catch (DuplicateDataException e)
				{
					_logger.debug(e.toString());
				}
			}
		}
		catch (SQLException e)
		{
			con.rollback();
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( rs != null )
				rs.close();

			if ( stmt != null )
				con.releaseStatement(stmt);

			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}
	}

        public static <T extends IDBMapper> void getCollection(Class<T> clazz, IDBQueryMapper selector, IQueryParamsQueue params, Collection<T> collection) 
		throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException
	{
		Connection2 con = null;
		PreparedStatement2 stmt = null;
		ResultSet rs = null;

		try
		{
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(selector.getStatement());
			if ( params != null )
				selector.setStatement(stmt, params);
			rs = stmt.executeQuery();

			while ( rs.next() )
			{
				T obj = clazz.newInstance();
				obj.map(rs);
				collection.add(obj);
			}
		}
		catch (SQLException e)
		{
			con.rollback();
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( rs != null )
				rs.close();

			if ( stmt != null )
				con.releaseStatement(stmt);

			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}
	}

	public static void update(IDBQueryMapper updator, IQueryParamsQueue params) throws EmptyPoolException, SQLException
	{
		Connection2 con = null;
		try
		{
			con = ConnectionPool.getConnection();
			update(con, updator, params, true);
		}
		catch ( SQLException e )
		{
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}
	}
	
        public static void update(Connection2 con, IDBQueryMapper updator, IQueryParamsQueue params, boolean commit) throws SQLException
        {
                PreparedStatement2 stmt = null;

                try
                {
                        stmt = con.prepareStatement(updator.getStatement());
			updator.setStatement(stmt, params);
                        stmt.executeUpdate();

                        if ( commit )
                                con.commit();

                }
                catch (SQLException e)
                {
			con.rollback();
                        _logger.debug(e.toString());
                        throw e;
                }
                finally
                {
                        if ( stmt != null )
                                con.releaseStatement(stmt);
                }
        }

	/**
	 * returns true if it can delete the object from DB
	 */
	public static boolean remove(IDBQueryMapper remover, IQueryParamsQueue params) throws SQLException, EmptyPoolException
	{
		boolean deleted = false;
		Connection2 con = null;
		try
		{
			con = ConnectionPool.getConnection();
			deleted = remove(con, remover, params, true);
		}
		catch (SQLException e)
		{
			_logger.debug(e.toString());
			throw e;
		}
		finally
		{
			if ( con != null )
				ConnectionPool.releaseConnection(con);
		}

		return deleted;
	}

	public static boolean remove(Connection2 con, IDBQueryMapper remover, IQueryParamsQueue params, boolean commit) throws SQLException, EmptyPoolException
	{
		PreparedStatement2 stmt = null;
		int deleted = 0;

		try
		{
			stmt = con.prepareStatement(remover.getStatement());
			remover.setStatement(stmt, params);
			deleted = stmt.executeUpdate();

			if ( commit )
				con.commit();
		}
		catch (SQLException e)
		{
			con.rollback();
			_logger.debug(e.toString() + ", error code:" + e.getErrorCode());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if ( stmt != null )
				con.releaseStatement(stmt);
		}

		return deleted > 0;
	}
}
