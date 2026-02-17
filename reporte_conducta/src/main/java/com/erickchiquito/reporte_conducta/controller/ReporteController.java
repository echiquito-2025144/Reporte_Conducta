package com.erickchiquito.reporte_conducta.controller;

import com.erickchiquito.reporte_conducta.entity.Reporte;
import com.erickchiquito.reporte_conducta.repository.ReporteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteRepository repo;

    public ReporteController(ReporteRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public List<Reporte> listar(){
        return repo.findAll();
    }

    @PostMapping
    public Reporte guardar(@RequestBody Reporte r){
        return repo.save(r);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizar(@PathVariable Long id, @RequestBody Reporte reporte){
        if (!repo.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        reporte.setId(id);
        return ResponseEntity.ok(repo.save(reporte));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Long id){
        return repo.findById(id)
                .map(reporte -> ResponseEntity.ok(reporte))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if (!repo.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
