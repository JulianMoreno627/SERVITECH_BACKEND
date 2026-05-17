package com.servitech.service;

import com.servitech.model.Repuesto;
import com.servitech.model.OrdenServicio;
import com.servitech.model.DetalleOrdenRepuesto;
import com.servitech.repository.RepuestoRepository;
import com.servitech.repository.OrdenServicioRepository;
import com.servitech.repository.DetalleOrdenRepuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RepuestoService implements IRepuestoService {

    @Autowired
    private RepuestoRepository repuestoRepository;

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Autowired
    private DetalleOrdenRepuestoRepository detalleRepository;

    @Override
    public Repuesto crearRepuesto(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }

    @Override
    public List<Repuesto> listarInventario() {
        return repuestoRepository.findAll();
    }

    @Override
    public Repuesto obtenerPorId(Long id) {
        return repuestoRepository.findById(id).orElse(null);
    }

    @Override
    public Repuesto actualizarStock(Long id, Integer nuevoStock) {
        Repuesto repuesto = obtenerPorId(id);
        if (repuesto != null) {
            repuesto.setStock(nuevoStock);
            return repuestoRepository.save(repuesto);
        }
        return null;
    }

    @Override
    @Transactional
    public void agregarRepuestoAOrden(Long ordenId, Long repuestoId, Integer cantidad) {
        OrdenServicio orden = ordenRepository.findById(ordenId).orElse(null);
        Repuesto repuesto = repuestoRepository.findById(repuestoId).orElse(null);

        if (orden != null && repuesto != null && repuesto.getStock() >= cantidad) {
            // Actualizar stock
            repuesto.setStock(repuesto.getStock() - cantidad);
            repuestoRepository.save(repuesto);

            // Crear detalle
            DetalleOrdenRepuesto detalle = new DetalleOrdenRepuesto();
            detalle.setOrdenServicio(orden);
            detalle.setRepuesto(repuesto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitarioSnapshot(repuesto.getPrecio());
            
            detalleRepository.save(detalle);
        }
    }
}
