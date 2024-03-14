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
        return isConfirmed;
    }

    public GenerateRandomMatrixDialog() {

        SpinnerNumberModel positiveIntegerSpinnerModel = new SpinnerNumberModel(1, 1, 10000, 1);
        RowsSpinner.setModel(positiveIntegerSpinnerModel);

        JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(RowsSpinner);
        numberEditor.getFormat().setGroupingUsed(false);
        RowsSpinner.setEditor(numberEditor);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public int getRows(){
        Object object = RowsSpinner.getValue();
        if (object instanceof Integer) {
            return (Integer) object;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public double getMinValue(){
        Object object = MinValueSpinner.getValue();
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
        Object object = MaxValueSpinner.getValue();
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


    private void onOK() {
        // add your code here
        isConfirmed = true;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GenerateRandomMatrixDialog dialog = new GenerateRandomMatrixDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
