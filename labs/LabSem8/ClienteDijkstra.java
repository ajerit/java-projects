/**
 * Laboratorio de Algoritmos III
 * Trimestre Abril - Julio 2016
 * Semana 8
 *
 * Cliente para probar el algoritmo de Dijkstra.
 *
 * @author Adolfo Jeritson 12-10523
 * @author Gianni Manilia  12-10903
 */

public class ClienteDijkstra {
    private Digraph G;

    /**
     * Constructor del cliente de pruebas.
     * Crea una instancia de la implementacion del algoritmo para
     * ejecutarlo y luego imprime los resultados por salida estandar.
     */
    public ClienteDijkstra(In in, int nodoInicial) {
        G = new Digraph(in);
        Dijkstra busqueda = new Dijkstra(G, nodoInicial);
        printSol(busqueda);
    }

    /** 
     * Imprime por salida estandar los resultados.
     * Para cada vértice alcanzable, imprime el costo del camino 
     * mínimo hacia él, así como el vértice antecesor en el camino 
     * de costo mínimo. Si el vértice no es alcanzabl imprime NA.
     *
     * @param busqueda Objeto instanciado de clase Dijkstra.
     */
    public void printSol(Dijkstra busqueda) {
        Integer[] costoA = busqueda.costs();
        Integer[] prev = busqueda.arcsVisited();
        for (int i = 0; i < G.V(); i++) {
            if (busqueda.hasPathTo(i)) {
                StdOut.println(String.format("%d  %d  %d", i, costoA[i], prev[i]));
            } else {
                StdOut.println(String.format("%d  NA", i));
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int nodoInicial = Integer.parseInt(args[1]);
        ClienteDijkstra cliente = new ClienteDijkstra(in, nodoInicial);
    }
}