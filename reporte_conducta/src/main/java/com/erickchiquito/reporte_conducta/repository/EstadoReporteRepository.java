package com.erickchiquito.reporte_conducta.repository;

import com.erickchiquito.reporte_conducta.entity.EstadoReporte;
import com.erickchiquito.reporte_conducta.entity.EstadoReporte.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadoReporteRepository extends JpaRepository<EstadoReporte, Long> {

    Optional<EstadoReporte> findByReporteId(Long reporteId);

    List<EstadoReporte> findByEstado(Estado estado);

    boolean existsByReporteId(Long reporteId);
}