import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.List;

public class UserUI extends JFrame {
    // Màu sắc chung
    private static final Color BACKGROUND_COLOR = new Color(245, 250, 255);
    private static final Color PRIMARY_COLOR = new Color(60, 116, 179);
    private static final Color SECONDARY_COLOR = new Color(100, 149, 237);

    // Phông chữ
    private static final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 18);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // Các thành phần giao diện
    private JLabel displayNameLabel;
    private JLabel phoneNumberLabel;
    private JLabel birthDateLabel;
    private JTable borrowedDocumentsTable;
    private JButton[] actionButtons;

    private User user;
    private Library library;

    public UserUI(Library library, User user) {
        this.library = library;
        this.user = user;

        // Cấu hình cửa sổ
        configureWindow();

        // Tạo giao diện
        createUI();

        // Thiết lập sự kiện
        setupEventListeners();
    }

    private void configureWindow() {
        setTitle("Hệ Thống Quản Lý Thư Viện - " + user.getDisplayName());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void createUI() {
        // Layout tổng thể
        setLayout(new BorderLayout(15, 15));

        // Panel thông tin người dùng
        JPanel userInfoPanel = createUserInfoPanel();
        add(userInfoPanel, BorderLayout.NORTH);

        // Bảng tài liệu đã mượn
        JScrollPane tableScrollPane = createBorrowedDocumentsTable();
        add(tableScrollPane, BorderLayout.CENTER);

        // Panel nút chức năng
        JPanel actionButtonPanel = createActionButtonPanel();
        add(actionButtonPanel, BorderLayout.EAST);
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Tiêu đề thông tin người dùng
        JLabel titleLabel = new JLabel("Thông Tin Cá Nhân");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Các nhãn thông tin
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        displayNameLabel = createInfoLabel("Tên hiển thị: " + user.getDisplayName());
        panel.add(displayNameLabel, gbc);

        gbc.gridy = 2;
        phoneNumberLabel = createInfoLabel("Số điện thoại: " + user.getPhoneNumber());
        panel.add(phoneNumberLabel, gbc);

        gbc.gridy = 3;
        birthDateLabel = createInfoLabel("Ngày sinh: " + user.getBirthDate());
        panel.add(birthDateLabel, gbc);

        return panel;
    }

    private JScrollPane createBorrowedDocumentsTable() {
        String[] columnNames = {"Tên Tài Liệu", "Tác Giả", "Thể Loại"};
        borrowedDocumentsTable = new JTable(new DefaultTableModel(columnNames, 0)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Cấu hình bảng
        borrowedDocumentsTable.setFont(NORMAL_FONT);
        borrowedDocumentsTable.setRowHeight(30);
        borrowedDocumentsTable.getTableHeader().setFont(NORMAL_FONT);
        borrowedDocumentsTable.setSelectionBackground(SECONDARY_COLOR);
        borrowedDocumentsTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(borrowedDocumentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Tài Liệu Đã Mượn"));
        return scrollPane;
    }

    private JPanel createActionButtonPanel() {
        // Main panel với padding và border
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel actionTitle = new JLabel("Thao Tác");
        actionTitle.setFont(TITLE_FONT);
        actionTitle.setForeground(PRIMARY_COLOR);
        titlePanel.add(actionTitle);
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(10));
    
        // Tạo panel cho các button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
    
        String[] buttonTexts = {
            "Chỉnh Sửa Thông Tin",
            "Mượn Tài Liệu",
            "Trả Tài Liệu",
            "Xem Tài Liệu",
            "Đăng Xuất"
        };
    
        String[] buttonIcons = {
            "👤", "📚", "↩️", "🔍", "🚪"
        };

        actionButtons = new JButton[buttonTexts.length];
    
        for (int i = 0; i < buttonTexts.length; i++) {
            // Container cho mỗi button để thêm margin
            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
            buttonContainer.setBackground(BACKGROUND_COLOR);
            
            // Tạo button với icon và text
            JButton button = new JButton();
            button.setLayout(new BoxLayout(button, BoxLayout.X_AXIS));
            
            // Icon panel
            JLabel iconLabel = new JLabel(buttonIcons[i]);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            
            // Text panel
            JLabel textLabel = new JLabel(buttonTexts[i]);
            textLabel.setFont(NORMAL_FONT);
            
            button.add(iconLabel);
            button.add(textLabel);
            button.add(Box.createHorizontalGlue());
            
            // Styling cho button
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(250, 45));
            button.setPreferredSize(new Dimension(250, 45));
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            
            // Hiệu ứng hover
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(SECONDARY_COLOR);
                    // Thêm hiệu ứng shadow khi hover
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)));
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(PRIMARY_COLOR);
                    button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
                }
    
                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(new Color(45, 87, 134));
                }
    
                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(SECONDARY_COLOR);
                }
            });
    
            actionButtons[i] = button;
            buttonContainer.add(button);
            buttonContainer.add(Box.createVerticalStrut(10));
            buttonPanel.add(buttonContainer);
            buttonPanel.add(Box.createVerticalStrut(10));
        }
    
        // Thêm panel button vào main panel
        mainPanel.add(buttonPanel);
        
        // Panel wrapper để căn chỉnh tổng thể
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);
        wrapperPanel.add(mainPanel, BorderLayout.NORTH);
        
        return wrapperPanel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private void setupEventListeners() {
      // Chỉnh sửa thông tin người dùng
      actionButtons[0].addActionListener(e -> {
          String newDisplayName = JOptionPane.showInputDialog(
              this, 
              "Nhập tên hiển thị mới:", 
              user.getDisplayName()
          );
          if (newDisplayName == null || newDisplayName.trim().isEmpty()) return;
  
          String newPhoneNumber = JOptionPane.showInputDialog(
              this, 
              "Nhập số điện thoại mới:", 
              user.getPhoneNumber()
          );
          if (newPhoneNumber == null || newPhoneNumber.trim().isEmpty()) return;
  
          String newBirthDate = JOptionPane.showInputDialog(
              this, 
              "Nhập ngày sinh mới (dd/MM/yyyy):", 
              user.getBirthDate()
          );
          if (newBirthDate == null || newBirthDate.trim().isEmpty()) return;
  
          // Cập nhật thông tin người dùng
          user.setDisplayName(newDisplayName);
          user.setPhoneNumber(newPhoneNumber);
          user.setBirthDate(newBirthDate);
  
          // Cập nhật giao diện hiển thị
          displayNameLabel.setText("Tên hiển thị: " + user.getDisplayName());
          phoneNumberLabel.setText("Số điện thoại: " + user.getPhoneNumber());
          birthDateLabel.setText("Ngày sinh: " + user.getBirthDate());
  
          JOptionPane.showMessageDialog(
              this, 
              "Cập nhật thông tin thành công!", 
              "Thông báo", 
              JOptionPane.INFORMATION_MESSAGE
          );
      });
  
      // Mượn tài liệu
      actionButtons[1].addActionListener(e -> {
          String keyword = JOptionPane.showInputDialog(this, "Nhập tên tài liệu muốn mượn:");
          if (keyword == null || keyword.trim().isEmpty()) return;
  
          List<Document> matchingDocuments = library.searchDocuments(keyword);
          if (!matchingDocuments.isEmpty()) {
              Document selectedDocument = matchingDocuments.get(0);
  
              if (selectedDocument.getQuantity() > 0) {
                  user.getBorrowedDocuments().add(selectedDocument);
                  selectedDocument.setQuantity(selectedDocument.getQuantity() - 1);
  
                  saveBorrowedDocumentsToFile();
                  populateBorrowedDocumentsTable();
  
                  JOptionPane.showMessageDialog(
                      this, 
                      "Mượn tài liệu thành công!", 
                      "Thông báo", 
                      JOptionPane.INFORMATION_MESSAGE
                  );
              } else {
                  JOptionPane.showMessageDialog(
                      this, 
                      "Tài liệu không có sẵn!", 
                      "Cảnh báo", 
                      JOptionPane.WARNING_MESSAGE
                  );
              }
          } else {
              JOptionPane.showMessageDialog(
                  this, 
                  "Không tìm thấy tài liệu!", 
                  "Lỗi", 
                  JOptionPane.ERROR_MESSAGE
              );
          }
      });
  
      // Trả tài liệu
      actionButtons[2].addActionListener(e -> {
          if (user.getBorrowedDocuments().isEmpty()) {
              JOptionPane.showMessageDialog(
                  this, 
                  "Bạn chưa mượn tài liệu nào để trả.", 
                  "Thông báo", 
                  JOptionPane.INFORMATION_MESSAGE
              );
              return;
          }
  
          String[] borrowedTitles = user.getBorrowedDocuments().stream()
              .map(Document::getTitle)
              .toArray(String[]::new);
  
          String returnedTitle = (String) JOptionPane.showInputDialog(
              this,
              "Chọn tài liệu để trả:",
              "Trả tài liệu",
              JOptionPane.PLAIN_MESSAGE,
              null,
              borrowedTitles,
              borrowedTitles[0]
          );
  
          if (returnedTitle != null) {
              Document returnedDocument = user.getBorrowedDocuments().stream()
                  .filter(doc -> doc.getTitle().equals(returnedTitle))
                  .findFirst()
                  .orElse(null);
  
              if (returnedDocument != null) {
                  returnedDocument.setQuantity(returnedDocument.getQuantity() + 1);
                  user.getBorrowedDocuments().remove(returnedDocument);
  
                  saveBorrowedDocumentsToFile();
                  populateBorrowedDocumentsTable();
  
                  JOptionPane.showMessageDialog(
                      this, 
                      "Trả tài liệu thành công!", 
                      "Thông báo", 
                      JOptionPane.INFORMATION_MESSAGE
                  );
              }
          }
      });
  
      // Xem tài liệu
      actionButtons[3].addActionListener(e -> 
          new AllDocumentsUI(library).setVisible(true)
      );
  
      // Đăng xuất
      actionButtons[4].addActionListener(e -> {
          new LoginUI(library).setVisible(true);
          dispose();
      });
    }

    private void populateBorrowedDocumentsTable() {
      DefaultTableModel tableModel = (DefaultTableModel) borrowedDocumentsTable.getModel();
      tableModel.setRowCount(0); // Xóa dữ liệu cũ
  
      for (Document doc : user.getBorrowedDocuments()) {
        Object[] rowData = {
            doc.getTitle(),   // Tên tài liệu
            doc.getAuthor(),  // Tên tác giả
            doc.getGenre()    // Thể loại
        };
        tableModel.addRow(rowData);
      }
    }
    private void saveBorrowedDocumentsToFile() {
      try (PrintWriter writer = new PrintWriter(new FileWriter(user.getUsername()
                                                + "_borrowed.txt"))) {
        for (Document doc : user.getBorrowedDocuments()) {
          writer.println(doc.getId() + ","
              + doc.getTitle() + ","
              + doc.getAuthor() + ","
              + doc.getGenre());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}