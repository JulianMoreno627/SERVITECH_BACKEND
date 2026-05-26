package com.servitech.controller;

import com.servitech.model.Factura;
import com.servitech.service.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @PostMapping("/orden/{ordenId}")
    public ResponseEntity<Factura> generar(@PathVariable Long ordenId) {
        Factura f = facturaService.generarFactura(ordenId);
        return f != null ? ResponseEntity.ok(f) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> ver(@PathVariable Long id) {
        Factura f = facturaService.obtenerPorId(id);
        return f != null ? ResponseEntity.ok(f) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Factura>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.listarPorCliente(clienteId));
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<?> verPorOrden(@PathVariable Long ordenId) {
        try {
            Factura f = facturaService.obtenerPorOrden(ordenId);
            if (f == null) {
                f = facturaService.generarFactura(ordenId);
            }
            
            if (f != null) {
                return ResponseEntity.ok(f);
            } else {
                return ResponseEntity.status(404).body("La factura no existe y no se pudo generar.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al procesar la factura: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarFactura(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facturaService.pagarFactura(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar el pago: " + e.getMessage());
        }
    }
}
