package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Activo_Fiat;

public class ComparadorSiglaActivosFiat implements Comparator<Activo_Fiat> {
    @Override
    public int compare(Activo_Fiat m1, Activo_Fiat m2) {
        return m1.getSigla().compareTo(m2.getSigla());
    }
}