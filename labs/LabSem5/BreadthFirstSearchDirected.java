/**
 * Laboratorio de Algoritmos III. Abril-Julio 2016
 * @author Gianni Manilia
 * @author Adolfo Jeritson
 *
 * Clase que implementa el algormitmo BFS de busqueda en amplitud y
 * otras funciones para el laboratorio. 
 *
 * Se tomo como base la implementacion elaborada por Robert Sedgewick y Kevin Wayne
 * http://algs4.cs.princeton.edu/42digraph/BreadthFirstDirectedPaths.java.html y
 * ha sido adaptado para el laboratorio.
 *
 * Ejecucion: java BreadthFirstSearchDirected nombreDelArchivoGrafo nodoInicial
 * Dependencias: StdIn.java StdOut.java Queue.java Digraph.java
 */
import java.util.*;

public class BreadthFirstSearchDirected {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private Integer[] ladoA;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distA;      // distTo[v] = length of shortest s->v path
    private int cantidadVertices;   

    /**
     * Calcula el camino mas corto desde <tt>s</tt> hasta los demas
     * vertices del grafo <tt>G</tt>.
     * @param G digrafo
     * @param s vertice origen
     */
    public BreadthFirstSearchDirected(Digraph G, int s) {
        marked = new boolean[G.V()];
        distA = new int[G.V()];
        ladoA = new Integer[G.V()];
        cantidadVertices = G.V();
        for (int v = 0; v < G.V(); v++)
            distA[v] = INFINITY;
        bfs(G, s);
    }

    /**
    * Implementacion del algoritmo BSF (busqueda en amplitud)
    * @param  G Digrafo sobre el cual realizar a la busqueda
    * @param  s Vertice origen de la busqueda
    */

    public void bfs(Digraph G, int s) {
        Queue<Integer> cola = new Queue<Integer>();
        marked[s] = true;
        distA[s] = 0;
        cola.enqueue(s);
        while (!cola.isEmpty()) {
            int v = cola.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    ladoA[w] = v;
                    distA[w] = distA[v] + 1;
                    marked[w] = true;
                    cola.enqueue(w);
                }
            }
        }
    }

    /**
    * Indica si hay un camino desde el nodo origen al nodo v.
    * @param   vertice v
    * @return  <tt>true</tt> si existe un camino, 
    *          <tt>false</tt> en caso contrario.
    */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Retorna el numero de lados del camino mas corto desde nodo origen
     * hasta el vertice <tt>v</tt>
     * @param   vertice v
     * @return  numero de lados del camino mas corto
     */
    public int distTo(int v) {
        return distA[v];
    }

    /**
    * Retorna un arreglo que indica para cada vertice, cual fue 
    * el vertice anteriormente visitado en la b busqueda
    * @return  arreglo de vertices visitados
    */
    public Integer[] arcsVisited() {
        return ladoA;
    }

    /**
    * Dado un vertice v, retorna los vertices del camino que 
    * encontro el algoritmo busqueda, desde el vertice fuente s hasta 
    * el vertice v, contenidos en una clase LinkedList.
    * @param   vertice v
    * @return  Lista enlazada con el camino s-v
    */
    public LinkedList<Integer> getDirectedPathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedList<Integer> directedPath = new LinkedList<Integer>();
        int x;
        for (x = v; distA[x] != 0; x = ladoA[x])
            directedPath.push(x);
        directedPath.push(x);
        return directedPath;
    }

    /**
    * Retorna una lista con todos los caminos desde el vertice 
    * fuente s, hasta todos vertices que le son alcanzables.
    * @return Lista de listas con los caminos que parten de s
    */
    public LinkedList<LinkedList<Integer>> getAllDirectedPath() {
        LinkedList<LinkedList<Integer>> allPaths = new LinkedList<LinkedList<Integer>>();
        for (int v = 0; v < cantidadVertices; v++) {
            if (hasPathTo(v))
                allPaths.add(getDirectedPathTo(v));
        }
        return allPaths;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        int s = Integer.parseInt(args[1]);
        BreadthFirstSearchDirected BFS = new BreadthFirstSearchDirected(G, s);
    }    
}