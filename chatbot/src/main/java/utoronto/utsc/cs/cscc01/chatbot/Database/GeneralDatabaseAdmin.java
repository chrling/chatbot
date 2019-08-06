package utoronto.utsc.cs.cscc01.chatbot.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A general database class that has the implementation of connect and close method for database connection and closing
 * @author Reynold
 */
abstract class GeneralDatabaseAdmin {


  public Connection connection;

  public GeneralDatabaseAdmin() {
    this.connection = null;
  }

  /**
   * Make a connection to database
   */
  public boolean connect() {

    // Connect to Chatbot.db at project folder and return true if it is
    // successful.
    try {
      this.connection = DriverManager.getConnection("jdbc:sqlite:Chatbot.db");
      return true;

    } catch (SQLException e) {

      return false;
    }
  }


  /**
   * Close a database connection if it is connected
   */
  public boolean close() {

    if (this.connection != null) {
      try {

        this.connection.close();

        return true;

      } catch (SQLException e) {

      }
    }
    return false;
  }


}
