package com.erickchiquito.reporte_conducta.repository;

import com.erickchiquito.reporte_conducta.entity.Solución;
import com.erickchiquito.reporte_conducta.entity.Solución.TipoApoyo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoluciónRepository extends JpaRepository<Solución, Long> {

    Optional<Solución> findByReporteId(Long reporteId);

    List<Solución> findByTipoApoyo(TipoApoyo tipoApoyo);

    boolean existsByReporteId(Long reporteId);
}