import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonRenderer extends JButton implements TableCellRenderer {
  public ButtonRenderer() {
    setOpaque(true);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setText((value == null) ? "" : value.toString());
    return this;
  }
}

class ButtonEditor extends DefaultCellEditor {
  protected JButton button;
  private String label;
  private boolean isPushed;
  private Library library;
  private JFrame parent;

  public ButtonEditor(JCheckBox checkBox, Library library, JFrame parent) {
    super(checkBox);
    this.library = library;
    this.parent = parent;
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
      }
    });
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    label = (value == null) ? "" : value.toString();
    button.setText(label);
    isPushed = true;

    // Lấy tài liệu từ thư viện theo ID
    int documentId = (int) table.getValueAt(row, 0);
    Document document = library.getDocumentById(documentId);

    if (document != null) {
      // Mở giao diện chi tiết tài liệu sau khi kết thúc việc chỉnh sửa
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          new DocumentDetailsUI(document, library);  // Nếu `DocumentDetailsUI` cần `library`
        }
      });
    }

    return button;
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

  @Override
  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }
}
