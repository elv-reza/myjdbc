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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.lang.String;

/**
 * Connection2 is a wrapper of Connection with a cache of prepared statements.
 * To obtain a PreparedStatement, a lookup will be made in the cache and if the
 * PreparedStatement is in the cache it will be used (such a PreparedStatement will
 * not be in the cache anymore and has to be released).
 * If there is no PreparedStatement, a new one will be created and will be returned.
 * 
 * @author Reza Mir
 */
public class Connection2
{
	private Connection _con;
	private StatementCache _stmtCache;

	public Connection2(Connection con)
	{
		if ( con == null )
			throw new NullPointerException("Connection2() connection is null.");

		_con = con;
		_stmtCache = new StatementCache(_con);
	}

	public Statement createStatement() throws SQLException
	{
		return _con.createStatement();
	}

	public java.sql.Array createArrayOf(String typeName, Object[] elements)
		throws SQLException
	{
		return _con.createArrayOf(typeName, elements);
	}

	public PreparedStatement2 prepareStatement(String sql) throws SQLException
	{
		return _stmtCache.get(sql);
	}

	public void releaseStatement(PreparedStatement2 ps) throws SQLException
	{
		_stmtCache.put(ps);
	}

	public void commit() throws SQLException
	{
		_con.commit();
	}

	public void rollback() throws SQLException
	{
		_con.rollback();
	}

	public void close() throws SQLException
	{
		_stmtCache.close();
		_stmtCache = null;
		_con.close();
		_con = null;
	}

	public boolean isClosed() throws SQLException
	{
		if ( _con == null )
			return true;

		return _con.isClosed();
	}
	
}
