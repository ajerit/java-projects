/**
 * Laboratorio de Algoritmos III. Abril-Julio 2016
 * @author Gianni Manilia
 * @author Adolfo Jeritson
 *
 * Clase que implementa el algormitmo DFS de busqueda en profundidad y
 * otras funciones para el laboratorio. 
 * Se usa una lista enlazada para caminos cerrados, y un Stack para 
 * caminos abiertos
 *  Dependencias: StdIn.java StdOut.java Stack.java Digraph.java
 */
import java.util.*;

public class DepthFirstSearch {

	Stack<LinkedList<Integer>> abiertos;        // Pila de caminos abiertos
	LinkedList<LinkedList<Integer>> cerrados;   // Lista de listas de caminos
	boolean[] estaCerrado;                  	// Booleano para ver si hay un camino cerrado en alguna posicion
	boolean[] estuvoEnAbierto;
	int cantidadVertices;                   	// Cantidad de vertices del grafo

	/**
	* Metodo constructor de la clase
	* @param  G Digrafo sobre el cual realizar a la busqueda
	* @param  s Vertice inicial de la busqueda
	* 
	*/
	public DepthFirstSearch (Graph G, int s) {
		abiertos = new Stack<LinkedList<Integer>>();
		cerrados = new LinkedList<LinkedList<Integer>>();
		estaCerrado = new boolean[G.V()];
		estuvoEnAbierto = new boolean[G.V()];
		cantidadVertices = G.V();
		// Inicializar arreglo de caminos cerrados
		for (int i=0; i<G.V(); i++) {
			estaCerrado[i] = false;
			estuvoEnAbierto[i] = false;
		}
		dfs(G,s);
	}

	/**
	* Implementacion del algoritmo DFS (busqueda en profundidad)
	* @param  G Digrafo sobre el cual realizar a la busqueda
	* @param  s Vertice inicial de la busqueda
	*/
	public void dfs (Graph G, int s) {
		LinkedList<Integer> topePila = new LinkedList<Integer>();
		LinkedList<Integer> camino = new LinkedList<Integer>();

		camino.add(s);
		// Empilar nodo inicio
		abiertos.push(camino);
		// Mientras que la pila de abiertos no esté vacía
		while(!abiertos.isEmpty()) {
			// Tomar primer elemento de la pila
			topePila = abiertos.pop();
			// Marcar como cerrado la posicion del vértice
			estaCerrado[topePila.getLast()] = true;
			// Verificar que no haya habido un camino con igual nodo terminal en abierto
			if(!estuvoEnAbierto[topePila.getLast()]) {
				// Marcar que pasó por la pila de abiertos
				estuvoEnAbierto[topePila.getLast()] = true;
				// Añadir a la lista de cerrados
				cerrados.add(topePila);
				// Recorrer sucesores
				for(int i : G.adj(topePila.getLast())) {
					// Si no existe un camino cerrado al sucesor i
					// System.out.println(""+i+"-"+topePila.getLast()+": "+estaCerrado[i]);
					if(!estaCerrado[i]) {
						camino = new LinkedList<Integer>();
						camino.addAll(topePila);
						camino.add(i);
						// Añadir ese camino a la pila de abiertos
						abiertos.push(camino);
					}
				}
			}
			
		}
	}

	/**
	* Retorna un arreglo que indica para cada vertice, cual fue 
	* el vertice anteriormente visitado en la b busqueda
	* @return  arreglo de vertices visitados
	*/
	public Integer[] arcsVisited () {
		int cantidadCaminos = cerrados.size();
		Integer[] verticeAnterior = new Integer[cantidadVertices];
		// Recorrer todos los vértices
		for(int i=0; i<cantidadVertices; i++) {
			// Recorrer todos los caminos
			for(int j=0; j<cantidadCaminos; j++) {
				// Largo del camino donde estoy parado
				int largoCamino = cerrados.get(j).size();
				// Último vértice del camino donde estoy parado
				int verticeLlegada = cerrados.get(j).get(largoCamino-1);
				// Si el ultimo vertice del camino es mi vertice de llegada
				if (verticeLlegada == i) {
					// Si el vertice de llegada es el mismo que el de salida
					if (largoCamino == 1) {
						verticeAnterior[i] = null;
					}
					else {
						verticeAnterior[i] = cerrados.get(j).get(largoCamino-2);
					}
					break;
				}
			}
		}
		return verticeAnterior;
	}

	/**
	* Dado un vertice v, retorna los vertices del camino que 
	* encontro el algoritmo busqueda, desde el vertice fuente s hasta 
	* el vertice v, contenidos en una clase LinkedList.
	* @param   vertice v
	* @return  Lista enlazada con el camino s-v
	*/
	public LinkedList<Integer> getDirectedPathTo(int v) {
		LinkedList<Integer> directedPath = new LinkedList<Integer>();
		for(LinkedList<Integer> path : cerrados) {
			if (path.peekLast() == v) {
				directedPath = path;
				break;
			}
		}
		return directedPath;
	}

	/**
	* Retorna una lista con todos los caminos desde el vertice 
	* fuente s, hasta todos vertices que le son alcanzables.
	* @return Lista de listas con los caminos que parten de s
	*/
	public LinkedList<LinkedList<Integer>> getAllDirectedPath() {
		LinkedList<LinkedList<Integer>> allPaths = new LinkedList<LinkedList<Integer>>();
		for(LinkedList<Integer> path : cerrados) {
			allPaths.add(path);
		}
		return allPaths;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);

		int s = Integer.parseInt(args[1]);
		DepthFirstSearch DFS = new DepthFirstSearch(G,s);
	}
}
