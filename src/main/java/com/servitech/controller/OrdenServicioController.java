package com.servitech.controller;

import com.servitech.model.OrdenServicio;
import com.servitech.model.EstadoOrden;
import com.servitech.service.IOrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenServicioController {

    @Autowired
    private IOrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenServicio> crear(@RequestBody OrdenServicio orden) {
        return ResponseEntity.ok(ordenService.crearOrden(orden));
    }

    @GetMapping
    public ResponseEntity<List<OrdenServicio>> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicio> verDetalle(@PathVariable Long id) {
        OrdenServicio orden = ordenService.obtenerPorId(id);
        return orden != null ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenServicio> cambiarEstado(@PathVariable Long id, @RequestParam EstadoOrden estado) {
        OrdenServicio orden = ordenService.cambiarEstado(id, estado);
        return orden != null ? ResponseEntity.ok(orden) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/tecnico")
    public ResponseEntity<OrdenServicio> asignarTecnico(@PathVariable Long id, @RequestParam Long tecnicoId) {
        OrdenServicio orden = ordenService.asignarTecnico(id, tecnicoId);
        return orden != null ? ResponseEntity.ok(orden) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenServicio>> historialCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.historialPorCliente(clienteId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<OrdenServicio>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(ordenService.listarPorTecnico(tecnicoId));
    }
}
