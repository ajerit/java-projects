/**
 * Laboratorio de Algoritmos III
 * 
 * Proyecto 1
 *
 * Solucion al problema 1
 * 
 * Compilacion javac Moria.java
 * Ejecucion: java Moria <nombre de archivo>
 * 
 */
import java.util.*;

public class Moria {
    Graph G;
    int V;
    int E;
    int nodoInicial;
    int nodoSalida;
    LinkedList<LinkedList<Integer>> posPaths;
    LinkedList<Integer> posSol;
    LinkedList<LinkedList<Integer>> solution;
    boolean[][] anillos;

    public Moria(In in) {
        G = new Graph(in.readInt());
        V = G.V();
        E = in.readInt();
        anillos = new boolean[V][V];
        nodoInicial = in.readInt();
        nodoSalida = in.readInt();
        for (int i=0;i<E;i++) {
            int x = in.readInt();
            int y = in.readInt();
            if (in.readInt() == 1)
                anillos[x][y] = true;
            G.addEdge(x, y);
        }
        solution = findSol();

    }

    // Encontrar la solucion al problema
    public LinkedList<LinkedList<Integer>> findSol() {
        LinkedList<LinkedList<Integer>> sol = new LinkedList<LinkedList<Integer>>();
        // Primer hacemos dfs a partir del nodo inicial
        BreadthFirstSearch bfs = new BreadthFirstSearch(G, nodoInicial);
        // Vemos cuales caminos recogen anillos
        posPaths = getRingPaths(bfs.getAllPath());
        //StdOut.println(posPaths);
        // Para cada camino donde recogimos anillos, vemos si existe una salida
        //StdOut.println(posPaths);
        for (LinkedList<Integer> path : posPaths) {
            Graph copyG = new Graph(G);
            for (int i=0;i<path.size()-1;i++) {
                copyG.removeEdge(path.get(i), path.get(i+1));
            }
            //StdOut.println(copyG);
            BreadthFirstSearch bfs2 = new BreadthFirstSearch(copyG, path.peekLast());
            LinkedList<Integer> salida = bfs2.getPathTo(nodoSalida);
            //StdOut.println(salida);
            // Si podemos salir, se añade a la solucion
            if (salida != null) {
                posSol = new LinkedList<Integer>();
                for (int n=0;n<path.size()-1;n++)
                    posSol.add(path.get(n));
                for (Integer v : salida)
                    posSol.add(v);
                sol.add(posSol);
            }
        }
        return sol;
    }

    // Determina si un camino dado como lista enlazada permite agarrar un anillo
    public boolean pathHasRing(LinkedList<Integer> path) {
        for (int i=0;i<path.size()-1;i++) {
            if (anillos[path.get(i)][path.get(i+1)])
                return true;    
        }
        return false;
    }

    // Dado una lista enlzd. de caminos, devuelve cuales permiten agarran un anillo
    public LinkedList<LinkedList<Integer>> getRingPaths(LinkedList<LinkedList<Integer>> paths) {
        LinkedList<LinkedList<Integer>> ringPaths = new LinkedList<LinkedList<Integer>>();
        for (LinkedList<Integer> camino : paths) {
            if (pathHasRing(camino))
                ringPaths.add(camino);
        }
        return ringPaths;
    }

    // Imprimir caminos que llevan a anillos
    public void printPosPaths() {
        for (LinkedList<Integer> path : posPaths) {
            StdOut.println(path);
            StdOut.println();
        }
    }

    // Imprimir solucion
    public void printSol() {
        if (solution.isEmpty()) {
            StdOut.println("El problema no tiene solucion.");
        } else {
            StdOut.println("El problema tiene solucion.");
            StdOut.println();
            // Solo nos piden una solucion
            StdOut.println(solution.peekFirst());
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Moria proyecto = new Moria(in);
        proyecto.printPosPaths();
        proyecto.printSol();
    }
}
