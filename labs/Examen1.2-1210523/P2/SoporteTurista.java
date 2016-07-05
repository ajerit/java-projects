/**
 * Laboratorio de Algoritmos III
 * Abril - Julio 2016
 * Examen 1
 *
 * Solucion que implementa una modificacion recursiva al algoritmo de busqueda
 * en profundidad visto el clase, para resolver el problema de buscar una ruta
 * para un turista que cumpla con: el presupuesto y las preferencias de transporte
 * 
 * Dependencias: In.java StdOut.java Graph.java
 * Ejecucion: java SoporteTurista <float: presupuesto> <ruta.txt> <noQuiero.txt>
 *
 * @author Adolfo Jeritson. 12-10523
 * 
 */
import java.util.LinkedList;

public class SoporteTurista {
	private Graph                   G;
	private int                     V, E, nodoInicial, nodoFinal;
	private float[][]               costos;
    private LinkedList<Integer>     rutaFinal;
    private float                   costoFinal;
    private boolean                 haySolucion = false;
    private float                   presupuesto;
    private String[][]              transportes;             
    private String[]                noQuieroTransportes;

    /**
     * Constructor de la clase, de los datos pasados por consola se generan
     * las estructuras necesarias para resolver el problema, tales como el grafo
     * matrices para los costos y transportes, etc.
     *
     * @param in:   Datos del mapa.
     * @param t:    Datos de los transportes.
     * @param p:    Presupuesto.
     *
     */
	public SoporteTurista(In in, In t, float p) {
        // Inicializacion de variables
        presupuesto = p;
		G = new Graph(in.readInt());
		V = G.V();
		E = in.readInt();
		costos = new float[V][V];
        transportes = new String[V][V];
        LinkedList<Integer> caminoInicial = new LinkedList<Integer>();

        // Obtenemos las restricciones en transporte
        noQuieroTransportes = t.readAllLines();

        // Llenamos el grafo, costes y transportes correspondientes
		for (int i=0;i<E;i++) {
			int x = in.readInt();
			int y = in.readInt();
            costos[x][y] = in.readFloat();
            costos[y][x] = costos [x][y];
            transportes[x][y] = in.readString();
            transportes[y][x] = transportes[x][y];
			G.addEdge(x, y);
		}

        nodoInicial = in.readInt();
        nodoFinal = in.readInt();

		caminoInicial.add(nodoInicial);
		hallarRuta(G,nodoInicial,caminoInicial,0);
	}

    /**
     * Verificar si para una ruta encontrada, esta debe ser anulada si
     * contiene algun transporte que el cliente definio que no queria
     *
     * @param camino:   Lista enlazada con los nodos del camino.
     *
     * @return true si el camino usa un transporte no deseado, false en
     *          caso contrario.
     */
    public boolean transporteAnulaRuta(LinkedList<Integer> camino) {
        for (int i=0;i<camino.size()-1;i++) {
            for (String trans : noQuieroTransportes) {
                if (transportes[camino.get(i)][camino.get(i+1)].equals(trans))
                    return true;
            }
        }
        return false;
    }

    /**
     * Implementacion de una variante recursiva de DFS para generar los caminos
     * entre los nodos de inicio y fin. Se realiza el chequeo de si el costo es
     * menor al presupuesto y si se cumple que no se usa un transporte no deseado.
     *
     * @param grafoViejo:   Grafo actual que representa el camino.
     * @param inicio:       Nodo de inicio de la ruta.
     * @param caminoViejo:  Camino que ya se ha recorrido.
     * @param costo:        Costo acumulado de la ruta.
     *
     */
	public void hallarRuta(Graph grafoViejo, int inicio, LinkedList<Integer> caminoViejo, float costo) {
		if(inicio == nodoFinal) {
            if (costo <= presupuesto && !transporteAnulaRuta(caminoViejo)) {
                // Solo nos piden una ruta.
                rutaFinal = caminoViejo;
                costoFinal = costo;
                haySolucion = true;
            }
            return;
		}

		for(int sucesor: grafoViejo.adj(inicio)) {
			// Actualizar el valor del costo del camino
			float costoNuevo = costo + costos[inicio][sucesor];
            
			// Crear una copia del grafo de entrada
            // Y eliminanos el lado para evitar repetirlo
			Graph grafoNuevo = new Graph (grafoViejo);
			grafoNuevo.removeEdge(inicio,sucesor);

			LinkedList<Integer> caminoNuevo = new LinkedList<Integer>();
			// Añadir camino ya recorrido
			caminoNuevo.addAll(caminoViejo);
			caminoNuevo.add(sucesor);

			hallarRuta(grafoNuevo, sucesor, caminoNuevo, costoNuevo);
		} 
	}

    /**
     * Imprimir la solucion, si existe, al problema de encontrar la ruta.
     */
    public void printSol() {
        if (haySolucion) {
            StdOut.println(rutaFinal);
            StdOut.println(costoFinal);
        } else {
            StdOut.println("No existe ruta.");
        }

    }

	public static void main(String[] args) {
        float p = Float.parseFloat(args[0]);
		In mapa = new In(args[1]);
        In trans = new In(args[2]);
		SoporteTurista problema = new SoporteTurista(mapa, trans, p);
        problema.printSol();
	}
}
