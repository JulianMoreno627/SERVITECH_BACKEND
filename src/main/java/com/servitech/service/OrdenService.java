package com.servitech.service;

import com.servitech.model.OrdenServicio;
import com.servitech.model.EstadoOrden;
import com.servitech.model.Tecnico;
import com.servitech.repository.OrdenServicioRepository;
import com.servitech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdenService implements IOrdenService {

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    public OrdenServicio crearOrden(OrdenServicio orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public List<OrdenServicio> listarTodas() {
        return ordenRepository.findAll();
    }

    @Override
    public OrdenServicio obtenerPorId(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    @Override
    public OrdenServicio cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        OrdenServicio orden = obtenerPorId(id);
        if (orden != null) {
            orden.setEstado(nuevoEstado);
            return ordenRepository.save(orden);
        }
        return null;
    }

    @Override
    public OrdenServicio asignarTecnico(Long id, Long tecnicoId) {
        OrdenServicio orden = obtenerPorId(id);
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElse(null);
        if (orden != null && tecnico != null) {
            orden.setTecnico(tecnico);
            return ordenRepository.save(orden);
        }
        return null;
    }

    @Override
    public List<OrdenServicio> historialPorCliente(Long clienteId) {
        // En un entorno real usaríamos un query method en el repo
        return ordenRepository.findAll().stream()
                .filter(o -> o.getCliente().getId().equals(clienteId))
                .toList();
    }

    @Override
    public List<OrdenServicio> listarPorTecnico(Long tecnicoId) {
        return ordenRepository.findAll().stream()
                .filter(o -> o.getTecnico() != null && o.getTecnico().getId().equals(tecnicoId))
                .toList();
    }
}
