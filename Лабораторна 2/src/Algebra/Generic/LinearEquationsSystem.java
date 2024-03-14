package Algebra.Generic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class LinearEquationsSystem {

    // Ax=b
    private Matrix LeftSideMatrix; // A
    private Matrix RightSideVector; // b
    private Vector Solution; // x


    //
    public int getEquationsNumber(){
        return LeftSideMatrix.getRows();
    }

    // constructors
    public LinearEquationsSystem(int equationsNumber) {
        this.LeftSideMatrix = new Matrix(equationsNumber, equationsNumber);
        this.RightSideVector = new Matrix(equationsNumber, 1);
    }

    public LinearEquationsSystem(Matrix leftSideMatrix, Matrix rightSideVector) {
        if (leftSideMatrix.getRows() != rightSideVector.getRows())
        {
            throw new IllegalArgumentException("Right side vector must have the same elements count as left side matrix");
        }

        this.LeftSideMatrix = leftSideMatrix;
        this.RightSideVector = rightSideVector;
    }

    public Matrix getLeftSideMatrix() {
        return this.LeftSideMatrix;
    }

    public void setLeftSideMatrix(Matrix leftSideMatrix){
        if (leftSideMatrix.getRows() != this.RightSideVector.getCols()){
            throw new IllegalArgumentException("Left side matrix must have the same count of rows as right side vector");
        }
        this.LeftSideMatrix = leftSideMatrix;
        this.Solution = null;
    }

    public Matrix getRightSideVector() {
        return RightSideVector;
    }

    public void setRightSideVector(Matrix rightSideVector){
        if (rightSideVector.getRows() != this.LeftSideMatrix.getCols()){
            throw new IllegalArgumentException("Left side matrix must have the same count of rows as right side vector");
        }
        this.RightSideVector = rightSideVector;
        this.Solution = null;
    }

    public Vector getSolution() {
        return Solution;
    }

    public static LinearEquationsSystem readFromFile(File file) throws FileNotFoundException {
        // treat as extended matrix
        Matrix matrix = Matrix.readFromFile(file);
        if (matrix.getRows() + 1 != matrix.getCols())
        {
            throw new IllegalArgumentException("Extended matrix in the file is not n x n+1");
        }
        else {
            Matrix leftSideMatrix = new Matrix(matrix.getRows(), matrix.getCols() - 1);
            Matrix rightSideVector = new Matrix(matrix.getRows(), 1);
            for (int i = 0; i < matrix.getRows(); ++i){
                for (int j = 0; j < matrix.getCols() - 1; ++j){
                    leftSideMatrix.set(i, j, matrix.get(i, j));
                }
                rightSideVector.set(i, 0, matrix.get(i, matrix.getCols() - 1));
            }
            return new LinearEquationsSystem(leftSideMatrix, rightSideVector);
        }
    }

    public void writeToFile(File file, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(file, append);

        fileWriter.write(this.LeftSideMatrix.getRows() + " " + (this.LeftSideMatrix.getCols() + 1) + "\n");
        for (int i = 0; i < this.LeftSideMatrix.getRows(); ++i) {
            for (int j = 0; j < this.LeftSideMatrix.getCols(); j++) {
                fileWriter.write(this.LeftSideMatrix.get(i, j) + " ");
            }
            fileWriter.write(this.RightSideVector.get(i, 0) + " \n");
        }
        fileWriter.write("\n");
        fileWriter.close();
    }

    public static LinearEquationsSystem random(int equations, double min, double max) {
        return new LinearEquationsSystem(
                Matrix.randomMatrix(equations, equations, min, max),
                Matrix.randomMatrix(equations, 1, min, max)
        );
    }


    public void GaussianMethodSolve() {

    }
}
