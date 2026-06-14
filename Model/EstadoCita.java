package Model;

public enum EstadoCita {

    EN_ESPERA("En espera"),
    EN_PROCESO("En proceso"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada");

    private final String etiqueta;

    EstadoCita(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
