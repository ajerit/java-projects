/**
 * Implementación del TAD Disjoint-Set Forest, basado en el
 * pseudocódigo del libro Introduction to Algorithms (Cormen).
 * 
 * @author Adolfo Jeritson.
 * @author Gianni Manilia.
 */
public class DisjointSetForest {
    private int[] parent, rank;

    /**
     * Construye un bosque vacio inicial.
     * Dada una cantidad de nodos fijas, el constructor inicializa 
     * el bosque para su uso.
     *
     * @param nNodos    Cantidad de nodos.
     */
    public DisjointSetForest(int nNodos) {
        parent = new int[nNodos];
        rank = new int[nNodos];
    }

    /**
     * Crea un conjunto dentro del bosque.
     * Dado un nodo, se crea un conjunto que solo lo contiene a él.
     *
     * @param x     Nodo.
     */
    public void makeSet(int x) {
        parent[x] = x;
        rank[x] = 0;
    }

    /**
     * Une dos conjuntos.
     * Dados dos nodos, la función se encarga de unir sus dos conjuntos
     * para formar uno nuevo.
     *
     * @param x     Nodo.
     * @param y     Nodo.
     */
    public void union(int x, int y) {
        link(findSet(x), findSet(y));
    }

    /**
     * Une dos conjuntos.
     * Dados dos nodos, la función se encarga de unir sus dos conjuntos
     * para formar uno nuevo.
     *
     * @param x     Nodo.
     * @param y     Nodo.
     */
    public void link(int x, int y) {
        if (rank[x] > rank[y]) {
            parent[y] = x;
        } else {
            parent[x] = y;
            if (rank[x] == rank[y]) {
                rank[y] = rank[y] + 1;
            }
        }
    }

    /**
     * Encuentra al representante de un conjunto.
     * Dado un nodo, se busca cual es el nodo padre que representa al
     * conjunto.
     *
     * @param x     Nodo.
     * 
     * @return Nodo representante del conjunto.
     */
    public int findSet(int x) {
        if (x != parent[x])
            parent[x] = findSet(parent[x]);
        return parent[x];
    }

    /**
     * Determina si dos nodos estan en el mismo conjunto.
     *
     * @param x     Nodo.
     * @param y     Nodo.
     *
     * @return true si los nodos pertenecen al mismo conjunto, 
     *          false en caso contrario.
     */
    public boolean conjuntosDiferentes(int x, int y) {
        return findSet(x) != findSet(y);
    }


    public static void main(String[] args) {
    }
}