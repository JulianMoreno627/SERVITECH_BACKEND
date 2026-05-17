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
}
