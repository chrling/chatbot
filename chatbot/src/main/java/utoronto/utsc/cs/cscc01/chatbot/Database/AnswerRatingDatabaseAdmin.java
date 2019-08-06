package utoronto.utsc.cs.cscc01.chatbot.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.AnswerRating;


/**
 * A class that will handle the ratings data associated with the answer by IBM Watson in the database
 *
 * @author Reynold
 */
public class AnswerRatingDatabaseAdmin extends GeneralDatabaseAdmin {


    /**
     * Insert the given information to the database, answer and the rating associated with it
     *
     * @param answer
     * @param rating
     */
    public void insert(String answer, String rating) throws SQLException {
        PreparedStatement stmt;
        // SQL code for insert
        String insertSQL = "INSERT INTO ANSWERRATING(ANSWER, GOOD, BAD) VALUES(?, ?, ?)";

        int numberOfRatings = getNumberOfRatings(answer, rating);

        if (numberOfRatings == -1) {

            this.connect();
            // Create SQL statement for inserting
            stmt = this.connection.prepareStatement(insertSQL);
            stmt.setString(1, answer);
            if (rating.equals("Good")) {
                stmt.setInt(2, 1);
                stmt.setInt(3, 0);
            } else if (rating.equals("Bad")) {
                stmt.setInt(2, 0);
                stmt.setInt(3, 1);
            }
            stmt.executeUpdate();
            this.close();

        } else {
            update(answer, rating, numberOfRatings);
        }
    }

    public void update(String answer, String rating, int numberOfRatings) throws SQLException {

        PreparedStatement stmt;
        String updateSQL = "UPDATE ANSWERRATING SET " + rating + " = ? WHERE ANSWER = ?";
        int newNumberOfRatings = numberOfRatings + 1;
        this.connect();
        // Create SQL statement for inserting
        stmt = this.connection.prepareStatement(updateSQL);
        stmt.setInt(1, newNumberOfRatings);
        stmt.setString(2, answer);
        stmt.executeUpdate();
        this.close();
    }

    /**
     * Remove the given information of the given answer rating from the database
     *
     * @param answer
     */
    public void remove(String answer) throws SQLException {
        PreparedStatement stmt;
        // SQL code for delete
        String deleteSQL = "DELETE FROM ANSWERRATING WHERE ANSWER = ?";
        if (getNumberOfRatings(answer, "Good") != -1) {
            connect();
            // Create SQL statement for deleting
            stmt = this.connection.prepareStatement(deleteSQL);
            stmt.setString(1, answer);
            stmt.executeUpdate();
            close();
        }
    }


    public int getNumberOfRatings(String answer, String rating) throws SQLException {
        String selectSQL = "SELECT " + rating + " FROM ANSWERRATING WHERE answer=?";
        ResultSet rs;
        PreparedStatement stmt;
        int numberOfRatings = -1;

            this.connect();
            stmt = this.connection.prepareStatement(selectSQL);
            stmt.setString(1, answer);
            rs = stmt.executeQuery();

            while (rs.next()) {
                numberOfRatings = rs.getInt(1);
            }
            this.close();

            return numberOfRatings;
    }


  public void extractCSV() throws SQLException, IOException {

    String selectSQL = "SELECT * FROM ANSWERRATING";
    ResultSet rs;
    PreparedStatement stmt;

    this.connect();
    stmt = this.connection.prepareStatement(selectSQL);
    rs = stmt.executeQuery();
    // Open CSV file.
    CSVWriter writer = new CSVWriter(
        new FileWriter("../chatbot/files/data/answerRatingData.csv"));

    writer.writeAll(rs, true);

    writer.close();

    this.close();

  }

  public List<AnswerRating> list() throws SQLException {

      List<AnswerRating> listAnswerRating = new ArrayList<>();


      String sql = "SELECT * FROM answerrating ORDER BY answer";
      this.connect();

      PreparedStatement stmt = this.connection.prepareStatement(sql);
      ResultSet result = stmt.executeQuery();

      while (result.next()) {
          int id = result.getInt("answerratingId");
          String answer = result.getString("answer");
          int good = result.getInt("good");
          int bad = result.getInt("bad");
          AnswerRating answerRating = new AnswerRating(id, answer, good, bad);

          listAnswerRating.add(answerRating);
      }
      return listAnswerRating;
  }
}
