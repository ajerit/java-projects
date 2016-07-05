/**
 * Implementación del algoritmo de Prim para obtener el arbol 
 * cobertor de costo mínimo de un grafo no orientado. 
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class Prim {
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
     * Inicialización para el algoritmo de Prim.
     * Se crea la cola con los lados y se añaden, luego se crea un conjunto
     * con todos los nodos y el conjunto de los nodos a revisar.
     *
     * Se inicia añadiendo el nodo 0 para empezar por el, se llama a la funcion
     * que escoge el lado minimo y se añade a la lista de lados del arbol.
     *
     * @param G     Grafo no orientado con costos a revisar.
     */
    public Prim(EdgeWeightedGraph G) {
        // Creamos y llenamos la cola ordenana de forma creciente.
        Comparator<Edge> comparator = new EdgeComparator();
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(G.E(), comparator);
        for (Edge e : G.edges()) 
            pq.add(e);

        ladosArbol = new LinkedHashSet<Edge>();
        LinkedHashSet<Integer> u = new LinkedHashSet<Integer>();
        LinkedHashSet<Integer> v = new LinkedHashSet<Integer>();
        for (int i = 0; i < G.V(); i++)
            v.add(i);

        // Se puede empezar con cualquier nodo, usamos 0.
        u.add(0);
        while(!u.containsAll(v)) {
            // Escoger lado costo minimo con inicial en U, final en V-U.
            Edge e = escogerLadoMin(u, v, pq);
            int inicial = e.either();
            ladosArbol.add(e);
            u.add(e.other(inicial));
        }
     }

    /**
     * Escoger el camino con costo minimo para el caso requerido.
     * El algorito de Prim necesita escoger un lado que sea de costo minimo
     * y que uno de sus extremos se encuentre en el conjunto U de nodos ya 
     * revisados, y el otro en la diferencia de los conjuntos V - U.
     *
     * @param u             Conjunto de nodos ya revisados.
     * @param v             Conjunto de todos los nodos del grafo.
     * @param colaLados     Cola con todos los caminos del grafo.
     *
     * @return Lado de costo minimo que cumple las condiciones, 
     *          null si no se consigue lado.
     */
    public Edge escogerLadoMin(LinkedHashSet<Integer> u, LinkedHashSet<Integer> v, PriorityQueue<Edge> colaLados) {
        // Copia local de la cola para no modificar.
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(colaLados);
        LinkedHashSet<Integer>  terminales = new LinkedHashSet<Integer>(v);
        
        for (Integer n : v) {
            if (u.contains(n))
                terminales.remove(n);
        }

        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            int inicial = e.either();
            if (u.contains(inicial) && terminales.contains(e.other(inicial)))
                return e;
        }
        return null;
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
