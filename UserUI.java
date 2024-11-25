import java.awt.Font;
import java.awt.*;
import java.awt.Color;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;

public class UserUI extends JFrame {
  private JLabel displayNameLabel;
  private JLabel phoneNumberLabel;
  private JLabel birthDateLabel;
  private JTable borrowedDocumentsTable;
  private JButton editUserInfoButton;
  private JButton borrowDocumentButton;
  private JButton returnDocumentButton;
  private JButton logoutButton;
  private JButton viewAllDocumentsButton;
  private JButton changePasswordButton;

  private User user;
  private Library library;

  public UserUI(Library library, User user) {
    this.library = library;
    this.user = user;

    setTitle("Library Management System - " + user.getDisplayName());
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Cấu hình màu nền chính
    getContentPane().setBackground(new Color(240, 248, 255));

    // Phông chữ chung
    Font font = new Font("Segoe UI", Font.PLAIN, 14);

    displayNameLabel = new JLabel("Tên hiển thị: " + user.getDisplayName());
    displayNameLabel.setFont(font);

    phoneNumberLabel = new JLabel("Số điện thoại: " + user.getPhoneNumber());
    phoneNumberLabel.setFont(font);

    birthDateLabel = new JLabel("Ngày sinh: " + user.getBirthDate());
    birthDateLabel.setFont(font);

    // Tạo bảng tài liệu đã mượn
    borrowedDocumentsTable = new JTable(new DefaultTableModel(new Object[]{"Tên tài liệu", "Tác giả", "Thể loại"}, 0));
    borrowedDocumentsTable.setFont(font);
    borrowedDocumentsTable.setRowHeight(25);
    borrowedDocumentsTable.setBackground(new Color(255, 255, 255));
    borrowedDocumentsTable.setGridColor(new Color(220, 220, 220));
    JScrollPane tableScrollPane = new JScrollPane(borrowedDocumentsTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách tài liệu đã mượn"));

    editUserInfoButton = createStyledButton("Chỉnh sửa thông tin");
    borrowDocumentButton = createStyledButton("Mượn tài liệu");
    returnDocumentButton = createStyledButton("Trả tài liệu");
    changePasswordButton = createStyledButton("Đổi mật khẩu");
    viewAllDocumentsButton = createStyledButton("Xem tất cả tài liệu");
    logoutButton = createStyledButton("Đăng xuất");

    setLayout(new BorderLayout());
    JPanel userInfoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
    userInfoPanel.setBackground(new Color(240, 248, 255));
    userInfoPanel.add(displayNameLabel);
    userInfoPanel.add(new JLabel());
    userInfoPanel.add(phoneNumberLabel);
    userInfoPanel.add(new JLabel());
    userInfoPanel.add(birthDateLabel);
    userInfoPanel.add(new JLabel());

    JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
    buttonPanel.setBackground(new Color(240, 248, 255));
    buttonPanel.add(editUserInfoButton);
    buttonPanel.add(borrowDocumentButton);
    buttonPanel.add(returnDocumentButton);
    buttonPanel.add(changePasswordButton);
    buttonPanel.add(viewAllDocumentsButton);
    buttonPanel.add(logoutButton);

    add(userInfoPanel, BorderLayout.NORTH);
    add(new JScrollPane(borrowedDocumentsTable), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.EAST);

    editUserInfoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String newDisplayName = JOptionPane.showInputDialog("Nhập tên hiển thị mới:", user.getDisplayName());
        if (newDisplayName == null) return;  // Hủy nếu nhấn Cancel

        String newPhoneNumber = JOptionPane.showInputDialog("Nhập số điện thoại mới:", user.getPhoneNumber());
        if (newPhoneNumber == null) return;

        String newBirthDate = JOptionPane.showInputDialog("Nhập ngày sinh mới (dd/MM/yyyy):", user.getBirthDate());
        if (newBirthDate == null) return;

        // Cập nhật thông tin người dùng
        user.setDisplayName(newDisplayName);
        user.setPhoneNumber(newPhoneNumber);
        user.setBirthDate(newBirthDate);

        // Cập nhật giao diện hiển thị
        displayNameLabel.setText("Tên hiển thị: " + user.getDisplayName());
        phoneNumberLabel.setText("Số điện thoại: " + user.getPhoneNumber());
        birthDateLabel.setText("Ngày sinh: " + user.getBirthDate());

        JOptionPane.showMessageDialog(UserUI.this, "Cập nhật thông tin thành công!");
      }
    });



    borrowDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
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
      }
    });

    returnDocumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Kiểm tra xem người dùng có mượn tài liệu nào không
        if (user.getBorrowedDocuments().isEmpty()) {
          JOptionPane.showMessageDialog(UserUI.this
                                      , "Bạn chưa mượn tài liệu nào để trả.");
          return;
        }

        // Tạo danh sách các tài liệu đã mượn để người dùng chọn
        String[] borrowedTitles = user.getBorrowedDocuments().stream()
            .map(Document::getTitle)
            .toArray(String[]::new);
        String returnedTitle = (String) JOptionPane.showInputDialog(
            UserUI.this,
            "Chọn tài liệu để trả:",
            "Trả tài liệu",
            JOptionPane.PLAIN_MESSAGE,
            null,
            borrowedTitles,
            borrowedTitles[0]);

        // Tìm tài liệu được chọn để trả
        if (returnedTitle != null) {
          Document returnedDocument = null;
          for (Document doc : user.getBorrowedDocuments()) {
            if (doc.getTitle().equals(returnedTitle)) {
              returnedDocument = doc;
              break;
            }
          }

          if (returnedDocument != null) {
            // Trả tài liệu (tăng số lượng tài liệu trong thư viện)
            returnedDocument.setQuantity(returnedDocument.getQuantity() + 1);
            user.getBorrowedDocuments().remove(returnedDocument);

            // Cập nhật lại file lưu thông tin tài liệu đã mượn
            saveBorrowedDocumentsToFile();

            // Cập nhật bảng hiển thị các tài liệu đã mượn
            populateBorrowedDocumentsTable();

            JOptionPane.showMessageDialog(UserUI.this
                                          , "Trả tài liệu thành công!");
          }
        }
      }
    });

    changePasswordButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Nhập mật khẩu cũ
        String oldPassword = JOptionPane.showInputDialog(null, "Nhập mật khẩu cũ:");
        if (oldPassword == null || !oldPassword.equals(user.getPassword())) {
          JOptionPane.showMessageDialog(null, "Mật khẩu cũ không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Nhập mật khẩu mới
        String newPassword = JOptionPane.showInputDialog(null, "Nhập mật khẩu mới:");
        if (newPassword == null || newPassword.length() < 6) {
          JOptionPane.showMessageDialog(null, "Mật khẩu mới phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Xác nhận mật khẩu mới
        String confirmPassword = JOptionPane.showInputDialog(null, "Xác nhận mật khẩu mới:");
        if (!newPassword.equals(confirmPassword)) {
          JOptionPane.showMessageDialog(null, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Cập nhật mật khẩu
        user.setPassword(newPassword);

        JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!");
      }
    });

    logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new LoginUI(library).setVisible(true);
        dispose();
      }
    });

    viewAllDocumentsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Mở giao diện AllDocumentsUI
        new AllDocumentsUI(library).setVisible(true);
      }
    });
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


  private void loadBorrowedDocumentsFromFile() {
    File file = new File(user.getUsername() + "_borrowed.txt");
    if (file.exists()) {
      // Xóa danh sách tài liệu đã mượn trước khi tải từ file
      user.getBorrowedDocuments().clear();

      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(",");
          Document doc = new Document(Integer.parseInt(parts[0]),parts[1]
              , parts[2] , 1, "N/A",0,parts[3]);
          user.getBorrowedDocuments().add(doc);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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

  // Phương thức tạo nút với kiểu dáng đã được tùy chỉnh
  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setBackground(new Color(60, 116, 179, 255));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    return button;
  }
}
