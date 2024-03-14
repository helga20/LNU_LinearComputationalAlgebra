import java.io.*;
import java.util.*;

public class SparseMatrix {

    private final int numRows;
    private final int numCols;
    private final HashMap<Integer, HashMap<Integer, Double>> matrix;

    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.matrix = new HashMap<>();
    }

    public int getNumCols() {
        return this.numCols;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumElems(){
        int elems = 0;
        for (var row: this.matrix.keySet()) {
            elems += this.matrix.get(row).size();
        }
        return elems;
    }

    public Set<Integer> getRows(){
        return this.matrix.keySet();
    }

    public Set<Integer> getCols(int i){
        if (this.matrix.containsKey(i))
            return this.matrix.get(i).keySet();
        return new HashSet<Integer>();
    }

    public void setValue(int row, int col, double value) {
        if (row < 0 || row >= this.numRows || col < 0 || col >= this.numCols) {
            throw new IllegalArgumentException("Invalid row or column index");
        }
        if (value == 0) {
            if (this.matrix.containsKey(row)) {
                this.matrix.get(row).remove(col);
                if (this.matrix.get(row).isEmpty()) {
                    this.matrix.remove(row);
                }
            }
        } else {
            if (!this.matrix.containsKey(row)) {
                this.matrix.put(row, new HashMap<>());
            }
            this.matrix.get(row).put(col, value);
        }
    }

    public double getValue(int row, int col) {
        if (row < 0 || row >= this.numRows || col < 0 || col >= this.numCols) {
            throw new IllegalArgumentException("Invalid row or column index");
        }
        if (this.matrix.containsKey(row) && this.matrix.get(row).containsKey(col)) {
            return this.matrix.get(row).get(col);
        }
        return 0.0;
    }

    public SparseMatrix add(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrices must have the same dimensions");
        }
        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        for (int i : this.matrix.keySet()) {
            HashMap<Integer, Double> row = this.matrix.get(i);
            for (int j : row.keySet()) {
                double sum = row.get(j) + other.getValue(i, j);
                if (sum != 0) {
                    result.setValue(i, j, sum);
                }
            }
        }
        for (int i : other.matrix.keySet()) {
            HashMap<Integer, Double> row = other.matrix.get(i);
            for (int j : row.keySet()) {
                if (!this.matrix.containsKey(i) || !this.matrix.get(i).containsKey(j)) {
                    double sum = row.get(j);
                    if (sum != 0) {
                        result.setValue(i, j, sum);
                    }
                }
            }
        }
        return result;
    }

    public SparseMatrix subtract(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrices must have the same dimensions");
        }
        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        for (int i : this.matrix.keySet()) {
            HashMap<Integer, Double> row = this.matrix.get(i);
            for (int j : row.keySet()) {
                double diff = row.get(j) - other.getValue(i, j);
                if (diff != 0) {
                    result.setValue(i, j, diff);
                }
            }
        }
        for (int i : other.matrix.keySet()) {
            HashMap<Integer, Double> row = other.matrix.get(i);
            for (int j : row.keySet()) {
                if (!this.matrix.containsKey(i) || !this.matrix.get(i).containsKey(j)) {
                    double diff = -row.get(j);
                    if (diff != 0) {
                        result.setValue(i, j, diff);
                    }
                }
            }
        }
        return result;
    }

    public SparseMatrix multiply(SparseMatrix other) {
        if (this.numCols != other.numRows) {
            throw new IllegalArgumentException("The number of columns in the first matrix must match the number of rows in the second matrix");
        }
        SparseMatrix result = new SparseMatrix(this.numRows, other.numCols);
        for (int i : this.matrix.keySet()) {
            HashMap<Integer, Double> row = this.matrix.get(i);
            for (int k : row.keySet()) {
                double value = row.get(k);
                if (value == 0) {
                    continue;
                }
                HashMap<Integer, Double> otherRow = other.matrix.get(k);
                if (otherRow == null) {
                    continue;
                }
                for (int j : otherRow.keySet()) {
                    double product = value * otherRow.get(j);
                    double sum = result.getValue(i, j) + product;
                    result.setValue(i, j, sum);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                sb.append(String.format("%8.2f", this.getValue(i, j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static SparseMatrix fromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String[] dimensions = reader.readLine().split(" ");
        int numRows = Integer.parseInt(dimensions[0]);
        int numCols = Integer.parseInt(dimensions[1]);
        SparseMatrix matrix = new SparseMatrix(numRows, numCols);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(" ");
            int row = Integer.parseInt(values[0]);
            int col = Integer.parseInt(values[1]);
            double value = Double.parseDouble(values[2]);
            matrix.setValue(row, col, value);
        }
        reader.close();
        return matrix;
    }

    public void toFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(this.numRows + " " + this.numCols + "\n");
        for (int i : this.matrix.keySet()) {
            HashMap<Integer, Double> row = this.matrix.get(i);
            for (int j : row.keySet()) {
                double value = row.get(j);
                if (value != 0) {
                    writer.write(i + " " + j + " " + value + "\n");
                }
            }
        }
        writer.close();
    }

    public static SparseMatrix generateRandomSparseMatrix(
            int numRows,
            int numCols,
            double minValue,
            double maxValue,
            double sparsity,
            boolean symmetric
    ) {
        SparseMatrix matrix = new SparseMatrix(numRows, numCols);
        Random random = new Random();
        if (!symmetric) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if (random.nextDouble() > sparsity) {
                        double value = minValue + (maxValue - minValue) * random.nextDouble();
                        matrix.setValue(i, j, value);
                    }
                }
            }
            return matrix;
        }
        else {
            for (int i = 0; i < numRows; i++) {
                for (int j = i; j < numCols; j++) {
                    if (random.nextDouble() > sparsity) {
                        double value = minValue + (maxValue - minValue) * random.nextDouble();
                        matrix.setValue(i, j, value);
                        matrix.setValue(j, i, value);
                    }
                }
            }
            return matrix;
        }
    }

    // graph part
    public List<Integer> getNeighbors(int vertex) {
        if (vertex < 0 || vertex >= this.numRows) {
            throw new IllegalArgumentException("Invalid vertex index");
        }
        if (this.matrix.containsKey(vertex)) {
            return this.matrix.get(vertex).keySet().stream().filter(integer -> integer != vertex).toList();
        }
        return new ArrayList<>();
    }

}
