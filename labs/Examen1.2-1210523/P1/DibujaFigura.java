/**
 * Laboratorio de Algoritmos III
 * Abril - Julio 2016
 * Examen 1
 *
 * Resuelve el problema de dibujar una figura sin pasar dos veces sobre un mismo
 * lado, el cual es equivalente a chequear si el grafo de la figura posee un ciclo
 * euleriano.
 *
 * Dependencias: In.java StdOut.java Graph.java
 * Ejecucion: java DibujaFigura <figura.txt>
 *
 * @author Adolfo Jeritson. 12-10523
 * 
 */
import java.util.LinkedList;

public class DibujaFigura {
    private Graph G;

    /**
     * Constructor de la clase, se inicializa el grafo con el archivo
     * pasado por consola.
     *
     * @param in:   Datos de la figura.
     *
     */
	public DibujaFigura(In in) {
        G = new Graph(in);
        hallarSol();
	}

    /**
     * Encuentra la solucion al buscar si el grafo posee un ciclo euleriano,
     * utilizando la implementacion que realizamos en un laboratorio pasado.
     * En caso se existir el ciclo, lo imprime.
     *
     */
    public void hallarSol() {
        if (G.hasEuler()) {
            StdOut.println("Es posible.");
            StdOut.println(G.getCicloEuleriano());
        } else {
            StdOut.println("No es posible.");
        }

    }

	public static void main(String[] args) {
		In in = new In(args[0]);
		DibujaFigura problema = new DibujaFigura(in);
	}
}
