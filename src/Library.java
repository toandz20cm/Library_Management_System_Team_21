import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Library {
  private List<Document> documents;
  private List<User> users;
  private static final String DOCUMENT_FILE = "documents.txt";
  private static final String USER_FILE = "users.txt";
  private HashMap<Integer, List<Review>> reviewsMap;

  public Library() {
    reviewsMap = new HashMap<>();
    this.documents = new ArrayList<>();
    this.users = new ArrayList<>();
    loadDocumentsFromFile();
    loadUsersFromFile();
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public List<User> getUsers() {
    return users;
  }

  public User getUserByUsername(String username) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return user;
      }
    }
    return null; // Trả về null nếu không tìm thấy người dùng với username tương ứng
  }

  public User retrieveDisplayName(String displayName) {
    for (User user : users) {
      if (user.getDisplayName().equals(displayName)) {
        return user;
      }
    }
    return null; // Trả về null nếu không tìm thấy người dùng với displayName tương ứng
  }

  public void addDocument(Document document) {
    document.setId(generateNewDocumentId());  // Cập nhật ID khi thêm tài liệu
    documents.add(document);
    saveDocumentsToFile();
  }

  public void removeDocument(int id) {
    documents.removeIf(doc -> doc.getId() == id);
    reassignDocumentIds();  // Điều chỉnh lại ID sau khi xóa
    saveDocumentsToFile();
    // Xóa file đánh giá liên quan
    File reviewFile = new File(id + "_reviews.txt");
    if (reviewFile.exists()) {
      if (reviewFile.delete()) {
        System.out.println("Đã xóa file đánh giá cho tài liệu ID: " + id);
      } else {
        System.out.println("Không thể xóa file đánh giá cho tài liệu ID: " + id);
      }
    }
  }

  public void editDocument(int id, String author, int quantity) {
    for (Document doc : documents) {
      if (doc.getId() == id) {
        doc.setAuthor(author);
        doc.setQuantity(quantity);
        break;
      }
    }
    saveDocumentsToFile();
  }

  public Document searchDocument(String keyword) {
    for (Document doc : documents) {
      if (doc.getTitle().contains(keyword) || doc.getAuthor().contains(keyword)
          || doc.getGenre().contains(keyword)) {
        return doc;
      }
    }
    return null;
  }

  public List<Document> searchDocuments(String keyword) {
    List<Document> matchingDocuments = new ArrayList<>();
    keyword = keyword.toLowerCase();  // Chuyển về chữ thường để tìm kiếm không phân biệt hoa/thường
    for (Document doc : documents) {
      if (doc.getTitle().toLowerCase().contains(keyword) ||
          doc.getAuthor().toLowerCase().contains(keyword) ||
          doc.getGenre().toLowerCase().contains(keyword)) {
        matchingDocuments.add(doc);
      }
    }
    return matchingDocuments;
  }

  public void addUser(User user) {
    users.add(user);
    saveUsersToFile();
  }

  public User authenticateUser(String username, String password) {
    for (User user : users) {
      if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }

  // Phương thức tìm tài liệu theo ID
  public Document getDocumentById(int id) {
    for (Document doc : documents) {
      if (doc.getId() == id) {
        return doc;
      }
    }
    return null; // Trả về null nếu không tìm thấy tài liệu với ID tương ứng
  }

  // Phương thức tạo ID mới
  private int generateNewDocumentId() {
    if (documents.isEmpty()) {
      return 1;  // Nếu danh sách rỗng, ID bắt đầu từ 1
    } else {
      return documents.get(documents.size() - 1).getId() + 1; // ID tiếp theo dựa trên ID cuối cùng
    }
  }

  // Phương thức sắp xếp lại ID sau khi xóa
  private void reassignDocumentIds() {
    int newId = 1;
    for (Document doc : documents) {
      doc.setId(newId++);
    }
  }

  // Lấy tất cả đánh giá của tài liệu
  public List<Review> getReviewsForDocument(int documentId) {
    List<Review> reviews = new ArrayList<>();
    File file = new File(documentId + "_reviews.txt");
    if (file.exists()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(";", 3);  // Giả sử lưu theo định dạng username;rating;comment
          if (parts.length == 3) {
            reviews.add(new Review(parts[0], Double.parseDouble(parts[1]), parts[2]));
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return reviews;
  }

  // Thêm đánh giá cho tài liệu
  public void addReview(int documentId, Review review) {
    File file = new File(documentId + "_reviews.txt");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
      writer.write(review.getUsername() + ";" + review.getRating() + ";" + review.getComment());
      writer.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveDocumentsToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DOCUMENT_FILE))) {
      oos.writeObject(documents);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadDocumentsFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DOCUMENT_FILE))) {
      documents = (List<Document>) ois.readObject();
    } catch (FileNotFoundException e) {
      // File doesn't exist yet, ignore
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void saveUsersToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
      oos.writeObject(users);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadUsersFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
      users = (List<User>) ois.readObject();
    } catch (FileNotFoundException e) {
      // File doesn't exist yet, ignore
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
