package com.servitech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servitech.model.DetalleFactura;
import com.servitech.model.DetalleOrdenRepuesto;
import com.servitech.model.EstadoOrden;
import com.servitech.model.Factura;
import com.servitech.model.OrdenServicio;
import com.servitech.repository.FacturaRepository;
import com.servitech.repository.OrdenServicioRepository;

@Service
public class FacturaService implements IFacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private OrdenServicioRepository ordenRepository;

    @Override
    @Transactional
    public Factura generarFactura(Long ordenId) {
        OrdenServicio orden = ordenRepository.findById(ordenId).orElse(null);
        if (orden == null || orden.getEstado() != EstadoOrden.LISTO) {
            return null; // Solo se factura si está listo
        }

        Factura factura = new Factura();
        factura.setNumeroFactura("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        factura.setOrdenServicio(orden);
        
        double total = 0.0;
        List<DetalleFactura> detalles = new ArrayList<>();

        // Detalle Mano de Obra
        if (orden.getCostoManoObra() != null) {
            DetalleFactura dMO = new DetalleFactura();
            dMO.setConcepto("Mano de Obra");
            dMO.setSubtotal(orden.getCostoManoObra());
            dMO.setFactura(factura);
            detalles.add(dMO);
            total += orden.getCostoManoObra();
        }

        // Detalles Repuestos
        if (orden.getRepuestosUtilizados() != null) {
            for (DetalleOrdenRepuesto dor : orden.getRepuestosUtilizados()) {
                DetalleFactura dR = new DetalleFactura();
                dR.setConcepto(dor.getRepuesto().getNombre() + " (x" + dor.getCantidad() + ")");
                double subtotalR = dor.getPrecioUnitarioSnapshot() * dor.getCantidad();
                dR.setSubtotal(subtotalR);
                dR.setFactura(factura);
                detalles.add(dR);
                total += subtotalR;
            }
        }

        factura.setTotal(total);
        factura.setDetalles(detalles);

        // Cambiar estado a ENTREGADO al facturar
        orden.setEstado(EstadoOrden.ENTREGADO);
        ordenRepository.save(orden);

        return facturaRepository.save(factura);
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepository.findAll().stream()
                .filter(f -> f.getOrdenServicio().getCliente().getId().equals(clienteId))
                .toList();
    }
}
