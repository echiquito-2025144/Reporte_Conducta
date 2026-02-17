package com.erickchiquito.reporte_conducta.repository;

import com.erickchiquito.reporte_conducta.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante,String> {
}
