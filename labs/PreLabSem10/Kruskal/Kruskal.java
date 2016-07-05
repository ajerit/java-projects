/**
 * Implementación del algoritmo de Kruskal para obtener el arbol 
 * cobertor de costo mínimo de un grafo no orientado.
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.PriorityQueue;
import java.util.Comparator;

public class Kruskal {
    private Graph arbolCobertor;
    private PriorityQueue<Lado> pq;
    private DisjointSetForest forest;

    /**
     * Clase que implementa el comparador para la cola de prioridad.
     */
    private class PathComparator implements Comparator<Lado> {
        @Override
        public int compare(Lado a, Lado b) {
            return (a.costo() - b.costo());
        }
    }

    /** 
     * Inicialización para el algoritmo de Kruskal.
     * Crea y llena una cola de prioridad con los lados ordenados 
     * por costo. Luego se crean los conjuntos para el bosque.
     *
     * @param G    Grafo donde se busca el arbol.
     */
    public Kruskal(Graph G) {
        Comparator<Lado> comparator = new PathComparator();
        pq = new PriorityQueue<Lado>(G.E(), comparator);

        for (Lado e : G.lados()) 
            pq.add(e);

        forest = new DisjointSetForest(G.V());
        for (int i = 0; i < G.V(); i++)
            forest.makeSet(i);
        
        obtenerArbolCobertor(G);
    }

    /**
     * Algoritmo de Krukal para obtener arbol cobertor mínimo.
     * Se crea un grafo nuevo. Luego sacamos de la cola la arista
     * con menor costo, y si no forma un ciclo en el grafo, se añade
     * al arbol cobertor y se unen los conjuntos correspondientes.
     * 
     * @param G    Grafo donde se busca el arbol.
     */
    public void obtenerArbolCobertor(Graph G) {
        Graph arbolCobertor = new Graph(G.V());
        // Se puede mejorar, revisar si solo queda un conj en el bosque
        while (!pq.isEmpty()) {
            // Tomamos la siguiente arista mas barata
            Lado e = pq.poll();
            // Si no forma ciclo con las ya añadidas
            if (forest.conjuntosDiferentes(e.from(),e.to())) {
                // La agregamos al arbol y unimos los conj
                arbolCobertor.addEdge(e.from(), e.to(), e.costo());
                forest.union(e.from(), e.to());
            }
        }
        this.arbolCobertor = arbolCobertor;
    }

    /** 
     * Retorna el arbol cobertor mínimo encontrado.
     *
     * @return Grafo con el Arbol Cobertor
     */
    public Graph arbolCobertor() {
        return arbolCobertor;
    }

    /** 
     * Imprime en salida estandar en arbol cobertor.
     */
    public void printArbol() {
        StdOut.println("Aristas del Arbol Cobertor:");
        for (Lado e : arbolCobertor.lados()) {
            StdOut.println(String.format("%d - %d (%d)", e.from(), e.to(), e.costo()));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        Kruskal test = new Kruskal(G);
        test.printArbol();
    }
}