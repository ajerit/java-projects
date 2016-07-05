/**
 * Algoritmo Heuristico para obtener solucion del problema RPP.
 *
 * Ejecucion: java SolverRPP [-g] [-s] <instancia.txt>
 * Solo admite 2 argumentos, se debe elegir entre:
 *      -g => Usar algoritmo tipo greedy para apareamiento perfecto.
 *      -s => Usar algoritmo vertex-scan para apareamiento perfecto.
 * El archivo de instancia debe terminar en .txt para que el programa
 * lo reconozca y lo pueda leer.
 *
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class SolverRPP {
	public EdgeWeightedGraph G, Gr;
	
	public SolverRPP(In in, String apcm) {
		in.readLine();                      // Nombre
		in.readLine();                      // Componentes
		in.readString();                    // Vertices 
		in.readString();                    // :

		int V = in.readInt();
		in.readString();                    // Aristas red
		in.readString();                    // :
		int Lr = in.readInt();
		in.readString();                    // Aristas no req
		in.readString();                    // :
		int Lnr = in.readInt();

		in.readString();                    // Lista arista
		in.readString();                    // :

		String[] lines = in.readAllLines();
		G = new EdgeWeightedGraph(V);
		Gr = new EdgeWeightedGraph(V);

		// iteramos sobre las aristas requeridas
		for (int i = 1; i <= Lr; i++) {
			lines[i] = lines[i].replace('(', ' ');
			lines[i] = lines[i].replace(')', ' ');
			lines[i] = lines[i].replace(',', ' ');
			lines[i] = lines[i].trim();
			String[] tokens = lines[i].split("\\s+");

			int v = Integer.parseInt(tokens[0]);
			int w = Integer.parseInt(tokens[1]);
			double c = Double.parseDouble(tokens[3]);
			Edge e = new Edge(v-1, w-1, c);

			G.addEdge(e);
			Gr.addEdge(e);
		}

		// iteramos sobre las aristas no requeridas
		for (int i = Lr+2; i <= Lr+Lnr+1; i++)  {
			lines[i] = lines[i].replace('(', ' ');
			lines[i] = lines[i].replace(')', ' ');
			lines[i] = lines[i].replace(',', ' ');
			lines[i] = lines[i].trim();
			String[] tokens = lines[i].split("\\s+");

			int v = Integer.parseInt(tokens[0]);
			int w = Integer.parseInt(tokens[1]);
			double c = Double.parseDouble(tokens[3]);
			Edge e = new Edge(v-1, w-1, c);

			G.addEdge(e);
		}
		SolveRPP(apcm);
	}

	/**
	 * Implementacion del algoritmo heuristico para resolver RPP.
	 */
	public void SolveRPP(String apcm) {
		long startTime = System.currentTimeMillis();
		// Crear copia de Gr, G'
		EdgeWeightedGraph grCopia = new EdgeWeightedGraph(Gr);

		if (!esGrafoConexo(grCopia)) {
			// Calcular componentes conexas de G'
			HashSet<HashSet<Integer>> compCon = grCopia.getComponentesConexas();

			// Construir un grafo completo Gt donde cada CompCo corresponde
			// a un vertice.
			EdgeWeightedGraph gt = new EdgeWeightedGraph(compCon.size());

			// Se deben buscar los caminos de costo minimo (CCM)
			completarGrafoCompCo(gt, compCon);;

			// Obtener arbol cobertor del Gt, da el conjunto eMST de lados.
			Kruskal kruskal = new Kruskal(gt);
			LinkedHashSet<Edge> eMST = kruskal.getEdgesMST();

			// Et0 es el conjunto de lados que son CCM y pertenecen a Emst
			// Como LinkedHashSet no agrega repetidos, la lista no tiene duplicados
			// Tenemos conjunto et directamente.
            LinkedHashSet<Edge> et0 = new LinkedHashSet<Edge>();
            for (Edge e : eMST) {
                // Evitar NullPointerException, aunque todos deberian tener
                //if (e.ccmAsoc() != null) {
                    for (Edge lado : e.ccmAsoc())
                        et0.add(lado);
                //}
            }

			// Agregar a G' los lados de Et, permite duplicados
			for (Edge e : et0)
				grCopia.addEdge(e);
		} 

		if (!esGrafoPar(grCopia)) {
			// Conjunto de vértices de grado impar de G'
			HashSet<Integer> V0 = new HashSet<Integer>();
			for (int v = 0; v < grCopia.V(); v++) {
				if ((grCopia.degree(v) % 2) != 0) 
						V0.add(v);
			}

			// Grafo completo G0
			EdgeWeightedGraph G0 = new EdgeWeightedGraph(V0.size());

			completarGrafoImpar(G0, V0);

			ArrayList<Edge> M = new ArrayList<Edge>();
			// Si se quiere usar Greedy
			if (apcm.equals("-g")) {
				Greedy greedy = new Greedy(G0);
				M = greedy.apareamiento();
			}
			// Si se quiere usar Vertex-Scan
			else if (apcm.equals("-s")) {
				VertexScan vertexScan = new VertexScan(G0);
				M = vertexScan.apareamiento();
			}

			// Para cada lado (vi,vj) en M:
			for (Edge camino : M) {
				for (Edge lado : camino.ccmAsoc()) {
					grCopia.addEdge(lado);
				}
			}
		}
		// conexo && par:
		// Obtener ciclo euleriano
		ArrayList<Integer> solucion = new ArrayList<Integer>(grCopia.getCicloEuleriano());
		long endTime = System.currentTimeMillis();
		// Solucion
		StdOut.println(solucion);
		// Costo
		double costo = 0.0;
		for (int i = 0; i < solucion.size()-1; i++) {
			int nodo1 = solucion.get(i);
			int nodo2 = solucion.get(i+1);
			costo = costo + grCopia.getEdgeWeight(nodo1, nodo2);
		}
		StdOut.println(costo);
		// Tiempo
		double total = (endTime - startTime) / 1000.0;
		StdOut.println(total + " segs.");

	}

	public void completarGrafoImpar(EdgeWeightedGraph g0, HashSet<Integer> v0) {
		ArrayList<Integer> impares = new ArrayList<Integer>(v0);
		Floyd floyd = new Floyd(G);
		ArrayList<Edge> caminoCM;

		for (int nodo1 = 0; nodo1 < g0.V(); nodo1++) {
			for (int nodo2 = 0; nodo2 < g0.V(); nodo2++) {
				if (nodo1 != nodo2) {
					if (!g0.containsEdge(nodo1, nodo2)) {
						double costo = floyd.dist(impares.get(nodo1), impares.get(nodo2));
						Edge lado = new Edge(nodo1, nodo2, costo);
						caminoCM = floyd.path(impares.get(nodo1), impares.get(nodo2));
						lado.asociarLado(caminoCM);
						g0.addEdge(lado);
					}
				}
			}
		}
	}

	/**
	 * Completa un grafo cuyos nodos son componentes conexas.
	 *
	 * @param gt    Grafo a completar.
	 * @param comps Lista con las componentes conexas.
	 */
	public void completarGrafoCompCo(EdgeWeightedGraph gt, HashSet<HashSet<Integer>> comps) {
        ArrayList<HashSet<Integer>> componentes = new ArrayList(comps);
        Floyd floyd = new Floyd(G);
        Edge ladoMin;
        ArrayList<Edge> caminoCM;

        for (int comp1 = 0; comp1 < gt.V(); comp1++) {
            for (int comp2 = 0; comp2 < gt.V(); comp2++) {
                if (comp1 != comp2) {
                    if (!gt.containsEdge(comp1, comp2)) {
                    	StdOut.println("Conectando componente #"+comp1+" con componente #"+comp2);
                        int imin = 0;
                        int jmin = 0;
                        double costoMin = Double.POSITIVE_INFINITY;
                        for (Integer i : componentes.get(comp1)) {
                            for (Integer j : componentes.get(comp2)) {
                                double costoActual = floyd.dist(i, j);
                                if (costoActual < costoMin) {
                                    costoMin = costoActual;
                                    imin = i;
                                    jmin = j;
                                }
                            }
                        }
                        ladoMin = new Edge(comp1, comp2, costoMin);
                        caminoCM = floyd.path(imin, jmin);
                        ladoMin.asociarLado(caminoCM);
                        gt.addEdge(ladoMin);
                    }
                }
            }
        }
	}

	/**
	 * Verifica si el grafo dado es conexo.
	 * Se calculan las componentes conexas, si tiene 1 sola componente,
	 * el grafo es conexo.
	 *
	 * @param G grafo.
	 * 
	 * @return true si es conexo, false en caso contrario.
	 */
	public boolean esGrafoConexo(EdgeWeightedGraph G) {
		if (G.getComponentesConexas().size() == 1)
			return true;
		else
			return false;
	}

	/**
	 * Verifica si el grafo dado es par.
	 * Itera sobre todos los nodos del grafo y revisa su grado.
	 *
	 * @param G grafo.
	 * 
	 * @return true si es par, false en caso contrario.
	 */
	public boolean esGrafoPar(EdgeWeightedGraph G) {
		for (int v = 0; v < G.V(); v++) {
			if (G.degree(v) % 2 != 0)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		In in = new In(args[1]);
		SolverRPP proyecto = new SolverRPP(in, args[0]);
	}
}