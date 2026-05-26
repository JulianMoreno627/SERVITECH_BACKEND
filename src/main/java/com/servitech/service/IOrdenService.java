package com.servitech.service;

import com.servitech.model.EstadoOrden;
import com.servitech.model.OrdenServicio;
import java.util.List;
import java.util.Optional;

public interface IOrdenService {
    List<OrdenServicio> listarTodas();
    Optional<OrdenServicio> obtenerPorId(Long id);
    OrdenServicio crearOrden(OrdenServicio orden);
    OrdenServicio actualizar(Long id, OrdenServicio ordenActualizada);
    void eliminar(Long id);
    OrdenServicio actualizarEstado(Long id, EstadoOrden nuevoEstado, String observacion);
    OrdenServicio actualizarEstadoConCosto(Long id, EstadoOrden nuevoEstado, String observacion, Double costoManoObra);
    OrdenServicio cambiarEstado(Long id, EstadoOrden nuevoEstado);
    OrdenServicio asignarTecnico(Long id, Long tecnicoId);
    List<OrdenServicio> listarPorCliente(Long clienteId);
    List<OrdenServicio> listarPorTecnico(Long tecnicoId);
    List<OrdenServicio> listarDisponiblesYAsignadas(Long tecnicoId);
    Double calcularCostoTotalRecursivo(Long id);
}
