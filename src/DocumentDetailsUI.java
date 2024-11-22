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

    public DocumentDetailsUI(Document document, Library library) {
        this.document = document;
        this.library = library;
        
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
        
        // Document title header
        JLabel titleHeader = new JLabel(document.getTitle());
        titleHeader.setFont(TITLE_FONT);
        titleHeader.setForeground(PRIMARY_COLOR);
        gbc.gridwidth = 2;
        panel.add(titleHeader, gbc);
        
        // Add details fields
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
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            new EmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel ratingLabel = new JLabel("Đánh giá tài liệu:");
        ratingLabel.setFont(HEADER_FONT);
        panel.add(ratingLabel, gbc);
        
        JSlider ratingSlider = createStyledSlider();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(ratingSlider, gbc);
        
        JButton submitButton = createStyledButton("Gửi đánh giá");
        submitButton.addActionListener(e -> addReview(ratingSlider.getValue() / 2.0));
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(submitButton, gbc);
        
        return panel;
    }

    private JSlider createStyledSlider() {
        JSlider slider = new JSlider(0, 10, 5);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(CARD_COLOR);
        slider.setForeground(PRIMARY_COLOR);
        return slider;
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
            // Average rating
            double averageRating = reviews.stream()
                                        .mapToDouble(Review::getRating)
                                        .average()
                                        .orElse(0.0);
            
            JLabel avgLabel = createStyledLabel(
                String.format("Đánh giá trung bình: %.1f/5★", averageRating),
                HEADER_FONT
            );
            avgLabel.setForeground(PRIMARY_COLOR);
            avgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            reviewsPanel.add(avgLabel);
            reviewsPanel.add(Box.createVerticalStrut(15));
            
            // Individual reviews
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
        
        JLabel ratingLabel = createStyledLabel(
            String.format("%.1f/5★", review.getRating()),
            HEADER_FONT
        );
        ratingLabel.setForeground(PRIMARY_COLOR);
        
        JLabel commentLabel = createStyledLabel(review.getComment(), CONTENT_FONT);
        
        card.add(ratingLabel);
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
            Review newReview = new Review("người đánh giá", rating, comment.trim());
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