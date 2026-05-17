package com.servitech.service;

import com.servitech.model.Equipo;
import com.servitech.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipoService implements IEquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public Equipo registrar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }

    @Override
    public Equipo obtenerPorId(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Equipo> listarPorCliente(Long clienteId) {
        return equipoRepository.findAll().stream()
                .filter(e -> e.getCliente() != null && e.getCliente().getId().equals(clienteId))
                .toList();
    }
}
