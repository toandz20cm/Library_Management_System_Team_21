package src;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DocumentDetailsUI extends JFrame {
  private static final Color PRIMARY_COLOR = new Color(63, 81, 181);
  private static final Color SECONDARY_COLOR = new Color(121, 134, 203);
  private static final Color BACKGROUND_COLOR = new Color(237, 241, 245);
  private static final Color CARD_COLOR = new Color(255, 255, 255);
  private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
  private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
  private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
  private static final int PADDING = 20;

  private final Document document;
  private final Library library;
  private final JPanel reviewsPanel;
  private final UserUI userUI;
  private final AllDocumentsUI allDocumentsUI;

  private int selectedRating = 0; // Lưu số sao được chọn

  public DocumentDetailsUI(Document document, Library library
      , UserUI userUI, AllDocumentsUI allDocumentsUI) {
    this.document = document;
    this.library = library;
    this.userUI = userUI;
    this.allDocumentsUI = allDocumentsUI;

    setupFrame();

    // Main container with shadow border
    JPanel mainContainer = new JPanel(new BorderLayout(0, PADDING));
    mainContainer.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    mainContainer.setBackground(BACKGROUND_COLOR);

    // Document details panel
    JPanel detailsCard = createDetailsPanel();
    mainContainer.add(detailsCard, BorderLayout.NORTH);

    // Reviews panel
    reviewsPanel = new JPanel();
    reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
    reviewsPanel.setBackground(CARD_COLOR);
    JScrollPane scrollPane = createReviewsScrollPane();
    mainContainer.add(scrollPane, BorderLayout.CENTER);

    // Rating input panel
    JPanel ratingCard = createRatingPanel();
    mainContainer.add(ratingCard, BorderLayout.SOUTH);

    add(mainContainer);
    displayReviews();
    setVisible(true);
  }

  private void setupFrame() {
    setTitle("Chi tiết tài liệu - " + document.getTitle());
    setSize(600, 700);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setBackground(BACKGROUND_COLOR);
  }

  private JPanel createDetailsPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(CARD_COLOR);
    panel.setBorder(BorderFactory.createCompoundBorder(
        new SoftBevelBorder(SoftBevelBorder.RAISED),
        new EmptyBorder(PADDING, PADDING, PADDING, PADDING)
    ));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 5, 5, 15);

    JLabel titleHeader = new JLabel(document.getTitle());
    titleHeader.setFont(TITLE_FONT);
    titleHeader.setForeground(PRIMARY_COLOR);
    titleHeader.setPreferredSize(new Dimension(250, 40)); // Đảm bảo không gian đủ rộng
    titleHeader.setMaximumSize(new Dimension(250, 40)); // Giới hạn kích thước tối đa
    titleHeader.setHorizontalAlignment(JLabel.LEFT); // Căn chỉnh văn bản
    gbc.gridwidth = 2;
    panel.add(titleHeader, gbc);

    // Chi tiết tài liệu
    addDetailField(panel, "ID:", String.valueOf(document.getId()), ++gbc.gridy, gbc);
    addDetailField(panel, "Tác giả:", document.getAuthor(), ++gbc.gridy, gbc);
    addDetailField(panel, "Số lượng:"
        , String.valueOf(document.getQuantity()), ++gbc.gridy, gbc);
    addDetailField(panel, "ISBN:", document.getIsbn(), ++gbc.gridy, gbc);
    addDetailField(panel, "Năm phát hành:"
        , String.valueOf(document.getPublicationYear()), ++gbc.gridy, gbc);
    addDetailField(panel, "Thể loại:", document.getGenre(), ++gbc.gridy, gbc);

    // Nút mượn tài liệu
    JButton borrowButton = new JButton("Mượn Tài Liệu");
    borrowButton.setFont(CONTENT_FONT);
    borrowButton.setBackground(PRIMARY_COLOR);
    borrowButton.setForeground(Color.WHITE);
    borrowButton.setFocusPainted(false);
    borrowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Vô hiệu hóa nút nếu người dùng hiện tại là admin
    borrowButton.setEnabled(library.getCurrentUser() != null);

    borrowButton.addActionListener(e -> {
      if (document.getQuantity() > 0) {
        User currentUser = library.getCurrentUser();
        if (currentUser != null) {
          // Thêm tài liệu vào danh sách tài liệu đã mượn
          currentUser.getBorrowedDocuments().add(document);
          document.setQuantity(document.getQuantity() - 1);

          // Lưu dữ liệu vào file
          saveLibraryToFile();
          saveBorrowedDocumentsToFile(currentUser);

          JOptionPane.showMessageDialog(
              this,
              "Mượn tài liệu thành công!",
              "Thành công",
              JOptionPane.INFORMATION_MESSAGE
          );

          // Gọi phương thức reload trên UserUI và AllDocumentsUI
          if (userUI != null) userUI.reload();
          if (allDocumentsUI != null) allDocumentsUI.reload();

          // Cập nhật lại giao diện
          displayReviews();

          dispose(); // Đóng cửa sổ DocumentDetailsUI
        }
      } else {
        JOptionPane.showMessageDialog(
            this,
            "Tài liệu không còn sẵn có!",
            "Thông báo",
            JOptionPane.WARNING_MESSAGE
        );
      }
    });

    // Thêm nút vào panel
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(borrowButton, gbc);

    return panel;
  }

  private void addDetailField(JPanel panel, String label
      , String value, int row, GridBagConstraints gbc) {
    gbc.gridx = 0;
    gbc.gridwidth = 1;
    JLabel labelComp = new JLabel(label);
    labelComp.setFont(HEADER_FONT);
    panel.add(labelComp, gbc);

    gbc.gridx = 1;
    JLabel valueComp = new JLabel(value);
    valueComp.setFont(CONTENT_FONT);
    panel.add(valueComp, gbc);
  }

  private JScrollPane createReviewsScrollPane() {
    JScrollPane scrollPane = new JScrollPane(reviewsPanel);
    scrollPane.setBorder(BorderFactory.createCompoundBorder(
        new TitledBorder(null, "Đánh giá và bình luận"
            , TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, PRIMARY_COLOR),
        new EmptyBorder(10, 10, 10, 10)
    ));
    scrollPane.getViewport().setBackground(CARD_COLOR);
    return scrollPane;
  }

  private JPanel createRatingPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(CARD_COLOR);
    panel.setBorder(BorderFactory.createCompoundBorder(
        new SoftBevelBorder(SoftBevelBorder.RAISED),
        new EmptyBorder(PADDING, PADDING, PADDING, PADDING)
    ));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel ratingLabel = new JLabel("Đánh giá tài liệu:");
    ratingLabel.setFont(HEADER_FONT);
    panel.add(ratingLabel, gbc);

    // Add 5 star icons
    JLabel[] stars = new JLabel[5];
    for (int i = 0; i < stars.length; i++) {
      ImageIcon emptyStar = new ImageIcon("image/Empty Star.png");
      Image scaledEmptyStar = emptyStar.getImage().getScaledInstance(32, 32
          , Image.SCALE_SMOOTH);
      stars[i] = new JLabel(new ImageIcon(scaledEmptyStar));
      stars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

      final int index = i; // lưu chỉ số sao
      stars[i].addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          updateStars(stars, index + 1);
        }
      });

      gbc.gridx = i + 1;
      panel.add(stars[i], gbc);
    }

    JButton submitButton = createStyledButton("Gửi đánh giá");
    submitButton.addActionListener(e -> {
      if (selectedRating > 0) {
        addReview(selectedRating);
      } else {
        JOptionPane.showMessageDialog(this,
            "Vui lòng chọn ít nhất 1 sao!",
            "Lỗi",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    gbc.gridx = stars.length + 1;
    panel.add(submitButton, gbc);

    return panel;
  }

  private void updateStars(JLabel[] stars, int rating) {
    this.selectedRating = rating;
    for (int i = 0; i < stars.length; i++) {
      if (i < rating) {
        ImageIcon fullStar = new ImageIcon("image/Full Star.png");
        Image scaledFullStar = fullStar.getImage().getScaledInstance(32, 32
            , Image.SCALE_SMOOTH);
        stars[i].setIcon(new ImageIcon(scaledFullStar));
      } else {
        ImageIcon emptyStar = new ImageIcon("image/Empty Star.png");
        Image scaledEmptyStar = emptyStar.getImage().getScaledInstance(32, 32
            , Image.SCALE_SMOOTH);
        stars[i].setIcon(new ImageIcon(scaledEmptyStar));
      }
    }
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(HEADER_FONT);
    button.setBackground(PRIMARY_COLOR);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        button.setBackground(SECONDARY_COLOR);
      }
      public void mouseExited(MouseEvent e) {
        button.setBackground(PRIMARY_COLOR);
      }
    });
    return button;
  }

  private void displayReviews() {
    reviewsPanel.removeAll();
    List<Review> reviews = library.getReviewsForDocument(document.getId());

    if (reviews.isEmpty()) {
      JLabel noReviewLabel = createStyledLabel("Chưa có đánh giá nào.", CONTENT_FONT);
      noReviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      reviewsPanel.add(noReviewLabel);
    } else {
      // Tính toán đánh giá trung bình
      double averageRating = reviews.stream()
          .mapToDouble(Review::getRating)
          .average()
          .orElse(0.0);

      // Panel hiển thị đánh giá trung bình
      JPanel averagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
      averagePanel.setBackground(CARD_COLOR);
      averagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

      // Hiển thị số điểm trung bình
      JLabel avgLabel = createStyledLabel(
          String.format("Đánh giá trung bình: %.1f/5", averageRating), HEADER_FONT);
      avgLabel.setForeground(PRIMARY_COLOR);
      averagePanel.add(avgLabel);

      // Thêm biểu tượng sao đầy
      ImageIcon fullStar = new ImageIcon("image/Full Star.png");
      Image scaledFullStar = fullStar.getImage().getScaledInstance(16, 16
          , Image.SCALE_SMOOTH);
      JLabel starLabel = new JLabel(new ImageIcon(scaledFullStar));
      averagePanel.add(starLabel);

      reviewsPanel.add(averagePanel);
      reviewsPanel.add(Box.createVerticalStrut(15));

      // Hiển thị các đánh giá cá nhân
      for (Review review : reviews) {
        JPanel reviewCard = createReviewCard(review);
        reviewCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewsPanel.add(reviewCard);
        reviewsPanel.add(Box.createVerticalStrut(10));
      }
    }

    reviewsPanel.revalidate();
    reviewsPanel.repaint();
  }

  private JPanel createReviewCard(Review review) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(new Color(245, 245, 245));
    card.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(new Color(230, 230, 230), 1, true),
        new EmptyBorder(10, 10, 10, 10)
    ));

    // Hiển thị tên người đánh giá
    JLabel userLabel = createStyledLabel("Người đánh giá: "
        + review.getUsername(), CONTENT_FONT);
    userLabel.setForeground(new Color(100, 100, 100));
    userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Hiển thị đánh giá bằng biểu tượng sao
    JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    starsPanel.setBackground(new Color(245, 245, 245));
    starsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    for (int i = 0; i < (int) review.getRating(); i++) {
      ImageIcon fullStar = new ImageIcon("image/Full Star.png");
      Image scaledFullStar = fullStar.getImage().getScaledInstance(16, 16
          , Image.SCALE_SMOOTH);
      JLabel starLabel = new JLabel(new ImageIcon(scaledFullStar));
      starsPanel.add(starLabel);
    }

    // Hiển thị bình luận
    JLabel commentLabel = createStyledLabel(review.getComment(), CONTENT_FONT);
    commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Thêm các thành phần vào card
    card.add(userLabel);
    card.add(Box.createVerticalStrut(5));
    card.add(starsPanel); // Đảm bảo thẳng hàng với phần userLabel
    card.add(Box.createVerticalStrut(5));
    card.add(commentLabel);

    return card;
  }

  private JLabel createStyledLabel(String text, Font font) {
    JLabel label = new JLabel(text);
    label.setFont(font);
    return label;
  }

  private void addReview(double rating) {
    String comment = JOptionPane.showInputDialog(this,
        "Nhập bình luận của bạn:",
        "Thêm đánh giá",
        JOptionPane.PLAIN_MESSAGE);

    if (comment != null && !comment.trim().isEmpty()) {
      // Lấy tên hiển thị của người đang đăng nhập
      String currentUserDisplayName = library.getCurrentUserDisplayName();

      Review newReview = new Review(currentUserDisplayName, rating, comment.trim());

      // Lưu đánh giá
      library.addReview(document.getId(), newReview);
      displayReviews(); // Hiển thị lại danh sách đánh giá
    } else if (comment != null) {
      JOptionPane.showMessageDialog(this,
          "Bình luận không được để trống.",
          "Lỗi",
          JOptionPane.ERROR_MESSAGE);
    }
  }
  // Lưu danh sách tài liệu vào file "documents.txt"
  private void saveLibraryToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("documents.txt"))) {
      oos.writeObject(library.getDocuments()); // Lưu danh sách tài liệu
      System.out.println("Lưu danh sách tài liệu thành công.");
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this
          , "Lỗi khi lưu danh sách tài liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Lưu danh sách tài liệu đã mượn của người dùng vào file "username_borrowed.txt"
  private void saveBorrowedDocumentsToFile(User user) {
    String fileName = user.getUsername() + "_borrowed.txt";
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
      oos.writeObject(user.getBorrowedDocuments());
      System.out.println("Lưu danh sách tài liệu đã mượn thành công.");
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this
          , "Lỗi khi lưu danh sách tài liệu đã mượn!"
          , "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
  }
}