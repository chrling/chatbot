package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Database.AnswerRatingDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.FeedbackDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.QueryDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.GetDataServlet;

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
