package utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects;

/**
 * Class used to encapsulate user answer ratings into an object
 *
 * @author Reynold
 */

public class AnswerRating {

  private int id;
  private String answer;
  private int good;
  private int bad;

  public AnswerRating(int id, String answer, int good, int bad) {
    this.id = id;
    this.answer = answer;
    this.good = good;
    this.bad = bad;

  }

  public void setId(int id) {
    this.id = id;
  }

  public void setGood(int good) {
    this.good = good;
  }

  public void setBad(int bad) {
    this.bad = bad;
  }

  public void setComments(String answer) {
    this.answer = answer;
  }

  public int getId() {
    return this.id;
  }

  public int getGood() {
    return this.good;
  }

  public int getBad() {
    return this.bad;
  }

  public String getComments() {
    return this.answer;
  }
}
