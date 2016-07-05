/**
 * Laboratorio de Algoritmos III
 * 
 * Programa que resuelve el problema de las 8 reinas
 * 
 * Compilacion javac OchoReinas.java
 * Ejecucion: java OchoReinas
 * 
 * @author Adolfo Jeritson
 * @author Gianni Manilia
 */
import java.util.*;

public class OchoReinas {
    int[] solucion; 
    int contador;
	
	/**
	 * Inicializacion del programa
	 */	
	public OchoReinas() {
		solucion = new int[8];
		contador = 0;
	}
	
	/**
	 * Hallar todas las soluciones del problema de las 8 reinas
	 * 
	 * @param: k Contador de la cantidad de recursiones
	 * @param: col ArrayList de columnas bloqueadas
	 * @param: diag45 Arraylist de diagonales de 45 grados bloqueadas
	 * @param: diag135 Arraylist de diagonales de 135 grados bloqueadas
	 * 
	 **/
	public void reinas(int k, ArrayList<Integer> col, 
		ArrayList<Integer> diag45, ArrayList<Integer> diag135) {

		contador+=1;
		
		if(k==8) {
			for(int e : solucion) 
				StdOut.print(e);
			StdOut.print(" Contador: "+contador);
			StdOut.println("");
		}
		else {
			for (int i=1; i<=8; i++) {
				if (!col.contains(i) && !diag45.contains(i-k) && !diag135.contains(i+k)) {
					solucion[k]=i;
					col = hallarCol(k+1);
					diag45 = hallarDiag45(k+1);
					diag135 = hallarDiag135(k+1);
					reinas(k+1,col,diag45,diag135);
				}
			}
		}
	}
	
	/**
	 * Hallar las columnas bloqueadas
	 * 
	 * @param: k Contador de la cantidad de recursividades
	 **/ 
	public ArrayList<Integer> hallarCol(int k) {
		ArrayList<Integer> col = new ArrayList<Integer>();
		for (int i=1; i<=k; i++) {
			col.add(solucion[i-1]);
		}
		return col;
	}
	
	/**
	 * Hallar las diagonales de 45 grados bloqueadas
	 * 
	 * @param: k Contador de la cantidad de recursividades
	 **/ 
	public ArrayList<Integer> hallarDiag45(int k) {
		ArrayList<Integer> diag45 = new ArrayList<Integer>();
		for (int i=1; i<=k; i++) {
			diag45.add(solucion[i-1]-i+1);
		}
		return diag45;
	}
	
	/**
	 * Hallar las diagonales de 135 grados bloqueadas
	 * 
	 * @param: k Contador de la cantidad de recursividades
	 **/ 
	public ArrayList<Integer> hallarDiag135(int k) {
		ArrayList<Integer> diag135 = new ArrayList<Integer>();
		for (int i=1; i<=k; i++) {
			diag135.add(solucion[i-1]+i-1);
		}
		return diag135;
	}	
    
	public static void main(String[] args) {
		OchoReinas ochoReinas = new OchoReinas();
		ArrayList<Integer> col = new ArrayList<Integer>();
		ArrayList<Integer> diag45 = new ArrayList<Integer>();
		ArrayList<Integer> diag135 = new ArrayList<Integer>();
		ochoReinas.reinas(0,col,diag45,diag135);
		StdOut.println("Cantidad total de vertices visitados: "+ochoReinas.contador);
		
	}
}
