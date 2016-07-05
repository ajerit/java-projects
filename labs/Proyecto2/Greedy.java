/**
 * Algoritmo avido para obtener un APCM del grafo.
 * Dado un grafo G no dirigido, completo y con numero par de vertices,
 * el algoritmo de tipo greedy encuentra un apareamiento perfecto de costo
 * minimo.
 *
 * @author
 * @author
 */

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Greedy {
    private ArrayList<Edge>     apPerf;
    private ArrayList<Integer>  copiaV;

    /**
     * Clase que implementa el comparador para la cola de prioridad.
     */
    private class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge a, Edge b) {
            if      (a.weight() < b.weight()) return -1;
            else if (a.weight() > b.weight()) return +1;
            else                              return  0;
        }
    }
    /**
     * Inicializacion para el algoritmo avido.
     * Crea la cola de prioridades con los lados y la llena.
     * Crea una lista con los nodos del grafo.
     *
     * @para G Grafo a encontrar APCM.
     */
    public Greedy(EdgeWeightedGraph G) {
        Comparator<Edge> comparator = new EdgeComparator();
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(G.E(), comparator);
        copiaV = new ArrayList<Integer>();

        for (Edge e : G.edges()) 
            pq.add(e);

        for (int v = 0; v < G.V(); v++)
            copiaV.add(v);
        
        getGreedy(G, pq);
    }

    public ArrayList<Edge> apareamiento() {
        return apPerf;
    }

    /**
     * Implementacion de algoritmo avido para hallar APCM.
     *
     * @param G Grafo a encontrar APCM.
     * @param L Cola con los lados orden ascendente por costo.
     */
    public ArrayList<Edge> getGreedy(EdgeWeightedGraph G, PriorityQueue<Edge> L) {
        apPerf = new ArrayList<Edge>();

        while (!copiaV.isEmpty()) {
            Edge e = L.poll();
            Integer i = e.either();
            Integer j = e.other(i);

            if (copiaV.contains(i) && copiaV.contains(j)) {
                apPerf.add(e);
                copiaV.remove(i);
                copiaV.remove(j);
            }
        }
        return apPerf;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        Greedy greedy = new Greedy(G);
        StdOut.println(greedy.apPerf);
    }
}