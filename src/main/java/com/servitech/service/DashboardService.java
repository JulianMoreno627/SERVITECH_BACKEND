package com.servitech.service;

import com.servitech.model.EstadoOrden;
import com.servitech.repository.OrdenServicioRepository;
import com.servitech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrdenes", ordenRepository.count());
        stats.put("ordenesPendientes", ordenRepository.findAll().stream()
                .filter(o -> o.getEstado() == EstadoOrden.PENDIENTE).count());
        stats.put("tecnicosDisponibles", tecnicoRepository.findAll().stream()
                .filter(t -> t.isDisponible()).count());
        return stats;
    }

    @Override
    public List<?> obtenerOrdenesActivas() {
        return ordenRepository.findAll().stream()
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .toList();
    }

    @Override
    public Map<String, Object> obtenerCargaTecnico(Long tecnicoId) {
        long carga = ordenRepository.findAll().stream()
                .filter(o -> o.getTecnico() != null && o.getTecnico().getId().equals(tecnicoId) 
                        && o.getEstado() != EstadoOrden.ENTREGADO)
                .count();
        Map<String, Object> res = new HashMap<>();
        res.put("tecnicoId", tecnicoId);
        res.put("ordenesActivas", carga);
        return res;
    }
}
