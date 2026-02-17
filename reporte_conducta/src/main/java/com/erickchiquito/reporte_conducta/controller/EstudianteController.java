package com.erickchiquito.reporte_conducta.controller;

import com.erickchiquito.reporte_conducta.entity.Estudiante;
import com.erickchiquito.reporte_conducta.repository.EstudianteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteRepository repo;

    public EstudianteController(EstudianteRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public List<Estudiante> listar(){
        return repo.findAll();
    }

    @PostMapping
    public Estudiante guardar(@RequestBody Estudiante u){
        return repo.save(u);
    }

    @PutMapping("/{carne}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String carne, @RequestBody Estudiante estudiante){
        if (!repo.existsById(carne)){
            return ResponseEntity.notFound().build();
        }
        estudiante.setNumeroCarne(carne);
        return ResponseEntity.ok(repo.save(estudiante));
    }

    @GetMapping("/{carne}")
    public ResponseEntity<Estudiante> buscarPorCarne(@PathVariable String carne){
        return repo.findById(carne)
                .map(estudiante -> ResponseEntity.ok(estudiante))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{carne}")
    public ResponseEntity<Void> eliminar(@PathVariable String carne){
        if (!repo.existsById(carne)){
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(carne);
        return ResponseEntity.noContent().build();
    }
}

