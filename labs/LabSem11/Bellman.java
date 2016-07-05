/**
 * Algoritmo de Bellman para encontrar caminos de costo minimo.
 *
 * @author Gianni Manilia
 * @author Adolfo Jeritson
 */
import java.util.*;

public class Bellman {
	private final double INFINITO = Double.POSITIVE_INFINITY;
	EdgeWeightedDigraph G;
	int s;
	double costos[];
	int apuntadores[];

	/** 
	 * Inicialización para el algoritmo de Bellman.
	 * 
	 * Se un arreglo de costos desde el nodo inicial
	 * y un arreglo de apuntadores para saber cuáles son los
	 * vértices anteriores
	 *
	 * @param G     Grafo no orientado con costos a revisar.
	 * @param s 	Vértice inicial
	 */
	public Bellman(EdgeWeightedDigraph G, int s) {
		this.G = G;
		this.s = s;
		costos = new double[G.V()];
		costos[s] = 0;
		apuntadores = new int[G.V()];
	}

	/** 
	 * Halla el costo del camino mínimo hasta el vértice final
	 *
	 * @param s 	Vértice final
	 */
	public double caminoCostoMinimo(int s) {
		for (int v=0;v<G.V();v++) {
			if (v!=this.s)
				costos[v] = INFINITO;
		}
		// Vértice para manipular la pila
		int n;
		// Pila de vértices
		Stack<Integer> T = new Stack<Integer>();
		T.push(this.s);
		// Mientras la pila no esté vacía
		while(!T.isEmpty()) {
			// Tomo un elemento de la pila
			n = T.pop();
			// Para cada sucesor de n
			for(DirectedEdge edge : G.adj(n)) {
				G.indegree[edge.to()] -= 1;
				if(G.indegree(edge.to())==0)
					T.push(edge.to());
				// Si el costo a m es menor que el costo hasta n más el costo de n a m
				if(costos[edge.to()]>costos[n]+edge.weight()) {
					// Guardar el costo del nuevo camino
					costos[edge.to()]=costos[n]+edge.weight();
					// Guardar el vértice anterior
					apuntadores[edge.to()] = n;
				}
			}
		}
		return costos[s];
	}

    /** 
     * Halla el camino de costo mínimo hasta el vértice final
	 *
     * @param v 	Vértice final
     */
	public ArrayList<Integer> obtenerCaminoMimino(int v) {
		ArrayList<Integer> caminoMinimo = new ArrayList<>();
		int actual = v;

		while (actual!=s) {
			caminoMinimo.add(actual);
			actual = apuntadores[actual];
		}
		caminoMinimo.add(actual);

		return caminoMinimo;
	}

    /** 
     * Chequea si existe un camino hasta el vértice final
	 *
     * @param s 	Vértice final
     */
	public boolean exitsteCaminoHasta (int s) {
		return (costos[s]!=INFINITO);
	}
}