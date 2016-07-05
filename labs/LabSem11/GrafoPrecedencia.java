/**
 * Algoritmo para encontrar caminos de costo minimmo y maximo en
 * grafos de precedencia.
 *
 * @author Adolfo Jeritson
 * @author Gianni Manilia
 */
import java.util.*;

public class GrafoPrecedencia {
    private boolean[]           visitado;
    double[]            cmin, cmax;
    private int[]               siguientemin, siguientemax;
    private final double        INFINITO = Double.POSITIVE_INFINITY; 
    private int                 fuente, destino;
    private EdgeWeightedDigraph G;

    /** 
     *  Inicializacion para encontrar caminos de costo minimo.
     *
     * @param G Grafo de precedencia.
     * @param s nodo fuente.
     */
    public GrafoPrecedencia(EdgeWeightedDigraph G, int s) {
        visitado = new boolean[G.V()];
        cmin = new double[G.V()];

        for (int i = 0; i < G.V(); i++)
            visitado[i] = false;

        for (int j = 0; j < G.V(); j++)
            cmin[j] = INFINITO;

        fuente = s; 
        this.G = G; 
    }

    /** 
     * Retorna el costo del camino hasta s.
     *
     * @param s Nodo destino.
     *
     * @return costo del camino.
     */
    public double caminoCostoMinimo(int s) {
        return cmin[s];
    }

    /**
     * Encuentra el camino de costo minimo desde el nodo fuente hasta v.
     *
     * @param v Nodo destino.
     *
     * @return Lista con los nodos del camino minimo.
     */
    public ArrayList<Integer> obtenerCaminoMinimo(int v) {
        siguientemin = new int[G.V()];
        destino = v;

        for (int i = 0; i < G.V(); i++)
            visitado[i] = false;

        for (int j = 0; j < G.V(); j++)
            cmin[j] = INFINITO;

        bProfundidadMin(fuente);

        reverse(cmin);

        if (!existeCaminoMinHasta(v)) return null;

        ArrayList<Integer> caminomin = new ArrayList<Integer>();
        
        int x;
        for (x = fuente; x != v; x = siguientemin[x])
            caminomin.add(x);
        caminomin.add(x);
        
        return caminomin; 
    }

    /**
     * Implementación de DFS para encontrar caminos de costo minimo en grafos de precedencia.
     *
     * @param x nodo a realizar DFS.
     * @param t nodo destino.
     */
    public void bProfundidadMin(int x) {
        visitado[x] = true;
        if (x == destino) {
                cmin[x] = 0;
        } else {

            for (DirectedEdge e : G.adj(x)) {
                int y = e.to();
                if (!visitado[y]) {
                    bProfundidadMin(y);
                }

                if ((cmin[y] != INFINITO) && (cmin[x] > cmin[y] + e.weight())) {
                    cmin[x] = cmin[y] + e.weight();
                    siguientemin[x] = y;
                }
            }
        }
    }

    /** 
     * Retorna el costo del camino maximo hasta s.
     *
     * @param s Nodo destino.
     *
     * @return costo del camino.
     */
    public double caminoCostoMaximo(int s) {
        return cmax[s];

    }

    /**
     * Encuentra el camino de costo maximo desde el nodo fuente hasta v.
     *
     * @param v Nodo destino.
     *
     * @return Lista con los nodos del camino maximo.
     */
    public ArrayList<Integer> obtenerCaminoMaximo(int v) {
        siguientemax = new int[G.V()];

        for (int i = 0; i < G.V(); i++)
            visitado[i] = false;

        for (int j = 0; j < G.V(); j++)
            cmax[j] = INFINITO;

        bProfundidadMax(fuente, v);

        reverse(cmax);

        if (!existeCaminoMaxHasta(v)) return null;
        ArrayList<Integer> caminomax = new ArrayList<Integer>();
        int x;
        for (x = v; cmax[x] != 0; x = siguientemax[x])
            caminomax.add(x);
        caminomax.add(x);
        return caminomax;

    }

    /**
     * Implementación de DFS para encontrar caminos de costo maximo en grafos de precedencia.
     *
     * @param x nodo a realizar DFS.
     * @param t nodo destino.
     */
    public void bProfundidadMax(int x, int t) {
        visitado[x] = true;
        if (x == t)
                cmax[x] = 0;
        else {
            for (DirectedEdge e : G.adj(x)) {
                int sucesor = e.to();
                if (!visitado[sucesor])
                    bProfundidadMax(sucesor, t);
            }

            for (DirectedEdge e : G.adj(x)) {
                int sucesor = e.to();
                double camino = cmax[sucesor] + e.weight();
                if (cmax[sucesor] != INFINITO && cmax[x] < camino) {
                    cmax[x] = camino; 
                    siguientemax[x] = sucesor;
                }
            }
        }
    }

    /**
     * Indica si existe un camino hasta el nodo s.
     * @param s nodo a verificar
     *
     * @return true si existe camino.
     */
    public boolean existeCaminoMinHasta(int s) {
        return (cmin[s] != INFINITO); 
    }

    /**
     * Indica si existe un camino hasta el nodo s.
     * @param s nodo a verificar
     *
     * @return true si existe camino.
     */
    public boolean existeCaminoMaxHasta(int s) {
        return (cmax[s] != INFINITO); 
    }

    /**
     * Revertir el orden de un arreglo.
     *
     * @param array arreglo a invertir
     */
    public static void reverse(double[] array) {
      if (array == null) {
          return;
      }
      int i = 0;
      int j = array.length - 1;
      double tmp;
      while (j > i) {
          tmp = array[j];
          array[j] = array[i];
          array[i] = tmp;
          j--;
          i++;
      }
  }

}