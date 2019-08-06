package utoronto.utsc.cs.cscc01.chatbot.Database;

import java.sql.SQLException;

public interface UserDatabase {
  boolean verifyUser(String username, String password) throws SQLException;
  boolean connect();
}
