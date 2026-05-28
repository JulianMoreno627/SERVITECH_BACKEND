package com.servitech.service;

import com.servitech.model.OpcionMenu;
import com.servitech.repository.OpcionMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OpcionMenuService {

    @Autowired
    private OpcionMenuRepository repository;

    public List<OpcionMenu> obtenerMenuRecursivo() {
        // Obtenemos solo los padres raíz. 
        // Gracias a FetchType.EAGER en la entidad, JPA traerá los hijos recursivamente.
        return repository.findByPadreIsNull();
    }
}
