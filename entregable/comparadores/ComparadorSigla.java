package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Moneda;

public class ComparadorSigla implements Comparator<Moneda> {
    @Override
    public int compare(Moneda m1, Moneda m2) {
        return m1.getSigla().compareTo(m2.getSigla());
    }
}