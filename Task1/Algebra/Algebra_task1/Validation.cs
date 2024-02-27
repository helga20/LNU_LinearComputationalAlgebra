using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Algebra_task1
{
    internal class Validation
    {
        protected Validation() { }
        public static bool IsInt(string numberToCheck, bool isPositive=false)
        {
            bool isInt = int.TryParse(numberToCheck, out int resultNumber);
            if (isPositive && isInt && resultNumber >= 0)
            {
                return true;
            }
            return isInt;
        }

        public static bool IsDouble(string numberToCheck)
        {
            return double.TryParse(numberToCheck, out double resultNumber);
        }

        public static bool FileExists(string fileName)
        {
            return File.Exists(fileName);
        }

        public static bool RangeIsCorrect(int a, int b)
        {
            if (b > a)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
