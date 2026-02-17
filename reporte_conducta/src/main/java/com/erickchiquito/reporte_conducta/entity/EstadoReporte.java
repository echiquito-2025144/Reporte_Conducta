package com.erickchiquito.reporte_conducta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "estados_reporte")
public class EstadoReporte {

    public enum Estado {
        PENDIENTE,
        EN_REVISION,
        VISUALIZADO,
        RESUELTO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el reporte
    @OneToOne
    @JoinColumn(name = "reporte_id", nullable = false, unique = true)
    private Reporte reporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_creacion_estado")
    private LocalDateTime fechaCreacionEstado;

    @Column(name = "fecha_visualizacion")
    private LocalDateTime fechaVisualizacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // Tiempo estimado de revisión en horas según la prioridad/tipo
    @Column(name = "tiempo_estimado_revision_horas")
    private Integer tiempoEstimadoRevisionHoras;

    public EstadoReporte() {
        this.estado = Estado.PENDIENTE;
        this.fechaCreacionEstado = LocalDateTime.now();
        this.tiempoEstimadoRevisionHoras = 48; // Por defecto 48 horas
    }

    public EstadoReporte(Reporte reporte, Integer tiempoEstimadoRevisionHoras) {
        this.reporte = reporte;
        this.estado = Estado.PENDIENTE;
        this.fechaCreacionEstado = LocalDateTime.now();
        this.tiempoEstimadoRevisionHoras = tiempoEstimadoRevisionHoras != null
                ? tiempoEstimadoRevisionHoras
                : 48;
    }

    // ─── Métodos de utilidad (no persistidos) ───────────────────────────────

    /**
     * Indica si el reporte ya fue visualizado al menos una vez.
     */
    @Transient
    public boolean fueVisualizado() {
        return fechaVisualizacion != null;
    }

    /**
     * Cuánto tiempo hace que se visualizó el reporte.
     * Devuelve null si aún no fue visualizado.
     */
    @Transient
    public String tiempoDesdeVisualizacion() {
        if (fechaVisualizacion == null) return null;

        Duration duracion = Duration.between(fechaVisualizacion, LocalDateTime.now());
        return formatearDuracion(duracion);
    }

    /**
     * Tiempo estimado restante para que el reporte sea revisado.
     * Devuelve null si ya fue visualizado o resuelto.
     */
    @Transient
    public String tiempoEstimadoRestante() {
        if (estado == Estado.VISUALIZADO || estado == Estado.RESUELTO || estado == Estado.EN_REVISION) {
            return "Ya en proceso de revisión";
        }

        LocalDateTime fechaEstimada = fechaCreacionEstado.plusHours(tiempoEstimadoRevisionHoras);
        Duration restante = Duration.between(LocalDateTime.now(), fechaEstimada);

        if (restante.isNegative()) {
            return "En revisión pendiente (tiempo estimado superado)";
        }

        return "Aproximadamente " + formatearDuracion(restante);
    }

    /**
     * Fecha estimada en que el reporte podría ser revisado.
     */
    @Transient
    public LocalDateTime fechaEstimadaRevision() {
        return fechaCreacionEstado.plusHours(tiempoEstimadoRevisionHoras);
    }

    private String formatearDuracion(Duration duracion) {
        long dias    = duracion.toDays();
        long horas   = duracion.toHoursPart();
        long minutos = duracion.toMinutesPart();

        if (dias > 0) {
            return dias + " día(s) y " + horas + " hora(s)";
        } else if (horas > 0) {
            return horas + " hora(s) y " + minutos + " minuto(s)";
        } else {
            return minutos + " minuto(s)";
        }
    }

    // ─── Getters y Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }

    public Reporte getReporte() { return reporte; }

    public Estado getEstado() { return estado; }

    public LocalDateTime getFechaCreacionEstado() { return fechaCreacionEstado; }

    public LocalDateTime getFechaVisualizacion() { return fechaVisualizacion; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }

    public String getObservaciones() { return observaciones; }

    public Integer getTiempoEstimadoRevisionHoras() { return tiempoEstimadoRevisionHoras; }

    public void setId(Long id) { this.id = id; }

    public void setReporte(Reporte reporte) { this.reporte = reporte; }

    public void setEstado(Estado estado) { this.estado = estado; }

    public void setFechaCreacionEstado(LocalDateTime fechaCreacionEstado) {
        this.fechaCreacionEstado = fechaCreacionEstado;
    }

    public void setFechaVisualizacion(LocalDateTime fechaVisualizacion) {
        this.fechaVisualizacion = fechaVisualizacion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public void setTiempoEstimadoRevisionHoras(Integer tiempoEstimadoRevisionHoras) {
        this.tiempoEstimadoRevisionHoras = tiempoEstimadoRevisionHoras;
    }
}
