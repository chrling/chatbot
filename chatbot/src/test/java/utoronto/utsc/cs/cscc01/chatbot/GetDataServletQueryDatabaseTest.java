package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Database.AnswerRatingDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.FeedbackDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.QueryDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.GetDataServlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDataServletQueryDatabaseTest {


    private GetDataServlet servlet;
    private AnswerRatingDatabaseAdmin ansDb;
    private FeedbackDatabaseAdmin feedbackDb;
    private QueryDatabaseAdmin queryDb;


    @Before
    public void setUp() {
        servlet = new GetDataServlet();
        ansDb = new AnswerRatingDatabaseAdmin();
        feedbackDb = new FeedbackDatabaseAdmin();
        queryDb = new QueryDatabaseAdmin();
        servlet.init();
    }

    @Test
    public void testGetQueriesData() throws Exception {
        ServletOutputStream os = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        queryDb.insertQuery("test");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("getQueries")).thenReturn("notNull");
        when(mockResponse.getOutputStream()).thenReturn(os);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"queriesData.csv extracted!\"}", stringWriter.toString());
        queryDb.removeAll();

    }

    @Test
    public void testGetFeedbackRatingData() throws Exception {
        ServletOutputStream os = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        feedbackDb.insertFeedback(4, "Test");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("getFeedback")).thenReturn("notNull");
        when(mockResponse.getOutputStream()).thenReturn(os);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"feedbackData.csv extracted!\"}", stringWriter.toString());
        feedbackDb.removeAll();
    }

    @Test
    public void testGetAnswerRatingData() throws Exception {
        ServletOutputStream os = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        ansDb.insert("Test", "Good");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("getAnswerRating")).thenReturn("notNull");
        when(mockResponse.getOutputStream()).thenReturn(os);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"answerRatingData.csv extracted!\"}", stringWriter.toString());
        ansDb.remove("Test");
    }

    @Test
    public void getQueriesFrequency() throws Exception {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doGet(mockRequest, mockResponse);
        String reply = stringWriter.toString();
        assertEquals("{\"queries\":", reply.subSequence(0, 11));
        assertEquals(" }", reply.subSequence(reply.length() - 2, reply.length()));

    }
}
