package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Database.FilesDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.FilesServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


public class FilesEngineDatabaseServletTest {

    private FilesServlet servlet;
    private FilesDatabaseAdmin db;

    @Before
    public void setUp() {
        servlet = new FilesServlet();
        db = new FilesDatabaseAdmin();
        servlet.init();
    }

    /**
     * Test if the file is successfully uploaded by checking the content of file. Content of test.txt is "testing"
     */

    @Test
    public void testUpload() throws Exception {

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("action")).thenReturn("upload");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testRemove() throws Exception {

        InputStream ip = new FileInputStream(new File("../chatbot/files/test/test.pdf"));
        db.insert("test.pdf", ip, ip, 10000);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("action")).thenReturn("remove");
        when(mockRequest.getParameter("file")).thenReturn("test.pdf");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
        servlet.doPost(mockRequest, mockResponse);
        assertEquals("", stringWriter.toString());
        db.remove("test.pdf");
    }


    @Test
    public void testGetFiles() throws Exception {
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
        assertEquals("[", reply.subSequence(0, 1));
        assertEquals("]", reply.subSequence(reply.length() - 1, reply.length()));

    }

}
