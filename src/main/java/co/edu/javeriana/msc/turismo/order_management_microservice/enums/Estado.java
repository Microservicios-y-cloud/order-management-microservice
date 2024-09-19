package co.edu.javeriana.msc.turismo.order_management_microservice.enums;

public enum Estado{
    POR_ACEPTAR(1),
    ACEPTADA(2),
    PAGADA(3),
    RECHAZADA(4),
    FINALIZADA(5);

    private final int codigo;

    Estado(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}