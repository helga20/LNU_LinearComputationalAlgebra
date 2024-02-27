using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace Algebra_task1
{
    public class CuthillMcKee
    {
        public void PrintMatrix(List<List<int>> matrix)
        {
            foreach (List<int> row in matrix)
            {
                foreach (int element in row)
                {
                    Console.Write(element + " ");
                }
                Console.WriteLine();
            }
        }

        public string MatrixToString(int[,] matrix)
        {
            string res = "";
            for (int i = 0; i < matrix.GetLength(0); i++)
            {
                for (int j = 0; j < matrix.GetLength(0); j++)
                {
                    res += matrix[i,j].ToString();
                    res += " ";
                }
                res += "\r\n\n";
            }
            return res;
        }
        public void PrintList(List<int> list)
        {
            foreach (int element in list)
            {
                Console.Write(element + " ");
            }
        }
        public int[,] Cuthill_McKee_Algorithm(List<List<int>> g_matrix)
        {
            if (!isSymetric(g_matrix))
            {
                throw new ArgumentException("Matrix is not symetric");
            }
            List<List<int>> g_graph = Build_G_Graph(g_matrix);

            Console.WriteLine("Graph:");
            PrintMatrix(g_graph);
            List<int> peripheral_values = new List<int>();
            bool isOne = false;

            List<List<int>> toMultiply = g_matrix;

            while (isOne == false)
            {
                peripheral_values = FindPeripheralValues(toMultiply);

                toMultiply = Multiplication(toMultiply, g_matrix);
                isOne = isAllOne(toMultiply);

                Console.WriteLine("\n\nperipheral_values: ");
                PrintList(peripheral_values);
                Console.WriteLine("\n\nto multiply");
                PrintMatrix(toMultiply);
            }
            int peripheral = 4;


            List<int> renumbering = new List<int>();
            List<int> adjacent_vertices = new List<int>();

            renumbering.Add(peripheral);
            adjacent_vertices.Add(peripheral);


            for (int i = 0; i < renumbering.Count; i++)
            {
                adjacent_vertices = FindAdjacentVertices(adjacent_vertices, renumbering, g_graph);
                foreach (int val in adjacent_vertices)
                {
                    renumbering.Add(val);
                }

            }
            Console.WriteLine("\n\nrenumbering");
            PrintList(renumbering);

            int[,]  m = MakeNewGraph(g_graph, renumbering);
            return m;

        }

        public List<List<int>> Build_G_Graph(List<List<int>> g_matrix)
        {
            List<List<int>> g_graph = new List<List<int>>(g_matrix.Count);
            for (int i = 0; i < g_matrix.Count; i++)
            {
                List<int> temp = new List<int>();
                for (int j = 0; j < g_matrix[i].Count; j++)
                {
                    if (i != j && g_matrix[i][j] == 1)
                    {
                        temp.Add(1);
                    }
                    else
                    {
                        temp.Add(0);
                    }
                }
                g_graph.Add(temp);
            }
            return g_graph;
        }

        public List<int> FindPeripheralValues(List<List<int>> matrix)
        {

            List<int> peref = new List<int>();
            for (int i = 0; i < matrix.Count; i++)
            {
                bool contains = false;
                for (int j = 0; j < matrix.Count; j++)
                {
                    if (matrix[i][j] == 0)
                    {
                        contains = true;
                        break;
                    }
                }
                if (contains)
                {
                    peref.Add(i);
                }
            }
            return peref;
        }

        public List<List<int>> Multiplication(List<List<int>> left, List<List<int>> right)
        {
            if (left.Count != right.Count)
                throw new ArgumentException("Matrixes are not valid for multiplication");

            var result = new List<List<int>>(left.Count);

            for (var i = 0; i < left.Count; i++)
            {
                List<int> temp = new List<int>();
                for (var j = 0; j < left.Count; j++)
                {
                    int t = 0;
                    for (var k = 0; k < left.Count; k++)
                        t |= (left[i][k] != 0 && right[k][j] != 0) ? 1 : 0;
                    temp.Add(t);
                }
                result.Add(temp);
            }
            return result;
        }
        public bool isSymetric(List<List<int>> matrix)
        {
            for (int i = 0; i < matrix.Count; i++)
            {
                for (int j = 0; j < matrix[i].Count; j++)
                {
                    if (matrix[i][j] != matrix[j][i])
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        public bool isAllOne(List<List<int>> matrix)
        {
            for (int i = 0; i < matrix.Count; i++)
            {
                if (matrix[i].Contains(0))
                {
                    return false;
                }
            }
            return true;
        }

        public List<int> FindAdjacentVertices(List<int> ver_index, List<int> visited, List<List<int>> grahp)
        {
            List<int> Vertices = new List<int>();
            Dictionary<int, int> dict = new Dictionary<int, int>();

            for (int k = 0; k < ver_index.Count; k++)
            {
                for (int i = 0; i < grahp.Count; i++)
                {
                    if (grahp[ver_index[k]][i] == 1 && !visited.Contains(i) && !dict.ContainsKey(i))
                    {
                        int rang = RangCount(i, grahp);
                        dict.Add(i, rang);
                    }
                }
            }

            List<KeyValuePair<int, int>> sortedList = dict.ToList();
            sortedList.Sort((x, y) => x.Value.CompareTo(y.Value));
            foreach (KeyValuePair<int, int> kvp in sortedList)
            {
                Vertices.Add(kvp.Key);
            }
            return Vertices;
        }
        public int RangCount(int ver_index, List<List<int>> grahp)
        {
            return grahp[ver_index].Count(x => x == 1);
        }

        public int[,] MakeNewGraph(List<List<int>> old, List<int> perenum)
        {
            int[,] graph = new int[old.Count, old.Count];

            for (int i = 0; i < perenum.Count; i++)
            {
                int number = perenum[i];
                List<int> indexes = new List<int>();
                for (int j = 0; j < old.Count; j++)
                {
                    if (old[number][j] == 1)
                        indexes.Add(j);
                }
                foreach (var item in indexes)
                {
                    int j = perenum.IndexOf(item);
                    graph[i, j] = 1;
                    graph[j, i] = 1;
                }
            }
            
            for (int i = 0; i < old.Count; i++)
            {
                for (int j = 0; j < old.Count; j++)
                {
                    if (i == j)
                    {
                        graph[i, j] = 1;
                    }
                }
            }
            return graph;
        }
    }
}
