package utoronto.utsc.cs.cscc01.chatbot.Servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.CrawledLink;
import utoronto.utsc.cs.cscc01.chatbot.Database.LinksDatabaseAdmin;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.Indexer;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.WebCrawler;

/**
 * Web servlet used to handle request to crawl a seed URL
 * 
 * @author Reynold
 *
 */
@WebServlet(urlPatterns = "/webcrawler")
public class CrawlerServlet extends HttpServlet {


  private WebCrawler crawler;
  private LinksDatabaseAdmin linksDb;
  private Indexer indexer;
  private String indexPath = "../chatbot/index/documents";

  @Override
  public void init() {
    this.linksDb = new LinksDatabaseAdmin();
  }

  /**
   * Post method used to specify a URL as seed and the depth limit for the crawl
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter writer = response.getWriter();
      String action = request.getParameter("action");

      if ("remove".equals(action)) {

          String url = request.getParameter("url");

          try {
              this.linksDb.remove(url) ;
              writer.write("{\"reply\": \"Link removed!\"}");

          } catch (SQLException e) {
              writer.write("{\"reply\": \"Can't remove link!\"}");
          }

      } else if ("crawl".equals(action)) {

          String url = request.getParameter("url");
          String depth = request.getParameter("depth");

          try {
              this.indexer = new Indexer(indexPath);
              this.crawler = new WebCrawler(Integer.parseInt(depth));
              crawler.crawl(url, 0, "?page_id", "?p");
              indexer.indexUrl(crawler.getLinks());
              linksDb.insert(url, crawler.getLinks());
              writer.write("{\"reply\": \"Website is crawled\"}");

          } catch (IOException | SQLException e) {
              writer.write("{\"reply\": \"Error spotted\"}");
          }
      }
  }


  private void listCrawledLink(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter writer = response.getWriter();

      try {

      List<CrawledLink> listCrawledLink = linksDb.list();

      request.setAttribute("listCrawledLink", listCrawledLink);

            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromJavaArrayList = gsonBuilder.toJson(listCrawledLink);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonFromJavaArrayList);


        } catch (SQLException e){

        writer.write("{\"reply\": \"Can't list links!\"}");

      }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        listCrawledLink(request, response);
    }

}
