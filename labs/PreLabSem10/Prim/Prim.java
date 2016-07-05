import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;

public class Prim {
    private ArrayList<Lado>     ladosArbol;
    private PriorityQueue<Lado> pq;

    private class PathComparator implements Comparator<Lado> {
        @Override
        public int compare(Lado a, Lado b) {
            return (a.costo() - b.costo());
        }
    }

    public Prim(Graph G) {
        Comparator<Lado> comparator = new PathComparator();
        pq = new PriorityQueue<Lado>(G.E(), comparator);

        for (Lado e : G.lados()) 
            pq.add(e);

        ladosArbol = obtenerArbolCobertor(G);
    }

    public ArrayList<Lado> obtenerArbolCobertor(Graph G) {
        ArrayList<Integer> v = new ArrayList<Integer>();
        ArrayList<Lado>    t = new ArrayList<Lado>();
        ArrayList<Integer> u = new ArrayList<Integer>();
        
        for (int i = 0; i < G.V(); i++)
            v.add(i);

        u.add(0);
        while(!u.containsAll(v)) {
            // Escoger lado costo minimo con inicial en u, final en v-u
            Lado e = escogerLadoMin(u, v, pq);
            //pq.remove(e);
            t.add(e);
            u.add(e.to());
        }

        return t;
    }

    public Lado escogerLadoMin(ArrayList<Integer> u, ArrayList<Integer> v, PriorityQueue<Lado> colaLados) {
        // Copia local de la cola para no modificar
        PriorityQueue<Lado> pq = new PriorityQueue<Lado>(colaLados);
        ArrayList<Integer>  terminales = new ArrayList<Integer>(v);
        
        for (Integer n : v) {
            if (u.contains(n))
                terminales.remove(n);
        }

        while (!pq.isEmpty()) {
            Lado e = pq.poll();
            if (u.contains(e.from()) && terminales.contains(e.to()))
                return e;
        }
        return null;

    }

    public void printArbol() {
        StdOut.println("Aristas del Arbol Cobertor:");
        for (Lado e : lados) {
            StdOut.println(String.format("%d - %d (%d)", e.from(), e.to(), e.costo()));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        Prim test = new Prim(G);
        test.printArbol();

    }
}