/**
 * Laboratorio de Algoritmos III
 * Abril - Julio 2016
 *
 * Proyecto 1
 * Solucion al problema 1
 * 
 * Compilacion javac Moria.java
 * Ejecucion: java Moria <nombre de archivo>
 * Dependencias: In.java StdIn.java StdOut.java  Graph.java BreathFirstSearch.java
 *
 * @author Gianni Manilia. 12-10903
 * @author Adolfo Jeritson. 12-10523
 * 
 */
import java.util.LinkedList;

public class Moria {
	private Graph                G;
	private int                  V, E, nodoInicial, nodoSalida;
	private boolean              haySolucion = false;
	private LinkedList<Integer>  solucion;
	private boolean[][]          anillos;

    /**
     *   Inicializacion de la clase para la solución del problema. Se lee el archivo de texto y se inicializa 
     * el grafo correspondiente al problema, tambien se extraen los nodos iniciales y finales, y se llena una
     * matriz para saber en cuales pasillos hay anillos.
     *
     * @param in:         Contenido del archivo de texto pasado por consola
     *
     */
	public Moria(In in) {
		G = new Graph(in.readInt());
		V = G.V();
		E = in.readInt();
		anillos = new boolean[V][V];
        solucion = new LinkedList<Integer>();
		nodoInicial = in.readInt();
		nodoSalida = in.readInt();

		for (int i=0;i<E;i++) {
			int x = in.readInt();
			int y = in.readInt();
			if (in.readInt() == 1) {
				anillos[x][y] = true;
				anillos[y][x] = true;
			}
			G.addEdge(x, y);
		}

		LinkedList<Integer> caminoViejo = new LinkedList<Integer>();
		caminoViejo.add(nodoInicial);
		hallarAnillos(G,nodoInicial,caminoViejo,false);
	}

    /**
     *   Implementacion de la solucion del problema. Se resuelve utilizando la tecnica de backtracking para
     * encontrar todos los caminos posibles desde el noco inicio hasta cada uno de los anillos. Luego se usa
     * BSF para hallar camino desde el anillo hasta la salida.
     *
     * @param grafoViejo:  Grafo no dirigido, donde ya se han recorrido pasillos y por lo tanto estan 'colapsados'
     * @param inicio:      Entero que representa el nodo de inicio
     * @param caminoViejo: Lista enlazada de enteros que representa el camino recorrido
     * @param conseguido:  Booleano que indica si se consiguio un camino hasta un anillo
     *
     */
	public void hallarAnillos(Graph grafoViejo, int inicio, LinkedList<Integer> caminoViejo, boolean conseguido) {
		if(conseguido) {
			// Imprimir el camino hasta el anillo conseguido
			for(int e : caminoViejo) 
				StdOut.print(e+" ");
			StdOut.println();
			// Verificar que haya un camino hacia la salida
			BreadthFirstSearch bfs = new BreadthFirstSearch(grafoViejo, inicio);
			if (bfs.hasPathTo(nodoSalida) && solucion.isEmpty()) {
				haySolucion = true;
				solucion.addAll(caminoViejo);
				// Camino a la salida desde
				LinkedList<Integer> caminoSalida = new LinkedList<Integer>();
				caminoSalida = bfs.getPathTo(nodoSalida);
				// Quitar el primer elemento para evitar repeticiones
				caminoSalida.remove(0);
				// Concatenar el camino viejo con el camino de salida
				solucion.addAll(caminoSalida);
			}
		}

		for(int sucesor: grafoViejo.adj(inicio)) {
			// Verificar si hay un anillo en la arista
			boolean hayAnillo = anillos[inicio][sucesor];
			// Crear una copia del grafo de entrada
			Graph grafoNuevo = new Graph (grafoViejo);
			// Eliminar la arista del grafo nuevo
			grafoNuevo.removeEdge(inicio,sucesor);
			// Inicializar camino solución
			LinkedList<Integer> caminoNuevo = new LinkedList<Integer>();
			// Añadir todo lo que había en el camino anterior
			caminoNuevo.addAll(caminoViejo);
			// Añadir el sucesor al camino solución
			caminoNuevo.add(sucesor);
			// Llama recursiva a la función
			hallarAnillos(grafoNuevo,sucesor,caminoNuevo,hayAnillo);
		} 
	}

    /**
     *   Imprimir en consola si se encontro una solucion al problema, y cual es
     * en caso de haberla encontrado.
     *
     */
	public void printSol() {
		StdOut.println();
		if (haySolucion) {
			StdOut.println("El problema tiene solución");
			StdOut.println();
			// Imprimir el camino solución
            StdOut.println();
			for(int e : solucion) 
				StdOut.print(e+" ");
			StdOut.println();
		}
		else
			StdOut.println("El problema no tiene solución");
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		Moria proyecto = new Moria(in);
		proyecto.printSol();
	}
}
