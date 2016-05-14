import java.util.*;

public class PruebaLabSem4 {
	
    public static void main(String[] args) {
		In in1 = new In(args[0]);
		In in2 = new In(args[0]);
        Digraph grafo = new Digraph(in1);
        AdjMatrixDigraph matriz = new AdjMatrixDigraph(in2);
        
        int[][] mAlcance = matriz.matrizAlcance();
        Digraph clauTran = matriz.clausuraTransitiva();
        HashSet<HashSet<Integer>> compCon = grafo.componentesConexas();
        
     // Matriz ady de ct
      StdOut.print("     ");
      for (int v = 0; v < matriz.V(); v++)
          StdOut.printf("%3d", v);
      StdOut.println();
      StdOut.println("--------------------------------------------");

      for (int v = 0; v < matriz.V(); v++) {
         StdOut.printf("%3d: ", v);
         for (int w = 0; w < matriz.V(); w++) {
             StdOut.printf("  "+mAlcance[v][w]);
         }
         StdOut.println();
      }

        StdOut.println();
        StdOut.println(clauTran);
        StdOut.println();
        
        for (HashSet<Integer> comp : compCon) {
			for (int elem : comp) {
				System.out.print(elem+" ");
				}
				StdOut.println();
			}
        

    }
}

