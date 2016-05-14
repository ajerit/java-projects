import java.util.*;
class HolaMundo {
	public static void main (String[] args) {
		System.out.println("Hello World!");
        LinkedList<Boolean> lista = new LinkedList<Boolean>();
        lista.add(true);
        lista.add(true);
        lista.add(true);

        boolean allTrue = Iterables.all(lista, new Predicate<Boolean>() {
            public boolean apply(boolean input) {
                return input==true;
            }
        });

        System.out.println(allTrue);

	}
}