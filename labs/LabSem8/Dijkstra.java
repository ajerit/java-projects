/**
 * Laboratorio de Algoritmos III
 * Trimestre Abril - Julio 2016
 * Semana 8
 *
 * Implementación del algoritmo de Dijkstra para encontrar caminos
 * de costos mínimos. El algoritmo falla si se usan caminos con 
 * costos negativos, por lo tanto el programa lanza una excepción
 * si encuentra alguno.
 *
 * Dependencias: StdOut.java Digraph.java 
 *
 * @author Adolfo Jeritson 12-10523
 * @author Gianni Manilia  12-10903
 */
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;

public class Dijkstra {
    private Integer[] costoA, prev;
    private int       costoMax = 0;

    /**
     * Clase que implementa el comparador para la cola de prioridad.
     */
    private class PathComparator implements Comparator<Integer> {
        private Integer[] costoA = costs();
        @Override
        public int compare(Integer v, Integer w) {
            return (costoA[v] - costoA[w]);
        }
    }

    /**
     * Constructor para realizar la busqueda de costos mínimos.
     * Se revisan los lados del grafo para comprobar que no tenga
     * caminos con costos negativos (falla el algoritmo) y se 
     * calcula el costo máximo para usarlo como tope.
     *
     * @throws IllegalArgumentException Si existe costo negativo.
     */
    public Dijkstra(Digraph G, int s) {
        costoA = new Integer[G.V()];
        prev = new Integer[G.V()];
        // Revisamos que no hayan caminos con costo negativo
        for (Arc e : G.lados()) {
                costoMax = costoMax + e.costo();
                if (e.costo() < 0) {
                    throw new IllegalArgumentException("No pueden haber lados con costos negativos");
            }
        }
        // Definimos el costo maximo
        costoMax++;
        caminosMinimo(G, s);
    }

    /**
     * Implementación del algoritmo de Dijkstra para costos mínimos.
     * Se utiliza una cola de prioridad para optimizar el tiempo del
     * algoritmo. Como costo máximo se utiliza la suma de todos los
     * costos + 1.
     */
    public void caminosMinimo(Digraph G, int s) {
        // Inicializar comparador y cola de prioridad
        Comparator<Integer> comparator = new PathComparator();
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(G.E(), comparator);
        
        // Inicializamos costos al maximo y añadimos a la cola
        for (int v = 0; v < G.V(); v++) {
            if (v != s) {
                costoA[v] = costoMax;
                pq.add(v);
            }
        }

        // Inicializar el costo del nodo inicial a 0 y añadimos
        costoA[s] = 0;
        pq.add(s);

        while (!pq.isEmpty()) {
            // Sacamos el nodo con menor costo
            int v = pq.poll();
            // Revisamos los vecinos
            for (int w : G.adj(v)) {
                // Costo de ruta alternativa
                Integer alt = costoA[v] + G.costo(v, w);
                // Si el costo alternativo es menor, actualizamos al nuevo
                if (alt < costoA[w]) {
                    costoA[w] = alt;
                    prev[w] = v;
                    // Actualizamos el valor del nodo en la cola
                    pq.remove(w); pq.add(w);
                }
            }
        }
    }

    /**
     * Verifica que se encontró un camino al vertice v.
     *
     * @param v Entero que representa al vertice.
     *
     * @return true si se encontró un camino, 
     *          false en caso contrario.
     */
    public boolean hasPathTo(int v) {
        return costoA[v] < costoMax;
    }

    /**
     * Retorna una lista con el camino encontrado al vertice v.
     * Usando los apuntadores se reconstruye el camino de costo
     * minimo y se guarda en una lista.
     *
     * @param v Entero que representa al vertice.
     *
     * @return Lista enlazada con el camino
     */
    public LinkedList<Integer> getPathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedList<Integer> path = new LinkedList<Integer>();
        int x;
        for (x = v; costoA[x] != 0; x = prev[x])
            path.push(x);
        path.push(x);
        return path;
    }

    /**
     * Retorna un arreglo con los costos hasta cada vértice.
     * La i-ésima posición del arreglo representa el costo
     * del camino mínimo encontrado al vértice i.
     *
     * @return Arreglo de enteros con los costos
     */
    public Integer[] costs() {
        return costoA;
    }

    /**
     * Retorna un arreglo con los vertices anteriores.
     * La i-ésima posición en el arreglo indica el vértice 
     * anterior en el camino de costo mínimo al vértice i. A través 
     * de los apuntadores es posible reconstruir el camino de costo 
     * mínimo a cada uno de los vértices.
     *
     * @return Arreglo de enteros con los vertices
     */
    public Integer[] arcsVisited() {
        return prev;
    }
}
