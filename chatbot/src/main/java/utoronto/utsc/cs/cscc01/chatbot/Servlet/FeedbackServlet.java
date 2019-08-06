package utoronto.utsc.cs.cscc01.chatbot.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utoronto.utsc.cs.cscc01.chatbot.Database.FeedbackDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.Feedback;


/**
 * Web servlet used to handle user feedback
 * 
 * @author Reynold
 */
@WebServlet(urlPatterns = "/feedback")
public class FeedbackServlet extends HttpServlet {

  private FeedbackDatabaseAdmin feedbackDb;

  public void init() {
    this.feedbackDb = new FeedbackDatabaseAdmin();
  }

  /**
   * Post request used to get the user ratings and comments from the user
   * feedback on the chatbot window
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

        String rating = request.getParameter("rating");
        String comments = request.getParameter("comments");

        try {


            if (Integer.parseInt(rating) != 0) {
                feedbackDb.insertFeedback(Integer.parseInt(rating), comments);
            }

      writer.write("{\"reply\": \"Feedback received. Thank you!\"}");

    } catch (SQLException e) {

      writer
          .write("{\"reply\": \"Error getting feedback. Please try again.\"}");
    }
  }

  /**
   * Get all the feedback data, make a Feedback object and put them into a list, which will be sent to frontend
   * for information displaying
   */

  private void listFeedback(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    try {


      List<Feedback> listFeedback = feedbackDb.list();

      request.setAttribute("listFeedback", listFeedback);

      Gson gsonBuilder = new GsonBuilder().create();
      String jsonFromJavaArrayList = gsonBuilder.toJson(listFeedback);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      float average = feedbackDb.getAverage();
      response.getWriter()
          .write(String.format("{\"feedback\": %s, \"average\": [%.2f] }",
              jsonFromJavaArrayList, average));


    } catch (SQLException e) {

      e.printStackTrace();
      throw new ServletException(e);

    }
  }

  /**
   * Get request for information displaying
   */

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        listFeedback(request, response);
  }

}
