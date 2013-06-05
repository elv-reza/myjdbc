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

import java.lang.Exception;
import java.lang.String;

public class BadXMLException extends Exception
{
	public BadXMLException()
	{
	}

	public BadXMLException(String s)
	{
		super(s);
	}
}
