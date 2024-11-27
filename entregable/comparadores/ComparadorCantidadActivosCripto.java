package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Activo_Cripto;

public class ComparadorCantidadActivosCripto implements Comparator<Activo_Cripto> {
    @Override
    public int compare(Activo_Cripto c1, Activo_Cripto c2) {
        return Double.compare(c1.getCantidad(), c2.getCantidad()); // Ordenar descendente
    }
}
