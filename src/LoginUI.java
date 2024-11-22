import java.text.ParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        setForeground(Color.WHITE);
        setBackground(new Color(79, 121, 255));
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, getBackground(),
            getWidth(), getHeight(), getBackground().darker()
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        // Draw slight shadow
        g2.setColor(new Color(0, 0, 0, 50));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        
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

    setTitle("Library Management System");
    setSize(450, 650);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(0, 20));
    getContentPane().setBackground(new Color(245, 245, 250));

    // Header Panel
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BorderLayout(0, 15));
    headerPanel.setBackground(new Color(245, 245, 250));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

    // Logo
    ImageIcon originalIcon = new ImageIcon("image/1.jpg");
    Image scaledImage = originalIcon.getImage().getScaledInstance(400, 160, Image.SCALE_SMOOTH);
    JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
    logoLabel.setHorizontalAlignment(JLabel.CENTER);
    headerPanel.add(logoLabel, BorderLayout.NORTH);

    // Welcome Text
    JLabel welcomeLabel = new JLabel("Welcome to Library Management System", JLabel.CENTER);
    welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    welcomeLabel.setForeground(new Color(50, 50, 50));
    headerPanel.add(welcomeLabel, BorderLayout.SOUTH);

    add(headerPanel, BorderLayout.NORTH);

    // Main Content Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridBagLayout());
    mainPanel.setBackground(new Color(245, 245, 250));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 30, 10, 30);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    // Input fields styling
    usernameField = createStyledTextField("Username");
    passwordField = createStyledPasswordField("Password");

    // Add components with proper spacing
    gbc.gridy = 0;
    mainPanel.add(usernameField, gbc);

    gbc.gridy = 1;
    mainPanel.add(passwordField, gbc);

    // Button styling
    loginButton = createStyledButton("Login", new Color(79, 121, 255));
    registerButton = createStyledButton("Register", new Color(92, 184, 92));
    adminLoginButton = createStyledButton("Admin Login", new Color(41, 128, 185));

    gbc.gridy = 2;
    gbc.insets = new Insets(25, 30, 10, 30);
    mainPanel.add(loginButton, gbc);

    gbc.gridy = 3;
    gbc.insets = new Insets(10, 30, 10, 30);
    mainPanel.add(registerButton, gbc);

    gbc.gridy = 4;
    mainPanel.add(adminLoginButton, gbc);

    // Add padding panel
    JPanel paddingPanel = new JPanel();
    paddingPanel.setBackground(new Color(245, 245, 250));
    gbc.gridy = 5;
    gbc.weighty = 1.0;
    mainPanel.add(paddingPanel, gbc);

    add(mainPanel, BorderLayout.CENTER);

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
  private JTextField createStyledTextField(String placeholder) {
    JTextField field = new JTextField(20);
    field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    field.setForeground(new Color(120, 120, 120));
    field.setBackground(Color.WHITE);
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    field.setText(placeholder);

    // Focus listener giữ nguyên với màu sắc mới
    field.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
            }
            field.setForeground(new Color(50, 50, 50));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79, 121, 255), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(new Color(120, 120, 120));
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        }
    });
    return field;
}

private JPasswordField createStyledPasswordField(String placeholder) {
    JPasswordField field = new JPasswordField(20);
    field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    field.setForeground(new Color(120, 120, 120));
    field.setBackground(Color.WHITE);
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    field.setEchoChar((char) 0);
    field.setText(placeholder);

    // Focus listener với màu sắc mới
    field.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (new String(field.getPassword()).equals(placeholder)) {
                field.setText("");
                field.setEchoChar('•');
            }
            field.setForeground(new Color(50, 50, 50));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79, 121, 255), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (field.getPassword().length == 0) {
                field.setText(placeholder);
                field.setEchoChar((char) 0);
                field.setForeground(new Color(120, 120, 120));
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        }
    });
    return field;
}

private RoundedButton createStyledButton(String text, Color baseColor) {
    RoundedButton button = new RoundedButton(text);
    button.setBackground(baseColor);
    button.setForeground(Color.WHITE);
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(baseColor.darker());
        }
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(baseColor);
        }
    });
    return button;
    }

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
