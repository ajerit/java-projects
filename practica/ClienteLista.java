// Archivo: ClienteLista.java
// Cliente de una lista simplemente enlazada
// Ejercicio de Lab. Algoritmos III


public class ClienteLista{
    
    //Metodo principal
    public static void main(String[] args) {
        ListaSimple lista = new ListaSimple();
        lista.agregar(1);
        lista.agregar(4);
        lista.agregar(2);
        lista.mostrar();
    }
}
