package Form.MainForm;

import Algebra.Generic.LinearEquationsSystem;
import Algebra.Generic.Matrix;
import Algebra.Generic.Vector;
import Algebra.Methods.Gaussian.GaussianMethodLinearSystemSolve;
import Algebra.Methods.GenericMethod;
import Algebra.Methods.Jacobi.Jacobi;
import Form.Interfaces.ExportFileInterface;
import Form.Interfaces.ImportFileInterface;
import Form.Interfaces.IntDoubleDoubleBoolInterface;
import Form.MethodsEnum;
import Form.RandomDialog.GenerateRandomMatrixDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MatrixVectorForm {

    // form
    public JPanel MainPanel;
    private JButton ImportSystemFromFileButton;
    private JButton GenerateRandomSystemButton;
    private JButton ExportSystemIntoFileButton;
    private JTable MatrixTable;
    private JTable VectorTable;
    private JComboBox<String> MethodComboBox;
    private JButton SolveButton;
    private JTable SolutionTable;
    private JScrollPane SolutionScrollPane;
    private JButton ImportMatrixFromFileButton;
    private JButton ExportMatrixIntoFileButton;
    private JButton GenerateRandomMatrixButton;
    private JButton IntermediateStepsChooseFileButton;
    private JTextField IntermediateFileField;
    private JTextField RelativeErrorField;
    private JLabel MetaInfoLabel;
    private JSpinner PrecisionSpinner;
    private JSpinner MaxIterSpinner;

    //
    LinearEquationsSystem linearEquationsSystem = null;
    File IntermediateStepsFile;

    // technical variables
    private boolean _ProgrammaticChange = false;


    public MatrixVectorForm() {

        SpinnerNumberModel precisionSpinnerModel = new SpinnerNumberModel(0.001, 0.0000000000001, 1, 0.0001);
        this.PrecisionSpinner.setModel(precisionSpinnerModel);

        JSpinner.NumberEditor editor_p = new JSpinner.NumberEditor(this.PrecisionSpinner);
        editor_p.getFormat().setGroupingUsed(false);
        editor_p.getFormat().setMaximumFractionDigits(20);
        this.PrecisionSpinner.setEditor(editor_p);

        SpinnerNumberModel MaxIterSpinnerModel = new SpinnerNumberModel(100, 10, 1000000, 1);
        this.MaxIterSpinner.setModel(MaxIterSpinnerModel);

        JSpinner.NumberEditor editor_i = new JSpinner.NumberEditor(this.MaxIterSpinner);
        editor_i.getFormat().setGroupingUsed(false);
        this.MaxIterSpinner.setEditor(editor_i);

        // combobox options
        MethodsEnum[] methods = MethodsEnum.values();
        for (MethodsEnum method : methods) {
            this.MethodComboBox.addItem(method.name());
        }

        this.MethodComboBox.setSelectedItem("None");
        this.MethodComboBox.addActionListener((e) -> {
            this.SolveButton.setEnabled(this.MethodComboBox.getSelectedItem() != "None" && this.MethodComboBox.getSelectedItem() != null);
        });


        // custom table restrictions
        this.MatrixTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.VectorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // system
        this.GenerateRandomSystemButton.addActionListener((e) -> this.onGenerateRandom(e, (rows, minValue, maxValue, ensure) -> {
            this.linearEquationsSystem = LinearEquationsSystem.random(rows, minValue, maxValue, ensure);
            this.refreshTables();
        }));
        this.ImportSystemFromFileButton.addActionListener((e) -> this.onImportClick(e, (file) -> {
            try {
                this.linearEquationsSystem = LinearEquationsSystem.readFromFile(file);
                this.refreshTables();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input matrix: should be n x n+1 dimensions",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }));
        this.ExportSystemIntoFileButton.addActionListener((e) -> this.onExportClick(e, (file) -> {
            try {
                this.linearEquationsSystem.writeToFile(file, false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Warning", JOptionPane.ERROR_MESSAGE
                );
            }
        }));

        // matrix
        this.GenerateRandomMatrixButton.addActionListener((e) -> this.onGenerateRandom(e, (rows, minValue, maxValue, ensure) -> {
            if (this.linearEquationsSystem == null) {
                this.linearEquationsSystem = new LinearEquationsSystem(rows);
            }

            this.linearEquationsSystem.setLeftSideMatrix(Matrix.randomMatrix(rows, rows, minValue, maxValue, ensure));
            this.refreshTables();
        }));
        this.ImportMatrixFromFileButton.addActionListener((e) -> this.onImportClick(e, (file) -> {
            try {
                Matrix matrix = Matrix.readFromFile(file);
                if (this.linearEquationsSystem == null) {
                    this.linearEquationsSystem = new LinearEquationsSystem(matrix, new Matrix(matrix.getRows(), 1));
                } else {
                    this.linearEquationsSystem.setLeftSideMatrix(matrix);
                }
                this.refreshTables();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input matrix: should be n x n+1 dimensions",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }));
        this.ExportMatrixIntoFileButton.addActionListener((e) -> this.onExportClick(e, (file) -> {
            try {
                this.linearEquationsSystem.getLeftSideMatrix().writeToFile(file, false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Warning", JOptionPane.ERROR_MESSAGE
                );
            }
        }));


        this.MatrixTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && !this._ProgrammaticChange) {
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
                        this._ProgrammaticChange = true;
                        model.setValueAt(this.linearEquationsSystem.getLeftSideMatrix().get(row, column), row, column);
                        this._ProgrammaticChange = false;
                    }
                }
            }
        });


        this.VectorTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && !this._ProgrammaticChange) {
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
                        this._ProgrammaticChange = true;
                        model.setValueAt(this.linearEquationsSystem.getRightSideVector().get(row, column), row, column);
                        this._ProgrammaticChange = false;
                    }
                }
            }
        });

        this.IntermediateStepsChooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().exists())
                {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to replace this file?",
                            "Confirm replace",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                this.IntermediateStepsFile = fileChooser.getSelectedFile();
                this.IntermediateFileField.setText(this.IntermediateStepsFile.getAbsolutePath());
            }
        });

        this.SolveButton.addActionListener(e -> {
            // common part
            if (this.IntermediateStepsFile == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Select file to output intermediate steps!.",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            } else if (this.IntermediateStepsFile.exists()) {
                boolean __ = this.IntermediateStepsFile.delete();
            }

            if (this.IntermediateStepsFile != null) {
                try {
                    boolean __ = this.IntermediateStepsFile.createNewFile();
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(
                            null,
                            exception.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            if (this.MethodComboBox.getSelectedItem() == MethodsEnum.GAUSSIAN_ELIMINATION.name()) {
                GaussianMethodLinearSystemSolve gauss;
                if (this.IntermediateStepsFile == null) {
                    gauss = new GaussianMethodLinearSystemSolve(this.linearEquationsSystem);
                }
                else {
                    gauss = new GaussianMethodLinearSystemSolve(this.linearEquationsSystem, obj -> {
                        obj.writeCurrentStateToFile(this.IntermediateStepsFile);
                    });
                }

                try {
                    gauss.Solve();
                    this.putSolutions(gauss);

                    this.MetaInfoLabel.setText("");
                }
                catch (IllegalArgumentException exc)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            exc.getMessage(),
                            "Matrix error", JOptionPane.ERROR_MESSAGE
                    );
                }

            }
            else if (this.MethodComboBox.getSelectedItem() == MethodsEnum.JACOBI_METHOD.name()) {
                Jacobi jacobi;
                double eps = (double) this.PrecisionSpinner.getValue();
                int iterations = (int) this.MaxIterSpinner.getValue();

                if (this.IntermediateStepsFile == null) {
                    jacobi = new Jacobi(this.linearEquationsSystem, eps, iterations);
                }
                else {
                    jacobi = new Jacobi(this.linearEquationsSystem, obj -> {
                        obj.writeCurrentStateToFile(this.IntermediateStepsFile);
                    }, eps, iterations);
                }

                try {
                    jacobi.Solve();
                    this.putSolutions(jacobi);

                    this.MetaInfoLabel.setText("Iterations: " + jacobi.getIteration());
                }
                catch (IllegalArgumentException exc)
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

    public void onGenerateRandom(ActionEvent e, IntDoubleDoubleBoolInterface post) {
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
            boolean ensure = generateRandomMatrixDialog.getEnsureSolutions();

            post.call(rows, minValue, maxValue, ensure);
        }
    }

    public void refreshTables() {
        this.setTableContent(this.MatrixTable, this.linearEquationsSystem.getLeftSideMatrix());
        this.setTableContent(this.VectorTable, this.linearEquationsSystem.getRightSideVector());

        if (!this.ExportMatrixIntoFileButton.isEnabled()) {
            this.ExportMatrixIntoFileButton.setEnabled(true);
        }

        if (!this.ExportSystemIntoFileButton.isEnabled()) {
            this.ExportSystemIntoFileButton.setEnabled(true);
        }
    }

    public void onImportClick(ActionEvent e, ImportFileInterface post) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                post.call(selectedFile);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "File " + selectedFile.getName() + " not found.",
                        "Warning", JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public void onExportClick(ActionEvent e, ExportFileInterface post) {
        if (this.linearEquationsSystem == null) {
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
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

        this._ProgrammaticChange = true;
        // set table contents
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                model.setValueAt(matrix.get(i, j), i, j);
            }
        }
        this._ProgrammaticChange = false;
    }


    public void putSolutions(GenericMethod obj) {
        Vector solutions = obj.getSolution();
        DefaultTableModel model = (DefaultTableModel) this.SolutionTable.getModel();

        // resize table if necessary
        model.setRowCount(1);
        model.setColumnCount(solutions.getSize());

        this._ProgrammaticChange = true;
        for (int i = 0; i < solutions.getSize(); i++) {
            model.setValueAt(solutions.get(i), 0, i);
        }
        this._ProgrammaticChange = false;

        this.RelativeErrorField.setText(Double.toString(obj.relativeError()));
    }
}
