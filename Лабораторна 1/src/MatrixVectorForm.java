import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

interface IntDoubleDoubleInterface{
    void call(int a, double b, double c);
}

interface ImportFileInterface {
    void call(File file) throws FileNotFoundException;
}

interface ExportFileInterface {
    void call(File file);
}


public class MatrixVectorForm {

    // form
    private JPanel MainPanel;
    private JButton ImportSystemFromFileButton;
    private JButton GenerateRandomSystemButton;
    private JButton ExportSystemIntoFileButton;
    private JTable MatrixTable;
    private JTable VectorTable;
    private JComboBox MethodComboBox;
    private JButton SolveButton;
    private JTable SolutionTable;
    private JScrollPane SolutionScrollPane;
    private JButton ImportMatrixFromFileButton;
    private JButton ExportMatrixIntoFileButton;
    private JButton GenerateRandomMatrixButton;

    //
    LinearEquationsSystem linearEquationsSystem = null;

    // technical variables
    private boolean _ProgrammaticChange = false;


    public MatrixVectorForm() {

        // custom table restrictions
        MatrixTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        VectorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // system
        GenerateRandomSystemButton.addActionListener((e) -> onGenerateRandom(e, (rows, minValue, maxValue) -> {
            this.linearEquationsSystem = LinearEquationsSystem.random(rows, minValue, maxValue);
            this.refreshTables();
        }));
        ImportSystemFromFileButton.addActionListener((e) -> onImportClick(e, (file) -> {
            try{
                this.linearEquationsSystem = LinearEquationsSystem.readFromFile(file);
                refreshTables();
            }
            catch (IllegalArgumentException ex)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input matrix: should be n x n+1 dimensions",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }));
        ExportSystemIntoFileButton.addActionListener((e) -> onExportClick(e, (file) -> {
            try {
                this.linearEquationsSystem.writeToFile(file);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Warning", JOptionPane.ERROR_MESSAGE
                );
            }
        }));

        // matrix
        GenerateRandomMatrixButton.addActionListener((e) -> onGenerateRandom(e, (rows, minValue, maxValue) -> {
            if(this.linearEquationsSystem == null)
            {
                this.linearEquationsSystem = new LinearEquationsSystem(rows);
            }

            this.linearEquationsSystem.setLeftSideMatrix(Matrix.randomMatrix(rows, rows, minValue, maxValue));
            this.refreshTables();
        }));
        ImportMatrixFromFileButton.addActionListener((e) -> onImportClick(e, (file) -> {
            try{
                Matrix matrix = Matrix.readFromFile(file);
                if(this.linearEquationsSystem == null)
                {
                    this.linearEquationsSystem = new LinearEquationsSystem(matrix, new Matrix(matrix.getRows(), 1));
                }
                else {
                    this.linearEquationsSystem.setLeftSideMatrix(matrix);
                }
                refreshTables();
            }
            catch (IllegalArgumentException ex)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input matrix: should be n x n+1 dimensions",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }));
        ExportMatrixIntoFileButton.addActionListener((e) -> onExportClick(e, (file) -> {
            try {
                this.linearEquationsSystem.getLeftSideMatrix().writeToFile(file);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Warning", JOptionPane.ERROR_MESSAGE
                );
            }
        }));



        MatrixTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && !_ProgrammaticChange) {
                int column = e.getColumn();
                int row = e.getFirstRow();

                if (row != -1 && column != -1) {

                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    Object data = model.getValueAt(row, column);
                    try {
                        double value = Double.parseDouble((String) data);
                        this.linearEquationsSystem.getLeftSideMatrix().set(row, column, value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid number format",
                                "Warning", JOptionPane.ERROR_MESSAGE
                        );
                        _ProgrammaticChange = true;
                        model.setValueAt(this.linearEquationsSystem.getLeftSideMatrix().get(row, column), row, column);
                        _ProgrammaticChange = false;
                    }
                }
            }
        });


        VectorTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && !_ProgrammaticChange) {
                int column = e.getColumn();
                int row = e.getFirstRow();

                if (row != -1 && column != -1) {

                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    Object data = model.getValueAt(row, column);
                    try {
                        double value = Double.parseDouble((String) data);
                        this.linearEquationsSystem.getRightSideVector().set(row, column, value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid number format",
                                "Warning", JOptionPane.ERROR_MESSAGE
                        );
                        _ProgrammaticChange = true;
                        model.setValueAt(this.linearEquationsSystem.getRightSideVector().get(row, column), row, column);
                        _ProgrammaticChange = false;
                    }
                }
            }
        });

    }

    public void onGenerateRandom(ActionEvent e, IntDoubleDoubleInterface post) {
        // open dialog
        GenerateRandomMatrixDialog generateRandomMatrixDialog = new GenerateRandomMatrixDialog();
        generateRandomMatrixDialog.setSize(
                Math.max(this.MainPanel.getWidth() / 3, 350),
                Math.max(this.MainPanel.getHeight() / 4, 300)
        );
        generateRandomMatrixDialog.setVisible(true);

        // get confirmation logic when closed
        if (generateRandomMatrixDialog.isConfirmed()) {
            int rows = generateRandomMatrixDialog.getRows();
            double minValue = generateRandomMatrixDialog.getMinValue();
            double maxValue = generateRandomMatrixDialog.getMaxValue();

            post.call(rows, minValue, maxValue);
        }
    }

    public void refreshTables(){
        this.setTableContent(MatrixTable, this.linearEquationsSystem.getLeftSideMatrix());
        this.setTableContent(VectorTable, this.linearEquationsSystem.getRightSideVector());

        if (!this.ExportMatrixIntoFileButton.isEnabled()) {
            this.ExportMatrixIntoFileButton.setEnabled(true);
        }

        if (!this.ExportSystemIntoFileButton.isEnabled()){
            this.ExportSystemIntoFileButton.setEnabled(true);
        }
    }

    public void onImportClick(ActionEvent e, ImportFileInterface post) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                post.call(selectedFile);
            }
            catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "File " + selectedFile.getName() + " not found.",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public void onExportClick(ActionEvent e, ExportFileInterface post) {
        if (this.linearEquationsSystem == null)
        {
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            post.call(selectedFile);
        }
    }

    private void setTableContent(JTable jTable, Matrix matrix) {
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();

        // resize table if necessary
        if (rows != model.getRowCount() || cols != model.getColumnCount()) {
            model.setRowCount(rows);
            model.setColumnCount(cols);
        }

        _ProgrammaticChange = true;
        // set table contents
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                model.setValueAt(matrix.get(i, j), i, j);
            }
        }
        _ProgrammaticChange = false;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Linear Algebra");
        frame.setContentPane(new MatrixVectorForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
