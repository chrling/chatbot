package utoronto.utsc.cs.cscc01.chatbot.Database;

import java.sql.*;



/**
 * Admin page for handling the user data inthe database
 *
 *
 */
public class UserDatabaseAdmin extends GeneralDatabaseAdmin implements UserDatabase {


    /**
     * Insert the given information to the database, username and password
     *
     * @param username
     * @param password
     */
    public void insertUser(String username, String password) throws SQLException {
        PreparedStatement stmt;
        // SQL code for insert
        String insertSQL = "INSERT INTO USERS(USERNAME, PASSWORD) VALUES(?, ?)";

        String pw = getPassword(username);

        if (pw.equals("")) {

            try {
                connect();
                // Create SQL statement for inserting
                stmt = this.connection.prepareStatement(insertSQL);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                close();
            }

        } else {
            System.out.println("Username is already used!");
        }
    }


    /**
     * Remove the given information of the given username from the database
     *
     * @param username
     */
    public void removeUser(String username) {
        PreparedStatement stmt;

        // SQL code for delete
        String deleteSQL = "DELETE FROM USERS WHERE username = ?";
        try {
            connect();
            // Create SQL statement for deleting
            stmt = this.connection.prepareStatement(deleteSQL);
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Verify the user information in the database
     *
     * @param username
     * @param password
     */

    public boolean verifyUser(String username, String password) throws SQLException {

        // update sql
        String pwGot = getPassword(username);

        if (pwGot.equals(password)) {
            return true;
        }

        return false;
    }


    public String getPassword(String username) throws SQLException {


        String sql = "SELECT * FROM Users WHERE username = ?";
        String password = "";
        ResultSet rs;
        PreparedStatement stmt;

        connect();
        stmt = this.connection.prepareStatement(sql);
        stmt.setString(1, username);
        rs = stmt.executeQuery();

        while (rs.next()) {
            password = rs.getString("password");
        }
        close();

        return password;
    }

}

