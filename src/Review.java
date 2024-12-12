package src;

public class Review {
  private String username;
  private double rating;
  private String comment;

  public Review(String username, double rating, String comment) {
    this.username = username;
    this.rating = rating;
    this.comment = comment;
  }

  public String getUsername() {
    return username;
  }

  public double getRating() {
    return rating;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return username + ": " + comment + " (" + rating + "/5)";
  }
}
