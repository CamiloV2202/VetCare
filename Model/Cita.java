package Model;

public class Cita {

    private int id;
    private Mascota mascota;
    private String fecha;
    private String hora;
    private String motivo;
    private String observaciones;
    private String prioridad;
    private EstadoCita estado;
    private String horaAtencion;
    private String resultadoAtencion;
    private String motivoCancelacion;
    private boolean modificada;

    public Cita(int id, Mascota mascota, String fecha, String hora, String motivo) {
        this.id = id;
        this.mascota = mascota;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.observaciones = "";
        this.prioridad = "Normal";
        this.estado = EstadoCita.EN_ESPERA;
        this.horaAtencion = "";
        this.resultadoAtencion = "";
        this.motivoCancelacion = "";
        this.modificada = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getHoraAtencion() { return horaAtencion; }
    public void setHoraAtencion(String horaAtencion) { this.horaAtencion = horaAtencion; }

    public String getResultadoAtencion() { return resultadoAtencion; }
    public void setResultadoAtencion(String resultadoAtencion) { this.resultadoAtencion = resultadoAtencion; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }

    public boolean isModificada() { return modificada; }
    public void setModificada(boolean modificada) { this.modificada = modificada; }
}
