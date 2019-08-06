package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.Indexer;

public class IndexerTest {
  private HashMap<String, HashMap<String, String>> testUrlHash;
  private HashMap<String, String> testJournalHash;
  private Indexer indexer;
  private IndexReader reader;
  private Directory indexDir;
  private static final String testIndexPath = "../chatbot/index/testIndex";

  @Before
  public void setUp() throws IOException {
    testUrlHash = new HashMap<>();
    testJournalHash = new HashMap<>();
    indexer = new Indexer(testIndexPath);
    indexDir = FSDirectory.open(Paths.get(testIndexPath));
  }

  @Test
  // test to see if we are creating 5 documents with a title field
  public void testIndexUrl() throws IOException {
    testJournalHash.put("Journal entry 1",
        "This is a great morning to exercise!");
    testJournalHash.put("Journal entry 2",
        "The weather today is kind of poop.");
    testJournalHash.put("Journal entry 3", "Wow, it is still raining today.");
    testJournalHash.put("Journal entry 4",
        "The rain finally stopped, but it is still cloudy");
    testJournalHash.put("Journal entry 5",
        "The sun is finally back! All praise the sun!");

    testUrlHash.put("www.myjournals.net", testJournalHash);

    indexer.indexUrl(testUrlHash);

    reader = DirectoryReader.open(indexDir);
    int i = reader.getDocCount("title");
    assertEquals(i, 5);
    reader.close();
  }

  @Test
  // test to see if we are creating 3 documents
  public void testIndexDoc() throws IOException {
    indexer.indexDoc("The Life of Pie",
        "Something something tiger, boat, stranded.");
    indexer.indexDoc("Operating Systems",
        "Context switch is the process where the CPU is passed between processes...");
    indexer.indexDoc("Learning Haskell",
        "Learning Haskell is as easy as 1, 2, lol you're dead");

    reader = DirectoryReader.open(indexDir);
    int i = reader.getDocCount("title");
    assertEquals(i, 3);
    reader.close();
  }

  @After
  // remove the test indices afterwards
  public void tearDown() throws IOException {
    File dirFile = new File(testIndexPath);
    FileUtils.cleanDirectory(dirFile);
  }
}

