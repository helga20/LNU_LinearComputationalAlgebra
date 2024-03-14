import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class ApplicationMainForm {
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JButton importMatrixFromFileButton;
    private JButton generateNewMatrixButton;
    private JButton exportMatrixIntoFileButton;
    private JButton generateBandMatrixButton;
    private JButton exportBandMatrixIntoButton;
    private JTable shortFormMatrixTable;
    private JTable longFormMatrixTable;
    private JTable shortFormBandMatrixTable;
    private JTable longFormBandMatrixTable;
    public JPanel MainPanel;

    private SparseMatrix sparseMatrix;
    private SparseMatrix bandSparseMatrix;

    private boolean _ProgrammaticChange = false;

    public ApplicationMainForm() {
        class CustomCellRenderer extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ((value instanceof Double && (Double) value != 0) || (value instanceof Integer && (Integer) value != 0)) {
                    c.setBackground(Color.RED);
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        }

        this.longFormBandMatrixTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        this.longFormMatrixTable.setDefaultRenderer(Object.class, new CustomCellRenderer());


        this.importMatrixFromFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ApplicationMainForm.this.setSparseMatrix(SparseMatrix.fromFile(selectedFile.getAbsolutePath()));
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(
                        null,
                        exc.getMessage(),
                        "Matrix error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        this.generateNewMatrixButton.addActionListener(e -> {
            // open dialog
            GenerateRandomSparseMatrixDialog generateRandomMatrixDialog = new GenerateRandomSparseMatrixDialog();
            generateRandomMatrixDialog.setSize(
                    Math.max(ApplicationMainForm.this.MainPanel.getWidth() / 3, 350),
                    Math.max(ApplicationMainForm.this.MainPanel.getHeight() / 4, 300)
            );
            generateRandomMatrixDialog.setVisible(true);

            // get confirmation logic when closed
            if (generateRandomMatrixDialog.isConfirmed()) {
                int rows = generateRandomMatrixDialog.getRows();
                int cols = generateRandomMatrixDialog.getCols();

                double sparsity = generateRandomMatrixDialog.getSparsity();

                double minValue = generateRandomMatrixDialog.getMin();
                double maxValue = generateRandomMatrixDialog.getMax();

                boolean isSymmetric = generateRandomMatrixDialog.isSymmetric();

                ApplicationMainForm.this.setSparseMatrix(
                        SparseMatrix.generateRandomSparseMatrix(
                                rows, cols, minValue, maxValue, sparsity, isSymmetric
                        )
                );
            }
        });
        this.generateBandMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ApplicationMainForm.this.sparseMatrix == null)
                    return;
                ApplicationMainForm.this.setBandSparseMatrix();
            }
        });
        this.exportMatrixIntoFileButton.addActionListener(e -> {
            if (ApplicationMainForm.this.sparseMatrix == null) {
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    ApplicationMainForm.this.sparseMatrix.toFile(selectedFile.getAbsolutePath());
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            exc.getMessage(),
                            "Matrix error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        this.exportBandMatrixIntoButton.addActionListener(e -> {
            if (ApplicationMainForm.this.bandSparseMatrix == null) {
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    ApplicationMainForm.this.bandSparseMatrix.toFile(selectedFile.getAbsolutePath());
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            exc.getMessage(),
                            "Matrix error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    public void setSparseMatrix(SparseMatrix sparseMatrix) {
        this.sparseMatrix = sparseMatrix;
        this.setTableContent(this.longFormMatrixTable, sparseMatrix);
        this.setShortTableContent(this.shortFormMatrixTable, sparseMatrix);
    }

    public void setBandSparseMatrix() {
        this.bandSparseMatrix = CuthillMcKee.getBandMatrix(this.sparseMatrix);
        this.setShortTableContent(this.shortFormBandMatrixTable, this.bandSparseMatrix);
        this.setTableContent(this.longFormBandMatrixTable, this.bandSparseMatrix);
    }

    private void setTableContent(JTable jTable, SparseMatrix matrix) {
        int rows = matrix.getNumRows();
        int cols = matrix.getNumCols();
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();

        model.setRowCount(rows);
        model.setColumnCount(cols);

        this._ProgrammaticChange = true;
        // set table contents
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                model.setValueAt(matrix.getValue(i, j), i, j);
            }
        }
        this._ProgrammaticChange = false;
    }

    private void setShortTableContent(JTable jTable, SparseMatrix matrix) {
        int rows = matrix.getNumElems();
        int cols = 3;
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();

        model.setRowCount(rows);
        model.setColumnCount(cols);

        this._ProgrammaticChange = true;
        // set table contents
        int count = 0;
        for (int i : matrix.getRows()) {
            for (int j : matrix.getCols(i)) {
                model.setValueAt(i, count, 0);
                model.setValueAt(j, count, 1);
                model.setValueAt(matrix.getValue(i, j), count, 2);
                ++count;
            }
        }
        this._ProgrammaticChange = false;
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Linear Algebra");
        frame.setContentPane(new ApplicationMainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
