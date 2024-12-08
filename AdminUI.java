import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class AdminUI extends JFrame {
  private JButton addDocumentButton;
  private JButton deleteDocumentButton;
  private JButton editDocumentButton;
  private JButton searchDocumentButton;
  private JButton logoutButton;
  private JButton viewAllDocumentsButton;
  private Library library;
  private JButton addDocumentByGoogleBooksAPIButton;
  private JButton userListButton;
  private Color primaryColor = new Color(41, 128, 185);
  private Color secondaryColor = new Color(52, 152, 219);
  private Color accentColor = new Color(236, 240, 241);

  public AdminUI(Library library) {
    this.library = library;

    // Frame setup
    setTitle("Library Management System - Administrator");
    setSize(1024, 768);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(0, 0));
    getContentPane().setBackground(new Color(245, 247, 250));

    // Modern header with gradient and icon
    JPanel headerPanel = createHeaderPanel();
    add(headerPanel, BorderLayout.NORTH);

    // Main content area with card layout and shadow
    JPanel mainPanel = createMainPanel();
    add(mainPanel, BorderLayout.CENTER);

    // Initialize all action listeners
    initializeActionListeners();

    // Add modern scrollbar style
    UIManager.put("ScrollBar.thumb", new Color(200, 200, 200));
    UIManager.put("ScrollBar.track", new Color(245, 245, 245));
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), 0, secondaryColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    headerPanel.setPreferredSize(new Dimension(getWidth(), 120));
    headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));

    JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
    titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

    // Add drop shadow to text
    titleLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(5, 15, 5, 15),
        new TextShadowBorder()
    ));

    headerPanel.add(titleLabel);
    return headerPanel;
  }

  private JPanel createMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(20, 20));
    mainPanel.setBackground(new Color(245, 247, 250));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    // Create modern dashboard grid
    JPanel dashboardGrid = createDashboardGrid();
    mainPanel.add(dashboardGrid, BorderLayout.CENTER);

    return mainPanel;
  }

  private JPanel createDashboardGrid() {
    JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
    grid.setBackground(new Color(245, 247, 250));

    // Initialize buttons with modern style
    addDocumentButton = createModernButton("Add Document");
    deleteDocumentButton = createModernButton("Delete Document");
    editDocumentButton = createModernButton("Edit Document");
    searchDocumentButton = createModernButton("Search Document");
    viewAllDocumentsButton = createModernButton("View Documents");
    addDocumentByGoogleBooksAPIButton = createModernButton("Add by Google Books API");
    userListButton = createModernButton("User List");
    logoutButton = createModernButton("Logout");

    // Add buttons to grid
    grid.add(createButtonPanel(addDocumentButton));
    grid.add(createButtonPanel(deleteDocumentButton));
    grid.add(createButtonPanel(editDocumentButton));
    grid.add(createButtonPanel(searchDocumentButton));
    grid.add(createButtonPanel(viewAllDocumentsButton));
    grid.add(createButtonPanel(addDocumentByGoogleBooksAPIButton));
    grid.add(createButtonPanel(userListButton));
    grid.add(createButtonPanel(logoutButton));

    return grid;
  }

  private JButton createModernButton(String text) {
    JButton button = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Gradient background
        GradientPaint gradient;
        if (getModel().isPressed()) {
          gradient = new GradientPaint(0, 0, new Color(41, 128, 185, 200),
              0, getHeight(), new Color(52, 152, 219, 200));
        } else if (getModel().isRollover()) {
          gradient = new GradientPaint(0, 0, new Color(52, 152, 219),
              0, getHeight(), new Color(41, 128, 185));
        } else {
          gradient = new GradientPaint(0, 0, new Color(41, 128, 185),
              0, getHeight(), new Color(52, 152, 219));
        }

        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Add subtle inner shadow
        if (!getModel().isPressed()) {
          g2d.setColor(new Color(255, 255, 255, 50));
          g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 15, 15);
        }

        // Text
        FontMetrics metrics = g2d.getFontMetrics(getFont());
        Rectangle stringBounds = metrics.getStringBounds(getText(), g2d).getBounds();

        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();

        // Draw text shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawString(getText(), textX + 1, textY + 1);

        // Draw text
        g2d.setColor(Color.WHITE);
        g2d.drawString(getText(), textX, textY);
      }
    };

    // Basic button setup
    button.setFont(new Font("Roboto", Font.BOLD, 16));
    button.setForeground(Color.WHITE);
    button.setPreferredSize(new Dimension(200, 60));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add padding
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    // Custom tooltip
    button.setToolTipText(text);

    // Add hover effect with timer
    Timer timer = new Timer(50, null);
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        timer.addActionListener(e -> {
          button.repaint();
          if (!button.getModel().isRollover()) {
            timer.stop();
          }
        });
        timer.start();
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent evt) {
        timer.stop();
        button.repaint();
      }
    });

    return button;
  }

  private JPanel createButtonPanel(JButton button) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(245, 247, 250));
    panel.add(button, BorderLayout.CENTER);
    return panel;
  }

  private void initializeActionListeners() {
    // Add Document Action
    addDocumentButton.addActionListener(e -> {
      JPanel panel = createModernInputPanel();
      JTextField titleField = new JTextField(20);
      JTextField authorField = new JTextField(20);
      JTextField quantityField = new JTextField(20);
      JTextField isbnField = new JTextField(20);
      JTextField publicationYearField = new JTextField(20);
      JTextField genreField = new JTextField(20);

      addInputField(panel, "Title:", titleField);
      addInputField(panel, "Author:", authorField);
      addInputField(panel, "Quantity:", quantityField);
      addInputField(panel, "ISBN:", isbnField);
      addInputField(panel, "Publication Year:", publicationYearField);
      addInputField(panel, "Genre:", genreField);

      int result = showModernDialog(panel, "Add New Document");

      if (result == JOptionPane.OK_OPTION) {
        try {
          Document document = new Document(
              library.getDocuments().size() + 1,
              titleField.getText(),
              authorField.getText(),
              Integer.parseInt(quantityField.getText()),
              isbnField.getText(),
              Integer.parseInt(publicationYearField.getText()),
              genreField.getText()
          );
          library.addDocument(document);
          showSuccessMessage("Document added successfully!");
        } catch (NumberFormatException ex) {
          showErrorMessage("Invalid input format!");
        }
      }
    });

    deleteDocumentButton.addActionListener(e -> {
      JPanel panel = createModernInputPanel();

      // Thay thế idField bằng JComboBox để hiển thị danh sách tài liệu
      JComboBox<Document> documentComboBox = new JComboBox<>();
      library.getDocuments().forEach(documentComboBox::addItem); // Thêm tài liệu vào JComboBox
      addInputField(panel, "Select Document:", documentComboBox);

      int result = showModernDialog(panel, "Delete Document");

      if (result == JOptionPane.OK_OPTION) {
        // Lấy tài liệu được chọn
        Document selectedDocument = (Document) documentComboBox.getSelectedItem();
        if (selectedDocument != null) {
          int id = selectedDocument.getId();
          library.removeDocument(id);
          showSuccessMessage("Document deleted successfully!");
        } else {
          showErrorMessage("No document selected!");
        }
      }
    });


    // Edit Document Action
    editDocumentButton.addActionListener(e -> {
      JPanel panel = createModernInputPanel();

      // Thay thế idField bằng JComboBox
      JComboBox<Document> documentComboBox = new JComboBox<>();
      library.getDocuments().forEach(documentComboBox::addItem); // Thêm các tài liệu vào JComboBox
      JTextField titleField = new JTextField(10);
      JTextField authorField = new JTextField(10);
      JTextField quantityField = new JTextField(10);
      JTextField isbnField = new JTextField(10);
      JTextField publicationYearField = new JTextField(10);
      JTextField genreField = new JTextField(10);

      // Tạo các ô nhập liệu
      addInputField(panel, "Select Document:", documentComboBox);
      addInputField(panel, "Title:", titleField);
      addInputField(panel, "Author:", authorField);
      addInputField(panel, "Quantity:", quantityField);
      addInputField(panel, "ISBN:", isbnField);
      addInputField(panel, "Publication Year:", publicationYearField);
      addInputField(panel, "Genre:", genreField);

      // Lắng nghe sự kiện thay đổi lựa chọn trong JComboBox
      documentComboBox.addActionListener(event -> {
        Document selectedDocument = (Document) documentComboBox.getSelectedItem();
        if (selectedDocument != null) {
          // Tự động điền thông tin tài liệu vào các ô nhập liệu
          titleField.setText(selectedDocument.getTitle());
          authorField.setText(selectedDocument.getAuthor());
          quantityField.setText(String.valueOf(selectedDocument.getQuantity()));
          isbnField.setText(selectedDocument.getIsbn());
          publicationYearField.setText(String.valueOf(selectedDocument.getPublicationYear()));
          genreField.setText(selectedDocument.getGenre());
        }
      });

      // Hiển thị thông tin tài liệu đầu tiên mặc định
      if (documentComboBox.getItemCount() > 0) {
        Document firstDocument = (Document) documentComboBox.getItemAt(0);
        if (firstDocument != null) {
          titleField.setText(firstDocument.getTitle());
          authorField.setText(firstDocument.getAuthor());
          quantityField.setText(String.valueOf(firstDocument.getQuantity()));
          isbnField.setText(firstDocument.getIsbn());
          publicationYearField.setText(String.valueOf(firstDocument.getPublicationYear()));
          genreField.setText(firstDocument.getGenre());
        }
      }

      // Hiển thị hộp thoại
      int result = showModernDialog(panel, "Edit Document");

      if (result == JOptionPane.OK_OPTION) {
        try {
          // Lấy tài liệu được chọn
          Document selectedDocument = (Document) documentComboBox.getSelectedItem();
          if (selectedDocument != null) {
            // Lấy dữ liệu đã chỉnh sửa từ các ô nhập liệu
            String title = titleField.getText();
            String author = authorField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String isbn = isbnField.getText();
            int publicationYear = Integer.parseInt(publicationYearField.getText());
            String genre = genreField.getText();

            // Cập nhật thông tin tài liệu
            library.editDocument(
                selectedDocument.getId(), // Giữ nguyên ID tài liệu
                title,
                author,
                quantity,
                isbn,
                publicationYear,
                genre
            );

            showSuccessMessage("Document updated successfully!");
          } else {
            showErrorMessage("No document selected. Please try again.");
          }
        } catch (NumberFormatException ex) {
          showErrorMessage("Invalid input format! Please check your input.");
        }
      }
    });


    // Search Document Action
    searchDocumentButton.addActionListener(e -> {
      JPanel panel = createModernInputPanel();
      JTextField searchField = new JTextField(20);
      addInputField(panel, "Search Keyword:(Title,Author,Genre)", searchField);

      int result = showModernDialog(panel, "Search Documents");

      if (result == JOptionPane.OK_OPTION) {
        String keyword = searchField.getText();
        List<Document> documents = library.searchDocuments(keyword);
        showSearchResults(documents);
      }
    });

    // Other existing action listeners
    viewAllDocumentsButton.addActionListener(e -> new AllDocumentsUI(library).setVisible(true));

    addDocumentByGoogleBooksAPIButton.addActionListener(e -> {
      // Panel chính chứa các tùy chọn
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBackground(Color.WHITE);

      // Panel chứa ô tìm kiếm và combo box
      JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      searchPanel.setBackground(Color.WHITE);

      JTextField keywordField = new JTextField(20);
      JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{"Title", "ISBN", "Genre", "Author", "Year"});
      JButton searchButton = new JButton("Search");

      searchPanel.add(new JLabel("Keyword:"));
      searchPanel.add(keywordField);
      searchPanel.add(new JLabel("Search by:"));
      searchPanel.add(searchTypeComboBox);
      searchPanel.add(searchButton);

      // Panel chứa bảng kết quả
      JPanel resultPanel = new JPanel(new BorderLayout());
      resultPanel.setBackground(Color.WHITE);

      // Bảng kết quả (khởi tạo rỗng)
      String[] columnNames = {"Title", "Author", "Year", "Genre", "ISBN", "Add"};
      DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return column == 5; // Chỉ cho phép chỉnh sửa ở cột "Add"
        }
      };
      JTable table = new JTable(tableModel);
      table.setRowHeight(30);
      JScrollPane scrollPane = new JScrollPane(table);
      resultPanel.add(scrollPane, BorderLayout.CENTER);

      // Thêm renderer và editor cho cột "Add"
      table.getColumn("Add").setCellRenderer(new ButtonRenderer());
      table.getColumn("Add").setCellEditor(new ButtonEditor(new JCheckBox(), library, tableModel));

      // Thêm các panel vào mainPanel
      mainPanel.add(searchPanel, BorderLayout.NORTH);
      mainPanel.add(resultPanel, BorderLayout.CENTER);

      // Hiển thị trong cửa sổ mới
      JFrame frame = new JFrame("Search and Add Documents");
      frame.setContentPane(mainPanel);
      frame.setSize(800, 600);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);

      // Xử lý khi nhấn nút "Search"
      searchButton.addActionListener(actionEvent -> {
        String keyword = keywordField.getText();
        String searchType = (String) searchTypeComboBox.getSelectedItem();

        if (!keyword.isEmpty()) {
          // Vô hiệu hóa nút "Search" để ngăn người dùng bấm liên tục
          searchButton.setEnabled(false);

          // Thực hiện tìm kiếm
          new FetchBooksTask(keyword, searchType, tableModel, searchButton).execute();
        } else {
          JOptionPane.showMessageDialog(frame, "Please enter a keyword.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      });
    });

    userListButton.addActionListener(e -> new UserListUI(library).setVisible(true));

    logoutButton.addActionListener(e -> {
      new LoginUI(library).setVisible(true);
      dispose();
    });
  }

  private void addInputField(JPanel panel, String label, JComponent field) {
    JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
    fieldPanel.setBackground(Color.WHITE);

    // Style cho label
    JLabel labelComponent = new JLabel(label);
    labelComponent.setFont(new Font("Roboto", Font.PLAIN, 13));
    labelComponent.setForeground(new Color(100, 100, 100));

    // Kiểm tra nếu là JTextField để áp dụng style riêng
    if (field instanceof JTextField) {
      JTextField textField = (JTextField) field;
      textField.setFont(new Font("Roboto", Font.PLAIN, 14));
      textField.setPreferredSize(new Dimension(250, 35));
      textField.setBackground(new Color(247, 248, 250));
      textField.setForeground(new Color(50, 50, 50));
      textField.setCaretColor(new Color(41, 128, 185));
      textField.setOpaque(true);

      // Custom border với màu nhạt ở dưới
      textField.setBorder(BorderFactory.createCompoundBorder(
          new MatteBorder(0, 0, 2, 0, new Color(225, 225, 225)),
          BorderFactory.createEmptyBorder(5, 10, 5, 10)
      ));

      // Thêm hiệu ứng hover và focus
      textField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
          textField.setBackground(new Color(242, 243, 245));
          textField.setBorder(BorderFactory.createCompoundBorder(
              new MatteBorder(0, 0, 2, 0, new Color(41, 128, 185)),
              BorderFactory.createEmptyBorder(5, 10, 5, 10)
          ));
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
          textField.setBackground(new Color(247, 248, 250));
          textField.setBorder(BorderFactory.createCompoundBorder(
              new MatteBorder(0, 0, 2, 0, new Color(225, 225, 225)),
              BorderFactory.createEmptyBorder(5, 10, 5, 10)
          ));
        }
      });
    }

    // Style riêng cho JComboBox (nếu cần)
    if (field instanceof JComboBox) {
      JComboBox<?> comboBox = (JComboBox<?>) field;
      comboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
      comboBox.setPreferredSize(new Dimension(250, 35));
      comboBox.setBackground(new Color(247, 248, 250));
      comboBox.setForeground(new Color(50, 50, 50));
      comboBox.setOpaque(true);
    }

    fieldPanel.add(labelComponent, BorderLayout.NORTH);
    fieldPanel.add(field, BorderLayout.CENTER);
    fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

    panel.add(fieldPanel);
  }


  // Cập nhật lại createModernInputPanel() để có padding tốt hơn
  private JPanel createModernInputPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
    panel.setBackground(Color.WHITE);

    return panel;
  }

  // Cập nhật showModernDialog() để dialog đẹp hơn
  private int showModernDialog(JPanel panel, String title) {
    UIManager.put("OptionPane.background", Color.WHITE);
    UIManager.put("Panel.background", Color.WHITE);
    UIManager.put("OptionPane.buttonFont", new Font("Roboto", Font.PLAIN, 13));
    UIManager.put("OptionPane.messageFont", new Font("Roboto", Font.PLAIN, 13));

    JOptionPane optionPane = new JOptionPane(
        panel,
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.OK_CANCEL_OPTION
    );

    // Tạo custom dialog
    JDialog dialog = optionPane.createDialog(this, title);
    dialog.setBackground(Color.WHITE);

    // Set size phù hợp
    dialog.setSize(400, dialog.getHeight());
    dialog.setLocationRelativeTo(this);

    dialog.setVisible(true);

    Object selectedValue = optionPane.getValue();
    if (selectedValue == null)
      return JOptionPane.CLOSED_OPTION;
    return ((Integer)selectedValue).intValue();
  }

  private void showSuccessMessage(String message) {
    JOptionPane.showMessageDialog(
        this, message, "Success",
        JOptionPane.INFORMATION_MESSAGE
    );
  }

  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(
        this, message, "Error",
        JOptionPane.ERROR_MESSAGE
    );
  }

  private void showSearchResults(List<Document> documents) {
    if (documents.isEmpty()) {
      showErrorMessage("No documents found!");
      return;
    }

    JPanel resultPanel = new JPanel();
    resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
    resultPanel.setBackground(Color.WHITE);

    JTextArea resultArea = new JTextArea();
    resultArea.setFont(new Font("Roboto", Font.PLAIN, 14));
    resultArea.setEditable(false);
    resultArea.setLineWrap(true);
    resultArea.setWrapStyleWord(true);

    StringBuilder sb = new StringBuilder();
    for (Document doc : documents) {
      sb.append(doc.toString()).append("\n------------------\n");
    }
    resultArea.setText(sb.toString());

    JScrollPane scrollPane = new JScrollPane(resultArea);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    resultPanel.add(scrollPane);

    JOptionPane.showMessageDialog(
        this, resultPanel, "Search Results",
        JOptionPane.PLAIN_MESSAGE
    );
  }

  // Add new modern border classes
  private class TextShadowBorder extends AbstractBorder {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      JLabel label = (JLabel) c;
      String text = label.getText();
      FontMetrics fm = label.getFontMetrics(label.getFont());

      int textX = x + (width - fm.stringWidth(text)) / 2;
      int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();

      // Draw shadow
      g2.setColor(new Color(0, 0, 0, 50));
      g2.drawString(text, textX + 2, textY + 2);

      g2.dispose();
    }
  }

  // Lớp xử lý tìm kiếm tài liệu qua API
  private class FetchBooksTask extends SwingWorker<List<Document>, Void> {
    private final String keyword;
    private final String searchType;
    private final DefaultTableModel tableModel;
    private final JButton searchButton; // Tham chiếu nút "Search"

    public FetchBooksTask(String keyword, String searchType, DefaultTableModel tableModel, JButton searchButton) {
      this.keyword = keyword;
      this.searchType = searchType;
      this.tableModel = tableModel;
      this.searchButton = searchButton;
    }

    @Override
    protected List<Document> doInBackground() throws Exception {
      String query = switch (searchType) {
        case "Title" -> "intitle:";
        case "ISBN" -> "isbn:";
        case "Genre" -> "subject:";
        case "Author" -> "inauthor:";
        case "Year" -> ""; // Không thêm gì cho Year, xử lý sau
        default -> "";
      } + URLEncoder.encode(keyword, StandardCharsets.UTF_8);

      // Đặt API Key
      String apiKey = "AIzaSyATiaHTM4OKzK2e0-z2ukpR4YeAH89RCgU";
      String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&key=" + apiKey;

      // In URL để kiểm tra
      System.out.println("API Request URL: " + apiUrl);

      // Gửi yêu cầu API
      String response = sendRequest(apiUrl);

      // Xử lý dữ liệu JSON trả về
      JSONObject json = new JSONObject(response);
      JSONArray items = json.optJSONArray("items");
      List<Document> documents = new ArrayList<>();

      if (items != null) {
        for (int i = 0; i < items.length(); i++) {
          JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

          String title = volumeInfo.optString("title", "Unknown");
          String author = volumeInfo.optJSONArray("authors") != null
              ? volumeInfo.getJSONArray("authors").getString(0)
              : "Unknown";
          String publishedDate = volumeInfo.optString("publishedDate", "");
          int publicationYear = 0;

          if (!publishedDate.isEmpty() && publishedDate.length() >= 4) {
            try {
              publicationYear = Integer.parseInt(publishedDate.substring(0, 4));
            } catch (NumberFormatException e) {
              publicationYear = 0;
            }
          }

          String genre = volumeInfo.optJSONArray("categories") != null
              ? volumeInfo.getJSONArray("categories").getString(0)
              : "Unknown";
          String isbn = volumeInfo.optJSONArray("industryIdentifiers") != null
              ? volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier")
              : "Unknown";

          // Lọc kết quả chính xác theo searchType
          if ("Title".equals(searchType)) {
            if (!title.toLowerCase().contains(keyword.toLowerCase())) {
              continue;
            }
          } else if ("Author".equals(searchType)) {
            if (!author.toLowerCase().contains(keyword.toLowerCase())) {
              continue;
            }
          } else if ("Genre".equals(searchType)) {
            if (!genre.toLowerCase().contains(keyword.toLowerCase())) {
              continue;
            }
          } else if ("Year".equals(searchType)) {
            if (publicationYear != Integer.parseInt(keyword)) {
              continue;
            }
          } else if ("ISBN".equals(searchType)) {
            if (!isbn.toLowerCase().contains(keyword.toLowerCase())) {
              continue;
            }
          }

          // Thêm tài liệu vào danh sách kết quả
          documents.add(new Document(
              library.getDocuments().size() + 1,
              title,
              author,
              1,
              isbn,
              publicationYear,
              genre
          ));
        }
      }

      return documents;
    }

    @Override
    protected void done() {
      try {
        // Lấy kết quả từ doInBackground()
        List<Document> documents = get();

        // Hiển thị kết quả trong bảng
        tableModel.setRowCount(0);
        for (Document doc : documents) {
          tableModel.addRow(new Object[]{
              doc.getTitle(),
              doc.getAuthor(),
              doc.getPublicationYear(),
              doc.getGenre(),
              doc.getIsbn(),
              "Add"
          });
        }

        if (documents.isEmpty()) {
          JOptionPane.showMessageDialog(
              null,
              "Không tìm thấy tài liệu phù hợp với từ khóa.",
              "Kết quả rỗng",
              JOptionPane.INFORMATION_MESSAGE
          );
        }
      } catch (ExecutionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof UnknownHostException) {
          JOptionPane.showMessageDialog(
              null,
              "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối internet.",
              "Lỗi mạng",
              JOptionPane.ERROR_MESSAGE
          );
        } else {
          JOptionPane.showMessageDialog(
              null,
              "Đã xảy ra lỗi không mong muốn: " + cause.getMessage(),
              "Lỗi",
              JOptionPane.ERROR_MESSAGE
          );
        }
      } catch (InterruptedException e) {
        JOptionPane.showMessageDialog(
            null,
            "Tác vụ đã bị gián đoạn.",
            "Lỗi",
            JOptionPane.WARNING_MESSAGE
        );
      } finally {
        // Bật lại nút "Search"
        searchButton.setEnabled(true);
      }
    }

  }

  // Renderer cho nút "Add"
  class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
      setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      setText((value == null) ? "" : value.toString());
      return this;
    }
  }

  // Editor cho nút "Add"
  class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private int row;
    private DefaultTableModel tableModel;
    private Library library;

    public ButtonEditor(JCheckBox checkBox, Library library, DefaultTableModel tableModel) {
      super(checkBox);
      this.library = library;
      this.tableModel = tableModel;
      button = new JButton();
      button.setOpaque(true);

      // Dừng chỉnh sửa khi nhấn nút
      button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column) {
      label = (value == null) ? "" : value.toString();
      button.setText(label);
      clicked = true;
      this.row = row;
      return button;
    }

    @Override
    public Object getCellEditorValue() {
      if (clicked) {
        try {
          // Lấy dữ liệu từ hàng
          String title = (String) tableModel.getValueAt(row, 0);
          String author = (String) tableModel.getValueAt(row, 1);
          int year = (int) tableModel.getValueAt(row, 2);
          String genre = (String) tableModel.getValueAt(row, 3);
          String isbn = (String) tableModel.getValueAt(row, 4);

          // Kiểm tra dữ liệu null hoặc rỗng
          if (title == null || title.isEmpty() || isbn == null || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(button, "Invalid data. Please check the input.", "Error", JOptionPane.ERROR_MESSAGE);
            return label;
          }

          // Kiểm tra trùng lặp trong thư viện
          Document duplicateDoc = library.getDocuments().stream()
              .filter(doc -> doc.getIsbn().equals(isbn))
              .findFirst()
              .orElse(null);

          if (duplicateDoc != null) {
            // Nếu tài liệu đã tồn tại, tăng số lượng
            duplicateDoc.setQuantity(duplicateDoc.getQuantity() + 1);
            JOptionPane.showMessageDialog(button, "This document already exists. Quantity has been updated.", "Document Updated", JOptionPane.INFORMATION_MESSAGE);
          } else {
            // Thêm tài liệu mới vào thư viện
            Document doc = new Document(
                library.getDocuments().size() + 1,
                title,
                author,
                1, // Số lượng khởi tạo là 1
                isbn,
                year,
                genre
            );

            library.addDocument(doc);
            JOptionPane.showMessageDialog(button, "Document added successfully: " + title, "Success", JOptionPane.INFORMATION_MESSAGE);
          }

        } catch (Exception e) {
          JOptionPane.showMessageDialog(button, "An error occurred while adding the document.", "Error", JOptionPane.ERROR_MESSAGE);
          e.printStackTrace();
        }
      }
      clicked = false;
      return label;
    }


    @Override
    public boolean stopCellEditing() {
      clicked = false;
      return super.stopCellEditing();
    }
  }

  private String sendRequest(String apiUrl) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
    conn.setRequestMethod("GET");

    try {
      int responseCode = conn.getResponseCode();
      if (responseCode >= 200 && responseCode < 300) {
        // Nếu phản hồi thành công
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          jsonBuilder.append(line);
        }
        reader.close();
        return jsonBuilder.toString();
      } else {
        // Nếu có lỗi HTTP, đọc phản hồi lỗi
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        StringBuilder errorBuilder = new StringBuilder();
        String line;
        while ((line = errorReader.readLine()) != null) {
          errorBuilder.append(line);
        }
        errorReader.close();

        System.err.println("Error response: " + errorBuilder.toString());
        throw new IOException("Unexpected response code: " + responseCode);
      }
    } catch (IOException e) {
      // Hiển thị thông báo nếu mất kết nối
      JOptionPane.showMessageDialog(null, "Failed to connect to the server. Please check your internet connection.", "Network Error", JOptionPane.ERROR_MESSAGE);
      throw e; // Ném lại ngoại lệ để xử lý tiếp
    }
  }

}