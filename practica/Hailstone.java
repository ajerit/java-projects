// Command Line: java Hailstone <entero>
// Implementacion de la funcion de Syracuse

public class Hailstone {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Error en los argumentos.");
			System.exit(1);
		}
		int n = Integer.parseInt(args[0]);
		System.out.println("Secuencia:");

		do {
			if (n%2 == 0) {n/=2;}
			else {n=3*n+1;}
			System.out.print(n+" ");
		} while (n>1);
		System.out.println();
	}
}