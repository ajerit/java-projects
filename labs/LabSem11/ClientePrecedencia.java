/**
 * Cliente para probar el algoritmo de grafos de precedencia.
 * @author Adolfo Jeritson
 * @author Gianni Manilia
 */
import java.util.ArrayList;

public class ClientePrecedencia {

    public static void main(String args[]) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        GrafoPrecedencia test = new GrafoPrecedencia(G, 0);
        ArrayList<Integer> min = test.obtenerCaminoMinimo(6);

        StdOut.println("Hay camino:");
        StdOut.println(test.existeCaminoMinHasta(6));

        StdOut.println("Costos: ");
        for (double n : test.cmin) {
            StdOut.println(n);
        }

        StdOut.println("Nodos del camino: ");
        for (int n : min) {
            StdOut.println(n);
        }
        StdOut.println("Costo: ");
        StdOut.println(test.caminoCostoMinimo(6));

    }
}
