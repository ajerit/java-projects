/**
 * Encuentra la menor cantidad de cableado necesario para 
 * conectar los faros de la ciudad de Paris, usando el Algoritmo
 * de Kruskal para hallar el arbol cobertor minimo del grafo asociado.
 *
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.lang.Math;

public class Paris {
    private EdgeWeightedGraph G;

    /**
     * Inicializa los datos para resolver el problema.
     * Del archivo fuente se leen la cantidad de nodos, se crea un grafo
     * y se añaden los lados con el costo asociado a la distancia entre los nodos.
     *
     * @param in Datos del archivo fuente.
     */
    public Paris(In in) {
        int nNodos = in.readInt();
        G = new EdgeWeightedGraph(nNodos);

        // Obtener posiciones de los nodos
        LinkedList<LinkedList<Double>> posiciones = new LinkedList<LinkedList<Double>>();
        for (int i=0; i<G.V(); i++) {
            LinkedList<Double> pos = new LinkedList<Double>();
            pos.add(in.readDouble());
            pos.add(in.readDouble());
            posiciones.add(pos);
        }

        // Calcular costo de cada arco del grafo
        for (int j=0; j<G.V();  j++) {
            for (int k=0; k<G.V(); k++) {
                if (j!=k) {
                    Double dist;
                    Double x2 = posiciones.get(k).get(0);
                    Double y2 = posiciones.get(k).get(1);
                    Double x1 = posiciones.get(j).get(0);
                    Double y1 = posiciones.get(j).get(1);

                    dist = Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
                    Edge e = new Edge(j, k, dist);
                    G.addEdge(e);
                }
            }
        }

        // Hallar arbol cobertor con kruskal e imprimir.
        Kruskal kruskal = new Kruskal(G);
        LinkedHashSet<Edge> ladosArbol = kruskal.getEdgesMST();
        for (Edge e : ladosArbol) {
            int v = e.either();
            int w = e.other(v);
            StdOut.println(String.format("%d-%d", v, w));
        }
        StdOut.println(String.format("%.2f", kruskal.weight()));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Paris paris = new Paris(in);
    }
}