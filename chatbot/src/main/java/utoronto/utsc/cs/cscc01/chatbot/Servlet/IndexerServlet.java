package utoronto.utsc.cs.cscc01.chatbot.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.CrawledLink;
import utoronto.utsc.cs.cscc01.chatbot.Database.FilesDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.Database.LinksDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.Indexer;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.UploadedFile;

/**
 * Web servlet used to handle web crawler's request to index certain files or
 * URL
 * 
 * @author Reynold
 */
@WebServlet(urlPatterns = "/index")
public class IndexerServlet extends HttpServlet {


  private Indexer indexer;
  private FilesDatabaseAdmin filesDb;
  private LinksDatabaseAdmin linksDb;
  private String indexPath = "../chatbot/index/documents";

  public void init() {

    this.filesDb = new FilesDatabaseAdmin();
    this.linksDb = new LinksDatabaseAdmin();
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    if (request.getParameter("reindex") != null) {


      List<UploadedFile> fileList;
      List<CrawledLink> linkList;

      try {
        this.indexer = new Indexer(indexPath);
        this.indexer.removeIndex();
        fileList = filesDb.list();
        for (UploadedFile file : fileList) {
          String fileName = file.getFilename();
          String content = filesDb.extractFile(fileName);
          if (fileName.equals("Chatbot Corpus.docx")) {
            Gson gson = new Gson();
            ArrayList<ArrayList<String>> obj =
                gson.fromJson(content, ArrayList.class);
            for (ArrayList<String> list : obj) {
              this.indexer.indexDoc(list.get(0), list.get(1));
            }
          } else {
            this.indexer.indexDoc(fileName, content);
          }
        }
        linkList = linksDb.list();
        for (CrawledLink link : linkList) {
          String seed = link.getSeed();
          HashMap<String, HashMap<String, String>> linkCollection =
              linksDb.extractLinkCollection(seed);
          this.indexer.indexUrl(linkCollection);
        }

        writer.write("{\"reply\": \"Indexed Files\"}");

      } catch (SQLException | ClassNotFoundException | IOException e) {

        writer.write("{\"reply\": \"Error getting information from database\"}");
      }

    } else if (request.getParameter("reset") != null) {

      try {
        this.indexer = new Indexer(indexPath);
        this.indexer.removeIndex();
        writer.write("{\"reply\": \"Removed Index\"}");

      } catch (IOException e) {
        writer.write("{\"reply\": \"Error removing index\"}");
      }
    }

  }

}
