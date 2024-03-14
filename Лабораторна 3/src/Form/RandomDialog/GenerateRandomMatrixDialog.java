package Form.RandomDialog;

import javax.swing.*;
import java.awt.event.*;

public class GenerateRandomMatrixDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JSpinner RowsSpinner;
    private JSpinner MinValueSpinner;
    private JSpinner MaxValueSpinner;
    private JCheckBox EnsureSolutionsCheckBox;

    private boolean isConfirmed = false;

    public boolean isConfirmed() {
        return this.isConfirmed;
    }

    public GenerateRandomMatrixDialog() {

        SpinnerNumberModel positiveIntegerSpinnerModel = new SpinnerNumberModel(1, 1, 10000, 1);
        this.RowsSpinner.setModel(positiveIntegerSpinnerModel);

        JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(this.RowsSpinner);
        numberEditor.getFormat().setGroupingUsed(false);
        this.RowsSpinner.setEditor(numberEditor);

        this.setContentPane(this.contentPane);
        this.setModal(true);
        this.getRootPane().setDefaultButton(this.buttonOK);

        this.buttonOK.addActionListener(e -> this.onOK());

        this.buttonCancel.addActionListener(e -> this.onCancel());

        // call onCancel() when cross is clicked
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                GenerateRandomMatrixDialog.this.onCancel();
            }
        });

        // call onCancel() on ESCAPE
        this.contentPane.registerKeyboardAction(e -> this.onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public int getRows(){
        Object object = this.RowsSpinner.getValue();
        if (object instanceof Integer) {
            return (Integer) object;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public double getMinValue(){
        Object object = this.MinValueSpinner.getValue();
        if (object instanceof Double) {
            return (Double) object;
        }
        else if (object instanceof Integer) {
            return ((Integer) object).doubleValue();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public double getMaxValue(){
        Object object = this.MaxValueSpinner.getValue();
        if (object instanceof Double) {
            return (Double) object;
        }
        else if (object instanceof Integer) {
            return ((Integer) object).doubleValue();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public boolean getEnsureSolutions(){
        return this.EnsureSolutionsCheckBox.isSelected();
    }


    private void onOK() {
        // add your code here
        this.isConfirmed = true;
        this.dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        this.dispose();
    }

    public static void main(String[] args) {
        GenerateRandomMatrixDialog dialog = new GenerateRandomMatrixDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
