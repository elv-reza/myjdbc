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

/**
 * A simple interface to add parameters for a DB query (SQL statement) and retrieve them later.
 * The order of paramaters inside the queue must be the same order that are in the query string.
 */
public interface IQueryParamsQueue 
{
	public void enqueueInt(Integer n);
	public Integer dequeueInt(); 

	public void enqueueLong(Long l);
	public Long dequeueLong();

	public void enqueueFloat(Float f);
	public Float dequeueFloat();

	public void enqueueDouble(Double d);
	public Double dequeueDouble();

	public void enqueueByte(Byte b);
	public Byte dequeueByte();

	public void enqueueChar(Character c);
	public Character dequeueChar();

	public void enqueueString(String s);
	public String dequeueString();

	public void enqueueBoolean(Boolean b);
	public Boolean dequeueBoolean();

	public int size();
	public void clear();
}
