package com.erickchiquito.reporte_conducta.controller;

import com.erickchiquito.reporte_conducta.entity.Reporte;
import com.erickchiquito.reporte_conducta.entity.Solución;
import com.erickchiquito.reporte_conducta.entity.Solución.TipoApoyo;
import com.erickchiquito.reporte_conducta.repository.ReporteRepository;
import com.erickchiquito.reporte_conducta.repository.SoluciónRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/soluciones")
public class SoluciónController {

    private final SoluciónRepository solucionRepo;
    private final ReporteRepository  reporteRepo;

    public SoluciónController(SoluciónRepository solucionRepo,
                              ReporteRepository reporteRepo) {
        this.solucionRepo = solucionRepo;
        this.reporteRepo  = reporteRepo;
    }



    @GetMapping
    public List<Solución> listar() {
        return solucionRepo.findAll();
    }


    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<Map<String, Object>> consultarPorReporte(@PathVariable Long reporteId) {
        Optional<Solución> opt = solucionRepo.findByReporteId(reporteId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(construirRespuesta(opt.get()));
    }


    @PostMapping("/reporte/{reporteId}")
    public ResponseEntity<?> crear(@PathVariable Long reporteId,
                                   @RequestBody Solución body) {

        Optional<Reporte> reporteOpt = reporteRepo.findById(reporteId);
        if (reporteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (solucionRepo.existsByReporteId(reporteId)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El reporte ya tiene una solución asignada."));
        }

        Solución nueva = new Solución(
                reporteOpt.get(),
                body.getTipoApoyo(),
                body.getConsejo(),
                body.getRecursoInstitucional(),
                body.getContactoEmergencia(),
                body.getMensajeMotivacional()
        );

        return ResponseEntity.ok(construirRespuesta(solucionRepo.save(nueva)));
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Solución body) {

        Optional<Solución> opt = solucionRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Solución existente = opt.get();
        if (body.getTipoApoyo()            != null) existente.setTipoApoyo(body.getTipoApoyo());
        if (body.getConsejo()              != null) existente.setConsejo(body.getConsejo());
        if (body.getRecursoInstitucional() != null) existente.setRecursoInstitucional(body.getRecursoInstitucional());
        if (body.getContactoEmergencia()   != null) existente.setContactoEmergencia(body.getContactoEmergencia());
        if (body.getMensajeMotivacional()  != null) existente.setMensajeMotivacional(body.getMensajeMotivacional());

        return ResponseEntity.ok(construirRespuesta(solucionRepo.save(existente)));
    }


    @GetMapping("/consejos-generales")
    public ResponseEntity<Map<String, Object>> consejosGenerales() {
        Map<String, Object> resp = new LinkedHashMap<>();

        resp.put("titulo", "Consejos de Apoyo ante el Bullying");
        resp.put("consejos", List.of(
                "Habla con un adulto de confianza: maestro, orientador o familiar.",
                "No estás solo/a. Buscar ayuda es un acto de valentía.",
                "Evita responder con violencia; aléjate y reporta lo ocurrido.",
                "Guarda evidencia (capturas, mensajes) si el acoso es digital.",
                "Cuida tu bienestar: habla con el psicólogo escolar."
        ));
        resp.put("recursosNacionales", List.of(
                "Línea de la Niñez y Adolescencia: 1545 (gratuita, 24/7)",
                "PRONICE Guatemala: (502) 2220-2848",
                "PGN – Procuraduría General de la Nación: (502) 2424-7777"
        ));
        resp.put("mensajeMotivacional",
                "Mereces sentirte seguro/a en tu escuela. ¡Tu voz importa!");

        return ResponseEntity.ok(resp);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!solucionRepo.existsById(id)) return ResponseEntity.notFound().build();
        solucionRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    private Map<String, Object> construirRespuesta(Solución s) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("id",                    s.getId());
        mapa.put("reporteId",             s.getReporte().getId());
        mapa.put("numeroCarneEstudiante", s.getReporte().getEstudiante().getNumeroCarne());
        mapa.put("tipoApoyo",             s.getTipoApoyo());
        mapa.put("consejo",               s.getConsejo());
        mapa.put("consejoRapido",         s.consejoRapido());
        mapa.put("recursoInstitucional",  s.getRecursoInstitucional());
        mapa.put("contactoEmergencia",    s.getContactoEmergencia());
        mapa.put("mensajeMotivacional",   s.getMensajeMotivacional());
        return mapa;
    }
}