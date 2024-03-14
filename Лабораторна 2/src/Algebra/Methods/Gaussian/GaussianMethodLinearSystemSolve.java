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
        return lower;
    }

    public Matrix getUpper() {
        return upper;
    }

    public Matrix getVector() {
        return vector;
    }

    public Vector getSolution() {
        return solution;
    }

    public int[] getOrderOfVariables() {
        return orderOfVariables;
    }

    public boolean isBackwardsSubstitutionCompleted() {
        return backwardsSubstitutionCompleted;
    }

    public boolean isForwardEliminationCompleted() {
        return forwardEliminationCompleted;
    }

    public int getBackwardStep() {
        return backwardStep;
    }

    public int getForwardStep() {
        return forwardStep;
    }

    public Matrix getInverse() {
        return Inverse;
    }

    public LinearEquationsSystem getEquationsSystem() {
        return equationsSystem;
    }

    public LinearEquationsSystem getLUSystem() {
        return LUSystem;
    }

    public GaussianMethodLinearSystemSolve(LinearEquationsSystem system)
    {
        this.equationsSystem = system;
        _initialize();
    }

    public GaussianMethodLinearSystemSolve(LinearEquationsSystem system, GaussianSolutionCallback callback)
    {
        this.equationsSystem = system;
        this.callback = callback;
        _initialize();
    }

    private void _initialize(){
        int equationsNumber = this.equationsSystem.getEquationsNumber();
        orderOfVariables = new int[this.equationsSystem.getEquationsNumber()];
        for(int i = 0; i < orderOfVariables.length; ++i){
            orderOfVariables[i] = i;
        }

        this.lower = Matrix.Identity(equationsNumber);
        this.vector = this.equationsSystem.getRightSideVector().copy();
        this.upper = this.equationsSystem.getLeftSideMatrix().copy();

        this.LUSystem = new LinearEquationsSystem(upper, vector);
        this.Inverse = Matrix.Identity(equationsNumber);
    }

    public void Solve()
    {
        if (callback != null){
            callback.call(this);
        }
        this.forwardElimination();
        this.forwardEliminationCompleted = true;

        this.backwardSubstitution();
        this.backwardsSubstitutionCompleted = true;

        if (callback != null){
            callback.call(this);
        }
    }


    private void forwardElimination()
    {
        int equationsNumber = this.equationsSystem.getEquationsNumber();

        // every iteration of direct
        for (forwardStep = 0; forwardStep < equationsNumber; ++forwardStep){

            // step 1: choose max pivot
            int pivot_row = forwardStep;
            for (int j = forwardStep + 1; j < equationsNumber; ++j)
            {
                if (Math.abs(upper.get(j, forwardStep)) > Math.abs(upper.get(pivot_row, forwardStep)))
                {
                    pivot_row = j;
                }
            }

            // step 2: check if matrix is not singular (== degenerate)
            if (upper.get(pivot_row, forwardStep) == 0)
            {
                throw new IllegalArgumentException("This matrix is singular and might have either infinity, or none solutions");
            }


            // step 3: if pivot row is other than k
            if (pivot_row != forwardStep)
            {
                upper.swapRows(forwardStep, pivot_row);
                lower.swapRows(forwardStep, pivot_row);

                Inverse.swapRows(forwardStep, pivot_row);

                this.vector.swapRows(forwardStep, pivot_row);

                int temp = this.orderOfVariables[forwardStep];
                this.orderOfVariables[forwardStep] = this.orderOfVariables[pivot_row];
                this.orderOfVariables[pivot_row] = temp;
            }


            // step 4: transform lower non-pivot rows
            for(int i = forwardStep + 1; i < upper.getRows(); ++i){
                double factor = upper.get(i, forwardStep) / upper.get(forwardStep, forwardStep);
                lower.set(i, forwardStep, -factor);

                for (int j = forwardStep + 1; j < upper.getCols(); ++j)
                {
                    upper.set(i, j, upper.get(i, j) - factor * upper.get(forwardStep, j));
                }
                for (int j = 0; j < Inverse.getCols(); ++j)
                {
                    Inverse.set(i, j, Inverse.get(i, j) - factor * Inverse.get(forwardStep, j));
                }

                upper.set(i, forwardStep, 0);
                vector.set(i, 0, vector.get(i, 0) - factor * vector.get(forwardStep, 0));

            }
            lower.set(forwardStep, forwardStep, 1 / upper.get(forwardStep, forwardStep));


            // step 5: normalize pivot row
            double pivot_value = upper.get(forwardStep, forwardStep);
            for (int j = 0; j < upper.getCols(); ++j)
            {
                upper.set(forwardStep, j, upper.get(forwardStep, j) / pivot_value);
            }
            for (int j = 0; j < Inverse.getCols(); ++j)
            {
                Inverse.set(forwardStep, j, Inverse.get(forwardStep, j) / pivot_value);
            }
            vector.set(forwardStep, 0, vector.get(forwardStep, 0) / pivot_value);

            if (callback != null){
                callback.call(this);
            }
        }
    }

    public void backwardSubstitution(){
        int equationsNumber = this.equationsSystem.getEquationsNumber();

        Matrix upperCopy = upper.copy();

        solution = new Vector(equationsNumber);

        for (backwardStep = equationsNumber - 1; backwardStep >= 0; --backwardStep) {
            T = Matrix.Identity(equationsNumber);
            for (int j = backwardStep - 1; j >= 0; --j)
            {
                T.set(j, backwardStep, -upperCopy.get(j, backwardStep));
            }

            Inverse = T.multiply(Inverse);

            double sum = 0;
            for (int j = backwardStep + 1; j < upperCopy.getCols(); ++j){
                sum += upperCopy.get(backwardStep, j) * solution.get(j);
            }
            solution.set(backwardStep, (vector.get(backwardStep, 0) - sum) / upperCopy.get(backwardStep, backwardStep));


            if (callback != null){
                callback.call(this);
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

                Inverse.writeToFile(file, true);
                upper.writeToFile(file, true);
                lower.writeToFile(file, true);

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
