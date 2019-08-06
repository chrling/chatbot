package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.QueryEngine;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.WatsonDiscovery;

public class QueryEngineTest {
  private QueryEngine qe;
  private WatsonDiscovery wd;

  @Before
  public void setUp() {
    wd = WatsonDiscovery.buildDiscovery();
    qe = new QueryEngine(wd);
  }

  @Test
  public void testQuery() {
    String query = "hello world";
    Hashtable<String, ArrayList<String>> result = qe.simpleQuery(query);
    // we can't test the content because that may change
    // so we test the format
    assertTrue(result.containsKey("url"));
    assertTrue(result.containsKey("file"));
    assertTrue(result.containsKey("image"));
    assertTrue(result.containsKey("text"));
  }
}
