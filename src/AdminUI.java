import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.MatteBorder;
import javax.swing.Timer;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;


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
        addDocumentByISBNButton = createModernButton("Add by ISBN");
        userListButton = createModernButton("User List");
        logoutButton = createModernButton("Logout");

        // Add buttons to grid
        grid.add(createButtonPanel(addDocumentButton));
        grid.add(createButtonPanel(deleteDocumentButton));
        grid.add(createButtonPanel(editDocumentButton));
        grid.add(createButtonPanel(searchDocumentButton));
        grid.add(createButtonPanel(viewAllDocumentsButton));
        grid.add(createButtonPanel(addDocumentByISBNButton));
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

        // Delete Document Action
        deleteDocumentButton.addActionListener(e -> {
            JPanel panel = createModernInputPanel();
            JTextField idField = new JTextField(20);
            addInputField(panel, "Document ID:", idField);

            int result = showModernDialog(panel, "Delete Document");

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    library.removeDocument(id);
                    showSuccessMessage("Document deleted successfully!");
                } catch (NumberFormatException ex) {
                    showErrorMessage("Invalid ID format!");
                }
            }
        });

        // Edit Document Action
        editDocumentButton.addActionListener(e -> {
            JPanel panel = createModernInputPanel();
            JTextField idField = new JTextField(20);
            JTextField authorField = new JTextField(20);
            JTextField quantityField = new JTextField(20);

            addInputField(panel, "Document ID:", idField);
            addInputField(panel, "New Author:", authorField);
            addInputField(panel, "New Quantity:", quantityField);

            int result = showModernDialog(panel, "Edit Document");

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    library.editDocument(id, authorField.getText(), quantity);
                    showSuccessMessage("Document updated successfully!");
                } catch (NumberFormatException ex) {
                    showErrorMessage("Invalid input format!");
                }
            }
        });

        // Search Document Action
// Search Document Action
searchDocumentButton.addActionListener(e -> {
    // Panel chính để chọn kiểu tìm kiếm
    JPanel panel = createModernInputPanel();
    JTextField searchField = new JTextField(20);
    addInputField(panel, "Search Keyword:", searchField);

    // Tùy chọn kiểu tìm kiếm
    JRadioButton localSearchButton = new JRadioButton("Search in Library");
    JRadioButton apiSearchButton = new JRadioButton("Search Online");
    ButtonGroup searchTypeGroup = new ButtonGroup();
    searchTypeGroup.add(localSearchButton);
    searchTypeGroup.add(apiSearchButton);
    localSearchButton.setSelected(true); // Mặc định chọn tìm kiếm nội bộ

    // Thêm radio buttons vào giao diện
    JPanel searchTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchTypePanel.setBackground(Color.WHITE);
    searchTypePanel.add(localSearchButton);
    searchTypePanel.add(apiSearchButton);
    searchTypePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
    panel.add(searchTypePanel);

    // Hiển thị dialog chọn kiểu tìm kiếm
    int result = showModernDialog(panel, "Search Documents");

    if (result == JOptionPane.OK_OPTION) {
        String keyword = searchField.getText();
        if (localSearchButton.isSelected()) {
            // Tìm kiếm trong thư viện nội bộ
            List<Document> documents = library.searchDocuments(keyword);
            showSearchResults(documents);
        } else if (apiSearchButton.isSelected()) {
            // Tìm kiếm trên mạng qua API
            new SearchOnlineTask(keyword).execute();
        }
    }
});
        // Other existing action listeners
        viewAllDocumentsButton.addActionListener(e -> new AllDocumentsUI(library).setVisible(true));
        addDocumentByISBNButton.addActionListener(e -> {
            JPanel panel = createModernInputPanel();
            JTextField isbnField = new JTextField(20);
            addInputField(panel, "ISBN:", isbnField);

            int result = showModernDialog(panel, "Add Document by ISBN");

            if (result == JOptionPane.OK_OPTION) {
                String isbn = isbnField.getText();
                if (!isbn.isEmpty()) {
                    new FetchBookDataTask(isbn).execute();
                }
            }
        });
        userListButton.addActionListener(e -> new UserListUI(library).setVisible(true));
        logoutButton.addActionListener(e -> {
            new LoginUI(library).setVisible(true);
            dispose();
        });
    }
private void addInputField(JPanel panel, String label, JTextField field) {
    JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
    fieldPanel.setBackground(Color.WHITE);
    
    // Style cho label
    JLabel labelComponent = new JLabel(label);
    labelComponent.setFont(new Font("Roboto", Font.PLAIN, 13));
    labelComponent.setForeground(new Color(100, 100, 100));
    
    // Style cho text field
    field.setFont(new Font("Roboto", Font.PLAIN, 14));
    field.setPreferredSize(new Dimension(250, 35));
    field.setBackground(new Color(247, 248, 250));
    field.setForeground(new Color(50, 50, 50));
    field.setCaretColor(new Color(41, 128, 185));
    field.setOpaque(true);
    
    // Custom border với màu nhạt ở dưới
    field.setBorder(BorderFactory.createCompoundBorder(
        new MatteBorder(0, 0, 2, 0, new Color(225, 225, 225)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    // Thêm hiệu ứng hover và focus
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            field.setBackground(new Color(242, 243, 245));
            field.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, new Color(41, 128, 185)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            field.setBackground(new Color(247, 248, 250));
            field.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
    });

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
    
    // Thêm tiêu đề cho panel
    JLabel titleLabel = new JLabel("Enter Information");
    titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
    titleLabel.setForeground(new Color(50, 50, 50));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    panel.add(titleLabel);
    
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

    // Existing inner classes (FetchBookDataTask, RoundedBorder, DropShadowBorder)
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
    private class SearchOnlineTask extends SwingWorker<List<Document>, Void> {
        private final String keyword;
    
        public SearchOnlineTask(String keyword) {
            this.keyword = keyword;
        }
    
        @Override
        protected List<Document> doInBackground() throws Exception {
            List<Document> onlineResults = new ArrayList<>();
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(keyword, "UTF-8");
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
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
                    String title = volumeInfo.optString("title", "Unknown Title");
                    String author = volumeInfo.optJSONArray("authors") != null ?
                            volumeInfo.getJSONArray("authors").getString(0) : "Unknown Author";
                    String isbn = volumeInfo.optJSONArray("industryIdentifiers") != null ?
                            volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier") : "Unknown ISBN";
                    int publicationYear = volumeInfo.optString("publishedDate", "0").isEmpty() ?
                            0 : Integer.parseInt(volumeInfo.optString("publishedDate").substring(0, 4));
                    String genre = volumeInfo.optJSONArray("categories") != null ?
                            volumeInfo.getJSONArray("categories").getString(0) : "Unknown Genre";
    
                    Document doc = new Document(0, title, author, 0, isbn, publicationYear, genre);
                    onlineResults.add(doc);
                }
            }
            return onlineResults;
        }
    
        @Override
        protected void done() {
            try {
                List<Document> documents = get();
                if (documents.isEmpty()) {
                    showErrorMessage("No online documents found!");
                } else {
                    showSearchResults(documents);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while fetching online documents!");
            }
        }
    }
}