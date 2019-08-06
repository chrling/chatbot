package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Database.LinksDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.CrawlerServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


public class CrawlerServletLinksDatabaseTest {

    private CrawlerServlet servlet;
    private LinksDatabaseAdmin db;
    private String link;

    @Before
    public void setUp() {
        servlet = new CrawlerServlet();
        db = new LinksDatabaseAdmin();
        link = "https://ca.yahoo.com";
        servlet.init();
    }


    /**
     *
     */

    @Test
    public void testInputCrawling() throws IOException, SQLException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("action")).thenReturn("crawl");
        when(mockRequest.getParameter("url")).thenReturn(link);
        when(mockRequest.getParameter("depth")).thenReturn("1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        // Test if link is in db
        assertEquals(db.getLink(link), link);
        // Test servlet response
        assertEquals("{\"reply\": \"Website is crawled\"}", stringWriter.toString());
        db.remove(link);
    }

    @Test
    public void testGetCrawledLinks() throws IOException, SQLException {
        HashMap<String, HashMap<String, String>> testMap = new HashMap<>();
        db.insert(link, testMap);
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
        // Test database as it will call list method in db
        assertTrue(reply.contains(link));
        // Test Servlet reply, as content might change, we can only test format
        assertEquals("[{", reply.subSequence(0, 2));
        assertEquals("}]", reply.subSequence(reply.length() - 2, reply.length()));
        db.remove(link);

    }

    @Test
    public void testRemoveLinks() throws IOException, SQLException {
        HashMap<String, HashMap<String, String>> testMap = new HashMap<>();
        db.insert(link, testMap);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("action")).thenReturn("remove");
        when(mockRequest.getParameter("url")).thenReturn(link);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        // Test if it is removed from database
        assertEquals(db.getLink(link), "");
        // Test servlet response
        assertEquals("{\"reply\": \"Link removed!\"}", stringWriter.toString());

    }

}