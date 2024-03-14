package Algebra.Generic;

import java.io.*;
import java.util.Random;

public class Vector {

    // private fields
    private final double[] values;

    // properties access
    public int getSize(){
        return this.values.length;
    }

    // constructors
    public Vector(double[] values) {
        this.values = values;
    }

    public Vector(int size) {
        this.values = new double[size];
    }

    public static Vector random(int size, double min, double max) {
        Vector vector = new Vector(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            vector.values[i] = min + (max - min) * random.nextDouble();
        }
        return vector;
    }

    public double get(int index) {
        return this.values[index];
    }

    public void set(int index, double value){
        this.values[index] = value;
    }

    public void writeIntoFile(File file, boolean append) throws IOException {
        FileWriter writer = new FileWriter(file, append);
        for (double value : this.values) {
            writer.write(value + " ");
        }
        writer.write("\n");
        writer.close();
    }

    public double euclideanNorm() {
        double sum = 0.0;
        for (double value : this.values) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    public double maxAbsNorm() {
        double currMax = 0.0;
        for (double value : this.values) {
            if (Math.abs(value) > currMax)
                currMax = Math.abs(value);
        }
        return currMax;
    }

    public Vector add(Vector other) {
        double[] result = new double[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            result[i] = this.values[i] + other.values[i];
        }
        return new Vector(result);
    }

    public Vector subtract(Vector other) {
        double[] result = new double[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            result[i] = this.values[i] - other.values[i];
        }
        return new Vector(result);
    }

    public double dotProduct(Vector other) {
        double sum = 0.0;
        for (int i = 0; i < this.values.length; i++) {
            sum += this.values[i] * other.values[i];
        }
        return sum;
    }

    public Matrix asColumnMatrix()
    {
        Matrix matrix = new Matrix(this.values.length, 1);
        for (int i = 0; i < this.values.length; i++) {
            matrix.set(i, 0, this.values[i]);
        }
        return matrix;
    }

    public Matrix asRowMatrix()
    {
        Matrix matrix = new Matrix(1, this.values.length);
        for (int i = 0; i < this.values.length; i++) {
            matrix.set(0, i, this.values[i]);
        }
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double val: this.values) {
            sb.append(val).append(" ");
        }
        return sb.toString();
    }
}
