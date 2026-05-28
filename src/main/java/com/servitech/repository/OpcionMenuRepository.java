package com.servitech.repository;

import com.servitech.model.OpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Long> {
    List<OpcionMenu> findByPadreIsNull();
}
