package src;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class UserDetailUI extends JFrame {
  private String username;
  private String displayName;

  public UserDetailUI(Library library, String username, String displayName) {
    this.username = username;
    this.displayName = displayName;

    setTitle("Chi tiết người dùng: " + displayName);
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Header
    JLabel headerLabel = new JLabel("Thông Tin Người Dùng", JLabel.CENTER);
    headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    headerLabel.setOpaque(true);
    headerLabel.setBackground(new Color(0x1976D2));
    headerLabel.setForeground(Color.WHITE);
    add(headerLabel, BorderLayout.NORTH);

    // Panel chính
    JPanel mainPanel = new JPanel(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);

    // Thông tin người dùng
    JTextArea userInfoArea = new JTextArea();
    userInfoArea.setEditable(false);
    userInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JScrollPane userInfoScrollPane = new JScrollPane(userInfoArea);
    userInfoScrollPane.setBorder(BorderFactory.createTitledBorder("Thông Tin Người Dùng"));
    mainPanel.add(userInfoScrollPane, BorderLayout.NORTH);

    // Bảng danh sách tài liệu mượn
    JTable borrowedTable = new JTable();
    borrowedTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JScrollPane tableScrollPane = new JScrollPane(borrowedTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Tài Liệu Mượn"));
    mainPanel.add(tableScrollPane, BorderLayout.CENTER);

    // Tải thông tin người dùng và danh sách tài liệu mượn
    loadUserDetails(userInfoArea, borrowedTable);

    setVisible(true);
  }

  private void loadUserDetails(JTextArea userInfoArea, JTable borrowedTable) {
    StringBuilder userDetails = new StringBuilder();

    // Đọc thông tin người dùng từ file users.txt sử dụng ObjectInputStream
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.txt"))) {
      List<User> users = (List<User>) ois.readObject();
      for (User user : users) {
        if (user.getUsername().equals(username)) {
          userDetails.append("Tên tài khoản: ").append(user.getUsername()).append("\n");
          userDetails.append("Mật khẩu: ").append(user.getPassword()).append("\n");
          userDetails.append("Tên hiển thị: ").append(user.getDisplayName()).append("\n");
          userDetails.append("Ngày sinh: ").append(user.getBirthDate()).append("\n");
          userDetails.append("Số điện thoại: ").append(user.getPhoneNumber()).append("\n\n");
          break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      userDetails.append("Không thể tải thông tin người dùng.\n");
      e.printStackTrace();
    }


    // Hiển thị thông tin người dùng
    userInfoArea.setText(userDetails.toString());

    // Đọc danh sách tài liệu mượn từ file username_borrowed.txt sử dụng ObjectInputStream
    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Tên tài liệu", "Tác giả", "Thể loại"}, 0);
    borrowedTable.setModel(tableModel);

    String filePath = username + "_borrowed.txt";
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
      List<Document> borrowedDocuments = (List<Document>) ois.readObject();
      for (Document doc : borrowedDocuments) {
        tableModel.addRow(new Object[]{doc.getTitle(), doc.getAuthor(), doc.getGenre()});
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Không thể đọc danh sách tài liệu mượn hoặc file không tồn tại.");
      e.printStackTrace();
    }
  }
}
