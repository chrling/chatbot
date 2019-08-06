package utoronto.utsc.cs.cscc01.chatbot;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.Indexer;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.IndexerServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IndexerServletTest {

    private IndexerServlet servlet;
    private Indexer indexer;
    private static final String testIndexPath = "../chatbot/index/testIndex";
    private Directory indexDir;

    @Before
    public void setUp() throws IOException {
        servlet = new IndexerServlet();
        servlet.init();
        indexer = new Indexer(testIndexPath);
        indexDir = FSDirectory.open(Paths.get(testIndexPath));
    }

    @Test
    public void testReindex() throws Exception {

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("reindex")).thenReturn("test");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"Indexed Files\"}", stringWriter.toString());
    }

    @Test
    public void testRemoveIndex() throws Exception {

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("reset")).thenReturn("test");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("{\"reply\": \"Removed Index\"}", stringWriter.toString());
    }
}
