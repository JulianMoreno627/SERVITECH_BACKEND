package com.servitech.service;

import com.servitech.model.OrdenServicio;
import com.servitech.model.EstadoOrden;
import java.util.List;

public interface IOrdenService {
    OrdenServicio crearOrden(OrdenServicio orden);
    List<OrdenServicio> listarTodas();
    OrdenServicio obtenerPorId(Long id);
    OrdenServicio cambiarEstado(Long id, EstadoOrden nuevoEstado);
    OrdenServicio asignarTecnico(Long id, Long tecnicoId);
    List<OrdenServicio> historialPorCliente(Long clienteId);
    List<OrdenServicio> listarPorTecnico(Long tecnicoId);
}
