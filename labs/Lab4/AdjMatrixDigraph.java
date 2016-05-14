/******************************************************************************
 * Laboratorio de Algoritmos III
 *
 *  Compilation:  javac AdjMatrixDigraph.java
 *  Execution:    java AdjMatrixDigraph input.txt
 *  Dependencies: StdOut.java Arc.java
 *
 *  An edge-weighted digraph, implemented using an adjacency matrix.
 *  Parallel edges are disallowed; self-loops are allowed.
 * 
 * % java AdjMatrixDigraph tinyDG.txt
 * 13 vertices, 22 edges 
 * 0: 0->1   0->5   
 * 1: 
 * 2: 2->0   2->3   
 * 3: 3->2   3->5   
 * 4: 4->2   4->3   
 * 5: 5->4   
 * 6: 6->0   6->4   6->8   6->9   
 * 7: 7->6   7->9   
 * 8: 8->6   
 * 9: 9->10   9->11   
 * 10: 10->12   
 * 11: 11->4   11->12   
 * 12: 12->9 
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

/**
 *  The <tt>AdjMatrixDigraph</tt> class represents a edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type {@link Arc}.
 *  It supports the following two primary operations: add a directed edge
 *  to the digraph and iterate over all of edges incident from a given vertex.
 *  It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges are disallowed; self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-matrix representation.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident from a given vertex, which takes
 *  time proportional to <em>V</em>.
 *  <p>
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Guillermo Palma
 *  @author Adolfo Jeritson
 *  @author Gianni Manilia
 */
