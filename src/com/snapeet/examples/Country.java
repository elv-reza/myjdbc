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
package com.snapeet.examples;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;
import com.snapeet.common.EmptyPoolException;
import com.snapeet.common.AppProperties;
import com.snapeet.dbcore.*;

public class Country implements IDBMapper
{
	private	static Logger _logger = Logger.getLogger(Country.class);
	private static String CREATE_STMT = "INSERT INTO countries (id, name) VALUES (?, ?)";
	private static CountryCreator CREATOR = new CountryCreator();
	private static String REMOVE_STMT = "DELETE FROM countries WHERE id=?";
	private static CountryRemover REMOVER = new CountryRemover();
	private static String SELECT_STMT = "SELECT * FROM countries";
	private static CountrySelector SELECTOR = new CountrySelector();

	private int _id;
	private String _name;

	public Country()
	{
		_id = 0;
		_name = null;
	}

	public Country(int id, String name)
	{
		_id = id;
		_name = name;
	}

	public int getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}

	@Override
	public boolean equals(Object o)
	{
		if ( o == this )
			return  true;

		if ( o == null )
			return false;

		if ( !(o instanceof Country ) )
			return false;

		Country c = (Country) o;
		if ( c._id == this._id && c._name.equals(this._name) )
			return true;

		return false;
	}

	@Override
	public int hashCode()
	{
		return _name.hashCode();
	}

	@Override
	public String toString()
	{
		String s = "" + _id;
		s += "|" + _name;
		return s;
	}

	public void map(ResultSet rs) throws SQLException
	{
		_id = rs.getInt("id");
		_name = rs.getString("name");
	}

	private static class CountryCreator implements IDBCreatorMapper
        {
		public void setStatement(PreparedStatement2 stmt, IQueryParamsQueue params) throws SQLException
		{
			if ( params == null )
				throw new NullPointerException("params is null");

			stmt.setInt(1, params.dequeueInt());
			stmt.setString(2, params.dequeueString());
		}

		public String getStatement()
		{
			return Country.CREATE_STMT;
		}

		public String getSequence()
		{
			return null;
		}
	}

	public static int create(Country country) throws EmptyPoolException, SQLException
	{
		IQueryParamsQueue params = new QueryParamsQueue();
		params.enqueueInt(country.getId());
		params.enqueueString(country.getName());
		return DBExecuter.create(Country.CREATOR, params);
	}

	private static class CountryRemover implements IDBQueryMapper
        {
		public void setStatement(PreparedStatement2 stmt, IQueryParamsQueue params) throws SQLException
		{
			if ( params == null )
				throw new NullPointerException("params is null");

			stmt.setInt(1, params.dequeueInt());
		}

		public String getStatement()
		{
			return Country.REMOVE_STMT;
		}
        }

	public static void remove(int id) throws EmptyPoolException, SQLException
	{
		IQueryParamsQueue params = new QueryParamsQueue();
		params.enqueueInt(id);
		DBExecuter.remove(Country.REMOVER, params);
	}

	private static class CountrySelector implements IDBQueryMapper
	{
		public void setStatement(PreparedStatement2 stmt, IQueryParamsQueue params) throws SQLException
		{
		}

		public String getStatement()
		{
			return Country.SELECT_STMT;
		}
	}

	public static void getAll(CountryCache cache) throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException
	{
		DBExecuter.getCached(Country.class, Country.SELECTOR, null, cache);

		_logger.debug("getAll() found " + cache.size() + " countries");
	}

	public static void main(String[] args)
	{
		Country[] countries = { new Country(100, "Canada"), new Country(200, "United States"), new Country(300, "Germany") };

		try
		{
			AppProperties.setAppConfigPath("conf/examples.cfg");
			AppProperties properties = AppProperties.getInstance();

			// make the connectoins to the DB
			ConnectionPool.init(properties.getNumConnections(), properties.getDbURL(),
				properties.getDBDriver(), properties.getDBUser(), properties.getDBPassword(), false);

			for (Country country : countries)
				Country.create(country);

			CountryCache cache = CountryCache.getInstance();
			cache.load();
			for (Country country : cache.getObjects())
				System.out.println("Country:" + country.toString());

			/** Clean up the DB */
			for (Country country : cache.getObjects())
				Country.remove(country.getId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
