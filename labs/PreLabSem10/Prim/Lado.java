/**
 * Clase auxliar para representar las aristas de un grafo.
 *
 * @author Adolfo Jeritson. 
 * @author Gianni Manilia.  
 */
public class Lado {
    private final int   v, w, costo;

    /**
     * Construye una arista del grafo.
     * Dado un nodo v, nodo w y un costo asociado, genera un objeto
     * que representa una arista del grafo.
     *
     * @param v         Entero (nodo).
     * @param w         Entero (nodo).
     * @param costo     Entero, costo de la arista.
     */
    public Lado(int v, int w, int costo) {
        this.v = v;
        this.w = w;
        this.costo = costo;         
    }

    /** 
     * Devuelve el costo de la arista.
     *
     * @return costo de la arista.
     */
    public int costo() {
        return costo;
    }

    /** 
     * Devuelve el extremo inicial de la arista.
     *
     * @return extremo inicial
     */
    public int from() {
        return v;
    }

    /** 
     * Devuelve el extremo final de la arista.
     *
     * @return extremo final
     */
    public int to() {
        return w;
    }      
}