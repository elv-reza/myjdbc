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

import java.util.Set;
import java.util.Collection;
import java.sql.SQLException;
import com.snapeet.common.EmptyPoolException;

/**
 * Defines a cache of objects of type E. K is the type of the key to access an object in the cache.
 * 
 */
public interface IDBCache<K, E>
{
	/**
	 * Loads some or all part of the data to the cache depending to the application.
	 */
	public void load() throws EmptyPoolException, SQLException, InstantiationException, IllegalAccessException;

	/**
	 * Puts a new object into the cache.
	 */
	public void put(E obj) throws DuplicateDataException;

	/**
	 * Retrieves an object from the cache or null if it is not in the cache.
	 */
	public E get(K id) ;

	/**
	 * Updates an existing object in the cache. If the object doesn't exist it will add it to the cache
	 */
	public void update(E obj);

	/** removes an objet from the cache based on the id. */
	public void remove(K id) throws DataException;

	/**
	 * Returns the set of keys of objects in the cache
	 */
	public Set<K> getIds();

	/**
	 * Returns the set of objects in the cache.
	 */
	public Collection<E> getObjects();

	/**
	 * Returns the size of the cache.
	 */
	public int size();

	/**
	 * Cleans up the cache by removing objecting inside the cache.
	 */
	public void cleanup();
}
