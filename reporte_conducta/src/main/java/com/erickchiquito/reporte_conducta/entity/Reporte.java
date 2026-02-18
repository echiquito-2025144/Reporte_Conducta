package com.erickchiquito.reporte_conducta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    // Relación Many-to-One con Estudiante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_carne_estudiante", referencedColumnName = "numero_carne")
    private Estudiante estudiante;

    public Reporte() {
        this.fecha = LocalDateTime.now();
    }

    public Reporte(String descripcion, String tipo, Estudiante estudiante) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estudiante = estudiante;
        this.fecha = LocalDateTime.now();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
