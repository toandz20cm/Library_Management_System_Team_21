import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Class để hiển thị nút bấm trên JTable
class ButtonRenderer extends JButton implements TableCellRenderer {
    private static final Color BUTTON_BACKGROUND = new Color(0x2196F3); // Màu xanh lam
    private static final Color BUTTON_FOREGROUND = Color.WHITE; // Màu trắng

    public ButtonRenderer() {
        setOpaque(true);
        setupButtonStyle();
    }

    private void setupButtonStyle() {
        setBackground(BUTTON_BACKGROUND);
        setForeground(BUTTON_FOREGROUND);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        setText(value != null ? value.toString() : "");
        if (isSelected) {
            setBackground(new Color(0x1976D2)); // Màu xanh lam đậm hơn khi được chọn
        } else {
            setBackground(BUTTON_BACKGROUND);
        }
        return this;
    }
}

// Class để xử lý sự kiện khi nhấn nút trên JTable
class ButtonEditor extends DefaultCellEditor {
    private static final Color BUTTON_BACKGROUND = new Color(0x2196F3); // Màu xanh lam
    private static final Color BUTTON_FOREGROUND = Color.WHITE; // Màu trắng

    private final JButton button;
    private String label;
    private boolean isPushed;
    private final Library library;
    private final JFrame parent;

    public ButtonEditor(JCheckBox checkBox, Library library, JFrame parent) {
        super(checkBox);
        this.library = library;
        this.parent = parent;

        button = createStyledButton();
        setupButtonListener();
    }

    private JButton createStyledButton() {
        JButton btn = new JButton();
        btn.setOpaque(true);
        btn.setBackground(BUTTON_BACKGROUND);
        btn.setForeground(BUTTON_FOREGROUND);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupButtonListener() {
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        label = value != null ? value.toString() : "";
        button.setText(label);
        isPushed = true;

        handleDocumentDetails(table, row);

        return button;
    }

    private void handleDocumentDetails(JTable table, int row) {
        try {
            int documentId = (int) table.getValueAt(row, 0);
            Document document = library.getDocumentById(documentId);

            if (document != null) {
                openDocumentDetails(document);
            } else {
                showErrorMessage("Không tìm thấy tài liệu với ID: " + documentId);
            }
        } catch (Exception e) {
            showErrorMessage("Lỗi khi truy xuất thông tin tài liệu: " + e.getMessage());
        }
    }

    private void openDocumentDetails(Document document) {
        SwingUtilities.invokeLater(() -> new DocumentDetailsUI(document, library));
    }

    private void showErrorMessage(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        parent,
                        message,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                )
        );
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
