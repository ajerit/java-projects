// Archivo: ListaSimple.java
// Implementacion de una lista simplemente enlazada
// Ejercicio de Lab. Algoritmos III


public class ListaSimple {
    
    private Nodo cabeza;
    
    public ListaSimple(){
        cabeza = null;
    }
    
    //Agrega un elemento a la lista (por la cabeza o cola)
    public void agregar(int elemento){        
       //System.out.println(" lo siento no se como agregar :( .");
        Nodo newNode = new Nodo(elemento);
        if (this.cabeza == null) {
            cabeza = newNode;
        } else {
            newNode.next = this.cabeza;
            this.cabeza = newNode;
        }
    }
    
    //Muestra los elementos desde la cabeza
    public void mostrar(){
       //System.out.println(" no se que y como mostrar, ayudame :( .");
        Nodo curNode = this.cabeza;
        while (curNode != null) {
            System.out.println(curNode.data);
            curNode = curNode.next;
        }
    }
}