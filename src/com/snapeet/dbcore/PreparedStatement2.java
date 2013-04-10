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
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * A wrapper class on top of PreparedStatement that keeps the corresponding query String too.
 */
public class PreparedStatement2
{
	private PreparedStatement _ps;
	private String _str;

	public PreparedStatement2(String str, PreparedStatement ps)
	{
		_str = str;
		_ps = ps;
	}

	public String getString()
	{
		return _str;
	}

	public PreparedStatement getStatement()
	{
		return _ps;
	}

	public void clearParameters() throws SQLException
	{
		_ps.clearParameters();
	}

	public boolean execute() throws SQLException
	{
		return _ps.execute();
	}

	public ResultSet executeQuery() throws SQLException
	{
		return _ps.executeQuery();
	}

	public int executeUpdate() throws SQLException
	{
		return _ps.executeUpdate();
	}

	public void setBoolean(int paramIndex, boolean x) throws SQLException
	{
		_ps.setBoolean(paramIndex, x);
	}

	public void setShort(int paramIndex, short x) throws SQLException
	{
		_ps.setShort(paramIndex, x);
	}

	public void setInt(int paramIndex, int x) throws SQLException
	{
		_ps.setInt(paramIndex, x);
	}

	public void setLong(int paramIndex, long x) throws SQLException
	{
		_ps.setLong(paramIndex, x);
	}

	public void setFloat(int paramIndex, float x) throws SQLException
	{
		_ps.setFloat(paramIndex, x);
	}

	public void setDouble(int paramIndex, double x) throws SQLException
	{
		_ps.setDouble(paramIndex, x);
	}

	public void setBigDecimal(int paramIndex, BigDecimal x) throws SQLException
	{
		_ps.setBigDecimal(paramIndex, x);
	}

	public void setString(int paramIndex, String x) throws SQLException
	{
		_ps.setString(paramIndex, x);
	}

	public void setByte(int paramIndex, byte x) throws SQLException
	{
		_ps.setByte(paramIndex, x);
	}

	public void setBytes(int paramIndex, byte[] x) throws SQLException
	{
		_ps.setBytes(paramIndex, x);
	}

	public void setDate(int paramIndex, Date x) throws SQLException
	{
		_ps.setDate(paramIndex, x);
	}

	public void setTime(int paramIndex, Time x) throws SQLException
	{
		_ps.setTime(paramIndex, x);
	}

	public void setTimestamp(int paramIndex, Timestamp x) throws SQLException
	{
		_ps.setTimestamp(paramIndex, x);
	}

	public void setArray(int paramIndex, java.sql.Array sqlArray) throws SQLException
	{
		_ps.setArray(paramIndex, sqlArray);
	}

	public void close() throws SQLException
	{
		_ps.close();
	}
}
