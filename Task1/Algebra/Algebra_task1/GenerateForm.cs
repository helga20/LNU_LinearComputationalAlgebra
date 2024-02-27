using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Algebra_task1
{
    public partial class GenerateForm : Form
    {
        Form1 mainform;
        
        
        public int RowsCount { get; set; }
        public int ColumnsCount { get; set; }
        public int LowerLimitValue { get; set; }
        public int UpperLimitValue { get; set; }
        public int MatrixNumber { get; set; }
        public GenerateForm(Form1 mainform, int number)
        {
            InitializeComponent();
            this.mainform = mainform;
            MatrixNumber = number;
        }

        private void GenerateButton_Click(object sender, EventArgs e)
        {
            if (Validation.IsInt(RowsCountBox.Text, true)
                && Validation.IsInt(ColumnsCountBox.Text, true))
            {
                RowsCount = int.Parse(RowsCountBox.Text);
                
                ColumnsCount = int.Parse(ColumnsCountBox.Text);

                if (Validation.IsDouble(LowerLimit.Text)
                    && Validation.IsDouble(UpperLimit.Text))
                {
                    int lowerLimit = int.Parse(LowerLimit.Text);
                    int upperLimit = int.Parse(UpperLimit.Text);
                    if (Validation.RangeIsCorrect(lowerLimit, upperLimit))
                    {
                        mainform.GenerateMatrix(RowsCount, ColumnsCount, lowerLimit, upperLimit, MatrixNumber);
                        this.Close();

                    }
                    else
                    {
                        MessageBox.Show("Upper limit should be bigger than lower limit! Try again");
                    }
                }
                else
                {
                    MessageBox.Show("You should enter valid limits! Try again");
                }
            }
            else
            {
                MessageBox.Show("You should enter valid size of matrix! Try again");
            }
            
        }
    }
}
