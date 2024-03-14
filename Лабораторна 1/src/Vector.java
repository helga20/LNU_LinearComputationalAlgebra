import java.io.*;
import java.util.Random;

public class Vector {

    // private fields
    private final double[] values;

    // properties access
    public int getSize(){
        return values.length;
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
        return values[index];
    }

    public void set(int index, double value){
        values[index] = value;
    }

    public void write(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < this.values.length; i++) {
                writer.write(Double.toString(this.values[i]));
                if (i < this.values.length - 1) {
                    writer.write(",");
                }
            }
        }
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
}
