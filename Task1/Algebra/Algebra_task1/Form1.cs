using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml.Linq;

namespace Algebra_task1
{
    public partial class Form1 : Form
    {
        private TwoDimMatrix FirstMatrix { get; set; }
        private TwoDimMatrix SecondMatrix { get; set; }
        private TwoDimMatrix ResultMatrix { get; set; }
        private string path = "C:\\Users\\lysko\\Studying\\Лінійна обчислювальна алгебра\\Task1\\Algebra\\Algebra_task1\\";
        public Form1()
        {
            InitializeComponent();
        }

        

        private void GenerateButton_Click(object sender, EventArgs e)
        {
            GenerateForm item = new GenerateForm(this, 1);
            item.Show();   
        }
        
        private void ReadMatrixFromFile_Click(object sender, EventArgs e)
        {
            string file = path + "FirstMatrix.txt";
            if (File.Exists(file))
            {
                FirstMatrix = ReadFromFile(file);
                WriteMatrixIntoGrid(FirstMatrix, FirstMatrixGrid);
            }
            else
            {
                MessageBox.Show("File doents exist! Try again");
            }
        }

        public static void WriteMatrixIntoGrid(TwoDimMatrix matrix, DataGridView grid)
        {
            grid.Columns.Clear();
            grid.ColumnCount = matrix.ColumnsCount;

            for (int r = 0; r < matrix.RowsCount; r++)
            {
                DataGridViewRow row = new DataGridViewRow();
                row.CreateCells(grid);

                for (int c = 0; c < matrix.ColumnsCount; c++)
                {
                    row.Cells[c].Value = matrix.Matrix[r, c];
                }
                grid.Rows.Add(row);
            }
        }

        public void GenerateMatrix(int rows, int columns, int lower, int upper, int number)
        {
            if (number == 1)
            {
                FirstMatrix = new TwoDimMatrix(rows, columns);
                FirstMatrix.Random_matrix_generation(lower, upper);
                WriteMatrixIntoGrid(FirstMatrix, FirstMatrixGrid);
            }
            //else if (number == 2) 
            //{
            //    SecondMatrix = new TwoDimMatrix(rows, columns);
            //    SecondMatrix.Random_matrix_generation(lower, upper);
            //    WriteMatrixIntoGrid(SecondMatrix, SecondMatrixGrid);
            //}

        }

        private void GenerateSecondMatrix_Click(object sender, EventArgs e)
        {
            GenerateForm item = new GenerateForm(this, 2);
            item.Show();
        }

        //private void PlusButton_Click(object sender, EventArgs e)
        //{
        //    if (FirstMatrix == null || SecondMatrix == null)
        //    {
        //        MessageBox.Show("Fill matrixes first!!");
        //    }
        //    else
        //    {
        //        if (FirstMatrix.RowsCount == SecondMatrix.RowsCount
        //        && FirstMatrix.ColumnsCount == SecondMatrix.ColumnsCount)
        //        {
        //            ResultMatrix = new TwoDimMatrix(0, 0);
        //            ResultMatrix = FirstMatrix + SecondMatrix;
        //            WriteMatrixIntoGrid(ResultMatrix, ResultGrid);
        //        }
        //        else
        //        {
        //            MessageBox.Show("You can`t add matrixes with different sizes");
        //        }
        //    }
            
        //}

        //private void MinusButton_Click(object sender, EventArgs e)
        //{
        //    if (FirstMatrix == null || SecondMatrix == null)
        //    {
        //        MessageBox.Show("Fill matrixes first!!");
        //    }
        //    else
        //    {
        //        if (FirstMatrix.RowsCount == SecondMatrix.RowsCount
        //        && FirstMatrix.ColumnsCount == SecondMatrix.ColumnsCount)
        //        {
        //            ResultMatrix = new TwoDimMatrix(0, 0);
        //            ResultMatrix = FirstMatrix - SecondMatrix;
        //            WriteMatrixIntoGrid(ResultMatrix, ResultGrid);
        //        }
        //        else
        //        {
        //            MessageBox.Show("You can`t minus this matrixes");
        //        }
        //    }
            
        //}

        //private void MultiplyButton_Click(object sender, EventArgs e)
        //{
        //    if (FirstMatrix == null || SecondMatrix == null)
        //    {
        //        MessageBox.Show("Fill matrixes first!!");
        //    }
        //    else
        //    {
        //        if (FirstMatrix.ColumnsCount == SecondMatrix.RowsCount)
        //        {
        //            ResultMatrix = new TwoDimMatrix(0, 0);
        //            ResultMatrix = FirstMatrix * SecondMatrix;
        //            WriteMatrixIntoGrid(ResultMatrix, ResultGrid);
        //        }
        //        else
        //        {
        //            MessageBox.Show("You can`t multiply this matrixes");
        //        }
        //    }
            
        //}

