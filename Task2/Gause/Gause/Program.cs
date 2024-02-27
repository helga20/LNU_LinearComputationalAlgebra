using System.Drawing;

double[,] A = new double[,] { { 1.0, 5, 3, -4, 20 }, { 3, 1, -2, 0, 9 }, { 5, -7, 0, 10, -9 }, { 0, 3, -5, 0, 1 } };

var max = Math.Abs(A[0, 0]);
int maxLine = 0;
int maxColumn = 0;
int rows = A.GetLength(0);
int columns = A.GetLength(1);


for (int i = 0; i < rows; i++)
{
    for (int j = 0; j < columns - 1; j++)
    {
        if (Math.Abs(A[i, j]) > max)
        {
            max = Math.Abs(A[i, j]);
            maxLine = i;
            maxColumn = j;
        }
    }
}

for (int i = 0; i < rows; i++)
{
    for (int j = 0; j < columns; j++)
    {
        if (i != maxLine)
        {
            A[i, j] = A[i, j] + A[maxLine, j] * (-A[i, maxColumn] / A[maxLine, maxColumn]);
        }
    }
}

for (int i = 0; i < rows; i++)
{
    for (int j = 0; j < columns; j++)
    {
        Console.Write(A[i, j] + "  ");
    }
    Console.WriteLine();
}




double[] X = new double[A.GetLength(0)];
int Size = A.GetLength(1);
for (int j = 0; j < Size - 1; j++)
{
    var max = Math.Abs(A[j, j]);
    int maxLine = j;
    for (int i = j + 1; i < Size; i++)
    {
        if (Math.Abs(A[i, j]) > max)
        {
            max = Math.Abs(A[i, j]);
            maxLine = i;
        }
    }

    if (maxLine != j)
    {
        for (int k = j; k < Size; k++)
        {
            (A[j, k], A[maxLine, k]) = (A[maxLine, k], A[j, k]);
        }
        (B[j], B[maxLine]) = (B[maxLine], B[j]);
    }

    for (int i = j + 1; i < Size; i++)
    {
        double mult = A[i, j] / max;
        for (int k = j; k < Size; k++)
        {
            A[i, k] -= mult * A[j, k];
        }
        B[i] -= mult * B[j];
    }
}
for (int j = Size - 1; j >= 0; j--)
{
    if (A[j, j] == 0)
    {
        Console.WriteLine("Wrong input");
        return;
    }
    X[j] = B[j] / A[j, j];
    for (int i = 0; i < j; i++)
    {
        B[i] -= A[i, j] * X[j];
    }
}

/*Console.WriteLine("Result = : ");
foreach (var x in X)
{
    Console.Write(x + "  ");
}*/