public class AdjMatrixDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private int V;
    private int E;
    private Arc[][] adj;
    
    /**
     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    public AdjMatrixDigraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        this.adj = new Arc[V][V];
    }

     /**  
     * Initializes a digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
     public AdjMatrixDigraph(In in) {
	  try {
	       this.V = in.readInt();
	       if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
	       this.adj = new Arc[V][V];
	       int E = in.readInt();
	       if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
	       for (int i = 0; i < E; i++) {
		    int v = in.readInt();
		    int w = in.readInt();
		    addEdge(v, w); 
	       }
	  }
	  catch (NoSuchElementException e) {
	       throw new InputMismatchException("Invalid input format in Digraph constructor");
        }
     }
     
    /**
     * Returns the number of vertices in the edge-weighted digraph.
     * @return the number of vertices in the edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the edge-weighted digraph.
     * @return the number of edges in the edge-weighted digraph
     */
    public int E() {
        return E;
    }

    /**
     * Adds the directed edge <tt>e</tt> to the edge digraph (if there
     * is not already an edge with the same endpoints).
     * @param e the edge
     */
    public void addEdge(Arc e) {
        int v = e.from();
        int w = e.to();
        if (adj[v][w] == null) {
            E++;
            adj[v][w] = e;
        }
    }

    /**
     * Creates a the directed edge <tt>e</tt> and 
     * adds the directed edge <tt>e</tt> to the edge digraph (if there
     * is not already an edge with the same endpoints).
     * @param v the tail vertex
     * @param w the head vertex
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     */
     public void addEdge(int v, int w) {
	  Arc e = new Arc(v, w);
	  if (adj[v][w] == null) {
	       E++;
	       adj[v][w] = e;
	  }
     }

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @param v the vertex
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Arc> adj(int v) {
        return new AdjIterator(v);
    }

    // support iteration over graph vertices
    private class AdjIterator implements Iterator<Arc>, Iterable<Arc> {
        private int v;
        private int w = 0;

        public AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Arc> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < V) {
                if (adj[v][w] != null) return true;
                w++;
            }
            return false;
        }

        public Arc next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return adj[v][w++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns a string representation of the edge-weighted digraph. This method takes
     * time proportional to <em>V</em><sup>2</sup>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
	s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Arc e : adj(v)) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
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
		return (adj[v][w] == null) ? false : (adj[v][w].from() == v && adj[v][w].to() == w);
	}
	
    /**
     * Obtener la lista de vertices adyacentes del vertice <tt>v</tt>
     *
     * @param  v  el vertice a obtener la lista
     * @return lista de vertices adyacentes
     * @throws IndexOutOfBoundsException si el vertice no pertenece al grafo
     */
	public Bag<Integer> getListAdy(int v) {
        validateVertex(v);
		Bag<Integer> bag = new Bag<Integer>();
		for (int i=0;i<V;i++) {
			if (adj[v][i] != null)
				bag.add(i);
		}
		return bag;
	}
	
    /**
     * Obtener la lista de los vertices predecesores al vertice <tt>v</tt>
     *
     * @param  v  el vertice a obtener la lista de predecesores
     * @return lista de predecesores
     * @throws IndexOutOfBoundsException si el vertice no pertenece al grafo
     */
	public Bag<Integer> getListPred(int v) {
        validateVertex(v);
		Bag<Integer> bag = new Bag<Integer>();
		for (int i=0;i<V;i++) {
			if (adj[i][v] != null) {
				Arc e = adj[i][v];
				if (e.to() == v)
					bag.add(e.from());
			}
		}
		return bag;
	}
	
    /**
     * Calcular el grado interior del vertice <tt>v</tt>
     *
     * @param  v  el vertice a obtener el grado interior
     * @return grado interior del vertice
     * @throws IndexOutOfBoundsException si el vertice no pertenece al grafo
     */
	public int indegree(int v) {
        validateVertex(v);
		int grado = 0;
		for (int i=0;i<V;i++) {
			if (adj[i][v] != null) {
				Arc e = adj[i][v];
				if (e.to() == v)
					grado++;
			}
		}
		return grado;
	}

    /**
     * Calcular el grado exterior del vertice <tt>v</tt>
     *
     * @param  v  el vertice a obtener el grado exterior
     * @return grado exterior del vertice
     * @throws IndexOutOfBoundsException si el vertice no pertenece al grafo
     */
	public int outdegree(int v) {
        validateVertex(v);
		int grado = 0;
		for (int i=0;i<V;i++) {
		    if (adj[v][i] != null) {
				Arc e = adj[v][i];
				if (e.from() == v)
					grado++;
			}
		}
		return grado;
	}
	
    /**
     * Obtener la matriz de alcance del grafo usando el algoritmo de
     * Roy-Warshall
     * 
     * @return matriz de alcance
     */
    public int[][] matrizAlcance() {
        // Llenar matriz de ady con 1 y 0
        int[][] mAlcance = new int[V][V];
        for (int i=0;i<V;i++) {
            for (int j=0;j<V;j++) {
                if (adj[i][j] != null) {
                    mAlcance[i][j] = 1;
                } else {
                    mAlcance[i][j] = 0;
                }

                // Suma boolena con matriz Identidad.
                if (i==j) {
                    mAlcance[i][j] = 1 | mAlcance[i][j];
                }
            }
        }

        // Roy-Warshall
        for (int k=0;k<V;k++) {
            for (int i=0;i<V;i++) {
                for (int j=0;j<V;j++) {
                    mAlcance[i][j] = mAlcance[i][j] | (mAlcance[i][k] & mAlcance[k][j]);
                }
            }
        }
        return mAlcance;
    }

    /**
     * Obtener la clausura transitiva del grafo G usando el algoritmo de
     * Roy-Warshall
     * 
     * @return Digrafo que representa la clausura transitivaS
     */
    public Digraph clausuraTransitiva() {
        // Llenar matriz de ady con 1 y 0
        Bag<Arc> conjLados = new Bag<Arc>();
        int[][] mAdy = new int[V][V];
        for (int i=0;i<V;i++) {
            for (int j=0;j<V;j++) {
                if (adj[i][j] != null) {
                    mAdy[i][j] = 1;
                } else {
                    mAdy[i][j] = 0;
                }
            }
        }

        // Algoritmo basado en Roy-Warshall
        for (int k=0;k<V;k++) {
            for (int i=0;i<V;i++) {
                for (int j=0;j<V;j++) {
                    mAdy[i][j] = mAdy[i][j] | (mAdy[i][k] & mAdy[k][j]);
                }
            }
        }
        
        // Obtenemos los Arcos de la matriz de ady
        for (int i=0;i<V;i++) {
            for (int j=0;j<V;j++) {
                if (mAdy[i][j] == 1)
                    conjLados.add(new Arc(i,j));
            }
        }
        
        Digraph grafoResultado = new Digraph(V);
        for (Arc lado : conjLados) {
			grafoResultado.addEdge(lado.from(), lado.to());
		}

        return grafoResultado;
    }

    /**
     * Unit tests the <tt>AdjMatrixDigraph</tt> data type.
     */
    public static void main(String[] args) {
	 In in = new In(args[0]);
	 AdjMatrixDigraph G = new AdjMatrixDigraph(in);
	 StdOut.println(G);

     }
    }


/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 * Copyright 2016, Guillermo Palma
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
