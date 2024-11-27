package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Activo_Cripto;

public class ComparadorSiglaActivosCripto implements Comparator<Activo_Cripto> {
    @Override
    public int compare(Activo_Cripto m1, Activo_Cripto m2) {
        return m1.getSigla().compareTo(m2.getSigla());
    }
}