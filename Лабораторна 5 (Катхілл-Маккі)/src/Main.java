import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows and columns in the matrix (separated by spaces):");
        int numRows = scanner.nextInt();
        int numCols = scanner.nextInt();
        SparseMatrix matrix = new SparseMatrix(numRows, numCols);
        System.out.println("Enter the values of the matrix in row-major order (separated by spaces):");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double value = scanner.nextDouble();
                if (value != 0) {
                    matrix.setValue(i, j, value);
                }
            }
        }
        System.out.println("Matrix:");
        System.out.println(matrix);

        System.out.println("Enter the number of rows in the second matrix:");
        int numRows2 = scanner.nextInt();
        System.out.println("Enter the number of columns in the second matrix:");
        int numCols2 = scanner.nextInt();
        SparseMatrix matrix2 = new SparseMatrix(numRows2, numCols2);
        System.out.println("Enter the values of the second matrix in row-major order (separated by spaces):");
        for (int i = 0; i < numRows2; i++) {
            for (int j = 0; j < numCols2; j++) {
                double value = scanner.nextDouble();
                if (value != 0) {
                    matrix2.setValue(i, j, value);
                }
            }
        }
        System.out.println("Second Matrix:");
        System.out.println(matrix2);

        SparseMatrix sum = matrix.add(matrix2);
        System.out.println("Sum of matrices:");
        System.out.println(sum);

        SparseMatrix difference = matrix.subtract(matrix2);
        System.out.println("Difference of matrices:");
        System.out.println(difference);

        SparseMatrix product = matrix.multiply(matrix2);
        System.out.println("Product of matrices:");
        System.out.println(product);
    }
}
