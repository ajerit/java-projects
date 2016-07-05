/**
 * Cliente para realizar la prueba al algoritmo de Bellman.
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.ArrayList;

public class ClienteBellman {

	/**
	 * Probar el algoritmo de Bellman.
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		Bellman bellman = new Bellman(G,0);
		StdOut.println("Costo del camino: "+bellman.caminoCostoMinimo(6));
		StdOut.print("Camino: ");
		ArrayList<Integer> caminoMinimo = bellman.obtenerCaminoMimino(6);
		for(int i=0; i<caminoMinimo.size();i++) {
			System.out.print(caminoMinimo.get(i)+" ");
		}
		System.out.println("");
		StdOut.println("¿Hay un camino? "+bellman.exitsteCaminoHasta(7));
	}
}