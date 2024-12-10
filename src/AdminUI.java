import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
        setTitle("Library Management System - Administrator");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(245, 247, 250));
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        initializeActionListeners();
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
        JPanel dashboardGrid = createDashboardGrid();
        mainPanel.add(dashboardGrid, BorderLayout.CENTER);
        return mainPanel;
    }
    private JPanel createDashboardGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBackground(new Color(245, 247, 250));
        addDocumentButton = createModernButton("Add Document");
        deleteDocumentButton = createModernButton("Delete Document");
        editDocumentButton = createModernButton("Edit Document");
        searchDocumentButton = createModernButton("Search Document");
        viewAllDocumentsButton = createModernButton("View Documents");  //fixx
        addDocumentByGoogleBooksAPIButton = createModernButton("Add by Google Books API");
        userListButton = createModernButton("User List");
        logoutButton = createModernButton("Logout");
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
                if (!getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 50));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 15, 15);
                }
                FontMetrics metrics = g2d.getFontMetrics(getFont());
                Rectangle stringBounds = metrics.getStringBounds(getText(), g2d).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawString(getText(), textX + 1, textY + 1);
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setToolTipText(text);
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
        editDocumentButton.addActionListener(e -> {
            JPanel panel = createModernInputPanel();
            JTextField idField = new JTextField(10);
            JTextField titleField = new JTextField(10);
            JTextField authorField = new JTextField(10);
            JTextField quantityField = new JTextField(10);
            JTextField isbnField = new JTextField(10);
            JTextField publicationYearField = new JTextField(10);
            JTextField genreField = new JTextField(10);
            addInputField(panel, "Document ID:", idField);
            addInputField(panel, "New Title:", titleField);
            addInputField(panel, "New Author:", authorField);
            addInputField(panel, "New Quantity:", quantityField);
            addInputField(panel, "New ISBN:", isbnField);
            addInputField(panel, "New Publication Year:", publicationYearField);
            addInputField(panel, "New Genre:", genreField);
            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(400, 300)); 
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            int result = JOptionPane.showConfirmDialog(
                null, scrollPane, "Edit Document", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    int publicationYear = Integer.parseInt(publicationYearField.getText());
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String isbn = isbnField.getText();
                    String genre = genreField.getText();
                    library.editDocument(id, title, author, quantity, isbn, publicationYear, genre);
                    showSuccessMessage("Document updated successfully!");
                } catch (NumberFormatException ex) {
                    showErrorMessage("Invalid input format! Please check your input.");
                }
            }
        });
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
        viewAllDocumentsButton.addActionListener(e -> new AllDocumentsUI(library).setVisible(true));
        addDocumentByGoogleBooksAPIButton.addActionListener(e -> {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);
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
            JPanel resultPanel = new JPanel(new BorderLayout());
            resultPanel.setBackground(Color.WHITE);
            String[] columnNames = {"Title", "Author", "Year", "Genre", "ISBN", "Add"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; 
                }
            };
            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            JScrollPane scrollPane = new JScrollPane(table);
            resultPanel.add(scrollPane, BorderLayout.CENTER);
            table.getColumn("Add").setCellRenderer(new ButtonRenderer());
            table.getColumn("Add").setCellEditor(new ButtonEditor(new JCheckBox(), library, tableModel));
            mainPanel.add(searchPanel, BorderLayout.NORTH);
            mainPanel.add(resultPanel, BorderLayout.CENTER);
            JFrame frame = new JFrame("Search and Add Documents");
            frame.setContentPane(mainPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            searchButton.addActionListener(actionEvent -> {
                String keyword = keywordField.getText();
                String searchType = (String) searchTypeComboBox.getSelectedItem();
                if (!keyword.isEmpty()) {
                    searchButton.setEnabled(false);
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
    private void addInputField(JPanel panel, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setBackground(Color.WHITE);
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Roboto", Font.PLAIN, 13));
        labelComponent.setForeground(new Color(100, 100, 100));
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBackground(new Color(247, 248, 250));
        field.setForeground(new Color(50, 50, 50));
        field.setCaretColor(new Color(41, 128, 185));
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(225, 225, 225)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
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
    private JPanel createModernInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255)); // Xanh nhạt
    
        JLabel titleLabel = new JLabel("Enter Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180)); // Xanh dương đậm
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        panel.add(titleLabel);
    
        return panel;
    }
    
    private int showModernDialog(JPanel panel, String title) {
        // Cài đặt UIManager cho giao diện hiện đại
        UIManager.put("OptionPane.background", new Color(240, 248, 255)); // Xanh nhạt
        UIManager.put("Panel.background", new Color(240, 248, 255)); 
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("Button.background", new Color(70, 130, 180)); // Xanh dương đậm
        UIManager.put("Button.foreground", Color.WHITE); // Chữ trắng trên nền nút xanh
    
        JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
        );
        
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setBackground(new Color(240, 248, 255)); // Màu nền hộp thoại
        dialog.setSize(450, dialog.getHeight()); // Tăng chiều rộng hộp thoại
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    
        Object selectedValue = optionPane.getValue();
        if (selectedValue == null)
            return JOptionPane.CLOSED_OPTION;
        return ((Integer) selectedValue).intValue();
    }
    
    private void showSuccessMessage(String message) {
        UIManager.put("OptionPane.background", new Color(240, 248, 255)); // Xanh nhạt
        UIManager.put("Panel.background", new Color(240, 248, 255));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(70, 130, 180)); // Nút xanh
        UIManager.put("Button.foreground", Color.WHITE); // Chữ trắng
    
        JOptionPane.showMessageDialog(
            this, message, "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showErrorMessage(String message) {
        UIManager.put("OptionPane.background", new Color(240, 248, 255)); // Xanh nhạt
        UIManager.put("Panel.background", new Color(240, 248, 255));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(255, 69, 58)); // Nút đỏ
        UIManager.put("Button.foreground", Color.WHITE); // Chữ trắng
    
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
        resultPanel.setBackground(new Color(240, 248, 255)); // Xanh nhạt
    
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
    
        StringBuilder sb = new StringBuilder();
        for (Document doc : documents) {
            sb.append(doc.toString()).append("\n------------------\n");
        }
        resultArea.setText(sb.toString());
    
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(450, 300)); // Rộng thoáng hơn
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1)); // Viền xanh dương
    
        resultPanel.add(scrollPane);
    
        UIManager.put("OptionPane.background", new Color(240, 248, 255));
        UIManager.put("Panel.background", new Color(240, 248, 255));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
    
        JOptionPane.showMessageDialog(
            this, resultPanel, "Search Results",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
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
    
            // Bóng chữ với độ mờ
            g2.setColor(new Color(70, 130, 180, 50));
            g2.drawString(text, textX + 2, textY + 2);
    
            g2.dispose();
        }
    }
    
    private class FetchBooksTask extends SwingWorker<List<Document>, Void> {
        private final String keyword;
        private final String searchType;
        private final DefaultTableModel tableModel;
        private final JButton searchButton; 
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
                case "Year" -> "";
                default -> "";
            } + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String apiKey = "AIzaSyATiaHTM4OKzK2e0-z2ukpR4YeAH89RCgU";
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&key=" + apiKey;
            System.out.println("API Request URL: " + apiUrl);
            String response = sendRequest(apiUrl);
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
                List<Document> documents = get();
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
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred while fetching books.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                searchButton.setEnabled(true);
            }
        }
    }
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
                    String title = (String) tableModel.getValueAt(row, 0);
                    String author = (String) tableModel.getValueAt(row, 1);
                    int year = (int) tableModel.getValueAt(row, 2);
                    String genre = (String) tableModel.getValueAt(row, 3);
                    String isbn = (String) tableModel.getValueAt(row, 4);
                    if (title == null || title.isEmpty() || isbn == null || isbn.isEmpty()) {
                        JOptionPane.showMessageDialog(button, "Invalid data. Please check the input.", "Error", JOptionPane.ERROR_MESSAGE);
                        return label;
                    }
                    boolean isDuplicate = library.getDocuments().stream().anyMatch(doc -> doc.getIsbn().equals(isbn));
                    if (isDuplicate) {
                        JOptionPane.showMessageDialog(button, "This document already exists in the library.", "Duplicate Document", JOptionPane.WARNING_MESSAGE);
                        return label;
                    }
                    Document doc = new Document(
                        library.getDocuments().size() + 1,
                        title,
                        author,
                        1,
                        isbn,
                        year,
                        genre
                    );
                    library.addDocument(doc);
                    JOptionPane.showMessageDialog(button, "Document added successfully: " + title, "Success", JOptionPane.INFORMATION_MESSAGE);
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();
                return jsonBuilder.toString();
            } else {
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
            JOptionPane.showMessageDialog(null, "Failed to connect to the server. Please check your internet connection.", "Network Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}