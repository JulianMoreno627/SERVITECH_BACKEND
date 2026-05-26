package com.servitech.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servitech.service.IDashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticas());
    }

    @GetMapping("/ordenes-activas")
    public ResponseEntity<?> getOrdenesActivas(@RequestParam(required = false) Long clienteId,
                                              @RequestParam(required = false) Long tecnicoId) {
        if (clienteId != null) {
            return ResponseEntity.ok(dashboardService.obtenerOrdenesActivasPorCliente(clienteId));
        }
        if (tecnicoId != null) {
            return ResponseEntity.ok(dashboardService.obtenerOrdenesActivasPorTecnico(tecnicoId));
        }
        return ResponseEntity.ok(dashboardService.obtenerOrdenesActivas());
    }

    @GetMapping("/tecnico/{id}/carga")
    public ResponseEntity<Map<String, Object>> getCargaTecnico(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.obtenerCargaTecnico(id));
    }
}
