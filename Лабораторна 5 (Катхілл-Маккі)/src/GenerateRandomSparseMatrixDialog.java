import javax.swing.*;
import java.awt.event.*;

public class GenerateRandomSparseMatrixDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner RowsSpinner;
    private JSpinner ColumnsSpinner;
    private JCheckBox symmetricCheckBox;
    private JSpinner minSpinner;
    private JSpinner maxSpinner;
    private JSpinner sparsitySpinner;

    private int rows;
    private int cols;
    private double min;
    private double max;
    private double sparsity;
    private boolean isSymmetric;
    private boolean isConfirmed = false;

    public GenerateRandomSparseMatrixDialog() {
        this.setContentPane(this.contentPane);
        this.setModal(true);
        this.getRootPane().setDefaultButton(this.buttonOK);

        this.minSpinner.setModel(new SpinnerNumberModel(-10, -100, 100, 0.01));
        this.maxSpinner.setModel(new SpinnerNumberModel(10, -100, 100, 0.01));

        this.sparsitySpinner.setModel(new SpinnerNumberModel(0.2, 0, 1, 0.001));

        this.buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateRandomSparseMatrixDialog.this.onOK();
            }
        });

        this.buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateRandomSparseMatrixDialog.this.onCancel();
            }
        });

        // call onCancel() when cross is clicked
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                GenerateRandomSparseMatrixDialog.this.onCancel();
            }
        });

        // call onCancel() on ESCAPE
        this.contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateRandomSparseMatrixDialog.this.onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public boolean isConfirmed() {
        return this.isConfirmed;
    }

    public boolean isSymmetric() {
        return this.isSymmetric;
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }

    public double getSparsity() {
        return this.sparsity;
    }

    private void onOK() {
        // add your code here
        this.isConfirmed = true;

        this.isSymmetric = this.symmetricCheckBox.isSelected();

        this.rows = (int) this.RowsSpinner.getValue();

        if (!this.isSymmetric) {
            this.cols = (int) this.ColumnsSpinner.getValue();
        }
        else {
            this.cols = this.rows;
        }

        this.min = (double) this.minSpinner.getValue();
        this.max = (double) this.maxSpinner.getValue();

        this.sparsity = (double) this.sparsitySpinner.getValue();




        this.dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        this.isConfirmed = false;
        this.dispose();
    }

    public static void main(String[] args) {
        GenerateRandomSparseMatrixDialog dialog = new GenerateRandomSparseMatrixDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
