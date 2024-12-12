import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.*;
import java.awt.*;
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
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0x1565C0)); // Blue
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xE3F2FD)); // Light blue background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Thông tin người dùng
        JTextArea userInfoArea = new JTextArea();
        userInfoArea.setEditable(false);
        userInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userInfoArea.setBackground(Color.WHITE);
        userInfoArea.setForeground(new Color(0x37474F)); // Dark gray text

        JScrollPane userInfoScrollPane = new JScrollPane(userInfoArea);
        userInfoScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x0288D1)), "Thông Tin Người Dùng", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(0x0288D1)));
        mainPanel.add(userInfoScrollPane, BorderLayout.NORTH);

        // Bảng danh sách tài liệu mượn
        JTable borrowedTable = new JTable();
        borrowedTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedTable.setRowHeight(25);
        borrowedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowedTable.getTableHeader().setBackground(new Color(0x0288D1));
        borrowedTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(borrowedTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x0288D1)), "Danh Sách Tài Liệu Mượn", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(0x0288D1)));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Tải thông tin người dùng và danh sách tài liệu mượn
        loadUserDetails(userInfoArea, borrowedTable);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0x1565C0));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel footerLabel = new JLabel("Thư viện quản lý - 2024", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadUserDetails(JTextArea userInfoArea, JTable borrowedTable) {
        StringBuilder userDetails = new StringBuilder();

        // Đọc thông tin người dùng từ file users.txt sử dụng ObjectInputStream
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.txt"))) {
            List<User> users = (List<User>) ois.readObject();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    userDetails.append("👤 Tên tài khoản: ").append(user.getUsername()).append("\n");
                    userDetails.append("🔒 Mật khẩu: ").append(user.getPassword()).append("\n");
                    userDetails.append("🏷️ Tên hiển thị: ").append(user.getDisplayName()).append("\n");
                    userDetails.append("📅 Ngày sinh: ").append(user.getBirthDate()).append("\n");
                    userDetails.append("📞 Số điện thoại: ").append(user.getPhoneNumber()).append("\n\n");
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
