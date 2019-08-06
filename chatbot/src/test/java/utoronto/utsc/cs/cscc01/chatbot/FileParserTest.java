package utoronto.utsc.cs.cscc01.chatbot;

import org.junit.Before;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.FileParser;


import java.io.*;

import static junit.framework.TestCase.assertEquals;

public class FileParserTest {

    FileParser fp;

    @Before
    public void setUp() {

        fp = new FileParser();
    }

    @Test
    public void testParsingPdf() throws FileNotFoundException {
        InputStream is = new FileInputStream(new File("../chatbot/files/test/test.pdf"));
        String content = fp.parsePdf(is);
        assertEquals(content, "test");
    }

    @Test
    public void testParsingDoc() throws FileNotFoundException {
        InputStream is = new FileInputStream(new File("../chatbot/files/test/test.doc"));
        String content = fp.parseDoc(is);
        assertEquals(content, "test");
    }

    @Test
    public void testParsingDocx() throws FileNotFoundException {
        InputStream is = new FileInputStream(new File("../chatbot/files/test/test.docx"));
        String content = fp.parseDocx(is);
        assertEquals(content, "test");
    }

    @Test
    public void testParsingHtml() throws IOException {
        InputStream is = new FileInputStream(new File("../chatbot/files/test/test.html"));
        String content = fp.parseHtml(is);
        assertEquals(content, "Test title\ntest");
    }
}
