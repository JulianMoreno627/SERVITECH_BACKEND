package com.servitech.controller;

import com.servitech.model.OpcionMenu;
import com.servitech.service.OpcionMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class OpcionMenuController {

    @Autowired
    private OpcionMenuService menuService;

    @GetMapping
    public List<OpcionMenu> getMenu() {
        return menuService.obtenerMenuRecursivo();
    }
}
