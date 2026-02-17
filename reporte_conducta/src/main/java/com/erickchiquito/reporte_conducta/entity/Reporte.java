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

    @Column(name = "numero_carne_estudiante")
    private String numeroCarneEstudiante;

    public Reporte() {
        this.fecha = LocalDateTime.now();
    }

    public Reporte(String descripcion, String tipo, String numeroCarneEstudiante) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.numeroCarneEstudiante = numeroCarneEstudiante;
        this.fecha = LocalDateTime.now();
    }

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

    public String getNumeroCarneEstudiante() {
        return numeroCarneEstudiante;
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

    public void setNumeroCarneEstudiante(String numeroCarneEstudiante) {
        this.numeroCarneEstudiante = numeroCarneEstudiante;
    }
}
