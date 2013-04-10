dbcore
======

- dbcore is a simple framework to access database and map tables to JAVA objects.
 
- dbcore is a simple framework based on JDBC to execute queries without dealing the details of JDBC.
  You can have your application access to DB with the following easy steps:

  1) You write your SQL queries in your java files and not config or some XML files.
  
  2) You write some simple mappers that mappes your data (ResultSet) to your objects.
  
  3) You implement some simple query mappers (IQueryMapper).
  
  4) Then you can execute your queries.
  
  5) If your query is a select, you can get the results in a Collection or in a IDBCache.

- dbcore has a simple connection pool that developers can set it up easily.

- dbcore has a cache of PreparedStatements which makes the execution of queries much faster.
  This feature gives additional performance to your application.


How to use it?
--------------

- Please refer to the examples directory to see some usage.
