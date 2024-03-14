package Algebra.Generic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Matrix{

    private final int rows;
    private final int cols;
    private final double[][] data;

    // matrix props access
    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    // matrix element access
    public double get(int row, int col) {
        if (0 <= row && row < this.rows && 0 <= this.cols && col <= this.cols)
        {
            return this.data[row][col];
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }

    public void set(int row, int col, double value) {
        if (0 <= row && row < this.rows && 0 <= this.cols && col <= this.cols)
        {
            this.data[row][col] = value;
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }

    // constructors
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                sb.append(this.data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.getRows() || this.cols != other.getCols()) {
            throw new IllegalArgumentException("Matrices must be of the same size.");
        }

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return result;
    }

    public Matrix subtract(Matrix other) {
        if (this.rows != other.getRows() || this.cols != other.getCols()) {
            throw new IllegalArgumentException("Matrices must be of the same size.");
        }

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.getRows()) {
            throw new IllegalArgumentException("Number of columns in first matrix must match number of rows in second matrix.");
        }

        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < other.cols; ++j) {

                double dotProduct = 0;
                for (int k = 0; k < this.cols; ++k) {
                    dotProduct += this.data[i][k] * other.data[k][j];
                }
                result.data[i][j] = dotProduct;
            }
        }
        return result;
    }

    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] * scalar;
            }
        }
        return result;
    }

    public double euclideanNorm() {
        double sumOfSquares = 0.0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                sumOfSquares += this.data[i][j] * this.data[i][j];
            }
        }
        return Math.sqrt(sumOfSquares);
    }

    public double maxAbsNorm() {
        double maxAbsValue = 0.0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (Math.abs(this.data[i][j]) > maxAbsValue)
                    maxAbsValue = Math.abs(this.data[i][j]);
            }
        }
        return maxAbsValue;
    }

    public static Matrix readFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (scanner.hasNext()) {
                    matrix.data[i][j] = Double.parseDouble(scanner.next());
                }
            }
        }
        scanner.close();
        return matrix;
    }

    public void writeToFile(File file, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(file, append);

        fileWriter.write(this.rows + " " + this.cols + "\n");
        fileWriter.write(this.toString());
        fileWriter.write("\n");
        fileWriter.close();
    }

    public static Matrix randomMatrix(int rows, int cols, double min, double max) {
        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.data[i][j] = min + (max - min) * Math.random();
            }
        }
        return matrix;
    }

    public static Matrix Identity(int rows) {
        Matrix matrix = new Matrix(rows, rows);
        for (int i = 0; i < rows; i++) {
            matrix.data[i][i] = 1;
        }
        return matrix;
    }

    public void swapRows(int first, int second){
        double[] temp = this.data[second];
        this.data[second] = this.data[first];
        this.data[first] = temp;
    }

    public Matrix copy() {
        Matrix cloned = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; ++i)
        {
            cloned.data[i] = this.data[i].clone();
        }
        return cloned;

    }
}
