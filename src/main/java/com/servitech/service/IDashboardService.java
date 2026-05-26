package com.servitech.service;

import com.servitech.model.OrdenServicio;
import java.util.List;
import java.util.Map;

public interface IDashboardService {
    Map<String, Object> obtenerEstadisticas();
    Map<String, Object> obtenerCargaTecnico(Long tecnicoId);
    List<OrdenServicio> obtenerOrdenesActivas();
    List<OrdenServicio> obtenerOrdenesActivasPorCliente(Long clienteId);
    List<OrdenServicio> obtenerOrdenesActivasPorTecnico(Long tecnicoId);
}
