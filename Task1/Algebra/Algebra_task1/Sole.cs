using System;
using System.Collections.Generic;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Algebra_task1
{
    internal class Sole
    {
        public TwoDimMatrix Ab { get; set; }
        public Vector X { get; set; }
        
        public Sole(TwoDimMatrix a)
        {
            Ab = a;
            X = new Vector(Ab.RowsCount);
        }

        public string getResult()
        {
            string res = "";
            for (int i = 0; i < X.DataLenght; i++)
            {
                res += "X" + (i + 1) + " = " + X.Data[i] + " \n";
            }
            return res;
        }
        
        public void GausseMethod(TwoDimMatrix AbMatrix)
        {
            string path = "C:\\Users\\lysko\\Studying\\Лінійна обчислювальна алгебра\\Task1\\Algebra\\Algebra_task1\\Gausse.txt";
            
            int rowCount = AbMatrix.RowsCount;
            int colCount = AbMatrix.ColumnsCount;

            for (int row = 0; row < rowCount - 1; row++)
            {
                int maxRow = row;
                // Знаходження максимального елемента в стовпці i
                for (int i = row + 1; i < rowCount; i++)
                {
                    if (Math.Abs(AbMatrix.Matrix[i, row]) > Math.Abs(AbMatrix.Matrix[maxRow, row]))
                    {
                        maxRow = i;
                    }
                }
                // Обмін рядків
                if (maxRow != row)
                {
                    for (int i = 0; i < colCount; i++)
                    {
                        double temp = AbMatrix.Matrix[row, i];
                        AbMatrix.Matrix[row, i] = AbMatrix.Matrix[maxRow, i];
                        AbMatrix.Matrix[maxRow, i] = temp;
                    }
                }
                File.AppendAllText(path, AbMatrix.ToString());
               
                for (int i = row + 1; i < rowCount; i++)
                {
                    double factor = AbMatrix.Matrix[i, row] / AbMatrix.Matrix[row, row];

                    for (int j = row; j < colCount; j++)
                    {
                        AbMatrix.Matrix[i, j] -= factor * AbMatrix.Matrix[row, j];
                    }
                }
                File.AppendAllText(path, AbMatrix.ToString());
            }
            // Зворотній хід методу Гауса
            Vector solution = new Vector(rowCount);

            for (int i = rowCount - 1; i >= 0; i--)
            {
                double sum = 0;

                for (int j = i + 1; j < colCount - 1; j++)
                {
                    sum += AbMatrix.Matrix[i, j] * solution.Data[j];
                }

                solution.Data[i] = (AbMatrix.Matrix[i, colCount - 1] - sum) / AbMatrix.Matrix[i, i];
            }

            X = solution;
        }

       

        public void LUDecomposition(TwoDimMatrix matrix, out TwoDimMatrix L, out TwoDimMatrix U)
        {
            int n = matrix.RowsCount;
            L = new TwoDimMatrix(n, n);
            U = new TwoDimMatrix(n, n);

            // Filling the L and U matrices with zeros
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    L.Matrix[i, j] = 0;
                    U.Matrix[i, j] = 0;
                }
            }

            // Filling the matrix U
            for (int j = 0; j < n; j++)
            {
                U.Matrix[0, j] = matrix.Matrix[0, j];
            }

            // Filling the matrix L
            for (int i = 1; i < n; i++)
            {
                L.Matrix[i, 0] = matrix.Matrix[i, 0] / U.Matrix[0, 0];
            }

            // Decomposing the matrix into L and U
            for (int i = 1; i < n; i++)
            {
                for (int j = i; j < n; j++)
                {
                    var sum = 0.0;
                    for (int k = 0; k < i; k++)
                    {
                        sum += L.Matrix[i, k] * U.Matrix[k, j];
                    }
                    U.Matrix[i, j] = matrix.Matrix[i, j] - sum;
                }

                for (int j = i + 1; j < n; j++)
                {
                    double sum = 0;
                    for (int k = 0; k < i; k++)
                    {
                        sum += L.Matrix[j, k] * U.Matrix[k, i];
                    }
                    L.Matrix[j, i] = (matrix.Matrix[j, i] - sum) / U.Matrix[i, i];
                }
            }

            for (int i = 0; i < n; i++)
            {
                L.Matrix[i, i] = 1;
            }
        }

       

        public TwoDimMatrix InverseMatrix(TwoDimMatrix A, TwoDimMatrix L, TwoDimMatrix U)
        {
            int n = A.RowsCount;
            var invA = new TwoDimMatrix(n, n);

            for (int j = 0; j < n; j++)
            {
                double[] e = new double[n];
                e[j] = 1;
                double[] y = new double[n];
                double[] x = new double[n];
                for (int i = 0; i < n; i++)
                {
                    double sum = 0;
                    for (int k = 0; k < i; k++)
                    {
                        sum += L.Matrix[i, k] * y[k];
                    }
                    y[i] = e[i] - sum;
                }
                for (int i = n - 1; i >= 0; i--)
                {
                    double sum = 0;
                    for (int k = i + 1; k < n; k++)
                    {
                        sum += U.Matrix[i, k] * x[k];
                    }
                    x[i] = (y[i] - sum) / U.Matrix[i, i];
                }
                for (int i = 0; i < n; i++)
                {
                    invA.Matrix[i, j] = x[i];
                }
            }

            return invA;
        }


        public double[] FrontalGausse(TwoDimMatrix matrix)
        {
            int n = matrix.RowsCount;
            double[] b = new double[n];
            double[,] A = new double[n, n-1];

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (j == n - 1)
                    {
                        b[i] = matrix.Matrix[i, j]; 
                    }
                    else
                    {
                        A[i, j] = matrix.Matrix[i, j];
                    }
                }
            }

            
            Factorize(A);
            ForwardSubstitution(A, b);

            // Backward substitution
            double[] x = new double[n];
            for (int i = n - 1; i >= 0; i--)
            {
                x[i] = b[i];
                for (int j = i + 1; j < n; j++)
                {
                    x[i] -= A[i, j] * x[j];
                }
                x[i] /= A[i, i];
            }

            return x;
        }

        private void Factorize(double[,] A)
        {
            int n = A.GetLength(0);
            for (int j = 0; j < n; j++)
            {
                // Find block rows
                int[] rows = new int[n];
                int k = 0;
                for (int i = j; i < n; i++)
                {
                    if (A[i, j] != 0)
                    {
                        rows[k] = i;
                        k++;
                    }
                }

                // Build block matrix
                double[,] B = new double[k, k];
                for (int i = 0; i < k; i++)
                {
                    for (int l = 0; l < k; l++)
                    {
                        B[i, l] = A[rows[i], rows[l]];
                    }
                }

                // Factorize block matrix
                LUFactorization(B);

                // Update A
                for (int i = j; i < n; i++)
                {
                    if (A[i, j] != 0)
                    {
                        for (int l = 0; l < k; l++)
                        {
                            A[i, rows[l]] = B[(i - j), l];
                        }
                    }
                }

                j += k - 1;
            }
        }

        private void LUFactorization(double[,] A)
        {
            int n = A.GetLength(0);
            for (int k = 0; k < n - 1; k++)
            {
                for (int i = k + 1; i < n; i++)
                {
                    A[i, k] /= A[k, k];
                    for (int j = k + 1; j < n; j++)
                    {
                        A[i, j] -= A[i, k] * A[k, j];
                    }
                }
            }
        }

        private void ForwardSubstitution(double[,] A, double[] b)
        {
            int n = A.GetLength(0);
            for (int k = 0; k < n - 1; k++)
            {
                for (int i = k + 1; i < n; i++)
                {
                    b[i] -= A[i, k] * b[k];
                }
            }
        }
    }
}
