import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.AbstractBorder;
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

    // Thiết lập cơ bản cho frame
    setTitle("Library Management System - Quản trị viên");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Header panel với gradient
    JPanel headerPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 118, 210), 
                                                     w, h, new Color(21, 101, 192));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
        }
    };
    headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
    headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));

    // Title với shadow effect
    JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    titleLabel.setForeground(Color.WHITE);
    // Drop shadow effect cho text
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    headerPanel.add(titleLabel);

    // Main content panel với card layout hiện đại
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(new Color(245, 245, 245));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    // Button panel với layout mới
    JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 20, 20));
    buttonPanel.setBackground(new Color(245, 245, 245));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
    buttonPanel.add(addDocumentButton);
    buttonPanel.add(viewAllDocumentsButton);
    buttonPanel.add(deleteDocumentButton);
    buttonPanel.add(addDocumentByISBNButton);
    buttonPanel.add(editDocumentButton);
    buttonPanel.add(userListButton);
    buttonPanel.add(searchDocumentButton);
    buttonPanel.add(logoutButton);

    // Card panel cho content chính
    JPanel cardPanel = new JPanel();
    cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
    cardPanel.setBackground(Color.WHITE);
    cardPanel.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(15, new Color(0, 0, 0, 20)),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    cardPanel.add(buttonPanel);

    // Thêm shadow effect cho card panel
    mainPanel.add(cardPanel, BorderLayout.CENTER);

    // Thêm các panel vào frame
    add(headerPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);
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
  class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color shadowColor;

    public RoundedBorder(int radius, Color shadowColor) {
        this.radius = radius;
        this.shadowColor = shadowColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ shadow
        g2d.setColor(shadowColor);
        g2d.fillRoundRect(x + 2, y + 2, width - 4, height - 4, radius, radius);
        
        // Vẽ border
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, width - 2, height - 2, radius, radius);
        
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius/2, radius/2, radius/2, radius/2);
    }
}
private JButton createStyledButton(String text) {
    JButton button = new JButton(text);

    try {
        Font roboto = Font.createFont(Font.TRUETYPE_FONT, 
            new File("Roboto-Regular.ttf")).deriveFont(15f);
        button.setFont(roboto);
    } catch (Exception e) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
    }

    button.setContentAreaFilled(false);
    button.setOpaque(true);
    
    // Thiết lập style cơ bản
    button.setBackground(new Color(79, 121, 255));  // Màu xanh đậm hơn
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(59, 89, 182), 1),
        BorderFactory.createEmptyBorder(12, 25, 12, 25)
    ));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Drop shadow
    button.setBorder(BorderFactory.createCompoundBorder(
        new DropShadowBorder(Color.BLACK, 3, 0.2f, 4, false, true, true, true),
        button.getBorder()
    ));

    // Hiệu ứng hover và click
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        private final Color DEFAULT_COLOR = new Color(79, 121, 255);
        private final Color HOVER_COLOR = new Color(65, 105, 225);
        private final Color PRESS_COLOR = new Color(51, 88, 201);
        
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(HOVER_COLOR);
            button.setFont(button.getFont().deriveFont(Font.BOLD));
            // Hiệu ứng scale up nhẹ
            button.setSize((int)(button.getWidth() * 1.05), 
                         (int)(button.getHeight() * 1.05));
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(DEFAULT_COLOR);
            button.setFont(button.getFont().deriveFont(Font.PLAIN));
            // Trả về kích thước ban đầu
            button.setSize((int)(button.getWidth() / 1.05), 
                         (int)(button.getHeight() / 1.05));
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent evt) {
            button.setBackground(PRESS_COLOR);
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            button.setBackground(HOVER_COLOR);
        }
    });

    return button;
}

// Class tạo viền đổ bóng
class DropShadowBorder extends AbstractBorder {
    private final Color shadowColor;
    private final int shadowSize;
    private final float shadowOpacity;
    private final int shadowOffset;
    private final boolean showTopShadow;
    private final boolean showLeftShadow;
    private final boolean showBottomShadow;
    private final boolean showRightShadow;

    public DropShadowBorder(Color shadowColor, int shadowSize, 
                           float shadowOpacity, int shadowOffset,
                           boolean showTopShadow, boolean showLeftShadow, 
                           boolean showBottomShadow, boolean showRightShadow) {
        this.shadowColor = shadowColor;
        this.shadowSize = shadowSize;
        this.shadowOpacity = shadowOpacity;
        this.shadowOffset = shadowOffset;
        this.showTopShadow = showTopShadow;
        this.showLeftShadow = showLeftShadow;
        this.showBottomShadow = showBottomShadow;
        this.showRightShadow = showRightShadow;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ đổ bóng
        for (int i = 0; i < shadowSize; i++) {
            float opacity = shadowOpacity * (shadowSize - i) / shadowSize;
            g2.setColor(new Color(shadowColor.getRed()/255f, 
                                shadowColor.getGreen()/255f,
                                shadowColor.getBlue()/255f, 
                                opacity));
                                
            if (showBottomShadow) {
                g2.drawLine(x + shadowOffset, y + height - i, 
                           x + width - shadowOffset, y + height - i);
            }
            if (showRightShadow) {
                g2.drawLine(x + width - i, y + shadowOffset, 
                           x + width - i, y + height - shadowOffset);
            }
        }
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(showTopShadow ? shadowSize : 0,
                         showLeftShadow ? shadowSize : 0,
                         showBottomShadow ? shadowSize : 0,
                         showRightShadow ? shadowSize : 0);
    }
}
}
