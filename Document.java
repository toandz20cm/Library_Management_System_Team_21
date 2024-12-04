import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class Document implements Serializable {
  private int id;
  private String title;
  private String author;
  private int quantity;
  private String isbn;
  private int publicationYear;
  private String genre;
  private List<Double> ratings;
  private List<String> reviews;  // Danh sách đánh giá của người dùng

  public Document(int id, String title, String author, int quantity, String isbn
      , int publicationYear, String genre) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.quantity = quantity;
    this.isbn = isbn;
    this.publicationYear = publicationYear;
    this.genre = genre;
    this.ratings = new ArrayList<>();
    this.reviews = new ArrayList<>();
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getAuthor() { return author; }
  public void setAuthor(String author) { this.author = author; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }

  public String getIsbn() { return isbn; }
  public void setIsbn(String isbn) { this.isbn = isbn; }

  public int getPublicationYear() { return publicationYear; }
  public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

  public String getGenre() { return genre; }
  public void setGenre(String genre) { this.genre = genre; }

  public double getAverageRating() {
    if (ratings.isEmpty()) return 0;
    double sum = 0;
    for (Double rating : ratings) {
      sum += rating;
    }
    return sum / ratings.size();
  }

  public void addRating(double rating) {
    ratings.add(rating);
  }

  public void addReview(double rating, String review) {
    ratings.add(rating);
    reviews.add(review);
  }

  // Load đánh giá từ file
  public List<String> loadRatingsFromFile() {
    List<String> comments = new ArrayList<>();
    File file = new File(id + "_ratings.txt");
    if (file.exists()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          comments.add(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return comments;
  }

  @Override
  public String toString() {
    return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Quantity: " + quantity +
        ", ISBN: " + isbn + ", Publication Year: " + publicationYear + ", Genre: " + genre;
  }
}
