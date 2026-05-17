package com.servitech.repository;

import com.servitech.model.DetalleOrdenRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenRepuestoRepository extends JpaRepository<DetalleOrdenRepuesto, Long> {
}
