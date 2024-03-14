import java.util.*;

public class CuthillMcKee {

    public static int findInitialVertex(SparseMatrix matrix) {
        int n = matrix.getNumRows();
        int maxEccentricity = -1;
        int initialVertex = -1;

        // Compute the eccentricity of each vertex
        for (int i = 0; i < n; i++) {
            int eccentricity = getEccentricity(matrix, i);
            if (eccentricity >= maxEccentricity ) {
                maxEccentricity = eccentricity;
                initialVertex = i;
            }
        }

        return initialVertex;
    }

    // Computes the eccentricity of a vertex using BFS
    private static int getEccentricity(SparseMatrix matrix, int vertex) {
        int n = matrix.getNumRows();
        int eccentricity = -1;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(vertex);
        visited[vertex] = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int currVertex = queue.poll();
                for (int neighbor : matrix.getNeighbors(currVertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            eccentricity++;
        }
        return eccentricity;
    }

    public static int[] findPermutations(SparseMatrix matrix, int startVertex) {
        int n = matrix.getNumRows();
        int[] perm = new int[n];

        int[] levels = new int[n];
        int[] degrees = new int[n];
        boolean[] visited = new boolean[n];

        var cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (levels[o1] != levels[o2])
                    return levels[o1] - levels[o2];
                if (degrees[o1] != degrees[o2])
                    return  degrees[o1] - degrees[o2];
                return o1 - o2; // o1 == 1 && o2 == 0 ?  -1 : o1 - o2;
            }
        };
        PriorityQueue<Integer> queue = new PriorityQueue<>(cmp);

        // Compute the degree of each vertex
        for (int i = 0; i < n; i++) {
            degrees[i] = matrix.getCols(i).size() - 1;
        }

        // Perform a breadth-first search starting at the start vertex
        queue.offer(startVertex);
        visited[startVertex] = true;
        levels[startVertex] = 0;

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            for (int neighbor : matrix.getNeighbors(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    levels[neighbor] = levels[vertex] + 1;
                    queue.offer(neighbor);
                }
            }
        }

        Integer[] vertices = new Integer[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = i;
        }
        Arrays.sort(vertices, cmp);

        for (int i = 0; i < vertices.length; ++i)
        {
            perm[i] = vertices[i];
        }
        return perm;
    }


    public static SparseMatrix permute(SparseMatrix matrix, int[] permutation) {
        int n = matrix.getNumRows();
        SparseMatrix result = new SparseMatrix(n, n);

        Map<Integer, Integer> newIndexMap = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            newIndexMap.put(permutation[i], i);
        }
        for (int i = 0; i < n; ++i) {
            int newRow = newIndexMap.get(i);
            for (int j : matrix.getCols(i)) {
                int newCol = newIndexMap.get(j);
                result.setValue(newRow, newCol, matrix.getValue(i, j));
            }
        }

        return result;
    }

    public static SparseMatrix getBandMatrix(SparseMatrix matrix){
        int initialVertex = CuthillMcKee.findInitialVertex(matrix);
        int[] perms = CuthillMcKee.findPermutations(matrix, initialVertex);
        for (int i = 0; i < perms.length; ++i){
            System.out.print(perms[i] + " ");
        }

        return CuthillMcKee.permute(matrix, perms);
    }


}
