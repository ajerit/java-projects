/******************************************************************************
 * Laboratorio de Algoritmos III
 *
 *  Compilation:  javac Graph.java        
 *  Execution:    java Graph input.txt
 *  Dependencies: Bag.java In.java StdOut.java Queue.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *
 *  A graph, implemented using an array of sets.
 *  Parallel edges and self-loops allowed.
 *
 *  % java Graph tinyG.txt
 *  13 vertices, 13 edges 
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
 *
 *  % java Graph mediumG.txt
 *  250 vertices, 1273 edges 
 *  0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 
 *  1: 220 203 200 194 189 164 150 130 107 72 
 *  2: 141 110 108 86 79 51 42 18 14 
 *  ...
 *  
 ******************************************************************************/

/**
 *  The <tt>Graph</tt> class represents an undirected graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent to a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Adolfo Jeritson
 *  @author Gianni Manilia
 */
import java.util.*;
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private LinkedList<Integer>[] adj;
    private Stack<Integer> cycle = new Stack<Integer>();
    /**
     * Initializes an empty graph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     *
     * @param  V number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Integer>[]) new LinkedList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<Integer>();
        }
    }

    /**  
     * Initializes a graph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

    /**
     * Initializes a new graph that is a deep copy of <tt>G</tt>.
     *
     * @param  G the graph to copy
     */
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }
    
    private static class Edge {
		private final int v;
		private final int w;
		private boolean isUsed;

		public Edge(int v, int w) {
			this.v = v;
			this.w = w;			
			isUsed = false;
		}

		// returns the other vertex of the edge
		public int other(int vertex) {
			if      (vertex == v) return w;
			else if (vertex == w) return v;
			else throw new IllegalArgumentException("Illegal endpoint");
		}		
	}

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].addFirst(w);
        adj[w].addFirst(v);
    }

    public void removeEdge(Integer v, Integer w) {
        validateVertex(v);
        validateVertex(w);
        E--;
        adj[v].remove(w);
        adj[w].remove(v);
    }

    /**
     * Returns the vertices adjacent to vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the vertices adjacent to vertex <tt>v</tt>, as an iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Integer> adj(int v) {
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
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    /**
     * Chequea si el vertice <tt>v</tt> pertenece al grafo.
     *
     * @param  v el vertice a chequear
     * @return true si pertenece, false en caso contrario
     */
	public boolean containsVertex(int v) {
        return (v < 0 || v >= V) ? false : true;
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
        boolean contains = false;
		for (int item : adj[v]) {
			if (item == w)
				contains = true;
		}
        return contains;
	}
	
    /**
     * Obtener la lista de vertices adyacentes del vertice <tt>v</tt>
     *
     * @param  v  el vertice a obtener la lista
     * @return ista de vertices adyacentes
     * @throws IndexOutOfBoundsException si el vertice no pertenece al grafo
     */
	public LinkedList<Integer> getListAdy(int v) {
		validateVertex(v);
		return adj[v];	
	}
	
    /**
     * Verificar si el grafo tiene ciclo de Euler
	 *
     * @return booleano que indica si el grafo tiene ciclo de Euler
     */
	public boolean hasEuler() {
		boolean hasEuler = true;
		for (int v = 0; v<V;v++) {
			if (degree(v) % 2 != 0)
				hasEuler = false;
		}
		return hasEuler;
	}


    // returns any non-isolated vertex; -1 if no such vertex
    private int nonIsolatedVertex() {
        for (int v = 0; v < V; v++)
            if (degree(v) > 0)
                return v;
        return -1;
    }

    /**
     * Retorna secuencia de vertices del ciclo euleriano
     * 
     * @return secuencia de vertices del ciclo euleriano
     *         <tt>null</tt> si no existe el ciclo.
     */
    public Iterable<Integer> cycle() {
        return cycle;
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

        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (int w : this.adj(v)) {
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w);
                        adj[v].enqueue(e);
                        adj[w].enqueue(e);
                    }
                    selfLoops++;
                }
                else if (v < w) {
                    Edge e = new Edge(v, w);
                    adj[v].enqueue(e);
                    adj[w].enqueue(e);
                }
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
     * Unit tests the <tt>Graph</tt> data type.
     */
    public static void main(String[] args) {
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
