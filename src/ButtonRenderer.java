import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class ButtonRenderer extends JButton implements TableCellRenderer {
  private static final Color BUTTON_BACKGROUND = new Color(0x4CAF50);
  private static final Color BUTTON_FOREGROUND = Color.WHITE;

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
    return this;
  }
}

class ButtonEditor extends DefaultCellEditor {
  private static final Color BUTTON_BACKGROUND = new Color(0x4CAF50);
  private static final Color BUTTON_FOREGROUND = Color.WHITE;

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
    SwingUtilities.invokeLater(() -> {
      if (parent instanceof AllDocumentsUI allDocumentsUI) { // Ép kiểu parent
        UserUI userUI = allDocumentsUI.getUserUI(); // Lấy UserUI từ AllDocumentsUI
        new DocumentDetailsUI(document, library, userUI, allDocumentsUI);
      }
    });
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