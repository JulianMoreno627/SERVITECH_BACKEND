package com.servitech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servitech.model.Equipo;
import com.servitech.service.IEquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private IEquipoService equipoService;

    @PostMapping
    public ResponseEntity<Equipo> registrar(@RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.registrar(equipo));
    }

    @GetMapping
    public ResponseEntity<List<Equipo>> listar() {
        return ResponseEntity.ok(equipoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> verDetalle(@PathVariable Long id) {
        Equipo equipo = equipoService.obtenerPorId(id);
        return equipo != null ? ResponseEntity.ok(equipo) : ResponseEntity.notFound().build();
    }
}
