package com.servitech.service;

import java.util.Map;
import java.util.List;

public interface IDashboardService {
    Map<String, Object> obtenerEstadisticas();
    List<?> obtenerOrdenesActivas();
    Map<String, Object> obtenerCargaTecnico(Long tecnicoId);
}
