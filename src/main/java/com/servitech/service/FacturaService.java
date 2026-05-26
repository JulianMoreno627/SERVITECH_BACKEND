package com.servitech.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        if (orden == null || (orden.getEstado() != EstadoOrden.LISTO && orden.getEstado() != EstadoOrden.ENTREGADO)) {
            System.err.println("Intento de generar factura para orden no válida: " + ordenId);
            return null;
        }

        // Si ya tiene factura, no generar otra
        Factura facturaExistente = obtenerPorOrden(ordenId);
        if (facturaExistente != null) {
            return facturaExistente;
        }

        try {
            Factura factura = new Factura();
            factura.setNumeroFactura("FAC-" + System.currentTimeMillis());
            factura.setOrdenServicio(orden);
            
            double total = 0.0;
            List<DetalleFactura> detalles = new java.util.ArrayList<>();

            // Detalle Mano de Obra
            double manoObra = (orden.getCostoManoObra() != null) ? (double) orden.getCostoManoObra() : 0.0;
            DetalleFactura dMO = new DetalleFactura();
            dMO.setConcepto("Mano de Obra");
            dMO.setSubtotal(manoObra);
            dMO.setFactura(factura);
            detalles.add(dMO);
            total += manoObra;

            // Detalles Repuestos
            if (orden.getRepuestosUtilizados() != null) {
                for (DetalleOrdenRepuesto dor : orden.getRepuestosUtilizados()) {
                    if (dor.getRepuesto() != null) {
                        DetalleFactura dR = new DetalleFactura();
                        int cantidad = (dor.getCantidad() != null) ? (int) dor.getCantidad() : 1;
                        dR.setConcepto(dor.getRepuesto().getNombre() + " (x" + cantidad + ")");
                        double precio = (dor.getPrecioUnitarioSnapshot() != null) ? (double) dor.getPrecioUnitarioSnapshot() : 
                                       ((dor.getRepuesto().getPrecio() != null) ? (double) dor.getRepuesto().getPrecio() : 0.0);
                        double subtotalR = precio * cantidad;
                        dR.setSubtotal(subtotalR);
                        dR.setFactura(factura);
                        detalles.add(dR);
                        total += subtotalR;
                    }
                }
            }

            factura.setTotal(total);
            factura.setDetalles(detalles);

            return facturaRepository.save(factura);
        } catch (Exception e) {
            System.err.println("Error al generar factura para orden " + ordenId + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public Factura obtenerPorOrden(Long ordenId) {
        return facturaRepository.findByOrdenServicioId(ordenId).orElse(null);
    }

    @Override
    public List<Factura> listarPorCliente(Long clienteId) {
        if (clienteId == null) return facturaRepository.findAll();
        return facturaRepository.findAll().stream()
                .filter(f -> f.getOrdenServicio().getCliente() != null && f.getOrdenServicio().getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Factura pagarFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        
        factura.setPagada(true);
        
        // Al pagar la factura, podemos marcar la orden como ENTREGADO si estaba en LISTO
        OrdenServicio orden = factura.getOrdenServicio();
        if (orden.getEstado() == EstadoOrden.LISTO) {
            orden.setEstado(EstadoOrden.ENTREGADO);
            orden.setFechaEntrega(LocalDateTime.now());
            ordenRepository.save(orden);
        }
        
        return facturaRepository.save(factura);
    }
}
