package utoronto.utsc.cs.cscc01.chatbot.LuceneEngine;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;

/**
 * File parser used to standardize the format of a file to be uploaded.
 * 
 * @author Reynold
 */
public class FileParser {

  public String parsePdf(InputStream file) {
    PDDocument pdDoc;
    String trimmedText = "";
    PDFTextStripper pdfStripper;

    try {
      pdDoc = PDDocument.load(file);
      pdfStripper = new PDFTextStripper();
      String parsedText = pdfStripper.getText(pdDoc).trim();
      trimmedText = parsedText.replaceAll("[\\n\\t\\v\\f\\r ]", " ");
      pdDoc.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return trimmedText;

  }

  /**
   * Used to handle parsing of docx file
   * 
   * @param file - input stream of file
   * @return string of standardized format with illegal characters removed
   */
  public String parseDocx(InputStream file) {

    String trimmedText = "";

    try {
      XWPFDocument doc = new XWPFDocument(file);
      XWPFWordExtractor extract = new XWPFWordExtractor(doc);
      String parsedText = extract.getText().trim();
      trimmedText = parsedText.replaceAll("[\\n\\t\\v\\f\\r ]", " ");
      file.close();
    } catch (IOException e) {

      e.printStackTrace();
    }

    return trimmedText;
  }

  /**
   * Used to handle parsing of doc files
   * 
   * @param file - input stream of file
   * @return string of standardized format with illegal characters removed
   */
  public String parseDoc(InputStream file) {

    String trimmedText = "";

    try {
      HWPFDocument doc = new HWPFDocument(file);
      WordExtractor extractor = new WordExtractor(doc);
      String parsedText = extractor.getText().trim();
      trimmedText = parsedText.replaceAll("[\\n\\t\\v\\f\\r ]", " ");
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return trimmedText;
  }

  /**
   * Used to handle parsing of html
   * 
   * @param file - input stream of file
   * @return string of standardized format with illegal characters removed
   */
  public String parseHtml(InputStream file) throws IOException {

    String content = IOUtils.toString(file, StandardCharsets.UTF_8);
    String text = "";
    Document document = Jsoup.parse(content);
    String title = document.title();
    text = text + title;
    Elements paragraphs = document.body().select("p");
    for (Element paragraph : paragraphs) {
      text = text + "\n" + paragraph.text();
    }
    return text;

  }

  /**
   * Special case parser used to handle parsing of corpus document
   * 
   * @param fileName - name of file
   * @param file - input stream of file
   */
  public ArrayList<ArrayList<String>> parseCorpus(String fileName,
      InputStream file) {
    XWPFDocument doc;
    ArrayList<ArrayList<String>> outerList = new ArrayList<>();
    try {
      doc = new XWPFDocument(file);
      XWPFWordExtractor extract = new XWPFWordExtractor(doc);
      String parsedText = extract.getText().trim();
      String[] strippedContent = parsedText.split("\\n[0-9]+[.]");
      int index = 0;
      for (String s : strippedContent) {
        String trimmedText = s.replaceAll("[\\n\\t\\v\\f\\r ]", " ");
        ArrayList<String> innerList = new ArrayList<>();
        innerList.add(fileName + "(" + index + ")");
        innerList.add(trimmedText);
        outerList.add(innerList);
        index++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return outerList;
  }


  /**
   * A method for overall file parsing, which will call other parsing methods
   *
   * @param fileName - name of file
   * @param file - input stream of file
   */
  public String parse(String fileName, InputStream file) throws IOException {
    String docType = FilenameUtils.getExtension(fileName);
    String text = "";
    switch (docType) {
      case "doc":
        text = parseDoc(file);
        break;
      case "docx":
        if (fileName.equals("Chatbot Corpus.docx")) {
          Gson gson = new Gson();
          text = gson.toJson(parseCorpus(fileName, file));
        } else {
          text = parseDocx(file);
        }
        break;
      case "pdf":
        text = parsePdf(file);
        break;
      case "txt":
        text = IOUtils.toString(file, StandardCharsets.UTF_8);
        break;
      case "html":
        text = parseHtml(file);
        break;
    }
    return text;

  }
}

