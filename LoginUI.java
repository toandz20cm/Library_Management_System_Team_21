import java.text.ParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;

class RoundedButton extends JButton {
  public RoundedButton(String text) {
    super(text);
    setContentAreaFilled(false);
    setFocusPainted(false);
    setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Tô màu nền và bo góc
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Độ bo góc (30, 30)

    // Vẽ chữ trên nút
    super.paintComponent(g);
    g2.dispose();
  }
}

public class LoginUI extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private RoundedButton loginButton;
  private RoundedButton registerButton;
  private RoundedButton adminLoginButton;
  private Library library;

  public LoginUI(Library library) {
    this.library = library;

    setTitle("Library Management System - Đăng nhập");
    setSize(400, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    getContentPane().setBackground(new Color(34, 34, 34)); // Màu nền tối

    // Panel chứa ảnh logo và dòng chào mừng
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBackground(new Color(34, 34, 34)); // Màu nền tối

    // Thêm ảnh logo
    ImageIcon originalIcon = new ImageIcon("image/1.jpg"); // Đặt đường dẫn ảnh
    Image scaledImage = originalIcon.getImage().getScaledInstance(380, 150, Image.SCALE_SMOOTH); // Chỉnh kích thước ảnh cho phù hợp
    JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
    logoLabel.setHorizontalAlignment(JLabel.CENTER);
    headerPanel.add(logoLabel, BorderLayout.NORTH);

    // Thêm dòng chào mừng
    JLabel welcomeLabel = new JLabel("Chào mừng đến với ứng dụng Quản lý Thư viện", JLabel.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
    welcomeLabel.setForeground(new Color(173, 216, 230)); // Màu xanh nhạt
    headerPanel.add(welcomeLabel, BorderLayout.SOUTH);

    add(headerPanel, BorderLayout.NORTH);

    // Panel chứa các ô nhập liệu và các nút
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    inputPanel.setBackground(new Color(34, 34, 34));
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Ô nhập tên đăng nhập với hiệu ứng placeholder
    usernameField = createPlaceholderField("Username");
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    inputPanel.add(usernameField, gbc);

    // Ô nhập mật khẩu với hiệu ứng placeholder
    passwordField = createPlaceholderPasswordField("Password");
    gbc.gridy = 1;
    inputPanel.add(passwordField, gbc);

    // Nút đăng nhập
    loginButton = new RoundedButton("Login");
    loginButton.setBackground(new Color(133, 133, 133));
    loginButton.setForeground(Color.LIGHT_GRAY);
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    inputPanel.add(loginButton, gbc);

    // Nút đăng ký
    registerButton = new RoundedButton("Register");
    registerButton.setBackground(new Color(133, 133, 133));
    registerButton.setForeground(Color.LIGHT_GRAY);
    gbc.gridy = 3;
    inputPanel.add(registerButton, gbc);

    // Nút đăng nhập với vai trò quản trị viên
    adminLoginButton = new RoundedButton("Admin Login");
    adminLoginButton.setBackground(new Color(133, 133, 133));
    adminLoginButton.setForeground(Color.LIGHT_GRAY);
    gbc.gridy = 4;
    inputPanel.add(adminLoginButton, gbc);

    add(inputPanel, BorderLayout.CENTER);

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (validateCredentials(username, password)) {
          User user = library.authenticateUser(username, password);
          if (user != null) {
            new UserUI(library, user).setVisible(true);
            dispose();
          } else {
            JOptionPane.showMessageDialog(LoginUI.this, "Sai tên đăng nhập hoặc mật khẩu!");
          }
        }
      }
    });

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Mở hộp thoại để nhập tất cả thông tin đăng ký
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField displayNameField = new JTextField();
        JTextField birthDateField = new JTextField();
        JTextField phoneNumberField = new JTextField();

        Object[] message = {
            "Username: (ít nhất 6 ký tự)", usernameField,
            "Password: (ít nhất 6 ký tự)", passwordField,
            "Tên hiển thị:", displayNameField,
            "Ngày sinh (dd/MM/yyyy):", birthDateField,
            "Số điện thoại: (chỉ chứa số và 10 chữ số)", phoneNumberField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Đăng ký tài khoản mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
          String username = usernameField.getText();
          String password = new String(passwordField.getPassword());
          String displayName = displayNameField.getText();
          String birthDate = birthDateField.getText();
          String phoneNumber = phoneNumberField.getText();

          // Kiểm tra độ dài của username và password
          if (username.length() < 6 || password.length() < 6) {
            JOptionPane.showMessageDialog(LoginUI.this, "Tên đăng nhập và mật khẩu phải có ít nhất 6 ký tự!");
            return;
          }

          // Kiểm tra tính hợp lệ của ngày sinh và số điện thoại
          if (!isValidBirthDate(birthDate)) {
            JOptionPane.showMessageDialog(LoginUI.this, "Ngày sinh không hợp lệ! Vui lòng nhập đúng định dạng dd/MM/yyyy.");
            return;
          }
          if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(LoginUI.this, "Số điện thoại không hợp lệ!");
            return;
          }

          // Kiểm tra xem tài khoản đã tồn tại chưa
          if (library.authenticateUser(username, password) == null) {
            User newUser = new User(username, password, displayName, birthDate, phoneNumber);
            library.addUser(newUser);
            JOptionPane.showMessageDialog(LoginUI.this, "Đăng ký thành công!");
          } else {
            JOptionPane.showMessageDialog(LoginUI.this, "Tài khoản đã tồn tại!");
          }
        } else {
          JOptionPane.showMessageDialog(LoginUI.this, "Hủy đăng ký!");
        }
      }
    });

    adminLoginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.equals("admin") && password.equals("1")) {
          new AdminUI(library).setVisible(true);
          dispose();
        } else {
          JOptionPane.showMessageDialog(LoginUI.this, "Sai thông tin quản trị viên!");
        }
      }
    });
  }

  // Tạo ô nhập với placeholder
  private JTextField createPlaceholderField(String placeholder) {
    JTextField textField = new JTextField(20);
    textField.setBackground(new Color(34, 34, 34));
    textField.setForeground(Color.GRAY);
    textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    textField.setText(placeholder);

    textField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if (textField.getText().equals(placeholder)) {
          textField.setText("");
        }
        textField.setForeground(Color.BLACK);
        textField.setBackground(new Color(169, 174, 190));
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (textField.getText().isEmpty()) {
          textField.setText(placeholder);
          textField.setForeground(Color.GRAY);
          textField.setBackground(new Color(34, 34, 34));
        }
      }
    });
    return textField;
  }

  // Tạo ô nhập mật khẩu với placeholder
  private JPasswordField createPlaceholderPasswordField(String placeholder) {
    JPasswordField passwordField = new JPasswordField(20);
    passwordField.setBackground(new Color(34, 34, 34));
    passwordField.setForeground(Color.GRAY);
    passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    passwordField.setEchoChar((char) 0);
    passwordField.setText(placeholder);

    passwordField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if (new String(passwordField.getPassword()).equals(placeholder)) {
          passwordField.setText("");
          passwordField.setEchoChar('\u2022');
        }
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(169, 174, 190));
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (passwordField.getPassword().length == 0) {
          passwordField.setEchoChar((char) 0);
          passwordField.setText(placeholder);
          passwordField.setForeground(Color.GRAY);
          passwordField.setBackground(new Color(34, 34, 34));
        }
      }
    });
    return passwordField;
  }

  // Hàm kiểm tra tính hợp lệ của ngày sinh (định dạng dd/MM/yyyy)
  private boolean isValidBirthDate(String birthDate) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      sdf.setLenient(false);
      sdf.parse(birthDate);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  // Hàm kiểm tra tính hợp lệ của số điện thoại (chỉ chứa số và 10 chữ số)
  private boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber.matches("\\d{10}");
  }

  private boolean validateCredentials(String username, String password) {
    if (username.length() < 6 || password.length() < 6) {
      JOptionPane.showMessageDialog(this, "Tên đăng nhập và mật khẩu phải có ít nhất 6 ký tự!");
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    Library library = new Library();
    new LoginUI(library).setVisible(true);
  }
}
