package com.servitech.repository;

import com.servitech.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {
    List<OrdenServicio> findByClienteId(Long clienteId);
    List<OrdenServicio> findByTecnicoId(Long tecnicoId);
}
