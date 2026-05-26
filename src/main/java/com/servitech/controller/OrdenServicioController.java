package com.servitech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servitech.model.EstadoOrden;
import com.servitech.model.OrdenServicio;
import com.servitech.service.IOrdenService;

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
        return ordenService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenServicio> actualizar(@PathVariable Long id, @RequestBody OrdenServicio orden) {
        return ResponseEntity.ok(ordenService.actualizar(id, orden));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, 
                                                     @RequestParam EstadoOrden estado,
                                                     @RequestBody(required = false) java.util.Map<String, Object> body) {
        String observacion = "Cambio de estado desde el panel";
        Double costoManoObra = null;

        if (body != null) {
            if (body.get("observacion") instanceof String) {
                observacion = (String) body.get("observacion");
            }
            
            Object costoObj = body.get("costoManoObra");
            if (costoObj instanceof Number) {
                costoManoObra = ((Number) costoObj).doubleValue();
            } else if (costoObj instanceof String) {
                costoManoObra = Double.valueOf((String) costoObj);
            }
        }

        OrdenServicio orden;
        switch (estado) {
            case LISTO:
                if (costoManoObra == null) {
                    orden = null;
                } else {
                    orden = ordenService.actualizarEstadoConCosto(id, estado, observacion, costoManoObra);
                }
                break;
            default:
                orden = ordenService.actualizarEstado(id, estado, observacion);
                break;
        }

        if (orden == null && estado == EstadoOrden.LISTO) {
            return ResponseEntity.badRequest().body("El costo de mano de obra es obligatorio para finalizar la orden.");
        }

        return ResponseEntity.ok(orden);
    }

    @PutMapping("/{id}/tecnico")
    public ResponseEntity<OrdenServicio> asignarTecnico(@PathVariable Long id, @RequestParam Long tecnicoId) {
        OrdenServicio orden = ordenService.asignarTecnico(id, tecnicoId);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenServicio>> historialCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.listarPorCliente(clienteId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<OrdenServicio>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(ordenService.listarDisponiblesYAsignadas(tecnicoId));
    }
}
