package com.erickchiquito.reporte_conducta.repository;

import com.erickchiquito.reporte_conducta.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
}
