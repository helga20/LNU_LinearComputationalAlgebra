package Algebra.Methods.Jacobi;

import Algebra.Generic.LinearEquationsSystem;
import Algebra.Generic.Matrix;
import Algebra.Generic.Vector;
import Algebra.Methods.GenericMethod;

import java.io.*;
import java.util.Arrays;

public class Jacobi implements GenericMethod {

    LinearEquationsSystem equationsSystem;
    JacobiSolutionCallback callback = null;

    double eps;
    double finalError = 1;
    int maxIterations = 1000;

    Vector x;
    Vector xPlusOne;
    int iteration = 0;
    double currentError = -1;

    boolean started = false;
    boolean finished = false;

    public Jacobi(LinearEquationsSystem linearEquationsSystem, double eps, int maxIterations)
    {
        this.equationsSystem = linearEquationsSystem;
        this.callback = null;
        this.eps = eps;
        this.maxIterations = maxIterations;

        this._initialize();
    }

    public Jacobi(LinearEquationsSystem linearEquationsSystem, JacobiSolutionCallback callback, double eps, int maxIterations)
    {
        this.equationsSystem = linearEquationsSystem;
        this.callback = callback;
        this.eps = eps;
        this.maxIterations = maxIterations;

        this._initialize();
    }

    private void _initialize(){
        int n = this.equationsSystem.getEquationsNumber();
        this.x = new Vector(n);
        this.xPlusOne = new Vector(n);
    }

    public LinearEquationsSystem getEquationsSystem() {
        return this.equationsSystem;
    }

    public int getIteration() {
        return this.iteration;
    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public double getCurrentError() {
        return this.currentError;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }

    public double getFinalError() {
        return this.finalError;
    }

    public double getEps() {
        return this.eps;
    }

    public Vector getX() {
        return this.x;
    }

    public Vector getXPlusOne() {
        return this.xPlusOne;
    }


    public void Solve() {

        if (this.callback != null)
            this.callback.call(this);

        this.started = true;

        Matrix left = this.equationsSystem.getLeftSideMatrix();
        Matrix right = this.equationsSystem.getRightSideVector();

        int n = this.equationsSystem.getEquationsNumber();

        for (int i = 0; i < n; i++) {
            this.x.set(i, right.get(i, 0) / left.get(i, i));
        }

        while ((this.currentError == -1) || (this.currentError > this.eps && this.iteration < this.maxIterations))
        {
            this.currentError = 0;
            for (int i = 0; i < n; i++) {
                double sum = 0;

                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sum += left.get(i, j) * this.x.get(j);
                    }
                }

                this.xPlusOne.set(i, (right.get(i, 0) - sum) / left.get(i, i));

                double relativeError = Math.abs((this.xPlusOne.get(i) - this.x.get(i)) / this.xPlusOne.get(i));
                if (this.currentError == -1 || relativeError > this.currentError) {
                    this.currentError = relativeError;
                }
            }

            if (this.callback != null)
                this.callback.call(this);

            for (int i = 0; i < n; i++) {
                this.x.set(i, this.xPlusOne.get(i));
            }

            this.iteration++;
        }

        this.finished = true;

        if (this.iteration == this.maxIterations && this.currentError > this.eps) {
            throw new IllegalArgumentException("This matrix is divergent or too slowly convergent");
        }
        this.finalError = this.currentError;
    }

    public void writeCurrentStateToFile(File file)
    {
        try {
            FileWriter fileWriter = new FileWriter(file, true);

            if (!this.isStarted()) {
                fileWriter.write("Starting Jacobi method\n");
            }
            else if (!this.isFinished()) {
                fileWriter.write("----------\n");
                fileWriter.write("Iteration " + this.getIteration() + ": \n");
                fileWriter.write("Current x: " + this.x.toString() + "\n");
                fileWriter.write("Current error: " + this.getCurrentError() + "\n");
                fileWriter.write("++++++++++\n");
                fileWriter.close();
            }
            else {
                fileWriter.write("----------\n");
                fileWriter.write("Iterations " + this.getIteration() + ": \n");
                fileWriter.write("Solution: " + this.x.toString() + "\n");
                fileWriter.write("Final error: " + this.getCurrentError() + "\n");
                fileWriter.write("++++++++++\n");
                fileWriter.close();
            }
        }
        catch (IOException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    @Override
    public Vector getSolution() {
        return this.x;
    }

    @Override
    public double relativeError() {
        return this.finalError;
    }
}
