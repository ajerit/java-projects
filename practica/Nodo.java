// Archivo: Nodo.java
// Implementacion de un nodo para una lista simplemente enlazada
// Ejercicio de Lab. Algoritmos III

public class Nodo{

    public Nodo next;
    public int data;

    public Nodo(int data) {
        this.data = data;
        this.next = null;
    }

    public void setLink(Nodo nodo) {
        this.next = nodo;
    }

    public int getData() {
        return this.data;
    }

    public Nodo getNext() {
        return this.next;
    }

}