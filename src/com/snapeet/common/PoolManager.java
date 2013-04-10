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
package com.snapeet.common;

import java.util.*;

public class PoolManager<E>
{
	private static class PoolItem<E>
	{
		boolean inUse = false;
		E item;
		
		PoolItem(E item)
		{
			this.item = item;
		}
	}

	private HashMap<E, PoolItem<E>> _items = new HashMap<E, PoolItem<E>>();
	private ArrayList<PoolItem<E>> _freeItems = new ArrayList<PoolItem<E>>();

	/**
	 * Adds an empty item to the pool
	 */
	public void add(E item)
	{
		PoolItem<E> poolItem = new PoolItem<E>(item);
		_items.put(item, poolItem);
		_freeItems.add(poolItem);
	}

	public E get() throws EmptyPoolException
	{
		if ( _freeItems.size() == 0 )
			throw new EmptyPoolException();

		PoolItem<E> poolItem = _freeItems.remove(0);
		if ( poolItem.inUse == true )
			throw new IllegalStateException("An inused pool item in the free list!");

		poolItem.inUse = true;
		return poolItem.item;
	}

	/**
	 * returns an in use item to the pool. 
	 * A released item can be used with other objects.
	 */
	public void release(E item) 
	{
		PoolItem poolItem = _items.get(item);
		if ( poolItem == null )
			throw new IllegalArgumentException("Released an item which is not in the pool!");

		poolItem.inUse = false;
		_freeItems.add(poolItem);
	}

	public int poolSize()
	{
		return _items.size();
	}

	public int freeSize()
	{
		return _freeItems.size();
	}

	public void clear()
	{
		for (E item : _items.keySet())
		{
			PoolItem<E> poolItem = _items.get(item);
			poolItem.item = null;
		}

		_items.clear();
		_freeItems.clear();
	}
}
