package com.servitech.service;

import com.servitech.model.Repuesto;
import java.util.List;

public interface IRepuestoService {
    Repuesto crearRepuesto(Repuesto repuesto);
    List<Repuesto> listarInventario();
    Repuesto obtenerPorId(Long id);
    Repuesto actualizarStock(Long id, Integer nuevoStock);
    void agregarRepuestoAOrden(Long ordenId, Long repuestoId, Integer cantidad);
}
