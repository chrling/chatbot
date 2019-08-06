package utoronto.utsc.cs.cscc01.chatbot.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.AnswerRating;
import utoronto.utsc.cs.cscc01.chatbot.Database.AnswerRatingDatabaseAdmin;

/**
 * Web servlet used to get user rating for chatbot answers to the admin page on
 * the chatbot application
 * 
 * @author Reynold
 *
 */
@WebServlet(urlPatterns = "/answerrating")
public class AnswerRatingServlet extends HttpServlet {

    private AnswerRatingDatabaseAdmin answerRatingDb;

    public void init() {
        this.answerRatingDb = new AnswerRatingDatabaseAdmin();
    }

    /**
     * Post method used to get the user rating feedback from the database and
     * writes it as a json object to the response
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String rating = request.getParameter("answerRating");
        String answer = request.getParameter("message");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            this.answerRatingDb.insert(answer, rating);
            writer.write("{\"reply\": \"AnswerRating received. Thank you!\"}");
        } catch (SQLException e) {
            writer.write("{\"reply\": \"Error receiving feedback!\"}");
        }
    }

    private void listAnswerRating(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {

        try {
            List<AnswerRating> listAnswerRating = answerRatingDb.list();

            request.setAttribute("listAnswerRating", listAnswerRating);
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromJavaArrayList = gsonBuilder.toJson(listAnswerRating);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonFromJavaArrayList);
        } catch (SQLException e) {
            response.getWriter().write("{\"reply\": \"Fail to list ratings!\"}");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        listAnswerRating(request, response);
    }
}

