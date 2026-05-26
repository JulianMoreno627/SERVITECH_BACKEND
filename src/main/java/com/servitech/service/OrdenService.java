package com.servitech.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servitech.model.Cliente;
import com.servitech.model.Persona;
import com.servitech.model.DetalleOrdenRepuesto;
import com.servitech.model.EstadoOrden;
import com.servitech.model.HistorialRevision;
import com.servitech.model.OrdenServicio;
import com.servitech.model.Tecnico;
import com.servitech.repository.PersonaRepository;
import com.servitech.repository.HistorialRevisionRepository;
import com.servitech.repository.OrdenServicioRepository;
import com.servitech.repository.TecnicoRepository;

@Service
@Transactional(readOnly = true)
public class OrdenService implements IOrdenService {

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private HistorialRevisionRepository historialRepository;

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<OrdenServicio> listarTodas() {
        return ordenRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getFechaIngreso().compareTo(o1.getFechaIngreso()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<OrdenServicio> obtenerPorId(Long id) {
        return ordenRepository.findById(id);
    }

    @Override
    @Transactional
    public OrdenServicio crearOrden(OrdenServicio orden) {
        System.out.println("Iniciando creación de orden para cliente ID: " + 
            (orden.getCliente() != null ? orden.getCliente().getId() : "null"));
        
        if (orden.getCliente() == null || orden.getCliente().getId() == null) {
            throw new RuntimeException("La solicitud debe tener un cliente con un ID válido.");
        }

        // Buscar el cliente en la base de datos
        Persona persona = personaRepository.findById(orden.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario con ID: " + orden.getCliente().getId()));
        
        if (!(persona instanceof Cliente)) {
            throw new RuntimeException("El usuario con ID " + orden.getCliente().getId() + " no es un cliente válido.");
        }
        
        Cliente cliente = (Cliente) persona;
        orden.setCliente(cliente);

        // Configurar el equipo
        if (orden.getEquipo() != null) {
            orden.getEquipo().setCliente(cliente);
        } else {
            throw new RuntimeException("La solicitud debe incluir la información del equipo.");
        }

        // Valores por defecto
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setFechaIngreso(LocalDateTime.now());
        if (orden.getCostoManoObra() == null) {
            orden.setCostoManoObra(0.0);
        }

        try {
            OrdenServicio guardada = ordenRepository.save(orden);
            System.out.println("Orden creada exitosamente con ID: " + guardada.getId());
            return guardada;
        } catch (Exception e) {
            System.err.println("Error al guardar la orden: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la orden en la base de datos: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrdenServicio actualizar(Long id, OrdenServicio ordenActualizada) {
        OrdenServicio ordenExistente = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        if (ordenActualizada.getEquipo() != null) {
            ordenExistente.setEquipo(ordenActualizada.getEquipo());
        }
        if (ordenActualizada.getCliente() != null) {
            ordenExistente.setCliente(ordenActualizada.getCliente());
        }
        if (ordenActualizada.getCostoManoObra() != null) {
            ordenExistente.setCostoManoObra(ordenActualizada.getCostoManoObra());
        }
        if (ordenActualizada.getTecnico() != null) {
            ordenExistente.setTecnico(ordenActualizada.getTecnico());
        }
        if (ordenActualizada.getEstado() != null) {
            ordenExistente.setEstado(ordenActualizada.getEstado());
        }

        return ordenRepository.save(ordenExistente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new RuntimeException("Orden no encontrada");
        }
        ordenRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrdenServicio actualizarEstado(Long id, EstadoOrden nuevoEstado, String observacion) {
        return actualizarEstadoConCosto(id, nuevoEstado, observacion, null);
    }

    @Override
    @Transactional
    public OrdenServicio actualizarEstadoConCosto(Long id, EstadoOrden nuevoEstado, String observacion, Double costoManoObra) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        HistorialRevision historial = new HistorialRevision();
        historial.setOrdenServicio(orden);
        historial.setEstadoAnterior(orden.getEstado());
        historial.setEstadoNuevo(nuevoEstado);
        historial.setObservacion(observacion);
        historialRepository.save(historial);

        orden.setEstado(nuevoEstado);
        if (costoManoObra != null) {
            orden.setCostoManoObra(costoManoObra);
        }

        if (nuevoEstado == EstadoOrden.LISTO || nuevoEstado == EstadoOrden.ENTREGADO) {
            if (nuevoEstado == EstadoOrden.ENTREGADO) {
                orden.setFechaEntrega(LocalDateTime.now());
            }
            ordenRepository.saveAndFlush(orden);
            facturaService.generarFactura(id);
            return orden;
        }
        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenServicio cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        return actualizarEstado(id, nuevoEstado, "Cambio de estado automático");
    }

    @Override
    @Transactional
    public OrdenServicio asignarTecnico(Long id, Long tecnicoId) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        orden.asignarTecnico(tecnico);
        return ordenRepository.save(orden);
    }

    @Override
    public List<OrdenServicio> listarPorCliente(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    @Override
    public List<OrdenServicio> listarPorTecnico(Long tecnicoId) {
        return ordenRepository.findByTecnicoId(tecnicoId);
    }

    @Override
    public List<OrdenServicio> listarDisponiblesYAsignadas(Long tecnicoId) {
        return ordenRepository.findAll().stream()
                .filter(o -> (o.getTecnico() != null && o.getTecnico().getId().equals(tecnicoId)) 
                          || (o.getEstado() == EstadoOrden.PENDIENTE && o.getTecnico() == null))
                .sorted((o1, o2) -> o2.getFechaIngreso().compareTo(o1.getFechaIngreso()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Double calcularCostoTotalRecursivo(Long id) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        List<DetalleOrdenRepuesto> repuestos = orden.getRepuestosUtilizados();
        double manoObra = (orden.getCostoManoObra() != null) ? (double) orden.getCostoManoObra() : 0.0;
        return manoObra + sumarRepuestosRecursivo(repuestos, 0);
    }

    private Double sumarRepuestosRecursivo(List<DetalleOrdenRepuesto> repuestos, int index) {
        if (repuestos == null || index >= repuestos.size()) {
            return 0.0;
        }
        DetalleOrdenRepuesto detalle = repuestos.get(index);
        double precio = (detalle.getRepuesto() != null && detalle.getRepuesto().getPrecio() != null) ? (double) detalle.getRepuesto().getPrecio() : 0.0;
        int cantidad = (detalle.getCantidad() != null) ? (int) detalle.getCantidad() : 0;
        double costoActual = precio * cantidad;
        return costoActual + sumarRepuestosRecursivo(repuestos, index + 1);
    }
}
