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

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.log4j.Logger;

import com.snapeet.common.BadXMLException;

/**
 *
  <dbcore>

    <jdbc>
      <url>jdbc:postgresql://localhost:5432/mydb</url>
      <driver>org.postgresql.Driver</driver>
      <user>myuser</user>
      <password>mypassword</password>
    </jdbc>

    <query>
      <name>insert_country</name>
      <value>"INSERT INTO countries (id, name) VALUES (?, ?)"</value>
    </query>

  </dbcore>
 */
public class DBCoreConfig
{
	private static Logger _logger = Logger.getLogger(DBCoreConfig.class);

	private String _url;
	private String _driver;
	private String _user;
	private String _password;
	private Map<String, DBCoreConfigNode> _map;

	/** I might add more properties to each config node, so keep this class */
	private static class DBCoreConfigNode
	{
		String _name;
		String _query;

		public DBCoreConfigNode(String name, String query)
		{
			_name = name;
			_query = query;
		}

		public String getName()
		{
			return _name;
		}

		public String getQueryString()
		{
			return _query;
		}
	}

	private String trim(String s)
	{
		s.trim();
		if ( s.charAt(0) == '"' )
			s = s.substring(1);

		if ( s.charAt(s.length()-1) == '"' )
			s = s.substring(0, s.length()-1);

		return s;
	}

	public DBCoreConfig(String xmlFile)
	{
		_map = new HashMap<String, DBCoreConfigNode>();

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(xmlFile));

			// normalize text representation
			doc.getDocumentElement().normalize();
			if ( !doc.getDocumentElement().getNodeName().toLowerCase().equals("dbcore") )
				throw new BadXMLException("invalid root document:" + doc.getDocumentElement().getNodeName());

			NodeList nodeList = doc.getElementsByTagName("jdbc");
			if ( nodeList == null )
				throw new BadXMLException("couldn't find jdbc section!");

			Node node = nodeList.item(0);
			if ( node.getNodeType() == Node.ELEMENT_NODE )
			{
				Element element = (Element) node;
				_url = trim(element.getElementsByTagName("url").item(0).getTextContent());
				_driver = trim(element.getElementsByTagName("driver").item(0).getTextContent());
				_user = trim(element.getElementsByTagName("user").item(0).getTextContent());
				_password = trim(element.getElementsByTagName("password").item(0).getTextContent());
			}

			nodeList = doc.getElementsByTagName("query");
			_logger.debug("Total no of queries : " + nodeList.getLength());

			for(int s=0; s<nodeList.getLength() ; s++)
			{
				node = nodeList.item(s);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element)node;

					String name = trim(element.getElementsByTagName("name").item(0).getTextContent());
					String query = trim(element.getElementsByTagName("value").item(0).getTextContent());
					DBCoreConfigNode configNode = new DBCoreConfigNode(name, query);
					_map.put(name, configNode);
        	        	}
			}

		}
		catch (SAXParseException err)
		{
			_logger.error("Parsing error, line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			_logger.error(" " + err.getMessage());
		} catch (SAXException e)
		{
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		} catch (Throwable t)
		{
        		t.printStackTrace ();
	        }
	}

	public String getURL()
	{
		return _url;
	}

	public String getDriver()
	{
		return _driver;
	}

	public String getUser()
	{
		return _user;
	}

	public String getPassword()
	{
		return _password;
	}

	public String getQueryString(String name)
	{
		DBCoreConfigNode configNode = _map.get(name);
		if ( configNode != null )
			return configNode.getQueryString();

		return null;
	}

	public static void main(String[] args)
	{
		if ( args.length != 1 )
		{
			System.out.println("usage: java DBCoreConfig <xmlfile>");
			System.exit(1);
		}

		DBCoreConfig config = new DBCoreConfig(args[0]);
		System.out.println("URL: " + config.getURL());
		System.out.println("Driver: " + config.getDriver());
		System.out.println("User: " + config.getUser());
		System.out.println("Password: " + config.getPassword());
		System.out.println("insert country query: " + config.getQueryString("insert_country"));
		System.out.println("delete country query: " + config.getQueryString("delete_country"));
	}
}

