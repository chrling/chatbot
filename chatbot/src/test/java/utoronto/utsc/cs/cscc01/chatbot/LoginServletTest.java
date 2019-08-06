package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.Servlet.LoginServlet;

public class LoginServletTest {
  private LoginServlet servlet;

  @Before
  public void setUp() throws ServletException {
    servlet = new LoginServlet();
    servlet.init();
  }

  // test to see if correct password is validated
  @Test
  public void testUserLoginValid() throws ServletException, IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    Principal principal = mock(Principal.class);

    when(mockRequest.getParameter("username")).thenReturn("admin");
    when(mockRequest.getParameter("password")).thenReturn("admin");
    when(mockRequest.getUserPrincipal()).thenReturn(principal);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(mockResponse.getWriter()).thenReturn(writer);

    servlet.doPost(mockRequest, mockResponse);

    assertEquals("{\"authenticated\":true}", stringWriter.toString());
  }

  // test to see if incorrect password fails validation
  @Test
  public void testUserLoginInvalid() throws ServletException, IOException {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    Principal principal = mock(Principal.class);

    when(mockRequest.getParameter("username")).thenReturn("fakename");
    when(mockRequest.getParameter("password")).thenReturn("fakepassword");
    when(mockRequest.getUserPrincipal()).thenReturn(principal);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(mockResponse.getWriter()).thenReturn(writer);

    servlet.doPost(mockRequest, mockResponse);

    assertEquals("{\"authenticated\":false}", stringWriter.toString());
  }
}
