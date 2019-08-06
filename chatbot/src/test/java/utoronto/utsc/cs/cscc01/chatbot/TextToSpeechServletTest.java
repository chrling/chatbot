package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.TextToSpeechServlet;

public class TextToSpeechServletTest {
  private TextToSpeechServlet servlet;

  @Before
  public void setUp() throws ServletException {
    servlet = new TextToSpeechServlet();
    servlet.init();
  }

  // test to see if we successfully created a byte string, since we can't
  // check the content, we just check if it is non empty
  @Test
  public void testUserLoginValid() throws ServletException, IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    String reply = "";

    when(mockRequest.getParameter("ttsText")).thenReturn("hello world");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(mockResponse.getWriter()).thenReturn(writer);
    
    servlet.doPost(mockRequest, mockResponse);
    reply = stringWriter.toString();
    
    assertFalse(reply.equals(""));
  }
}
