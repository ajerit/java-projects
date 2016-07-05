/**
 * Cliente para realizar la prueba a los algoritmos de Kruskal
 * y de Prim.
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.LinkedHashSet;

public class TestPrimKruskal {

    /**
     * Probar los algoritmos de Kruskal y Prim.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);

        Kruskal testKruskal = new Kruskal(G);
        Prim testPrim = new Prim(G);

        LinkedHashSet<Edge> arbolKruskal = testKruskal.getEdgesMST();
        LinkedHashSet<Edge> arbolPrim = testPrim.getEdgesMST();

        StdOut.println();
        StdOut.println("Lados del A.C.M. de Kruskal:");
        for (Edge e : arbolKruskal)
            StdOut.println(e.toString());
        StdOut.println();
        StdOut.println("Lados del A.C.M. de Prim:");
        for (Edge e : arbolPrim)
            StdOut.println(e.toString());
        StdOut.println();

        boolean mismoCosto = (testKruskal.weight() == testPrim.weight());
        StdOut.println(String.format("Los algoritmos encontraron el mismo arbol: %B", mismoCosto));
        StdOut.println();
    }
}
