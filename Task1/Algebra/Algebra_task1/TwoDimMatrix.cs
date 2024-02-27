using System;
using System.Collections.Generic;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace Algebra_task1
{
    public class TwoDimMatrix
    {
        public int RowsCount { get; set; }
        public int ColumnsCount { get; set; }
        public double[,] Matrix { get; set; }
        public TwoDimMatrix() 
        {
            Matrix = new double[0, 0];
        }

        public TwoDimMatrix(int N, int M) 
        {
            this.RowsCount = N;
            this.ColumnsCount = M;
            this.Matrix = new double[N, M];
        }

        public override string ToString()
        {
            string toPrint = "";
            for (int i = 0; i < RowsCount; i++)
            {
                for (int j = 0; j < ColumnsCount; j++)
                {
                    toPrint += Matrix[i, j] + " ";
                }
                toPrint += "\r\n";
            }
            return toPrint;
        }

        public static TwoDimMatrix operator -(TwoDimMatrix a, TwoDimMatrix b)
        {
             
            TwoDimMatrix result_matrix = new TwoDimMatrix(a.RowsCount, a.ColumnsCount);
                for (int i = 0; i < a.RowsCount; i++)
                {
                    for (int j = 0; j < a.ColumnsCount; j++)
                    {
                        result_matrix.Matrix[i, j] = a.Matrix[i, j] - b.Matrix[i, j];
                    }
                }
                
           
            return result_matrix;
        }
        public static TwoDimMatrix operator +(TwoDimMatrix a, TwoDimMatrix b)
        {
            
            TwoDimMatrix result_matrix = new TwoDimMatrix(a.RowsCount, a.ColumnsCount);
            for (int i = 0; i < a.RowsCount; i++)
            {
                for (int j = 0; j < a.ColumnsCount; j++)
                {
                    result_matrix.Matrix[i, j] = a.Matrix[i, j] + b.Matrix[i, j];
                }
            }
            
            return result_matrix;
        }

        public static TwoDimMatrix operator *(TwoDimMatrix a, TwoDimMatrix b)
        {
          

                TwoDimMatrix result_matrix = new TwoDimMatrix(a.RowsCount, b.ColumnsCount);

                for (int i = 0; i < a.RowsCount; i++)
                {
                    for (int j = 0; j < b.ColumnsCount; j++)
                    {
                        result_matrix.Matrix[i, j] = 0;

                        for (var k = 0; k < a.ColumnsCount; k++)
                        {
                            result_matrix.Matrix[i, j] += a.Matrix[i, k] * b.Matrix[k, j];
                        }
                    }
                }
            
            return result_matrix;
        }

        public void Random_matrix_generation(int lower_limit, int upper_limit)
        {
            var random = new Random();
            for (int i = 0; i<RowsCount; i++)
            {
                for(int j =0; j < ColumnsCount; j++)
                {
                    Matrix[i, j] = random.Next(lower_limit, upper_limit);
                    Matrix[j, i] = Matrix[i, j];
                }
            }
        }

        public TwoDimMatrix ReadMatrixFromFile(string filename)
        {
                string[] lines = File.ReadAllLines(filename);
                int rowsCount = lines.Length;
                int columnsCount = lines[0].Split(' ').Length;
                TwoDimMatrix newMatrix = new TwoDimMatrix(rowsCount, columnsCount);

                for (int i = 0; i < rowsCount; i++)
                {
                    string[] rowValues = lines[i].Split(' ');
                    for (int j = 0; j < columnsCount; j++)
                        newMatrix.Matrix[i, j] = double.Parse(rowValues[j]);
                }
                return newMatrix;
        }

        public void WriteMatrixIntoFile(string filename)
        {
            File.WriteAllText(filename, this.ToString());
        }

        public double MatrixNorm()
        {
            double result = 0.0;

            for (int i = 0; i < RowsCount; i++)
            {
                double sum = 0.0;
                for (int j = 0; j < ColumnsCount; j++)
                {
                    sum += Math.Abs(Matrix[i, j]);
                }
                result = Math.Max(result, sum);
            }

            return result;
        }
    }
}
