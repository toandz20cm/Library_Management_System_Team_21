import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.List;

public class UserListUI extends JFrame {
  private JTable userTable;
  private Library library;

  public UserListUI(Library library) {
    this.library = library;
    setTitle("Danh sách người dùng");
    setSize(800, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    // Tùy chỉnh giao diện tổng quan
    setLayout(new BorderLayout());

    // Tiêu đề giao diện
    JLabel headerLabel = new JLabel("Danh Sách Người Dùng", JLabel.CENTER);
    headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    headerLabel.setForeground(Color.WHITE);
    headerLabel.setOpaque(true);
    headerLabel.setBackground(new Color(0x1976D2));
    headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    add(headerLabel, BorderLayout.NORTH);

    // Tạo model cho bảng với các cột
    DefaultTableModel model = new DefaultTableModel(new Object[]{"Tên hiển thị"
        , "Ngày sinh", "Số điện thoại"}, 0);
    userTable = new JTable(model);

    // Tùy chỉnh header của bảng
    JTableHeader header = userTable.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 16));
    header.setBackground(new Color(0x2196F3));
    header.setForeground(Color.WHITE);
    header.setReorderingAllowed(false);

    // Tùy chỉnh bảng
    userTable.setRowHeight(35);
    userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    userTable.setSelectionBackground(new Color(0xBBDEFB));
    userTable.setSelectionForeground(Color.BLACK);
    userTable.setGridColor(new Color(0xBDBDBD));

    // Màu nền xen kẽ cho các hàng
    userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value
          , boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value
            , isSelected, hasFocus, row, column);
        if (!isSelected) {
          c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xE3F2FD));
        }
        setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nội dung
        return c;
      }
    });

    // Căn giữa tiêu đề các cột
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    header.setDefaultRenderer(headerRenderer);

    // Bọc bảng trong JScrollPane
    JScrollPane scrollPane = new JScrollPane(userTable);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(scrollPane, BorderLayout.CENTER);

    // Nạp dữ liệu người dùng vào bảng
    loadUserData(model);

    // Thêm footer
    JLabel footerLabel = new JLabel("© 2024 Library Management System", JLabel.CENTER);
    footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
    footerLabel.setForeground(new Color(0x616161));
    footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    add(footerLabel, BorderLayout.SOUTH);

    // Thêm footer với nút "Xem Chi Tiết"
    JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton detailButton = new JButton("Xem Chi Tiết");
    detailButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    detailButton.setBackground(new Color(0x64B5F6));
    detailButton.setForeground(Color.WHITE);

    detailButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
          String displayName = (String) userTable.getValueAt(selectedRow, 0);
          String username = getUsernameByDisplayName(displayName); // Tìm username

          if (username != null) {
            new UserDetailUI(library, username, displayName); // Mở giao diện chi tiết
          } else {
            JOptionPane.showMessageDialog(UserListUI.this, "Không tìm thấy username cho người dùng này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(UserListUI.this, "Vui lòng chọn một người dùng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    footerPanel.add(detailButton);
    add(footerPanel, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void loadUserData(DefaultTableModel model) {
    library.loadUsersFromFile();
    model.setRowCount(0); // Đảm bảo bảng trống trước khi thêm dữ liệu
    for (User user : library.getUsers()) {
      model.addRow(new Object[]{user.getDisplayName()
          , user.getBirthDate()
          , user.getPhoneNumber()});
    }
  }
  private String getUsernameByDisplayName(String displayName) {
    String filePath = "users.txt"; // File lưu danh sách người dùng
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
      // Đọc danh sách người dùng từ file
      List<User> users = (List<User>) ois.readObject();
      for (User user : users) {
        if (user.getDisplayName().equals(displayName)) {
          return user.getUsername(); // Trả về username nếu tìm thấy displayName khớp
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null; // Trả về null nếu không tìm thấy
  }
}
