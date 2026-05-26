package com.servitech.service;

import com.servitech.model.Factura;
import java.util.List;

public interface IFacturaService {
    Factura generarFactura(Long ordenId);
    Factura obtenerPorId(Long id);
    Factura obtenerPorOrden(Long ordenId);
    List<Factura> listarPorCliente(Long clienteId);
    Factura pagarFactura(Long id);
}
