package enums;

/** Indica los niveles permitidos en la jerarquía de la biblioteca. */
public enum TipoUnidad {
    BIBLIOTECA, AREA, ESTANTE;

    /** Solo BIBLIOTECA -> AREA y AREA -> ESTANTE. */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) return false;
        return (this == BIBLIOTECA && hijo == AREA)
                || (this == AREA && hijo == ESTANTE);
    }
}
