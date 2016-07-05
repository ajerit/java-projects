/**
 * Implementación del algoritmo de Floyd-Warshall para obtener los
 * caminos de costo minimo entre cada par de vertices en un grafo.
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
import java.util.ArrayList;
import java.util.Collections;

public class Floyd {
	private Edge[][] M;
    private final double INFINITO = Double.POSITIVE_INFINITY; 

    /**
     * Iniciarlizacion de la matriz del algoritmo.
     * En este primer paso, se llena la matriz con lado cuyo costo es
     * infinito, y en la diagonal de costo cero.
     *
     * @param G     Digrafo para buscar caminos.
     */
	public Floyd(EdgeWeightedGraph G) {
		Edge nuevoLado;
		M = new Edge[G.V()][G.V()];
		for (int i = 0; i < G.V(); i++) {
			for (int j = 0; j < G.V(); j++) {
				if (i == j) {
					nuevoLado = new Edge(i, j, 0);
					M[i][j] = nuevoLado;
				} else {
					nuevoLado = new Edge(i, j, INFINITO);
					M[i][j] = nuevoLado;
				}
			}
		}		
		calcularMatriz(G);
	}

    /**
     * Obtiene los caminos de costo minimo entre cada par de nodos.
     * Primero se actualiza la matriz con los arcos y sus costos del grafo,
     * luego se aplica el algoritmo de Floyd-Warshall para calcular el camino
     * minimo entre cada par de vertices.
     *
     * @param G     grafo para buscar caminos.
     */
	public void calcularMatriz(EdgeWeightedGraph G) {
		Edge nuevoLado;
        ArrayList<Edge> edges = G.edges();
		// Llenar con arcos del grafo.
		for (Edge e : edges) {
            int v = e.either();
			nuevoLado = new Edge(v, e.other(v), e.weight());
			M[v][e.other(v)] = nuevoLado;
            M[e.other(v)][v] = nuevoLado;
		}
		// Hallar caminos de costo mínimo
		for (int k = 0; k < G.V(); k++) {
			for (int i = 0; i < G.V(); i++) {
                if (M[i][k].weight() < INFINITO && i != k) {
    				for (int j = 0; j < G.V(); j++) {
                        if (j!=k && i!=j && M[k][j].weight() < INFINITO) {
        					if (M[i][j].weight() > M[i][k].weight() + M[k][j].weight()) {
        						nuevoLado = new Edge(k, j, M[i][k].weight() + M[k][j].weight());
        						M[i][j] = nuevoLado;
        					}
                        }
    				}
                }
			}
		}
	}

    /** 
     * Retorna la matriz de arcos del grafo.
     *
     * @return Matriz con los lados.
     */
	public Edge[][] obtenerMatriz() {
		return M;
	}

    /**
     * Retorna el costo del camino minimo entre los vertices. 
     * El costo del camino minimo es la suma de todos los costos (o pesos) 
     * de los lados que son parte del camino de costo minimo entre s y t. 
     * En caso de que no exista un camino entre s y t, se retorna Double.MAX VALUE.
     *
     * @param s  Nodo del grafo.
     * @param t  Nodo del grafo.
     *
     * @return entero que representa el costo del camino minimo entre s y t, 
     *           Double.MAX VALUE si el camino no existe.
     */
	public double dist(int s, int t) {
		if (M[s][t].weight() == INFINITO)
			return Double.MAX_VALUE;
		else
			return M[s][t].weight();
	}

    /**
     * Retorna el camino de costo minimo entre dos vertices. 
     * En caso de no existir un camino, retorna null.
     *
     * @param s  Nodo del grafo.
     * @param t  Nodo del grafo.
     *
     * @return Lista con los arcos del camino, null si no hay camino.
     */

    // Edite el algoritmo para que funcione con grafo no orientado
    // pero se queda pegado cuando busca el path de un camino
	public ArrayList<Edge> path(int s, int t) {
		if (M[s][t].weight() == INFINITO)
			return null;
		else {
			ArrayList<Edge> camino = new ArrayList<Edge>();
			int inicio = t;
            while (inicio != s) {
				camino.add(M[s][inicio]);
                int v = M[s][inicio].either();
				if (inicio == v)
                    inicio = M[s][inicio].other(v);
                else
                    inicio = v;
			}
            Collections.reverse(camino);
			return camino;
		}
	}
}
