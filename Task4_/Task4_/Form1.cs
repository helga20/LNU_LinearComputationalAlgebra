using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Task4_
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                CuthillMcKee cm = new CuthillMcKee();

                List<List<int>> matrix = new List<List<int>>();
                matrix.Add(new List<int> { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0 });
                matrix.Add(new List<int> { 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0 });
                matrix.Add(new List<int> { 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1 });
                matrix.Add(new List<int> { 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0 });
                matrix.Add(new List<int> { 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0 });
                matrix.Add(new List<int> { 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 });
                matrix.Add(new List<int> { 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1 });
                matrix.Add(new List<int> { 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1 });
                matrix.Add(new List<int> { 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1 });
                matrix.Add(new List<int> { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 });
                matrix.Add(new List<int> { 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1 });

                cm.Cuthill_McKee_Algorithm(matrix);
            }
            catch(ArgumentException ex)
            {
                MessageBox.Show(ex.Message);
            }
            
        }

        private void Generate_button_Click(object sender, EventArgs e)
        {

        }
    }
}
