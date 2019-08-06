package utoronto.utsc.cs.cscc01.chatbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.WebCrawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebCrawlerTest {

    WebCrawler wc;
    String seedValue;

    @Before
    public void setUp() {

        wc = new WebCrawler(2);
        seedValue = "https://digitalfinanceinstitute.org/";
    }

    @Test
    public void testCrawlingLinks() {
        boolean verify = true;
        wc.crawl(seedValue, 0, "?page_id", "?p");
        for (Map.Entry<String, HashMap<String, String>> entry : wc.getLinks().entrySet()) {
            if (!entry.getKey().contains("digitalfinanceinstitute")) {
                verify = false;
                break;
            }
        }
        assertTrue(verify);
        assertEquals(wc.getLinks().size(), 11);
    }

    @Test
    public void testCrawlingArticle() throws IOException {
        boolean verify = true;
        Document document = Jsoup.connect("https://www.digitalfinanceinstitute.org/?p=2406").get();
        HashMap<String, String> article = wc.crawlArticles(document);
        for (Map.Entry<String, String> entry : article.entrySet()) {
            if (! entry.getKey().contains("Announcing Canada’s Top 50 Women in FinTech")) {
                verify = false;
            }
            if (! entry.getValue().contains(" white paper on women in FinTech")) {
                verify = false;
            }
        }
        assertTrue(verify);
    }

}
