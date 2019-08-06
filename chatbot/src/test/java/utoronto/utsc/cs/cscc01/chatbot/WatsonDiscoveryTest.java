package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.WatsonDiscovery;

public class WatsonDiscoveryTest {
  private WatsonDiscovery discovery;

  @Before
  public void setUp() {
    this.discovery = WatsonDiscovery.buildDiscovery();
  }

  @Test
  public void testDiscoverySingleton() {
    WatsonDiscovery discovery2 = WatsonDiscovery.buildDiscovery();
    assertSame(discovery, discovery2);
  }

  @Test
  public void testEnvironmentId() {
    String envId = discovery.getEnvironmentId();
    assertEquals("35ee748a-5486-4809-a8be-1c1b5773b73e", envId);
  }

  @Test
  public void testCrawlerCollectionId() {
    String collectionId = discovery.getCrawlerCollectionId();
    assertEquals("bedf65b9-55ff-4260-9d44-56adbd5b5722", collectionId);
  }

  @Test
  public void testFileCollectionId() {
    String collectionId = discovery.getUploadedFilesCollectionId();
    assertEquals("5907e278-2e52-440a-b6e2-8dd58d0d956b", collectionId);
  }
}
