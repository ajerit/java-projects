/******************************************************************************
 *  Compilation:  javac EdgeWeightedGraph.java
 *  Execution:    java EdgeWeightedGraph filename.txt
 *  Dependencies: Edge.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *
 *  An edge-weighted undirected graph, implemented using adjacency lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java EdgeWeightedGraph tinyEWG.txt 
 *  8 16
 *  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000  
 *  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000  
 *  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000  
 *  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000  
 *  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000  
 *  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000  
 *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 *  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 ******************************************************************************/
/**
 *  The <tt>EdgeWeightedGraph</tt> class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> - 1, where each
 *  undirected edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the edges incident to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of @link{LinkedList} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident to a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;


public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private LinkedList<Edge>[] adj;
    private Stack<Integer> cycle;
    private HashSet<Integer> nodosActivos = new HashSet<Integer>();
    
    /**
     * Initializes an empty edge-weighted graph with <tt>V</tt> vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Edge>[]) new LinkedList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<Edge>();
        }
    }

    /**
     * Initializes a random edge-weighted graph with <tt>V</tt> vertices and <em>E</em> edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     * @throws IllegalArgumentException if <tt>E</tt> < 0
     */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double weight = Math.round(100 * StdRandom.uniform()) / 100.0;
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

    /**  
     * Initializes an edge-weighted graph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of <tt>G</tt>.
     *
     * @param  G the edge-weighted graph to copy
     */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adj[v].add(e);
            }
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge <tt>e</tt> to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IndexOutOfBoundsException unless both endpoints are between 0 and V-1
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        nodosActivos.add(v);
        nodosActivos.add(w);
        E++;
    }

    /**
     * Determinar el lado entre <tt>v</tt> y <tt>w</tt> pertenece al grafo.
     *
     * @param  v,w los vertices a verificar
     * @return true si pertenece, false en caso contrario
     * @throws IndexOutOfBoundsException si algun vertice no pertenece al grafo
     */
    public boolean containsEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        for (Edge e : adj[v]) {
            if (e.other(v) == w)
                return true;
        }
        return false;
    }

    public double getEdgeWeight(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (containsEdge(v, w)) {
            for (Edge e : adj[v]) {
                if (e.other(v) == w)
                    return e.weight();
            }
        }
        return 0.0;
    }

    public boolean nodoPertenece(int v) {
        if (nodosActivos.contains(v))
            return true;
        return false;
    }

    public void agregarNodo(int v) {
        nodosActivos.add(v);
    }

    /**
     * Returns the edges incident on vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the edges incident on vertex <tt>v</tt> as an Iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the degree of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the degree of vertex <tt>v</tt>               
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * <tt>for (Edge e : G.edges())</tt>.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public ArrayList<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v+1 + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Verificar si el grafo tiene ciclo de Euler
     *
     * @return booleano que indica si el grafo tiene ciclo de Euler
     */
    public boolean tieneEuler() {
        for (int v = 0; v<V;v++) {
            if (degree(v) % 2 != 0)
                return false;
        }
        return true;
    }

    // Retorna un vertice no aislado; -1 si no existe
    private int nonIsolatedVertex() {
        for (int v = 0; v < V; v++)
            if (degree(v) > 0)
                return v;
        return -1;
    }

    /**
     * Calcula el ciclo euleriano de un grafo, si es que existe.
     * 
     * @return ciclo euleriano
     */
    public Stack<Integer> getCicloEuleriano() {

        // Crear una copia local de la lista de adyacencias para no modificarla
        // Se usa la clase Edge para evitar recorrer un mismo lado dos veces.
        Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Queue<Edge>();

        for (int k = 0; k < V; k++) {   
            for (Edge e : this.adj(k)) {
                int v = e.either();
                int w = e.other(v);
                adj[v].enqueue(e);
                adj[w].enqueue(e);
            }
        }
        
        // inicializamos con un vertice que no este aislado
        int s = nonIsolatedVertex();
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);

        // realizamos la busqueda de los lados
        cycle = new Stack<Integer>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].dequeue();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // agregamos vertice sin mas lados de salida
            cycle.push(v);
        }

        // verificar que todos los lados fueron usados
        if (cycle.size() != E + 1)
            cycle = null;
        
        return cycle;
    }

    /**
     * Obtener la lista de componentes fuertemente conexas del grafo G
     *
     * @return Lista de listas con las componentes conexas
     */
    public HashSet<HashSet<Integer>> getComponentesConexas(){
        // Calcular matriz ady
        int[][] mAdy = new int[V][V];
        for (int i = 0; i < V; i++){
            for (int j = 0; j < V; j++) {
                if (containsEdge(i, j)) 
                    mAdy[i][j] = 1;
                else
                    mAdy[i][j] = 0;
                
                // Suma boolena con matriz Identidad.
                if (i == j)
                    mAdy[i][j] = 1 | mAdy[i][j];
            }
        }
        
        // Roy-Warshall
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    mAdy[i][j] = mAdy[i][j] | (mAdy[i][k] & mAdy[k][j]);
                }
            }
        }
        
        // Algoritmo para Componentes Conexas
        ArrayList<Integer> resto = new ArrayList<Integer>();
        HashSet<HashSet<Integer>> compCon = new HashSet<HashSet<Integer>>();
        for (int i = 0; i < V; i++)
            resto.add(i);

        while (!resto.isEmpty()) {
            HashSet<Integer> comp = new HashSet<Integer>();
            int v = resto.get(0);
            for (int w = 0; w < V; w++) {
                if (mAdy[v][w] == 1)  
                    comp.add(w);
            }
            compCon.add(comp);
            resto.removeAll(comp);
        }
        return compCon;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        in.readLine();                      // Nombre
        in.readLine();                      // Componentes
        in.readString();                    // Vertices 
        in.readString();                    // :

        int V = in.readInt();
        in.readString();                    // Aristas red
        in.readString();                    // :
        int Lr = in.readInt();
        in.readString();                    // Aristas no req
        in.readString();                    // :
        int Lnr = in.readInt();

        in.readString();                    // Lista arista
        in.readString();                    // :

        String[] lines = in.readAllLines();
        EdgeWeightedGraph G = new EdgeWeightedGraph(V);
        EdgeWeightedGraph Gr = new EdgeWeightedGraph(V);

        // iteramos sobre las aristas requeridas
        for (int i = 1; i <= Lr; i++) {
            lines[i] = lines[i].replace('(', ' ');
            lines[i] = lines[i].replace(')', ' ');
            lines[i] = lines[i].replace(',', ' ');
            lines[i] = lines[i].trim();
            String[] tokens = lines[i].split("\\s+");

            int v = Integer.parseInt(tokens[0]);
            int w = Integer.parseInt(tokens[1]);
            double c = Double.parseDouble(tokens[3]);
            Edge e = new Edge(v-1, w-1, c);
            StdOut.println(String.format("%d-%d, %f", v, w, c));

            G.addEdge(e);
            Gr.addEdge(e);
        }

        // iteramos sobre las aristas no requeridas
        for (int i = Lr+2; i <= Lr+Lnr+1; i++)  {
            lines[i] = lines[i].replace('(', ' ');
            lines[i] = lines[i].replace(')', ' ');
            lines[i] = lines[i].replace(',', ' ');
            lines[i] = lines[i].trim();
            String[] tokens = lines[i].split("\\s+");

            int v = Integer.parseInt(tokens[0]);
            int w = Integer.parseInt(tokens[1]);
            double c = Double.parseDouble(tokens[3]);
            Edge e = new Edge(v-1, w-1, c);

            G.addEdge(e);
        }

    }
}



/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
