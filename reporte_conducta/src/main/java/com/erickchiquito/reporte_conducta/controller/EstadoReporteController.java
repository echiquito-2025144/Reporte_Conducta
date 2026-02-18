package com.erickchiquito.reporte_conducta.controller;

import com.erickchiquito.reporte_conducta.entity.EstadoReporte;
import com.erickchiquito.reporte_conducta.entity.EstadoReporte.Estado;
import com.erickchiquito.reporte_conducta.entity.Reporte;
import com.erickchiquito.reporte_conducta.repository.EstadoReporteRepository;
import com.erickchiquito.reporte_conducta.repository.ReporteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/estados-reporte")
public class EstadoReporteController {

    private final EstadoReporteRepository estadoRepo;
    private final ReporteRepository reporteRepo;

    public EstadoReporteController(EstadoReporteRepository estadoRepo,
                                   ReporteRepository reporteRepo) {
        this.estadoRepo  = estadoRepo;
        this.reporteRepo = reporteRepo;
    }

    // ─── GET /estados-reporte ────────────────────────────────────────────────
    // Lista todos los estados
    @GetMapping
    public List<EstadoReporte> listar() {
        return estadoRepo.findAll();
    }

    // ─── POST /estados-reporte/reporte/{reporteId} ───────────────────────────
    // Crea un estado inicial para un reporte existente
    @PostMapping("/reporte/{reporteId}")
    public ResponseEntity<?> crear(@PathVariable Long reporteId,
                                   @RequestParam(required = false) Integer tiempoEstimadoHoras) {

        Optional<Reporte> reporteOpt = reporteRepo.findById(reporteId);
        if (reporteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (estadoRepo.existsByReporteId(reporteId)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El reporte ya tiene un estado asignado."));
        }

        EstadoReporte estado = new EstadoReporte(reporteOpt.get(), tiempoEstimadoHoras);
        return ResponseEntity.ok(estadoRepo.save(estado));
    }

    // ─── GET /estados-reporte/reporte/{reporteId} ────────────────────────────
    // Obtiene el estado de un reporte específico con información de tiempos
    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<Map<String, Object>> consultarPorReporte(@PathVariable Long reporteId) {

        Optional<EstadoReporte> estadoOpt = estadoRepo.findByReporteId(reporteId);
        if (estadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EstadoReporte estado = estadoOpt.get();
        Map<String, Object> respuesta = construirRespuestaDetallada(estado);

        return ResponseEntity.ok(respuesta);
    }

    // ─── GET /estados-reporte/{id}/detalle ───────────────────────────────────
    // Obtiene detalle completo de un estado por su propio ID
    @GetMapping("/{id}/detalle")
    public ResponseEntity<Map<String, Object>> detalle(@PathVariable Long id) {

        Optional<EstadoReporte> estadoOpt = estadoRepo.findById(id);
        if (estadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(construirRespuestaDetallada(estadoOpt.get()));
    }

    // ─── PATCH /estados-reporte/reporte/{reporteId}/visualizar ──────────────
    // Marca el reporte como visualizado (lo pasa a EN_REVISION)
    @PatchMapping("/reporte/{reporteId}/visualizar")
    public ResponseEntity<Map<String, Object>> marcarVisualizado(@PathVariable Long reporteId,
                                                                 @RequestBody(required = false)
                                                                 Map<String, String> body) {

        Optional<EstadoReporte> estadoOpt = estadoRepo.findByReporteId(reporteId);
        if (estadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EstadoReporte estado = estadoOpt.get();

        if (estado.getEstado() == Estado.RESUELTO) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El reporte ya fue resuelto."));
        }

        estado.setEstado(Estado.EN_REVISION);
        if (estado.getFechaVisualizacion() == null) {
            estado.setFechaVisualizacion(LocalDateTime.now());
        }

        if (body != null && body.containsKey("observaciones")) {
            estado.setObservaciones(body.get("observaciones"));
        }

        return ResponseEntity.ok(construirRespuestaDetallada(estadoRepo.save(estado)));
    }

    // ─── PATCH /estados-reporte/reporte/{reporteId}/resolver ────────────────
    // Marca el reporte como resuelto
    @PatchMapping("/reporte/{reporteId}/resolver")
    public ResponseEntity<Map<String, Object>> marcarResuelto(@PathVariable Long reporteId,
                                                              @RequestBody(required = false)
                                                              Map<String, String> body) {

        Optional<EstadoReporte> estadoOpt = estadoRepo.findByReporteId(reporteId);
        if (estadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EstadoReporte estado = estadoOpt.get();

        if (estado.getEstado() == Estado.RESUELTO) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El reporte ya estaba resuelto."));
        }

        estado.setEstado(Estado.RESUELTO);
        estado.setFechaResolucion(LocalDateTime.now());

        if (estado.getFechaVisualizacion() == null) {
            estado.setFechaVisualizacion(LocalDateTime.now());
        }

        if (body != null && body.containsKey("observaciones")) {
            estado.setObservaciones(body.get("observaciones"));
        }

        return ResponseEntity.ok(construirRespuestaDetallada(estadoRepo.save(estado)));
    }

    // ─── GET /estados-reporte/filtrar?estado=PENDIENTE ──────────────────────
    // Filtra por estado (PENDIENTE, EN_REVISION, VISUALIZADO, RESUELTO)
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarPorEstado(@RequestParam Estado estado) {
        List<EstadoReporte> resultados = estadoRepo.findByEstado(estado);
        return ResponseEntity.ok(resultados);
    }

    // ─── DELETE /estados-reporte/{id} ────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!estadoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        estadoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private Map<String, Object> construirRespuestaDetallada(EstadoReporte estado) {
        Map<String, Object> mapa = new HashMap<>();

        mapa.put("id",                         estado.getId());
        mapa.put("numeroReporte",              estado.getReporte().getId());
        mapa.put("numeroCarneEstudiante",      estado.getReporte().getEstudiante().getNumeroCarne());
        mapa.put("estado",                     estado.getEstado());
        mapa.put("fueVisualizado",             estado.fueVisualizado());
        mapa.put("fechaCreacionEstado",        estado.getFechaCreacionEstado());
        mapa.put("fechaVisualizacion",         estado.getFechaVisualizacion());
        mapa.put("fechaResolucion",            estado.getFechaResolucion());
        mapa.put("tiempoDesdeVisualizacion",   estado.tiempoDesdeVisualizacion());
        mapa.put("tiempoEstimadoRestante",     estado.tiempoEstimadoRestante());
        mapa.put("fechaEstimadaRevision",      estado.fechaEstimadaRevision());
        mapa.put("observaciones",              estado.getObservaciones());

        return mapa;
    }
}