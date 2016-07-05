import java.util.ArrayList;

public class VertexScan {

	private EdgeWeightedGraph G;
	private ArrayList<Integer> vertices;
	private ArrayList<Edge> lados;

	public VertexScan(EdgeWeightedGraph G) {
		// Inicializar grafo
		this.G = G;
		// Inicializar lista de vértices
		vertices = new ArrayList<>();
		for (int i=0; i<G.V(); i++) {
			if (G.nodoPertenece(i))
				vertices.add(i);
		}
		// Inicializar lista de lados
		lados = G.edges();
	}

	public ArrayList<Edge> apareamiento() {
		ArrayList<Edge> M = new ArrayList<>();
		ArrayList<Integer> Vp = vertices;
		ArrayList<Edge> Ep = lados;
		ArrayList<Edge> eliminar;
		int vertice, posicion;
		Edge ladoMenor;
		int i,j;

		while (!Vp.isEmpty()) {
			eliminar = new ArrayList<>();
			// Escoger un vértice de Vp
			vertice = Vp.get(0);
			ladoMenor =  new Edge(vertice,vertice,Double.POSITIVE_INFINITY);
			// Escoger el lado (i,j) de menor costo
			for (Edge e : Ep) {
				if (e.either()==vertice || e.other((e.either()))==vertice) {
					if (e.weight()<ladoMenor.weight())
						ladoMenor = e;
					eliminar.add(e);
				}	
			}
			// Agregar (i,j) a M
			M.add(ladoMenor);
			// Eliminar los vértices i y j de Vp
			i = ladoMenor.either();
			j = ladoMenor.other(i);
			// Eliminar i de Vp si existe
			if (Vp.contains(i)) {
				posicion = Vp.indexOf(i);
				Vp.remove(posicion);
			}
			// Eliminar j de Vp si existe
			if (Vp.contains(j)) {
				posicion = Vp.indexOf(j);
				Vp.remove(posicion);
			}			
			// Buscar todos los lados que tengan como adyacente a j
			for (Edge e : Ep) {
				if (e.either()==j) {
					if (e.other(j)!=i)
						eliminar.add(e);
				}
				else if (e.other((e.either()))==j) {
					if (e.either()!=i)
						eliminar.add(e);
				}					
			}
			// Eliminar de Ep todos los lados que tengan como adyacentes a i o j
			for (int k=0; k<eliminar.size(); k++)
				Ep.remove(eliminar.get(k));
		}
		return M;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		in.readLine();                      // Nombre
		in.readLine();                      // Componentes
		in.readString();                    // Vertices 
		in.readString();                    // :

		int Vp = in.readInt();
		in.readString();                    // Aristas red
		in.readString();                    // :
		int Lr = in.readInt();
		in.readString();                    // Aristas no req
		in.readString();                    // :
		int Lnr = in.readInt();

		in.readString();                    // Lista arista
		in.readString();                    // :

		String[] lines = in.readAllLines();
		EdgeWeightedGraph G = new EdgeWeightedGraph(Vp);

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

		VertexScan vertexScan = new VertexScan(G);
		vertexScan.apareamiento();
	}
}