package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.QueryServlet;

public class QueryServletTest {
  private QueryServlet servlet;

  @Before
  public void setUp() {
    this.servlet = new QueryServlet();
    servlet.init();
  }

  // check to see if we are returned the correct json format
  @Test
  public void testValidQuery() throws IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(mockResponse.getWriter()).thenReturn(writer);
    when(mockRequest.getQueryString()).thenReturn("What is DFI?");

    servlet.doGet(mockRequest, mockResponse);
    String reply = stringWriter.toString();
    // we can't test the exact content because that may change, but we can test
    // the json format
    assertEquals("{\"watson\":{\"", reply.substring(0, 12));
    assertEquals("}}", reply.substring(reply.length() - 2, reply.length()));
  }

  // check to see if a random garbage query give us empty json
  @Test
  public void testInvalidQuery() throws IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(mockResponse.getWriter()).thenReturn(writer);
    when(mockRequest.getQueryString())
        .thenReturn("qwer98df7ywo34y5biq236rgqfivb");

    servlet.doGet(mockRequest, mockResponse);
    String reply = stringWriter.toString();
    assertEquals("{\"watson\":{},\"lucene\":{}}", reply);
  }
}
