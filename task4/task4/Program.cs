using System;


////double[,] A = new double[,]{
////    {1, 2, 3, 4, 0, 0 },
////    { 2, 5, 8, 7, 0, 0},
////    { 3, 8, 14, 12, 3, 4},
////    { 4, 7, 12, 22, 8, 7},
////    { 0, 0, 3, 8, 14, 12},
////    { 0, 0, 4, 7, 12, 22 } };

////double[] b = new double[] { 10, 22, 44, 60, 37, 45 };

//double[,] A = {
//    {1, 2, 3, 4},
//    {2, 5, 8, 7},
//    {3, 8, 15, 14},
//    {4, 7, 14, 27}
//};

//double[] b = { 10, 22, 40, 52 };
//double k = -0.4285714285714288 + 2* -0.4285714285714288 + 3 * -0.4285714285714288+4* -0.4285714285714288;
//Console.WriteLine(k);

//BlockGaussSolver.Solve(A, b, 2);
////double[] x = solver.Solve(A, b, 2);

//Console.WriteLine("Solution:");
//for (int i = 0; i < b.Length; i++)
//{
//    Console.WriteLine("x[{0}] = {1}", i, b[i]);
//}



//public class FrontalSolver
//{
//    public double[] Solve(double[,] A, double[] b)
//    {
//        int n = A.GetLength(0);
//        Factorize(A);
//        ForwardSubstitution(A, b);

//        // Backward substitution
//        double[] x = new double[n];
//        for (int i = n - 1; i >= 0; i--)
//        {
//            x[i] = b[i];
//            for (int j = i + 1; j < n; j++)
//            {
//                x[i] -= A[i, j] * x[j];
//            }
//            x[i] /= A[i, i];
//        }

//        return x;
//    }

//    private void Factorize(double[,] A)
//    {
//        int n = A.GetLength(0);
//        for (int j = 0; j < n; j++)
//        {
//            // Find block rows
//            int[] rows = new int[n];
//            int k = 0;
//            for (int i = j; i < n; i++)
//            {
//                if (A[i, j] != 0)
//                {
//                    rows[k] = i;
//                    k++;
//                }
//            }

//            // Build block matrix
//            double[,] B = new double[k, k];
//            for (int i = 0; i < k; i++)
//            {
//                for (int l = 0; l < k; l++)
//                {
//                    B[i, l] = A[rows[i], rows[l]];
//                }
//            }

//            // Factorize block matrix
//            LUFactorization(B);

//            // Update A
//            for (int i = j; i < n; i++)
//            {
//                if (A[i, j] != 0)
//                {
//                    for (int l = 0; l < k; l++)
//                    {
//                        A[i, rows[l]] = B[(i - j), l];
//                    }
//                }
//            }

//            j += k - 1;
//        }
//    }

//    private void LUFactorization(double[,] A)
//    {
//        int n = A.GetLength(0);
//        for (int k = 0; k < n - 1; k++)
//        {
//            for (int i = k + 1; i < n; i++)
//            {
//                A[i, k] /= A[k, k];
//                for (int j = k + 1; j < n; j++)
//                {
//                    A[i, j] -= A[i, k] * A[k, j];
//                }
//            }
//        }
//    }

//    private void ForwardSubstitution(double[,] A, double[] b)
//    {
//        int n = A.GetLength(0);
//        for (int k = 0; k < n - 1; k++)
//        {
//            for (int i = k + 1; i < n; i++)
//            {
//                b[i] -= A[i, k] * b[k];
//            }
//        }
//    }
//}




public partial class Program
{
    public static void Main()
    {
        double[,] matrix = {
            { 1, 2, 3, 4, 0, 0},
            {2, 5, 8, 7, 0, 0 },
            {3, 8, 14, 12, 3, 4 },
            {4, 7, 12, 22, 8, 7 },
            {0, 0, 3, 8, 14, 12 },
            {0, 0, 4, 7, 12, 22}
        };
        
        double[] b = { 10, 22, 44, 60, 37, 45 };
    }
    public void solve(double[,] A, double[] b)
    {

    }

    public double[,] Multiply(double[,] a, double[,] b)
    {
        int n = a.GetLength(0);
        int m = b.GetLength(1);
        double[,] result_matrix = new double[n, m];

        for (int i = 0; i < n; i++) 
        {
            for (int j = 0; j < m; j++)
            {
                result_matrix[i, j] = 0;

                for (var k = 0; k < a.GetLength(1); k++)
                {
                    result_matrix[i, j] += a[i, k] * b[k, j];
                }
            }
        }

        return result_matrix;
    }
}
     

