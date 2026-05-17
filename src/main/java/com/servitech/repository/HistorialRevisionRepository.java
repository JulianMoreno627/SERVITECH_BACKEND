package com.servitech.repository;

import com.servitech.model.HistorialRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRevisionRepository extends JpaRepository<HistorialRevision, Long> {
}
