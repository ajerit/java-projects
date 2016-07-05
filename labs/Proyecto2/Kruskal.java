/**
 * Implementación del algoritmo de Kruskal para obtener el arbol 
 * cobertor de costo mínimo de un grafo no orientado. Se utiliza
 * el TAD DisjointSetForest sacado del libro de Cormen para la
 * implementación del algoritmo.
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.ArrayList;

public class Kruskal {
    private LinkedHashSet<Edge> ladosArbol;

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
     * Inicialización para el algoritmo de Kruskal.
     * Crea y llena una cola de prioridad con los lados ordenados 
     * por costo. Luego se crean los conjuntos para el bosque.
     *
     * Sacamos de la cola la arista con menor costo, si no forma
     * un ciclo, se añade a la lista y se unen los conjuntos 
     * correspondientes.
     *
     * @param G     Grafo no orientado con costos a revisar.
     */
    public Kruskal(EdgeWeightedGraph G) {
        // Creamos y llenamos la cola ordenana de forma creciente
        Comparator<Edge> comparator = new EdgeComparator();
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(G.E(), comparator);
        ladosArbol = new LinkedHashSet<Edge>();
        ArrayList<Edge> edges = G.edges();
        for (Edge e : edges) 
            pq.add(e);

        // Creamos el bosque con un conjunto para cada nodo
        DisjointSetForest forest = new DisjointSetForest(G.V());
        for (int i = 0; i < G.V(); i++)
            forest.makeSet(i);

        // Algoritmmo de Kruskal
        while (!pq.isEmpty()) {
            // Tomamos la siguiente arista mas barata
            Edge e = pq.poll();
            // Si no forma ciclo con las ya añadidas
            int v = e.either();
            if (forest.conjuntosDiferentes(v, e.other(v))) {
                // La agregamos al arbol y unimos los conj
                ladosArbol.add(e);
                forest.union(v, e.other(v));
            }
        }
     }

    /**
     * Retorna el conjunto de lados que forman parte del arbol minimo cobertor.
     *
     * @return Lista con los lados del arbol.
     */
    public LinkedHashSet<Edge> getEdgesMST() {
	   return ladosArbol;
    }

    /**
    * Retorna la suma de los costos de los lados que forman parte del arbol 
    * minimo cobertor.
    *
    * @return Suma de los costos.
    */
    public double weight() {
        double total = 0.0;
        for (Edge e : ladosArbol) {
            total = total + e.weight();
       }
       return total;
    }
}
