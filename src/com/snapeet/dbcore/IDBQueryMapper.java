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

import java.sql.SQLException;

/**
 * Represents a DB query in general. The query can be a select, insert, delete, or update on some data.
 * There can be different queries and each one has to to implement this interface.
 * A usual way to represent queries is using private static classes inside a plain Java object.
 */
public interface IDBQueryMapper
{
	/**
	 * Sets the parameter of the query.
	 * If there is no parameter to be set on the query (i.e "select * from users") 
	 * then this function will do nothing.
	 */
	public void setStatement(PreparedStatement2 stmt, IQueryParamsQueue params) throws SQLException;

	/** Returns the String containing the query */
	public String getStatement();
}
