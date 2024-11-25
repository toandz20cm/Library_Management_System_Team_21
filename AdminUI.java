import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class AdminUI extends JFrame {
  private JButton addDocumentButton;
  private JButton deleteDocumentButton;
  private JButton editDocumentButton;
  private JButton searchDocumentButton;
  private JButton logoutButton;
  private JButton viewAllDocumentsButton;
  private Library library;
  private JButton addDocumentByISBNButton;
  private JButton userListButton;

  public AdminUI(Library library) {
    this.library = library;

    setTitle("Library Management System - Quản trị viên");
    setSize(700, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    // Header panel
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(new Color(0x1976D2));
    headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

    JLabel titleLabel = new JLabel("Quản trị hệ thống thư viện");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    headerPanel.add(titleLabel);

    getContentPane().setBackground(new Color(240, 248, 255)); // Màu nền nhạt hơn

    // Main panel for buttons
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    buttonPanel.setBackground(new Color(0xE3F2FD));
    buttonPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

    // GridBag layout setup
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;

    // Tạo các nút
    addDocumentButton = createStyledButton("Thêm tài liệu");
    deleteDocumentButton = createStyledButton("Xóa tài liệu");
    editDocumentButton = createStyledButton("Chỉnh sửa tài liệu");
    searchDocumentButton = createStyledButton("Tìm kiếm tài liệu");
    logoutButton = createStyledButton("Đăng xuất");
    viewAllDocumentsButton = createStyledButton("Xem các tài liệu sẵn có");
    addDocumentByISBNButton = createStyledButton("Thêm tài liệu bằng ISBN");
    userListButton = createStyledButton("Danh sách người dùng");

    // Đặt các nút vào bố cục với GridBagLayout

    gbc.insets = new Insets(10, 20, 10, 20); // Khoảng cách giữa các nút
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 1;
    add(addDocumentButton, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    add(deleteDocumentButton, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    add(editDocumentButton, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    add(searchDocumentButton, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    add(viewAllDocumentsButton, gbc);

    gbc.gridy++;
    add(addDocumentByISBNButton, gbc);

    gbc.gridy++;
    add(userListButton, gbc);

    gbc.gridy++;
    add(logoutButton, gbc);

    // Add header and button panels to frame
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2; // Để headerPanel chiếm cả chiều ngang
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add(headerPanel, gbc);

    // Thêm tài liệu
    addDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField quantityField = new JTextField(15);
        JTextField isbnField = new JTextField(15);
        JTextField publicationYearField = new JTextField(15);
        JTextField genreField = new JTextField(15);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Tên tài liệu:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Tác giả:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Số lượng:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel("Năm xuất bản:"));
        inputPanel.add(publicationYearField);
        inputPanel.add(new JLabel("Thể loại:"));
        inputPanel.add(genreField);

        int result = JOptionPane.showConfirmDialog(AdminUI.this, inputPanel,
            "Nhập thông tin tài liệu mới", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
          try {
            String title = titleField.getText();
            String author = authorField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String isbn = isbnField.getText();
            int publicationYear = Integer.parseInt(publicationYearField.getText());
            String genre = genreField.getText();

            int id = library.getDocuments().size() + 1;
            Document document = new Document(id, title, author, quantity, isbn, publicationYear, genre);

            library.addDocument(document);
            JOptionPane.showMessageDialog(AdminUI.this, "Thêm tài liệu thành công!");
          } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(AdminUI.this,
                "Định dạng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });


    // Xóa tài liệu
    deleteDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int id;
        try {
          id = Integer.parseInt(JOptionPane.showInputDialog("Nhập ID tài liệu cần xóa:"));
          library.removeDocument(id);
          JOptionPane.showMessageDialog(AdminUI.this,
              "Xóa tài liệu và các đánh giá liên quan thành công!");
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(AdminUI.this,
              "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Chỉnh sửa tài liệu
    editDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          int id = Integer.parseInt(JOptionPane.showInputDialog("Nhập ID tài liệu cần sửa:"));
          String newAuthor = JOptionPane.showInputDialog("Nhập tác giả mới:");
          int newQuantity = Integer.parseInt(JOptionPane.showInputDialog("Nhập số lượng mới:"));

          library.editDocument(id, newAuthor, newQuantity);
          JOptionPane.showMessageDialog(AdminUI.this
              , "Chỉnh sửa tài liệu thành công!");
        } catch (NumberFormatException ex) {
        }
      }
    });

    // Tìm kiếm tài liệu
    searchDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String keyword = JOptionPane.showInputDialog("Nhập từ khóa tìm kiếm (tên, tác giả, thể loại):");
        if (keyword == null) return;

        List<Document> matchingDocuments = library.searchDocuments(keyword);
        if (!matchingDocuments.isEmpty()) {
          StringBuilder result = new StringBuilder("Kết quả tìm kiếm:\n");
          for (Document doc : matchingDocuments) {
            result.append(doc.toString()).append("\n------------------\n");
          }
          JOptionPane.showMessageDialog(AdminUI.this, result.toString());
        } else {
          JOptionPane.showMessageDialog(AdminUI.this, "Không tìm thấy tài liệu nào!");
        }
      }
    });

    // Đăng xuất
    logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new LoginUI(library).setVisible(true);
        dispose();
      }
    });

    // Thêm ActionListener cho nút
    viewAllDocumentsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Mở giao diện AllDocumentsUI
        new AllDocumentsUI(library).setVisible(true);
      }
    });

    // Tạo sự kiện cho nút thêm tài liệu bằng ISBN
    addDocumentByISBNButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String isbn = JOptionPane.showInputDialog("Nhập ISBN:");
        if (isbn != null && !isbn.isEmpty()) {
          new FetchBookDataTask(isbn).execute(); // Gọi đa luồng để tải thông tin tài liệu
        }
      }
    });

    userListButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new UserListUI(library).setVisible(true);
      }
    });

  }

  // SwingWorker để gọi Google Books API và cập nhật giao diện
  private class FetchBookDataTask extends SwingWorker<Document, Void> {
    private final String isbn;

    public FetchBookDataTask(String isbn) {
      this.isbn = isbn;
    }

    @Override
    protected Document doInBackground() throws Exception {
      // Tải thông tin từ Google Books API
      String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
      HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
      conn.setRequestMethod("GET");
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder jsonBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        jsonBuilder.append(line);
      }
      reader.close();

      JSONObject json = new JSONObject(jsonBuilder.toString());
      JSONArray items = json.optJSONArray("items");
      if (items != null && items.length() > 0) {
        JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
        String title = volumeInfo.optString("title", "Không rõ");
        String author = volumeInfo.optJSONArray("authors") != null ?
            volumeInfo.getJSONArray("authors").getString(0) : "Không rõ";
        int publicationYear = volumeInfo.optString("publishedDate", "0").isEmpty() ?
            0 : Integer.parseInt(volumeInfo.optString("publishedDate").substring(0, 4));
        String genre = volumeInfo.optJSONArray("categories") != null ?
            volumeInfo.getJSONArray("categories").getString(0) : "Không rõ";

        return new Document(library.getDocuments().size() + 1, title, author, 1, isbn, publicationYear, genre);
      }
      return null;
    }

    @Override
    protected void done() {
      try {
        Document document = get();
        if (document != null) {
          library.addDocument(document);
          JOptionPane.showMessageDialog(AdminUI.this, "Thêm tài liệu thành công!");
        } else {
          JOptionPane.showMessageDialog(AdminUI.this, "Không tìm thấy tài liệu với ISBN đã nhập.");
        }
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(AdminUI.this, "Có lỗi xảy ra khi thêm tài liệu.");
      }
    }
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("SansSerif", Font.PLAIN, 15));
    button.setBackground(new Color(100, 149, 237));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Hiệu ứng hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(70, 130, 180));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(100, 149, 237));
        button.setFont(button.getFont().deriveFont(Font.PLAIN));
      }
    });

    return button;
  }

}
