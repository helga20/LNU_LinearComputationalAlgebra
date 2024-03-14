package Algebra.Methods.Gaussian;

import Algebra.Generic.LinearEquationsSystem;
import Algebra.Generic.Matrix;
import Algebra.Generic.Vector;
import Algebra.Methods.GenericMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class GaussianMethodLinearSystemSolve implements GenericMethod {


    LinearEquationsSystem equationsSystem;
    GaussianSolutionCallback callback = null;

    // upper and lower matrices
    Matrix upper = null;
    Matrix lower = null;
    Matrix vector = null;

    LinearEquationsSystem LUSystem;
    Matrix Inverse;

    // solution
    Vector solution;

    // decomposed matrix

    // tech variables
    int[] orderOfVariables;
    Matrix T;
    boolean forwardEliminationCompleted = false;
    int forwardStep = -1;
    boolean backwardsSubstitutionCompleted = false;
    int backwardStep = -1;

    public Matrix getLower() {
        return this.lower;
    }

    public Matrix getUpper() {
        return this.upper;
    }

    public Matrix getVector() {
        return this.vector;
    }

    public Vector getSolution() {
        return this.solution;
    }

    public int[] getOrderOfVariables() {
        return this.orderOfVariables;
    }

    public boolean isBackwardsSubstitutionCompleted() {
        return this.backwardsSubstitutionCompleted;
    }

    public boolean isForwardEliminationCompleted() {
        return this.forwardEliminationCompleted;
    }

    public int getBackwardStep() {
        return this.backwardStep;
    }

    public int getForwardStep() {
        return this.forwardStep;
    }

    public Matrix getInverse() {
        return this.Inverse;
    }

    public LinearEquationsSystem getEquationsSystem() {
        return this.equationsSystem;
    }

    public LinearEquationsSystem getLUSystem() {
        return this.LUSystem;
    }

    public GaussianMethodLinearSystemSolve(LinearEquationsSystem system)
    {
        this.equationsSystem = system;
        this._initialize();
    }

    public GaussianMethodLinearSystemSolve(LinearEquationsSystem system, GaussianSolutionCallback callback)
    {
        this.equationsSystem = system;
        this.callback = callback;
        this._initialize();
    }

    private void _initialize(){
        int equationsNumber = this.equationsSystem.getEquationsNumber();
        this.orderOfVariables = new int[this.equationsSystem.getEquationsNumber()];
        for(int i = 0; i < this.orderOfVariables.length; ++i){
            this.orderOfVariables[i] = i;
        }

        this.lower = Matrix.Identity(equationsNumber);
        this.vector = this.equationsSystem.getRightSideVector().copy();
        this.upper = this.equationsSystem.getLeftSideMatrix().copy();

        this.LUSystem = new LinearEquationsSystem(this.upper, this.vector);
        this.Inverse = Matrix.Identity(equationsNumber);
    }

    public void Solve()
    {
        if (this.callback != null){
            this.callback.call(this);
        }
        this.forwardElimination();
        this.forwardEliminationCompleted = true;

        this.backwardSubstitution();
        this.backwardsSubstitutionCompleted = true;

        if (this.callback != null){
            this.callback.call(this);
        }
    }


    private void forwardElimination()
    {
        int equationsNumber = this.equationsSystem.getEquationsNumber();

        // every iteration of direct
        for (this.forwardStep = 0; this.forwardStep < equationsNumber; ++this.forwardStep){

            // step 1: choose max pivot
            int pivot_row = this.forwardStep;
            for (int j = this.forwardStep + 1; j < equationsNumber; ++j)
            {
                if (Math.abs(this.upper.get(j, this.forwardStep)) > Math.abs(this.upper.get(pivot_row, this.forwardStep)))
                {
                    pivot_row = j;
                }
            }

            // step 2: check if matrix is not singular (== degenerate)
            if (this.upper.get(pivot_row, this.forwardStep) == 0)
            {
                throw new IllegalArgumentException("This matrix is singular and might have either infinity, or none solutions");
            }


            // step 3: if pivot row is other than k
            if (pivot_row != this.forwardStep)
            {
                this.upper.swapRows(this.forwardStep, pivot_row);
                this.lower.swapRows(this.forwardStep, pivot_row);

                this.Inverse.swapRows(this.forwardStep, pivot_row);

                this.vector.swapRows(this.forwardStep, pivot_row);

                int temp = this.orderOfVariables[this.forwardStep];
                this.orderOfVariables[this.forwardStep] = this.orderOfVariables[pivot_row];
                this.orderOfVariables[pivot_row] = temp;
            }


            // step 4: transform lower non-pivot rows
            for(int i = this.forwardStep + 1; i < this.upper.getRows(); ++i){
                double factor = this.upper.get(i, this.forwardStep) / this.upper.get(this.forwardStep, this.forwardStep);
                this.lower.set(i, this.forwardStep, -factor);

                for (int j = this.forwardStep + 1; j < this.upper.getCols(); ++j)
                {
                    this.upper.set(i, j, this.upper.get(i, j) - factor * this.upper.get(this.forwardStep, j));
                }
                for (int j = 0; j < this.Inverse.getCols(); ++j)
                {
                    this.Inverse.set(i, j, this.Inverse.get(i, j) - factor * this.Inverse.get(this.forwardStep, j));
                }

                this.upper.set(i, this.forwardStep, 0);
                this.vector.set(i, 0, this.vector.get(i, 0) - factor * this.vector.get(this.forwardStep, 0));

            }
            this.lower.set(this.forwardStep, this.forwardStep, 1 / this.upper.get(this.forwardStep, this.forwardStep));


            // step 5: normalize pivot row
            double pivot_value = this.upper.get(this.forwardStep, this.forwardStep);
            for (int j = 0; j < this.upper.getCols(); ++j)
            {
                this.upper.set(this.forwardStep, j, this.upper.get(this.forwardStep, j) / pivot_value);
            }
            for (int j = 0; j < this.Inverse.getCols(); ++j)
            {
                this.Inverse.set(this.forwardStep, j, this.Inverse.get(this.forwardStep, j) / pivot_value);
            }
            this.vector.set(this.forwardStep, 0, this.vector.get(this.forwardStep, 0) / pivot_value);

            if (this.callback != null){
                this.callback.call(this);
            }
        }
    }

    public void backwardSubstitution(){
        int equationsNumber = this.equationsSystem.getEquationsNumber();

        Matrix upperCopy = this.upper.copy();

        this.solution = new Vector(equationsNumber);

        for (this.backwardStep = equationsNumber - 1; this.backwardStep >= 0; --this.backwardStep) {
            this.T = Matrix.Identity(equationsNumber);
            for (int j = this.backwardStep - 1; j >= 0; --j)
            {
                this.T.set(j, this.backwardStep, -upperCopy.get(j, this.backwardStep));
            }

            this.Inverse = this.T.multiply(this.Inverse);

            double sum = 0;
            for (int j = this.backwardStep + 1; j < upperCopy.getCols(); ++j){
                sum += upperCopy.get(this.backwardStep, j) * this.solution.get(j);
            }
            this.solution.set(this.backwardStep, (this.vector.get(this.backwardStep, 0) - sum) / upperCopy.get(this.backwardStep, this.backwardStep));


            if (this.callback != null){
                this.callback.call(this);
            }
        }
    }



    public void writeCurrentStateToFile(File file)
    {
        if (!this.isForwardEliminationCompleted())
        {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("Iteration " + this.getForwardStep() +": \n");
                fileWriter.write("Current transpositions: " + Arrays.toString(this.getOrderOfVariables()) + "\n");
                fileWriter.close();

                this.getLUSystem().writeToFile(file, true);

            }
            catch (IOException ignored)
            {

            }
        }
        else if (!this.isBackwardsSubstitutionCompleted()) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("Backward iteration " + this.getBackwardStep() +": \n");
                fileWriter.close();

                this.getSolution().writeIntoFile(file, true);

                FileWriter fileWriter2 = new FileWriter(file, true);
                fileWriter2.write("T matrix " + this.getBackwardStep() +": \n");
                fileWriter2.close();

                this.T.writeToFile(file, true);
            }
            catch (IOException ignored){

            }
        } else {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("Inverse matrix for initial system, and LU Matrices accordingly: \n");
                fileWriter.close();

                this.Inverse.writeToFile(file, true);
                this.upper.writeToFile(file, true);
                this.lower.writeToFile(file, true);

            } catch (IOException ignored) {}
        }
    }

    public double relativeError(){
        double AMinusOneDeltaB = this.Inverse.multiply(this.equationsSystem.getRightSideVector().subtract(
                this.equationsSystem.getLeftSideMatrix().multiply(
                        this.getSolution().asColumnMatrix()
                )
            )
        ).euclideanNorm();

        double AMinusOneB = this.Inverse.multiply(this.equationsSystem.getRightSideVector()).euclideanNorm();
        return AMinusOneDeltaB / AMinusOneB;
    }
}
