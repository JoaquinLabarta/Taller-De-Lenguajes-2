package entregable.comparadores;

import java.util.Comparator;

import entregable.Clases.Activo_Fiat;

public class ComparadorCantidadActivosFiat implements Comparator<Activo_Fiat> {
    @Override
    public int compare(Activo_Fiat c1, Activo_Fiat c2) {
        return Double.compare(c1.getCantidad(), c2.getCantidad()); // Ordenar descendente
    }
}