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

import java.util.Properties;
import java.lang.String;
import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AppProperties extends Properties
{
	static public final String APP_CONFIG_PATH = "../config.properties";
	static private Logger _logger = Logger.getLogger(AppProperties.class);
	static private String _appConfigPath = APP_CONFIG_PATH;
	static private AppProperties _instance = null;

	private String _dbURL;
        private String _hostname;
        private String _dbDriver;
        private String _dbUser;
        private String _dbPassword;
        private int  _nConnections;

	private AppProperties() throws
		FileNotFoundException, IOException, 
		NumberFormatException, BadPropertyException
	{
		if ( _instance != null )
			return;

		_logger.debug("AppProperties(): " + _appConfigPath);

		load(new FileInputStream(_appConfigPath));

		_instance = this;
		_dbURL = getProperty("DB_URL");
		_hostname = getProperty("HOSTNAME");
		_dbDriver = getProperty("JDBC_DRIVER");
		_dbUser = getProperty("DB_USER");
		_dbPassword = getProperty("DB_PASSWORD");
		_nConnections = getIntProperty("POOL_CONNECTIONS");

		_logger.debug("dbURL:" + _dbURL + ", hostname:" + _hostname + ", dbDriver:" + _dbDriver + ", dbUser" + _dbUser + ", dbPassword:" + _dbPassword + ", nConnections:" + _nConnections);

	}

	public static void setAppConfigPath(String path)
	{
		_appConfigPath = path;
	}

	public static synchronized AppProperties getInstance()
	{
		try
		{
			if ( _instance == null )
				_instance = new AppProperties();
		}
		catch (Exception e)
		{
			return null;
		}

		return _instance;
	}

	public static void configureLog4j()
	{
		PropertyConfigurator.configure(_appConfigPath);
	}

	public int getIntProperty(String p) throws BadPropertyException, NumberFormatException
	{
		return getInt(p);
	}

	public String getString(String p) throws BadPropertyException
	{
		String value = getProperty(p);

		if ( value == null )
			throw new BadPropertyException("Error: null " + p);

		return value;
	}

	public int getInt(String p) throws BadPropertyException, NumberFormatException
	{
		String value = getProperty(p);

		if ( value == null )
			throw new BadPropertyException("Error: null " + p);

		return Integer.valueOf(value);
	}

	public String getDbURL()
	{
		return _dbURL;
	}

	public String getHostname()
	{
		return _hostname;
	}

	public String getDBDriver()
	{
		return _dbDriver;
	}

	public String getDBUser()
	{
		return _dbUser;
	}

	public String getDBPassword()
	{
		return _dbPassword;
	}

	public int getNumConnections()
	{
		return _nConnections;
	}

}
