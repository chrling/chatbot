package utoronto.utsc.cs.cscc01.chatbot.Servlet;

import utoronto.utsc.cs.cscc01.chatbot.Database.AnswerRatingDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.FeedbackDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.QueryDatabaseAdmin;

import java.io.*;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web servlet used to handle traffic for getting user feedback from the
 * database to the admin page of the chatbot application
 * 
 * @author Reynold
 *
 */
@WebServlet(urlPatterns = "/getdata")
public class GetDataServlet extends HttpServlet {

  private QueryDatabaseAdmin queryDb;
  private FeedbackDatabaseAdmin feedbackDb;
  private AnswerRatingDatabaseAdmin answerRatingDb;

  public void init() {
    this.queryDb = new QueryDatabaseAdmin();
    this.feedbackDb = new FeedbackDatabaseAdmin();
    this.answerRatingDb = new AnswerRatingDatabaseAdmin();
  }

  /**
   * Post method used to get the information from the database and sends it to
   * the admin page
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {


    try {
      if (request.getParameter("getQueries") != null) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=queriesData.csv");

        this.queryDb.extractCSV();

        InputStream inStream = new FileInputStream(
                new File("../chatbot/files/data/queriesData.csv"));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();


      } else if (request.getParameter("getFeedback") != null) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=feedbackData.csv");

        this.feedbackDb.extractCSV();
        InputStream inStream = new FileInputStream(
                new File("../chatbot/files/data/feedbackData.csv"));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();

      } else if (request.getParameter("getAnswerRating") != null) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=answerRatingData.csv");
        this.answerRatingDb.extractCSV();

        InputStream inStream = new FileInputStream(
                new File("../chatbot/files/data/answerRatingData.csv"));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();

      }
    } catch (SQLException e) {
      response.getWriter().write("{\"reply\": \"error extracting file!\"}");
    }
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String result = this.queryDb.list().toString();
    response.getWriter().write(String.format("{\"queries\": %s }", result));
  }


}
