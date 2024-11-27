package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Criptomoneda;

public class ComparadorStock implements Comparator<Criptomoneda> {
    @Override
    public int compare(Criptomoneda c1, Criptomoneda c2) {
        return Double.compare(c1.getStock(), c2.getStock()); // Ordenar descendente
    }
}
