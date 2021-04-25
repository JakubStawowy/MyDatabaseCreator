package database.templates;

import java.sql.Connection;

public interface DatabaseConnectorConnectionApi extends DatabaseConnectorApi  {
    Connection getConnection();
}
