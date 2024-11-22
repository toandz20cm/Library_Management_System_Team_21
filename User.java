import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
  private String username;
  private String password;
  private String displayName;
  private String birthDate;
  private String phoneNumber;
  private List<Document> borrowedDocuments;

  public User(String username, String password, String displayName
      , String birthDate, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.borrowedDocuments = new ArrayList<>();
      }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getDisplayName() { return displayName; }
  public void setDisplayName(String displayName) { this.displayName = displayName; }

  public String getBirthDate() { return birthDate; }
  public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public List<Document> getBorrowedDocuments() { return borrowedDocuments; }

  // Thêm tài liệu vào danh sách mượn
  public void borrowDocument(Document document) {
    borrowedDocuments.add(document);
  }

  // Trả tài liệu và xóa khỏi danh sách mượn
  public void returnDocument(Document document) {
    borrowedDocuments.remove(document);
  }

  @Override
  public String toString() {
    return "Username: " + username + ", Display Name: " + displayName + ", Birth Date: "
        + birthDate + ", Phone Number: " + phoneNumber;
  }
}
