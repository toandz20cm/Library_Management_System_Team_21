import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.MatteBorder;
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

    populateBorrowedDocumentsTable();

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
        "Đăng Xuất",
        "Đổi Mật Khẩu"
    };

    String[] buttonIcons = {
        "👤    ", "📚      ", "↩️", "🔍     ", "🚪     ","      ",
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
      JPanel panel = createModernInputPanel();
      JTextField displayNameField = new JTextField(user.getDisplayName(), 20);
      JTextField phoneNumberField = new JTextField(user.getPhoneNumber(), 20);
      JTextField birthDateField = new JTextField(user.getBirthDate(), 20);

      addInputField(panel, "Tên hiển thị:", displayNameField);
      addInputField(panel, "Số điện thoại:", phoneNumberField);
      addInputField(panel, "Ngày sinh:", birthDateField);

      int result = showModernDialog(panel, "Chỉnh sửa thông tin");

      if (result == JOptionPane.OK_OPTION) {
        String newDisplayName = displayNameField.getText();
        String newPhoneNumber = phoneNumberField.getText();
        String newBirthDate = birthDateField.getText();

        if (newDisplayName.trim().isEmpty() || newPhoneNumber.trim().isEmpty() || newBirthDate.trim().isEmpty()) {
          showErrorMessage("Vui lòng điền đầy đủ thông tin!");
          return;
        }

        user.setDisplayName(newDisplayName);
        user.setPhoneNumber(newPhoneNumber);
        user.setBirthDate(newBirthDate);
        updateUserInfoInFile(user);

        displayNameLabel.setText("Tên hiển thị: " + user.getDisplayName());
        phoneNumberLabel.setText("Số điện thoại: " + user.getPhoneNumber());
        birthDateLabel.setText("Ngày sinh: " + user.getBirthDate());

        showSuccessMessage("Cập nhật thông tin thành công!");
      }
    });

    // Mượn tài liệu
    actionButtons[1].addActionListener(e -> {
      // Hiển thị hộp thoại để người dùng nhập từ khóa tìm kiếm
      String keyword = JOptionPane.showInputDialog(
          UserUI.this,
          "Nhập ID, ISBN hoặc tên tài liệu muốn mượn:",
          "Tìm tài liệu",
          JOptionPane.PLAIN_MESSAGE
      );

      if (keyword == null || keyword.trim().isEmpty()) return; // Hủy nếu nhấn Cancel hoặc không nhập gì

      // Tìm kiếm tài liệu theo ID, ISBN hoặc Tên
      Document foundDocument = null;
      try {
        int id = Integer.parseInt(keyword); // Kiểm tra nếu người dùng nhập ID
        foundDocument = library.getDocumentById(id); // Tìm tài liệu theo ID
      } catch (NumberFormatException ex) {
        // Nếu không phải ID, kiểm tra tiếp theo ISBN
        foundDocument = library.getDocumentByIsbn(keyword);
        if (foundDocument == null) {
          // Nếu không phải ISBN, tìm theo tên
          foundDocument = library.searchDocument(keyword);
        }
      }

      // Xử lý nếu tìm thấy tài liệu
      if (foundDocument != null) {
        if (foundDocument.getQuantity() > 0) {
          // Cập nhật danh sách tài liệu đã mượn
          user.getBorrowedDocuments().add(foundDocument);
          foundDocument.setQuantity(foundDocument.getQuantity() - 1);

          // Lưu danh sách tài liệu vào file
          saveLibraryToFile(); // <== Lưu lại sau khi cập nhật số lượng

          // Lưu lại thông tin vào file và cập nhật giao diện
          saveBorrowedDocumentsToFile();
          populateBorrowedDocumentsTable();

          JOptionPane.showMessageDialog(
              UserUI.this,
              "Mượn tài liệu thành công!",
              "Thành công",
              JOptionPane.INFORMATION_MESSAGE
          );
        } else {
          JOptionPane.showMessageDialog(
              UserUI.this,
              "Tài liệu không có sẵn!",
              "Thông báo",
              JOptionPane.WARNING_MESSAGE
          );
        }
      } else {
        JOptionPane.showMessageDialog(
            UserUI.this,
            "Không tìm thấy tài liệu với từ khóa: " + keyword,
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

      // Hiển thị danh sách các tài liệu mà người dùng đã mượn
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
        // Tìm tài liệu trong danh sách tài liệu đã mượn
        Document returnedDocument = user.getBorrowedDocuments().stream()
            .filter(doc -> doc.getTitle().equals(returnedTitle))
            .findFirst()
            .orElse(null);

        if (returnedDocument != null) {
          // Tìm tài liệu trong thư viện để cập nhật số lượng
          Document libraryDocument = library.getDocuments().stream()
              .filter(doc -> doc.getId() == returnedDocument.getId()) // So sánh theo ID
              .findFirst()
              .orElse(null);

          if (libraryDocument != null) {
            // Cập nhật số lượng trong thư viện
            libraryDocument.setQuantity(libraryDocument.getQuantity() + 1);
          }

          // Loại bỏ tài liệu khỏi danh sách tài liệu đã mượn của người dùng
          user.getBorrowedDocuments().remove(returnedDocument);

          // Lưu lại thay đổi vào file và cập nhật giao diện
          saveLibraryToFile(); // Lưu danh sách tài liệu trong thư viện
          saveBorrowedDocumentsToFile(); // Lưu danh sách tài liệu đã mượn
          populateBorrowedDocumentsTable(); // Cập nhật giao diện

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

    // Đổi mật khẩu
    actionButtons[5].addActionListener(e -> {
      JPanel panel = createModernInputPanel();
      JPasswordField oldPasswordField = new JPasswordField(20);
      JPasswordField newPasswordField = new JPasswordField(20);
      JPasswordField confirmPasswordField = new JPasswordField(20);

      addInputField(panel, "Mật khẩu cũ:", oldPasswordField);
      addInputField(panel, "Mật khẩu mới:", newPasswordField);
      addInputField(panel, "Xác nhận mật khẩu mới:", confirmPasswordField);

      int result = showModernDialog(panel, "Đổi mật khẩu");

      if (result == JOptionPane.OK_OPTION) {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
          showErrorMessage("Mật khẩu xác nhận không khớp!");
          return;
        }

        if (oldPassword.equals(user.getPassword())) {
          user.setPassword(newPassword);
          updateUserPasswordInFile(user);
          showSuccessMessage("Đổi mật khẩu thành công!");
        } else {
          showErrorMessage("Mật khẩu cũ không đúng!");
        }
      }
    });

  }

  private void updateUserInfoInFile(User updatedUser) {
    List<User> userList = loadUsersFromFile();

    for (User u : userList) {
      if (u.getUsername().equals(updatedUser.getUsername())) {
        u.setDisplayName(updatedUser.getDisplayName()); // Cập nhật tên hiển thị
        u.setPhoneNumber(updatedUser.getPhoneNumber()); // Cập nhật số điện thoại
        u.setBirthDate(updatedUser.getBirthDate());     // Cập nhật ngày sinh
        break;
      }
    }

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.txt"))) {
      oos.writeObject(userList); // Lưu danh sách người dùng
      System.out.println("Cập nhật thông tin người dùng thành công.");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Lỗi khi lưu danh sách người dùng.");
    }
  }

  private void updateUserPasswordInFile(User updatedUser) {
    List<User> userList = loadUsersFromFile();

    // Cập nhật thông tin người dùng
    for (User u : userList) {
      if (u.getUsername().equals(updatedUser.getUsername())) {
        u.setPassword(updatedUser.getPassword());
        break;
      }
    }

    // Ghi lại toàn bộ danh sách vào file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.txt"))) {
      oos.writeObject(userList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<User> loadUsersFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.txt"))) {
      return (List<User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }


  private void populateBorrowedDocumentsTable() {
    // Xóa dữ liệu cũ trong bảng
    DefaultTableModel tableModel = (DefaultTableModel) borrowedDocumentsTable.getModel();
    tableModel.setRowCount(0);

    // Đọc danh sách tài liệu đã mượn từ file
    List<Document> borrowedDocuments = loadBorrowedDocumentsFromFile();
    user.getBorrowedDocuments().clear(); // Xóa danh sách cũ
    user.getBorrowedDocuments().addAll(borrowedDocuments); // Cập nhật danh sách tài liệu mượn của User

    // Thêm dữ liệu vào bảng
    for (Document doc : borrowedDocuments) {
      tableModel.addRow(new Object[]{
          doc.getTitle(),
          doc.getAuthor(),
          doc.getGenre()
      });
    }
  }


  private void saveBorrowedDocumentsToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(user.getUsername() + "_borrowed.txt"))) {
      oos.writeObject(user.getBorrowedDocuments());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<Document> loadBorrowedDocumentsFromFile() {
    File file = new File(user.getUsername() + "_borrowed.txt");
    if (!file.exists()) {
      return new ArrayList<>();
    }

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      return (List<Document>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  private void saveLibraryToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("documents.txt"))) {
      oos.writeObject(library.getDocuments()); // Lưu toàn bộ danh sách tài liệu
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Lỗi khi lưu danh sách tài liệu vào file.");
    }
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

}