package com.erickchiquito.reporte_conducta.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "soluciones")
public class Solución {

    public enum TipoApoyo {
        CONSEJO_EMOCIONAL,
        RECURSOS_INSTITUCIONALES,
        CONTACTO_EMERGENCIA,
        TECNICA_DEFENSA_VERBAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "reporte_id", nullable = false, unique = true)
    private Reporte reporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_apoyo", nullable = false)
    private TipoApoyo tipoApoyo;

    @Column(name = "consejo", length = 1000, nullable = false)
    private String consejo;

    @Column(name = "recurso_institucional", length = 500)
    private String recursoInstitucional;

    @Column(name = "contacto_emergencia", length = 200)
    private String contactoEmergencia;

    @Column(name = "mensaje_motivacional", length = 500)
    private String mensajeMotivacional;


    public Solución() {}


    public Solución(Reporte reporte, TipoApoyo tipoApoyo,
                    String consejo, String recursoInstitucional,
                    String contactoEmergencia, String mensajeMotivacional) {
        this.reporte              = reporte;
        this.tipoApoyo            = tipoApoyo;
        this.consejo              = consejo;
        this.recursoInstitucional = recursoInstitucional;
        this.contactoEmergencia   = contactoEmergencia;
        this.mensajeMotivacional  = mensajeMotivacional;
    }


    @Transient
    public String consejoRapido() {
        return switch (tipoApoyo) {
            case CONSEJO_EMOCIONAL ->
                    "Habla con alguien de confianza. No estás solo/a.";
            case RECURSOS_INSTITUCIONALES ->
                    "Solicita una cita con orientación o psicología del plantel.";
            case CONTACTO_EMERGENCIA ->
                    "Si sientes que corres peligro, llama a emergencias o a un adulto de confianza.";
            case TECNICA_DEFENSA_VERBAL ->
                    "Mantén la calma, habla con voz firme y busca alejarte del agresor.";
        };
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Reporte getReporte() { return reporte; }
    public void setReporte(Reporte reporte) { this.reporte = reporte; }

    public TipoApoyo getTipoApoyo() { return tipoApoyo; }
    public void setTipoApoyo(TipoApoyo tipoApoyo) { this.tipoApoyo = tipoApoyo; }

    public String getConsejo() { return consejo; }
    public void setConsejo(String consejo) { this.consejo = consejo; }

    public String getRecursoInstitucional() { return recursoInstitucional; }
    public void setRecursoInstitucional(String r) { this.recursoInstitucional = r; }

    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String c) { this.contactoEmergencia = c; }

    public String getMensajeMotivacional() { return mensajeMotivacional; }
    public void setMensajeMotivacional(String m) { this.mensajeMotivacional = m; }
}