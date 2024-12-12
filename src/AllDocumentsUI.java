package src;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AllDocumentsUI extends JFrame {
  private static final String TITLE = "Library - Tài liệu";
  private static final int WINDOW_WIDTH = 900;
  private static final int WINDOW_HEIGHT = 600;
  private static final Color HEADER_COLOR = new Color(0x4CAF50);
  private static final Color SELECTION_COLOR = new Color(0xA5D6A7);
  private static final Color ALTERNATE_ROW_COLOR = new Color(0xF1F8E9);

  private final JTable documentsTable;
  private final Library library;
  private final UserUI userUI;

  public AllDocumentsUI(Library library, UserUI userUI) {
    this.library = library;
    this.userUI = userUI;

    setupFrame();
    addReloadButton(); // Thêm nút reload

    documentsTable = createTable();
    setupTableAppearance();
    populateTableData();
    setupDetailButton();

    add(new JScrollPane(documentsTable), BorderLayout.CENTER);
    setVisible(true);
  }

  private void setupFrame() {
    setTitle(TITLE);
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  private JTable createTable() {
    String[] columnNames = {
        "ID", "Tên tài liệu", "Tác giả", "Số lượng",
        "ISBN", "Năm phát hành", "Thể loại", ""
    };
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(tableModel);

    // Lấy TableColumnModel để thay đổi kích thước các cột
    TableColumnModel columnModel = table.getColumnModel();

    // Đặt kích thước cột ID (cột đầu tiên) là nhỏ nhất
    columnModel.getColumn(0).setPreferredWidth(30); // Cột "ID"

    // Đặt kích thước các cột còn lại
    columnModel.getColumn(1).setPreferredWidth(200); // Cột "Tên tài liệu"
    columnModel.getColumn(2).setPreferredWidth(100); // Cột "Tác giả"
    columnModel.getColumn(3).setPreferredWidth(100); // Cột "Số lượng"
    columnModel.getColumn(4).setPreferredWidth(100); // Cột "ISBN"
    columnModel.getColumn(5).setPreferredWidth(100); // Cột "Năm phát hành"
    columnModel.getColumn(6).setPreferredWidth(100); // Cột "Thể loại"
    columnModel.getColumn(7).setPreferredWidth(100); // Cột "Chi tiết"

    return table;
  }

  private void setupTableAppearance() {
    // Header styling
    JTableHeader header = documentsTable.getTableHeader();
    header.setFont(new Font("Roboto", Font.BOLD, 16));
    header.setBackground(HEADER_COLOR);
    header.setForeground(Color.WHITE);

    // Table styling
    documentsTable.setRowHeight(30);
    documentsTable.setFont(new Font("Roboto", Font.PLAIN, 14));
    documentsTable.setSelectionBackground(SELECTION_COLOR);
    documentsTable.setSelectionForeground(Color.BLACK);

    // Alternate row colors
    documentsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
          c.setBackground(row % 2 == 0 ? Color.WHITE : ALTERNATE_ROW_COLOR);
        }
        return c;
      }
    });

  }

  private void populateTableData() {
    DefaultTableModel model = (DefaultTableModel) documentsTable.getModel();
    model.setRowCount(0); // xóa dữ liệu cũ

    //Load dữ liệu từ file
    List<Document> documents = library.getDocuments();

    //Thêm dữ liệu vào bảng
    for (Document doc : documents) {
      Object[] rowData = {
          doc.getId(),
          doc.getTitle(),
          doc.getAuthor(),
          doc.getQuantity(),
          doc.getIsbn(),
          doc.getPublicationYear(),
          doc.getGenre(),
          "Chi tiết"
      };
      model.addRow(rowData);
    }
  }

  private void setupDetailButton() {
    TableColumn detailColumn = documentsTable.getColumn("");
    detailColumn.setCellRenderer(new ButtonRenderer());
    detailColumn.setCellEditor(new ButtonEditor(new JCheckBox()
                                , library
                                , this)); // Truyền this làm parent
  }

  private void addReloadButton() {
    JButton reloadButton = new JButton("Reload");
    reloadButton.setFont(new Font("Roboto", Font.PLAIN, 14));
    reloadButton.setBackground(new Color(0x4CAF50));
    reloadButton.setForeground(Color.WHITE);
    reloadButton.setFocusPainted(false);
    reloadButton.addActionListener(e -> populateTableData()); // Gọi lại phương thức load dữ liệu
    add(reloadButton, BorderLayout.SOUTH); // Thêm nút vào cuối giao diện
  }

  // Getter để ButtonEditor lấy UserUI
  public UserUI getUserUI() {
    return userUI;
  }

  public void reload() {
    populateTableData(); // Tải lại danh sách tài liệu
  }

}