        private void ApplyChangesOne_Click(object sender, EventArgs e)
        {
            for (int r = 0; r < FirstMatrixGrid.RowCount-1; r++)
            {
                for (int c = 0; c < FirstMatrixGrid.ColumnCount; c++)
                {
                    string res = FirstMatrixGrid[c, r].Value.ToString();
                    FirstMatrix.Matrix[r, c] = double.Parse(res);
                   
                }
                
            }
            MessageBox.Show("Changes are saved");
        }
        public TwoDimMatrix ReadFromFile(string file)
        {
            TwoDimMatrix m = new TwoDimMatrix();
            m = m.ReadMatrixFromFile(file);
            return m;
            
        }

        //private void ReadMatrixTwoFromFile_Click(object sender, EventArgs e)
        //{
        //    string file = path + "SecondMatrix.txt";
        //    if (File.Exists(file))
        //    {
        //        SecondMatrix = ReadFromFile(file);
        //        WriteMatrixIntoGrid(SecondMatrix, SecondMatrixGrid);
        //    }
        //    else
        //    {
        //        MessageBox.Show("File doents exist! Try again");
        //    }
        //}

        //private void ApplyChangesTwo_Click(object sender, EventArgs e)
        //{
        //    for (int r = 0; r < SecondMatrixGrid.RowCount - 1; r++)
        //    {
        //        for (int c = 0; c < SecondMatrixGrid.ColumnCount; c++)
        //        {
        //            string res = SecondMatrixGrid[c, r].Value.ToString();
        //            SecondMatrix.Matrix[r, c] = double.Parse(res);

        //        }

        //    }
        //    MessageBox.Show("Changes are saved");
        //}

        private void NormOne_Click(object sender, EventArgs e)
        {
            if (FirstMatrix != null)
            {
                double norm = FirstMatrix.MatrixNorm();
                MessageBox.Show("Norm = " + norm);
            }
            else
            {
                MessageBox.Show("Fill matrix first!!");
            }
        }

        

        private void NormTwo_Click(object sender, EventArgs e)
        {
            if (SecondMatrix != null)
            {
                double norm = SecondMatrix.MatrixNorm();
                MessageBox.Show("Norm = " + norm);
            }
            else
            {
                MessageBox.Show("Fill matrix first!!");
            }
            
        }

        private void WriteInFileOne_Click(object sender, EventArgs e)
        {
            if (FirstMatrix != null)
            {
                FirstMatrix.WriteMatrixIntoFile(path + "FirstMatrixResult.txt");
            }
            else
            {
                MessageBox.Show("Fill matrix first!!");
            }
        }

        private void WriteInFileTwo_Click(object sender, EventArgs e)
        {
            if (SecondMatrix != null)
            {
                SecondMatrix.WriteMatrixIntoFile(path + "SecondMatrixResult.txt");
            }
            else
            {
                MessageBox.Show("Fill matrix first!!");
            }
            
        }

        private void Gausse1_Click(object sender, EventArgs e)
        {
            Sole firstSOLE = new Sole(FirstMatrix);
            firstSOLE.GausseMethod(FirstMatrix);
            MessageBox.Show(firstSOLE.getResult());
            
        }

        private void LU_Click(object sender, EventArgs e)
        {
            Sole firstSOLE = new Sole(FirstMatrix);
            TwoDimMatrix L;
            TwoDimMatrix U;
            firstSOLE.LUDecomposition(FirstMatrix, out L, out U);
            MessageBox.Show("A: \n" + FirstMatrix.ToString() + "\n\nL:\n" + L.ToString() + "\n\nU:\n" + U.ToString());
        }

        private void Inverse_Click(object sender, EventArgs e)
        {
            
            Sole firstSOLE = new Sole(FirstMatrix);
            TwoDimMatrix L;
            TwoDimMatrix U;
            firstSOLE.LUDecomposition(FirstMatrix, out L, out U);
            TwoDimMatrix inv =  firstSOLE.InverseMatrix(FirstMatrix, L, U);
            MessageBox.Show(inv.ToString());

        }

        private void CuthillMcKee_Click(object sender, EventArgs e)
        {
            try
            {
                if (FirstMatrix != null)
                {
                    CuthillMcKee cm = new CuthillMcKee();

                    List<List<int>> matrix = new List<List<int>>();

                    for (var i = 0; i < FirstMatrix.RowsCount; i++)
                    {
                        List<int> temp = new List<int>();
                        for (var j = 0; j < FirstMatrix.RowsCount; j++)
                        {
                            temp.Add((int)FirstMatrix.Matrix[i, j]);
                        }
                        matrix.Add(temp);
                    }
                    var res = cm.Cuthill_McKee_Algorithm(matrix);
                    var pr = cm.MatrixToString(res);
                    textBox1.Text = pr;
                }
            }
            catch(ArgumentException ex)
            {
                MessageBox.Show(ex.Message);
            }
            
           
        }
    }
}
