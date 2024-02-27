using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Algebra_task1
{
    internal class Vector
    {
        public int DataLenght { get; set; }
        public double[] Data { get; set; }
        public Vector(int dataLenght)
        {
            DataLenght = dataLenght;
            Data = new double[dataLenght];
        }

        public override string ToString()
        {
            string result = "";
            for (int i = 0; i<DataLenght; i++)
            {
                result += Data[i] + " ";
            }
            return result;
        }
    }
}
