package com.servitech.service;

import com.servitech.model.Factura;
import java.util.List;

public interface IFacturaService {
    Factura generarFactura(Long ordenId);
    Factura obtenerPorId(Long id);
    List<Factura> listarPorCliente(Long clienteId);
}
