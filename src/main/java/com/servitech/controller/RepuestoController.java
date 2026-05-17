package com.servitech.controller;

import com.servitech.model.Repuesto;
import com.servitech.service.IRepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    @Autowired
    private IRepuestoService repuestoService;

    @PostMapping
    public ResponseEntity<Repuesto> crear(@RequestBody Repuesto repuesto) {
        return ResponseEntity.ok(repuestoService.crearRepuesto(repuesto));
    }

    @GetMapping
    public ResponseEntity<List<Repuesto>> listar() {
        return ResponseEntity.ok(repuestoService.listarInventario());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repuesto> actualizarStock(@PathVariable Long id, @RequestParam Integer stock) {
        Repuesto r = repuestoService.actualizarStock(id, stock);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @PostMapping("/orden/{ordenId}")
    public ResponseEntity<Void> agregarAOrden(
            @PathVariable Long ordenId, 
            @RequestParam Long repuestoId, 
            @RequestParam Integer cantidad) {
        repuestoService.agregarRepuestoAOrden(ordenId, repuestoId, cantidad);
        return ResponseEntity.ok().build();
    }
}
