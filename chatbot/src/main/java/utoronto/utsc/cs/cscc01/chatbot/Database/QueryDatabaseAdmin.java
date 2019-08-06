package utoronto.utsc.cs.cscc01.chatbot.Database;

import com.opencsv.CSVWriter;
import utoronto.utsc.cs.cscc01.chatbot.Database.GeneralDatabaseAdmin;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that will handle the user queries data in the database
 *
 * @author Reynold
 */
public class QueryDatabaseAdmin extends GeneralDatabaseAdmin {



    /**
     * Insert the given information to the database, filename and the content of files
     *
     * @param query
     */
    public void insertQuery(String query) {
        PreparedStatement stmt;
        // SQL code for insert
        String insertSQL = "INSERT INTO QUERIES(QUERY, FREQUENCY) VALUES(?, ?)";

        String trimmedQuery = query.trim().replaceFirst("^[^a-zA-Z0-9]+", "").replaceAll("[^a-zA-Z0-9]+$", "");

        int frequency = getFrequency(trimmedQuery);

        if (frequency == 0) {

            try {
                this.connect();
                // Create SQL statement for inserting
                stmt = this.connection.prepareStatement(insertSQL);
                stmt.setString(1, trimmedQuery);
                stmt.setInt(2, 1);
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {
            update(trimmedQuery, frequency);
        }
    }

    public void update(String query, int frequency) {
        PreparedStatement stmt;
        String updateSQL = "UPDATE QUERIES SET FREQUENCY = ? WHERE QUERY = ?";
        int newFrequency = frequency + 1;
        try {
            this.connect();
            // Create SQL statement for inserting
            stmt = this.connection.prepareStatement(updateSQL);
            stmt.setInt(1, newFrequency);
            stmt.setString(2, query);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Can't update frequency");
        }

        finally{
            this.close();
        }
    }

    public int getFrequency(String query) {
        String selectSQL = "SELECT * FROM QUERIES WHERE query=?";
        ResultSet rs;
        PreparedStatement stmt;
        int frequency = 0;

        try {
            this.connect();
            stmt = this.connection.prepareStatement(selectSQL);
            stmt.setString(1, query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                frequency = rs.getInt("FREQUENCY");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{
            this.close();
        }

        return frequency;
    }



    public void extractCSV() {

        String selectSQL = "SELECT * FROM QUERIES ORDER BY QUERY";
        ResultSet rs;
        PreparedStatement stmt;

        try {
            this.connect();
            stmt = this.connection.prepareStatement(selectSQL);
            rs = stmt.executeQuery();
            // Open CSV file.
            CSVWriter writer = new CSVWriter(new FileWriter("../chatbot/files/data/queriesData.csv"));

            writer.writeAll(rs, true);

            writer.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();

        } finally{
            this.close();
        }

    }

    /**
     * Remove all the queries record from the database
     *
     */
    public void removeAll() throws SQLException {
        PreparedStatement stmt;
        // SQL code for delete
        String deleteSQL = "DELETE FROM FEEDBACK";
        connect();
        // Create SQL statement for deleting
        stmt = this.connection.prepareStatement(deleteSQL);
        stmt.executeUpdate();
        close();
    }

    public ArrayList<ArrayList<String>> list() {
        String selectSQL = "SELECT * FROM QUERIES ORDER BY QUERY";
        ResultSet rs;
        PreparedStatement stmt;
        ArrayList<ArrayList<String>> outerList = new ArrayList<>();
        ArrayList<String> header = new ArrayList<>(Arrays.asList("\"Query\"", "\"Frequency\""));
        outerList.add(header);

        try {
            this.connect();
            stmt = this.connection.prepareStatement(selectSQL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ArrayList<String> innerList = new ArrayList<>();
                String content = rs.getString("query");
                innerList.add("\"" + content + "\"");
                int frequency = rs.getInt("frequency");
                innerList.add(String.valueOf(frequency));
                outerList.add(innerList);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outerList;
    }

}
