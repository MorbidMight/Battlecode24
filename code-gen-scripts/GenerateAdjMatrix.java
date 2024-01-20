import java.io.*;
import java.util.*;
import java.util.ArrayList;
public class GenerateAdjMatrix
{
   public static GridPoint[] directions =
   {
          new GridPoint(0,1),
      new GridPoint(1,1),
      new GridPoint(1,0),
      new GridPoint(1,-1),
      new GridPoint(0,-1),
      new GridPoint(-1,-1),
      new GridPoint(-1,0),
      new GridPoint(-1, 1)
   };
   public static void main(String[] args) throws IOException
   {
      writeToFile(listNeighborsToStringArray(matrixToArrayOfNeighbors(createMatrix())), "bellmanFordMatrix.txt");
   }

   public static int[][] createMatrix ()
   {
      GridPoint center = new GridPoint(3, 3);
      int[][] adjacencyMatrix = new int[25][25];
      int count = 0;
      
      for (int i = center.y + 2; i >= center.y - 2; i--) {
         for (int j = center.x - 2; j <= center.x + 2; j++) {
            System.out.println(new GridPoint(i, j));
            for (int k = 0; k < 8; k++) {
               GridPoint tempLocation = (new GridPoint(j, i)).add(directions[k]);
               if(tempLocation.x > 5 || tempLocation.x < 1) continue;
               if(tempLocation.y > 5 || tempLocation.y < 1) continue;
               try {
                  int squareToCheck = -1;
                  switch (k) {
                     case 0:
                        squareToCheck = count - 5;
                        break;
                     case 1:
                        if(count == 4) System.out.println("here");
                        squareToCheck = count - 4;
                        break;
                     case 2:
                        squareToCheck = count + 1;
                        break;
                     case 3:
                        squareToCheck = count + 6;
                        break;
                     case 4:
                        squareToCheck = count + 5;
                        break;
                     case 5:
                        squareToCheck = count + 4;
                        break;
                     case 6:
                        squareToCheck = count - 1;
                        break;
                     case 7:
                        squareToCheck = count - 6;
                        break;
                  }
                  if (squareToCheck < 0 || squareToCheck > 24)
                     continue;
                  adjacencyMatrix[count][squareToCheck] = 1;
               } catch (Exception e) {
                  System.out.println(e);
               }
            }
            count++;
         }
      }
      return adjacencyMatrix;
   }
   
   public static String stringMatrix(int[][] adjacencyMatrix)
  {
      String temp = "0   ";
      for(int x = 0; x < 9; x++)
      {
         temp += x + "   ";
      }
      for(int x = 9; x < 25; x++)
      {
         temp += x + "  ";
      }
      temp += "\n";
      for(int i = 0; i < adjacencyMatrix.length; i++)
      {
         if(i < 10)
         temp += i + "   ";
         else
         temp += i + "  ";
         for(int j  = 0; j < adjacencyMatrix[0].length; j++)
         {
            temp += adjacencyMatrix[i][j] + "   ";
         }
         temp += "\n";
      }
      return temp;
   }

   public static String[] matrixToStringArray(int[][] matrix)
   {
      String[] array = new String[27];
      array[0] = "public static final int[][] bellmanFordAdjacencyMatrix = new int[][]";
      array[1] = "{";
      for(int i = 0; i < matrix.length; i++)
      {
         array[i + 2] = "{";
         for(int j = 0; j < matrix[i].length; j++)
         {
            if(j != matrix[i].length - 1)
               array[i + 2] += matrix[i][j] + ",";
            else
               array[i + 2] += matrix[i][j];
         }
         if(i != matrix.length - 1)
            array[i + 2] += "},";
         else
            array[i + 2] += "}";
      }
      array[26] += "};";
      return array;
   }

   public static String[] listNeighborsToStringArray(ArrayList<ArrayList<Integer>> matrix)
   {
      String[] array = new String[27];
      array[0] = "public static final int[][] neighborLookup5x5 = new int[][]";
      array[1] = "{";
      for(int i = 0; i < matrix.size(); i++)
      {
         array[i + 2] = "{";
         for(int j = 0; j < matrix.get(i).size(); j++)
         {
            if(j != matrix.get(i).size() - 1)
               array[i + 2] += matrix.get(i).get(j) + ",";
            else
               array[i + 2] += matrix.get(i).get(j);
         }
         if(i != matrix.size() - 1)
            array[i + 2] += "},";
         else
            array[i + 2] += "}";
      }
      array[26] += "};";
      return array;
   }

   public static  ArrayList<ArrayList<Integer>> matrixToArrayOfNeighbors(int[][] matrix)
   {
      ArrayList<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
      for(int i = 0; i < matrix.length; i++)
      {
         lists.add(new ArrayList<Integer>());
         for(int j = 0; j < matrix[i].length; j++)
         {
            if(matrix[i][j] == 1)
            {
               lists.get(i).add(j);
            }
         }
      }
      return lists;
   }

   public static void writeToFile(String[] array, String filename) throws IOException
   {
      System.setOut(new PrintStream(new FileOutputStream(filename)));
      for(int i = 0; i < array.length; i++)
         System.out.println(array[i]);
   }
}
class GridPoint
{
   public int x;
   public int y;
   public GridPoint(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public GridPoint add(GridPoint other)
   {
      return new GridPoint(x + other.x, y + other.y);
   }
   
   public String toString()
   {
      return "[" + x + ", " + y + "]";
   }
}