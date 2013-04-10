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
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * StatementCache is a cache of PreparedStatements with in a Connection.
 * To obtain a PreparedStatement, a lookup will be made in the cache and if the
 * PreparedStatement is in the cache it will be used (such a PreparedStatement will
 * not be in the cache anymore and has to be released).
 * If there is no PreparedStatement, a new one will be created and will be returned.
 * Notice that after using PreparedStatement, the programmer has to release it.
 * Do not close a PreparedStatemet that obtained from the cache.
 * 
 * @author Reza Mir
 */

public class StatementCache
{
	private static Logger _logger = Logger.getLogger(StatementCache.class);
	private Connection _con;
	private Hashtable<String, List<PreparedStatement2> > _table;

	public StatementCache(Connection con)
	{
		_con = con;
		_table = new Hashtable<String, List<PreparedStatement2> >();
	}

	private PreparedStatement2 allocStatement(String stmtstr) throws SQLException
	{
		PreparedStatement ps = _con.prepareStatement(stmtstr);
		PreparedStatement2 ps2 = new PreparedStatement2(stmtstr, ps);
		_logger.debug("StatementCache.allocStatement(): '" + stmtstr + "'");
		return ps2;
	}

	synchronized PreparedStatement2 get(String stmtstr) throws SQLException
	{
		List<PreparedStatement2> l = _table.get(stmtstr);
		PreparedStatement2 ps2 = null;

		_logger.debug("StatementCache.get(): '" + stmtstr + "'");
		if ( l == null )
		{
			ps2 = allocStatement(stmtstr);
			return ps2;
		}

		/** List of PreparedStatement2 may have one idle */
		Iterator<PreparedStatement2> itr = l.iterator();
		if ( itr.hasNext() )
		{
			ps2 = itr.next();
			l.remove(itr);
			_logger.debug("StatementCache.get() '" + stmtstr + "' from cache");
			return ps2;
		}

		ps2 = allocStatement(stmtstr);
		return ps2;
	}

	synchronized void put(PreparedStatement2 stmt)
	{
		List<PreparedStatement2> l = _table.get(stmt.getString());
		if ( l == null )
			l = new LinkedList<PreparedStatement2>();

		l.add(stmt);
		_table.put(stmt.getString(), l);

		_logger.debug("StatementCache.put(): '" + stmt + "'");
	}
	
	synchronized void close() throws SQLException
	{
		for (List<PreparedStatement2> l : _table.values())
		{
			for (PreparedStatement2 stmt: l)
				stmt.close();
		}

		_table.clear();
	}
}
