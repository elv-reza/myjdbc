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

import java.util.*;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.snapeet.common.EmptyPoolException;
import com.snapeet.dbcore.*;

public class CountryCache implements IDBCache<Integer, Country>
{
	private Logger _logger = Logger.getLogger(CountryCache.class);

	private static CountryCache _instance = null;
	private Map<Integer, Country> _countryMap;
	private Map<String, Country> _nameMap;

	private CountryCache()
	{
		_countryMap = new Hashtable<Integer, Country>();
		_nameMap = new Hashtable<String, Country>();
		_logger.debug("CountryCache() instance was created.");
	}

	public static synchronized CountryCache getInstance()
	{
		if ( _instance == null )
			_instance = new CountryCache();
		return _instance;
	}

	public synchronized void load() throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException
	{
		Country.getAll(this);

		_logger.debug("load() was done successfully, cache siz:" + size());
	}

	public void put(Country country) throws DuplicateDataException
	{
		int id = country.getId();

		_logger.debug("country:" + country.toString());

		Country old_country = _countryMap.get(new Integer(id));

		if ( old_country != null )
			throw new DuplicateDataException("Duplicate country id:" + id);

		_countryMap.put(id, country);
		_nameMap.put(country.getName(), country);
	}

	public Country get(Integer id)
	{
		Country country = _countryMap.get(id);

		if ( country != null )
			_logger.debug("get() country:" + country.toString());

		return country;
	}

	public Country get(String name)
	{
		return _nameMap.get(name);
	}

	public Set<Integer> getIds()
	{
		return _countryMap.keySet();
	}

	public Collection<Country> getObjects()
	{
		return _countryMap.values();
	}

	public SortedSet<String> getCountryNames()
	{
		TreeSet<String> set = new TreeSet<String>();

		Collection<Country> countrys = getObjects();

		Iterator<Country> itr = countrys.iterator();
		while ( itr.hasNext() )
		{
			Country country = itr.next();
			String name = country.getName();
			set.add(name);
		}

		return set;
	}

	public void update(Country country)
	{
		throw new UnsupportedOperationException("not implemented yet!");
	}

	public void remove(Integer id)
	{
		throw new UnsupportedOperationException("not implemented yet!");
	}

	public int size()
	{
		return _countryMap.size();
	}

	public void cleanup()
	{
		_countryMap.clear();
		_nameMap.clear();
	}
}
