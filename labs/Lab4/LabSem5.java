/**
 * Laboratorio de Algoritmos III
 * 
 * Programa que resuelve el problema del camino del caballo, utilizando
 * busqueda en amplitud para hallar el camino mas corto y usando grafico
 * implicito para correr el algoritmo de busqueda
 * 
 * Compilacion javac LabSem5.java
 * Ejecucion: java LabSem5 <tablero.txt>
 * 
 * @author Adolfo Jeritson
 * @author Gianni Manilia
 */
import java.util.*;

public class LabSem5 {
	private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[][] marked;  // marked[v] = is there an s->v path?
    private Nodo[][] ladoA;      // edgeTo[v] = last edge on shortest s->v path
    private int[][] distA;      // distTo[v] = length of shortest s->v path
    private int N;   
    private int tamTablero;
    private LinkedList<Nodo> bloqueados;
    private LinkedList<Nodo> nodos;
    
	/**
    * Clase axiliar que representa los nodos del grafico implicito
    */
	private static class Nodo {
		private int x;
		private int y;
		}
	
	/**
	 * Inicializacion del programa para resolver el camino del caballo
	 * lee el archivo con los datos del tablero y genera la busqueda.
	 */	
	public LabSem5(In in) {
		bloqueados = new LinkedList<Nodo>();
		nodos = new LinkedList<Nodo>();
		
		tamTablero = in.readInt();
		Nodo n = new Nodo();
		String linea = in.readLine();
		for (int ii=0;ii<tamTablero;ii++) {
			linea = in.readLine();
			for (int jj=0;jj<linea.length();jj++) {
				if (linea.charAt(jj) == '@') {
					n = new Nodo();
					n.x = ii+1;
					n.y = jj+1;
					nodos.add(n);
				} else if (linea.charAt(jj) == '#') {
					n = new Nodo();
					n.x = ii+1;
					n.y = jj+1;
					bloqueados.add(n);
					
					}
				}
			}

		marked = new boolean[tamTablero][tamTablero];
        distA = new int[tamTablero][tamTablero];
        ladoA = new Nodo[tamTablero][tamTablero];
        for (int i=0;i<tamTablero ; i++)
			for (int j=0;j<tamTablero; j++)
				distA[i][j] = INFINITY;
				
		bfs();
		}
		
    /**
    * Implementacion del algoritmo BSF (busqueda en amplitud) para
    * resolver el problema del camino del caballo
    * 
    */
	public void bfs() {
		Nodo n = nodos.pop();
        Queue<Nodo> cola = new Queue<Nodo>();
        marked[n.x-1][n.y-1] = true;
        distA[n.x-1][n.y-1] = 0;
        cola.enqueue(n);
        while (!cola.isEmpty()) {
            Nodo v = cola.dequeue();
            // Generar sucesores
            LinkedList<Nodo> suc = generarSuc(v);
            for (Nodo w : suc) {
				if (!bloqueados.contains(w)) {
					if (!marked[w.x-1][w.y-1]) {
						ladoA[w.x-1][w.y-1] = v;
						distA[w.x-1][w.y-1] = distA[w.x-1][w.y-1] + 1;
						marked[w.x-1][w.y-1] = true;
						cola.enqueue(w);
					}
				}
            }
        }
    }
    
    
    /**
    * Indica si hay un camino desde el nodo origen al nodo v.
    * @param   vertice v
    * @return  <tt>true</tt> si existe un camino, 
    *          <tt>false</tt> en caso contrario.
    */
    public boolean hasPathTo(Nodo n) {
        return marked[n.x-1][n.y-1];
    }

    /**
     * Retorna el numero de lados del camino mas corto desde nodo origen
     * hasta el vertice <tt>v</tt>
     * @param   vertice v
     * @return  numero de lados del camino mas corto
     */
    public int distTo(Nodo n) {
        return distA[n.x-1][n.y-1];
    }

    /**
    * Retorna un arreglo que indica para cada vertice, cual fue 
    * el vertice anteriormente visitado en la b busqueda
    * @return  arreglo de vertices visitados
    */
    public Nodo[][] arcsVisited() {
        return ladoA;
    }
    
    /**
    * Retorna los nodos del camino que encontro el algoritmo busqueda, 
    * desde el vertice inicial del algoritmo hasta el vertice final.
    * @param  
    * @return  Lista enlazada con el camino inicial-final
    */
    public LinkedList<Nodo> getDirectedPathTo() {
		Nodo n = nodos.pop();
        if (!hasPathTo(n)) return null;
        LinkedList<Nodo> directedPath = new LinkedList<Nodo>();
        Nodo x;
        for (x = n; distA[x.x-1][x.y-1] != 0; x = ladoA[x.x-1][x.y-1])
            directedPath.push(x);
        directedPath.push(x);
        return directedPath;
    }

    /**
    * Indica si un nodo esta bloqueado
    * @return  true si esta bloqueado, false en caso contrario
    */
    public boolean estaBloq(Nodo n) {
		for (Nodo b : bloqueados) {
			if (b.x == n.x && b.y == n.y) 
				return true;
			}
		return false;
	}
   
   
    /**
    * Genera los sucesores con movimientos posibles del nodo dado
    * @param n Nodo
    * @return  Lista de los sucesores no bloqueados
    */    
    public LinkedList<Nodo> generarSuc(Nodo n) {
		LinkedList<Nodo> suc = new LinkedList<Nodo>();
		for (int i=1;i<3;i++) {
			for (int j=1;j<3;j++) {
				if (i != j) {
					Nodo nuevo = new Nodo();
					nuevo.x = n.x - i;
					nuevo.y = n.y - j;
					if (nuevo.x > 0 && nuevo.y > 0 && !estaBloq(nuevo))
						suc.add(nuevo);

					nuevo = new Nodo();
					nuevo.x = n.x + i;
					nuevo.y = n.y + j;
					if (nuevo.x <= tamTablero && nuevo.y <= tamTablero && !estaBloq(nuevo))
						suc.add(nuevo);

					nuevo = new Nodo();
					nuevo.x = n.x - i;
					nuevo.y = n.y + j;
					if (nuevo.x > 0 && nuevo.y <= tamTablero && !estaBloq(nuevo))
						suc.add(nuevo);
							
					nuevo = new Nodo();
					nuevo.x = n.x + i;
					nuevo.y = n.y - j;
					if (nuevo.x <= tamTablero && nuevo.y > 0 && !estaBloq(nuevo))
						suc.add(nuevo);
				} 
			}
		}
		
		return suc;
	}
    
	
	public static void main(String[] args) {
		In in = new In(args[0]);
		LabSem5 lab = new LabSem5(in);
		
		LinkedList<Nodo> path = lab.getDirectedPathTo();
		
		if (path != null) {
			for (Nodo nodo : path) {
				StdOut.print("(");
				StdOut.print(nodo.x);
				StdOut.print(", ");
				StdOut.print(nodo.y);
				StdOut.println(")");
				
				}
			} else {
				StdOut.println("Imposible");
			}
		
		
		
	}
}
