package utoronto.utsc.cs.cscc01.chatbot.Servlet;

import utoronto.utsc.cs.cscc01.chatbot.Database.UserDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.UserDatabase;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web servlet that handles user login for the admin page of the chatbot
 * application
 * 
 * @author Chris
 *
 */
@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
  // dependency injection of database into login servlet so we can test it
  private UserDatabase db;

  public void init() {
    this.db = new UserDatabaseAdmin();
  }

  /**
   * Post method used to verify user name and password by looking it up in the
   * admin user database
   * 
   * @param req - http request containing user name and password
   * @param resp - http response containing a string of json format to verify if
   *        access should be granted or denied
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    // grab username and password from the fields
    String username = req.getParameter("username");
    String password = req.getParameter("password");

    try {
      req.login(username, password);
      if (req.getUserPrincipal() != null && db.verifyUser(username, password)) {

        resp.getWriter().write("{\"authenticated\":true}");

      } else {
        // if we couldn't verify the user, try again
        resp.getWriter().write("{\"authenticated\":false}");
      }

    } catch (ServletException | SQLException e) {

      resp.getWriter().write("{\"authenticated\":false}");
    }
  }
}
