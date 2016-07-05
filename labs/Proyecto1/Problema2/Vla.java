/**
 * Laboratorio de Algoritmos III
 * Abril - Julio 2016
 *
 * Proyecto 1
 * Solucion al problema 2
 * 
 * Compilacion javac Vla.java
 * Ejecucion: java Vla <nombre de archivo>
 * Dependencias: In.java StdIn.java StdOut.java  Graph.java
 *
 * @author Gianni Manilia.  12-10903
 * @author Adolfo Jeritson. 12-10523
 * 
 */
import java.util.LinkedList;

public class Vla {
    private Digraph                            G;
    private boolean[]                          visit;
    private LinkedList<LinkedList<Integer>>    caminos;
    private int                                cantidadLetras;
    private LinkedList<Character>              letrasEnUso;

    /**
     * Constructor de la clase para inicializar la solucion al problema, 
     * se inicilizan las variables necesarias, se lee del archivo cuales
     * letras estan presentes en la palabra y finalmente se añaden los
     * lados al grafo dirigido que representa la palabra a buscar.
     */
    public Vla(In in) {
        // Inicializar variables
        int n = in.readInt();
        LinkedList<String> lineas = new LinkedList<String>();
        letrasEnUso = new LinkedList<Character>();
        caminos = new LinkedList<LinkedList<Integer>>();
        cantidadLetras = 0;

        // Leer cada linea para ver cuales letras se estan usando
        for (int i=0;i<=n;i++) {
            String line = in.readLine();
            lineas.add(line);
            for (int j=0;j<line.length();j++) {
                if (!letrasEnUso.contains(line.charAt(j))) {
                    letrasEnUso.add(line.charAt(j));
                    cantidadLetras++;
                }
            }
        }

        // Añadir los lados correspondientes al grafo
        G = new Digraph(cantidadLetras);
        for (String line : lineas) {
            for (int j=0;j<line.length()-1;j++) {
                int x = letrasEnUso.indexOf(line.charAt(j));
                int y = letrasEnUso.indexOf(line.charAt(j+1));
                // No tomamos en cuenta lados repetidos
                if (!G.containsEdge(x, y))
                    G.addEdge(x, y);
            }       
        }
        // Ejecutar busqueda
        visitarNodos();
    }

    /**
     * Dado un nodo v, se verifica si todos sus sucesores ya fueron visitados.
     *
     * @param v:    Nodo a revisar.
     * @return  true si <tt>todos</tt> los sucesores fueron visitados,
     *          false en caso contrario
     */
    public boolean sucesorFueVisitado(int v) {
        for (int suc : G.adj(v)) {
            if (!visit[suc])
                return false; 
        }
        return true;
    }

    /**
     * Algoritmo para hacer visita de nodos, utilizando una implementación recursiva 
     * de busqueda en profundidad, luego añade los camino encontrados para finalmente 
     * formar la palabra.
     */
    public void visitarNodos() {
        visit = new boolean[letrasEnUso.size()];
        for (boolean v : visit)
            v = false;

        for (int v=0;v<G.V();v++) {
            if (!visit[v]) {
                LinkedList<Integer> caminoActual = new LinkedList<Integer>();
                caminoActual.add(v);
                dfsRecursivo(v, caminoActual);
            }
        }
    }

    /**
     * Implementacion recursiva de busqueda en profundidad, para formar los caminos
     * desde un nodo de inicio dado. Genera un bosque formado por fragmentos de la palabra.
     *
     * @param v:            Nodo desde el cual se hace la busqueda.
     * @param caminoViejo:  Camino formado hasta el momento.
     */
    public void dfsRecursivo(int v, LinkedList<Integer> caminoViejo) {
        visit[v] = true;
        if (sucesorFueVisitado(v))
            caminos.add(caminoViejo);

        for (int sucesor : G.adj(v)) {
            if (!visit[sucesor]) {
                LinkedList<Integer> caminoNuevo = new LinkedList<Integer>();
                caminoNuevo.addAll(caminoViejo);
                caminoNuevo.add(sucesor);
                dfsRecursivo(sucesor, caminoNuevo);
            }
        }
    }
    
    /** 
     * Funcion que imprime la palabra final, que se forma uniendo las diferentes
     * arborescencias del bosque conseguidas por la visita de nodos.
     */
    public void printSol() {
        for (LinkedList<Integer> path : caminos) {
            for (int v : path) {
                StdOut.print(letrasEnUso.get(v));
            }
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Vla proyecto = new Vla(in);
        proyecto.printSol();
    }
}