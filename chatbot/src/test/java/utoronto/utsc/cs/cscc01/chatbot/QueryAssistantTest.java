package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.QueryAssistant;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.WatsonAssistant;

public class QueryAssistantTest {
  private QueryAssistant qa;

  @Before
  public void setUp() {
    this.qa = new QueryAssistant(WatsonAssistant.buildAssistant());
  }

  // test to see if we are returned a hashtable of correct format
  @Test
  public void testAssistantQuery() {
    String query = "hello world";
    Hashtable<String, ArrayList<String>> result =
        qa.simpleAssistantQuery(query);
    // we can't test the content because that may change
    // so we test the format
    assertTrue(result.containsKey("text"));
    assertTrue(result.containsKey("image"));
    assertTrue(result.containsKey("url"));
    assertTrue(result.containsKey("queryFlag"));
  }

  // query that assistant cannot answer should return "Need to query"
  @Test
  public void testAssistantInvalidQuery() {
    String query = "adfgjhaeltuqwnokehrtiawg587q=tr8q3trfguawgruja";
    Hashtable<String, ArrayList<String>> result =
        qa.simpleAssistantQuery(query);
    assertEquals("Need to query", result.get("queryFlag").get(0));
  }
}
