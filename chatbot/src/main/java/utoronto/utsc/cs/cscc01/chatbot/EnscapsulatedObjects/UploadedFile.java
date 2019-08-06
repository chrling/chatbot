package utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects;

/**
 * Class used to encapsulate an uploaded file into an object
 * @author Reynold
 */
public class UploadedFile {

  private int id;
  private String filename;
  private String date;

  public UploadedFile(int id, String filename, String date) {
    this.id = id;
    this.filename = filename;
    this.date = date;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public int getId() {
    return id;
  }

  public String getFilename() {
    return filename;
  }

  public String getDate() {
    return date;
  }
}
