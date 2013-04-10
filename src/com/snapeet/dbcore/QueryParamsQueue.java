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

import java.util.ArrayList;

/**
 * An implementation of IQueryParamsQueue interface that can be used for passing query parameters
 * to an IDBQueryMapper.
 */
public class QueryParamsQueue implements IQueryParamsQueue
{
	private ArrayList<Object> _list;

	public QueryParamsQueue()
	{
		_list = new ArrayList<Object>();
	}

	public void enqueueInt(Integer n)
	{
		_list.add(n);
	}

	public Integer dequeueInt()
	{
		Integer n = (Integer) _list.get(0);
		_list.remove(0);
		return n;
	}

	public void enqueueLong(Long l)
	{
		_list.add(l);
	}

	public Long dequeueLong()
	{
		Long l = (Long) _list.get(0);
		_list.remove(0);
		return l;
	}

	public void enqueueFloat(Float f)
	{
		_list.add(f);
	}

	public Float dequeueFloat()
	{
		Float f = (Float) _list.get(0);
		_list.remove(0);
		return f;
	}

	public void enqueueDouble(Double d)
	{
		_list.add(d);
	}

	public Double dequeueDouble()
	{
		Double d = (Double) _list.get(0);
		_list.remove(0);
		return d;
	}

	public void enqueueByte(Byte b)
	{
		_list.add(b);
	}

	public Byte dequeueByte() 
	{
		Byte b = (Byte) _list.get(0);
		_list.remove(0);
		return b;
	}

	public void enqueueChar(Character c)
	{
		_list.add(c);
	}

	public Character dequeueChar()
	{
		Character c = (Character) _list.get(0);
		_list.remove(0);
		return c;
	}

	public void enqueueString(String s)
	{
		_list.add(s);
	}

	public String dequeueString()
	{
		String s = (String) _list.get(0);
		_list.remove(0);
		return s;
	}

	public void enqueueBoolean(Boolean b)
	{
		_list.add(b);
	}

	public Boolean dequeueBoolean()
	{
		Boolean b = (Boolean) _list.get(0);
		_list.remove(0);
		return b;
	}

	public int size()
	{
		return _list.size();
	}

	public void clear()
	{
		_list.clear();
	}
}
