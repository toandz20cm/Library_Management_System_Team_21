import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DocumentDetailsUI extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Xanh dương chính
    private static final Color SECONDARY_COLOR = new Color(30, 136, 229); // Xanh đậm hơn
    private static final Color BACKGROUND_COLOR = new Color(236, 239, 241); // Nền xám nhạt
    private static final Color CARD_COLOR = Color.WHITE; // Màu trắng cho thẻ
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int PADDING = 20;

    private final Document document;
    private final Library library;
    private final JPanel reviewsPanel;

    private int selectedRating = 0;

    public DocumentDetailsUI(Document document, Library library) {
        this.document = document;
        this.library = library;

        setupFrame();

        JPanel mainContainer = new JPanel(new BorderLayout(0, PADDING));
        mainContainer.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainContainer.setBackground(BACKGROUND_COLOR);

        JPanel detailsCard = createDetailsPanel();
        mainContainer.add(detailsCard, BorderLayout.NORTH);

        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        reviewsPanel.setBackground(CARD_COLOR);
        JScrollPane scrollPane = createReviewsScrollPane();
        mainContainer.add(scrollPane, BorderLayout.CENTER);

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
            new LineBorder(PRIMARY_COLOR, 2, true),
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
        gbc.gridwidth = 2;
        panel.add(titleHeader, gbc);

        addDetailField(panel, "ID:", String.valueOf(document.getId()), ++gbc.gridy, gbc);
        addDetailField(panel, "Tác giả:", document.getAuthor(), ++gbc.gridy, gbc);
        addDetailField(panel, "Số lượng:", String.valueOf(document.getQuantity()), ++gbc.gridy, gbc);
        addDetailField(panel, "ISBN:", document.getIsbn(), ++gbc.gridy, gbc);
        addDetailField(panel, "Năm phát hành:", String.valueOf(document.getPublicationYear()), ++gbc.gridy, gbc);
        addDetailField(panel, "Thể loại:", document.getGenre(), ++gbc.gridy, gbc);

        return panel;
    }

    private void addDetailField(JPanel panel, String label, String value, int row, GridBagConstraints gbc) {
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
            new TitledBorder(null, "Đánh giá và bình luận", TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, PRIMARY_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        return scrollPane;
    }

    private JPanel createRatingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2, true),
            new EmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel ratingLabel = new JLabel("Đánh giá tài liệu:");
        ratingLabel.setFont(HEADER_FONT);
        panel.add(ratingLabel, gbc);

        JLabel[] stars = new JLabel[5];
        for (int i = 0; i < stars.length; i++) {
            ImageIcon emptyStar = new ImageIcon("image/Empty Star.png");
            Image scaledEmptyStar = emptyStar.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            stars[i] = new JLabel(new ImageIcon(scaledEmptyStar));
            stars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            final int index = i;
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
            ImageIcon icon = new ImageIcon("image/" + (i < rating ? "Full Star.png" : "Empty Star.png"));
            Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            stars[i].setIcon(new ImageIcon(scaledIcon));
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
            double averageRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);

            JPanel averagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            averagePanel.setBackground(CARD_COLOR);
            averagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel avgLabel = createStyledLabel(String.format("Đánh giá trung bình: %.1f/5", averageRating), HEADER_FONT);
            avgLabel.setForeground(PRIMARY_COLOR);
            averagePanel.add(avgLabel);

            ImageIcon fullStar = new ImageIcon("image/Full Star.png");
            Image scaledFullStar = fullStar.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JLabel starLabel = new JLabel(new ImageIcon(scaledFullStar));
            averagePanel.add(starLabel);

            reviewsPanel.add(averagePanel);
            reviewsPanel.add(Box.createVerticalStrut(15));

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
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel userLabel = createStyledLabel("Người đánh giá: " + review.getUsername(), CONTENT_FONT);
        userLabel.setForeground(new Color(100, 100, 100));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starsPanel.setBackground(CARD_COLOR);
        starsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < (int) review.getRating(); i++) {
            ImageIcon fullStar = new ImageIcon("image/Full Star.png");
            Image scaledFullStar = fullStar.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JLabel starLabel = new JLabel(new ImageIcon(scaledFullStar));
            starsPanel.add(starLabel);
        }

        JLabel commentLabel = createStyledLabel(review.getComment(), CONTENT_FONT);
        commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(starsPanel);
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
            String currentUserDisplayName = library.getCurrentUserDisplayName();
            Review newReview = new Review(currentUserDisplayName, rating, comment.trim());
            library.addReview(document.getId(), newReview);
            displayReviews();
        } else if (comment != null) {
            JOptionPane.showMessageDialog(this,
                "Bình luận không được để trống.",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
