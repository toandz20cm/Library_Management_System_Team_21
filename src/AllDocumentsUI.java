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

    public AllDocumentsUI(Library library) {
        this.library = library;
        setupFrame();
        
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
        return new JTable(tableModel);
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
        List<Document> documents = library.getDocuments();
        
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
        detailColumn.setCellEditor(new ButtonEditor(new JCheckBox(), library, this));
    }
}