package com.servitech.service;

import com.servitech.model.EstadoOrden;
import com.servitech.model.OrdenServicio;
import com.servitech.repository.OrdenServicioRepository;
import com.servitech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService implements IDashboardService {

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrdenes", ordenRepository.count());
        stats.put("ordenesPendientes", ordenRepository.findAll().stream().filter(o -> o.getEstado() == EstadoOrden.PENDIENTE).count());
        stats.put("tecnicosDisponibles", tecnicoRepository.findAll().stream().filter(t -> t.isDisponible()).count());
        
        double ingresos = ordenRepository.findAll().stream()
                .filter(o -> o.getFactura() != null)
                .mapToDouble(o -> o.getFactura().getTotal())
                .sum();
        stats.put("ingresosTotales", ingresos);
        
        return stats;
    }

    @Override
    public Map<String, Object> obtenerCargaTecnico(Long tecnicoId) {
        Map<String, Object> carga = new HashMap<>();
        long activas = ordenRepository.findByTecnicoId(tecnicoId).stream()
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .count();
        carga.put("ordenesActivas", activas);
        return carga;
    }

    @Override
    public List<OrdenServicio> obtenerOrdenesActivas() {
        return ordenRepository.findAll().stream()
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenServicio> obtenerOrdenesActivasPorCliente(Long clienteId) {
        return ordenRepository.findAll().stream()
                .filter(o -> o.getCliente() != null && o.getCliente().getId().equals(clienteId))
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenServicio> obtenerOrdenesActivasPorTecnico(Long tecnicoId) {
        return ordenRepository.findAll().stream()
                .filter(o -> (o.getTecnico() != null && o.getTecnico().getId().equals(tecnicoId)) 
                          || (o.getTecnico() == null && o.getEstado() == EstadoOrden.PENDIENTE))
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .collect(Collectors.toList());
    }
}
