import Algebra.Generic.Matrix;
import Form.MainForm.MatrixVectorForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Linear Algebra");
        frame.setContentPane(new MatrixVectorForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//        Matrix A = new Matrix(3, 3);
//        Matrix B = new Matrix(3, 4);
//        for(int i = 0; i < A.getRows(); ++i) {
//            for (int j = 0; j < A.getCols(); ++j) {
//                A.set(i, j, i + j);
//            }
//        }
//
//        for(int i = 0; i < A.getRows(); ++i) {
//            for (int j = 0; j < B.getCols(); ++j)
//            {
//                B.set(i, j, i - j);
//            }
//        }
//
//        System.out.println(A);
//        System.out.println(B);
//
//        System.out.println(A.multiply(B));
    }
}
