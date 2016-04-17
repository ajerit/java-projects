// Archivo AreaRectangulo.java
// Calcula el area de un rectangulo
// Ejemplo para el lab. de algoritmos III
import java.util.Scanner; //Util clase para lectura desde "flujos"

public class AreaRectangulo{
    public static void main(String [] args){
        int ancho,alto;
        Scanner entradaIO = new Scanner (System.in); 
        
        System.out.print("Introduzca el ancho del rectangulo: ");
        ancho = entradaIO.nextInt(); //nextInt lee un entero
        
        System.out.print("Introduzca el alto del rectangulo: ");
        alto = entradaIO.nextInt(); 
        
        System.out.println("El area del rectangulo es: " + (ancho * alto));
   }
}
