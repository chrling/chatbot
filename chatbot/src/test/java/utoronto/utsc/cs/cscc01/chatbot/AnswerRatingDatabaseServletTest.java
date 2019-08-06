package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Database.AnswerRatingDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.AnswerRatingServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


public class AnswerRatingDatabaseServletTest {

    private AnswerRatingServlet servlet;
    private AnswerRatingDatabaseAdmin db;

    @Before
    public void setUp() {
        servlet = new AnswerRatingServlet();
        db = new AnswerRatingDatabaseAdmin();
        servlet.init();
    }


    /**
     * Test if the file is successfully uploaded by checking the content of file. Content of test.txt is "testing"
     */

    @Test
    public void testInsertRating() throws Exception {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("answerRating")).thenReturn("Good");
        when(mockRequest.getParameter("message")).thenReturn("Test");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"AnswerRating received. Thank you!\"}", stringWriter.toString());
        assertEquals(db.getNumberOfRatings("Test", "Good"), 1);
        db.remove("Test");

    }

    @Test
    public void testGetRating() throws Exception {
        db.insert("Test", "Good");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        try {
            when(mockResponse.getWriter()).thenReturn(writer);
        } catch (IOException e1) {
            fail();
        }
        servlet.doGet(mockRequest, mockResponse);
        String reply = stringWriter.toString();
        assertEquals("[{", reply.subSequence(0, 2));
        assertEquals("}]", reply.subSequence(reply.length() - 2, reply.length()));
        db.remove("Test");

    }

}