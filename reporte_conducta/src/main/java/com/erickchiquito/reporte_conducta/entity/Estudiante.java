package com.erickchiquito.reporte_conducta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "estudiante")
public class Estudiante {
    public String getNumeroCarne() {
        return numeroCarne;
    }

    public void setNumeroCarne(String numeroCarne) {
        this.numeroCarne = numeroCarne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoTecnico() {
        return codigoTecnico;
    }

    public void setCodigoTecnico(String codigoTecnico) {
        this.codigoTecnico = codigoTecnico;
    }

    @Id
    @Column(name = "numero_carne")
    private String numeroCarne;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo_tecnico")
    private String codigoTecnico;

    public Estudiante() {
    }

    public Estudiante(String numeroCarne, String nombre, String codigoTecnico) {
        this.numeroCarne = numeroCarne;
        this.nombre = nombre;
        this.codigoTecnico = codigoTecnico;
    }

}

