package utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects;

/**
 * A class that stores all the information associated with crawled links. It will be put into a list and sent to
 * the frontend for information displaying
 *
 * @author Reynold
 */

public class CrawledLink {

    private int id;
    private String seed;
    private String date;

    public CrawledLink(int id, String seed, String date) {
        this.id = id;
        this.seed = seed;
        this.date = date;
    }

    public void setId(int id) { this.id = id; }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public void setDate(String date) {this.date = date; }

    public int getId() { return this.id; }

    public String getSeed() {
        return this.seed;
    }

    public String getDate() {return this.date; }


}
