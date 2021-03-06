# MyDatabaseCreator

This is one of my biggest projects written in Java. It's main function is to create and manipulate tables on local/remote server without any knowledge of Sql language (DDL/DML).
The project is divided into two packages:
* Logic - package that contains logic classes (model, controller, database connector and table entity) that contains methods that mostly uses DDL and DML SQL language queries
* view - package that contains GUI classes. Those classes are inheriting from Java Swing library classes (JFrame, JPanel etc.)

For a proper work of this application, the Java Database Connectivity (JDBC) interface is required (mysql java connector .jar file)
