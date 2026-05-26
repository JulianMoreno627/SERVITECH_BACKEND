package com.servitech.config;

import com.servitech.model.*;
import com.servitech.service.IUsuarioService;
import com.servitech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioService.buscarPorUsuario("admin").isEmpty()) {
            Administrador admin = new Administrador();
            admin.setUsuario("admin");
            admin.setContrasena("admin123");
            admin.setNombre("Administrador General");
            admin.setEmail("admin@servitech.com");
            admin.setRol(Rol.ADMIN);
            usuarioService.guardar(admin);
        }

        if (usuarioService.buscarPorUsuario("tecnico1").isEmpty()) {
            Tecnico tecnico = new Tecnico();
            tecnico.setUsuario("tecnico1");
            tecnico.setContrasena("tecnico123");
            tecnico.setNombre("Juan Técnico");
            tecnico.setEmail("juan@servitech.com");
            tecnico.setRol(Rol.TECNICO);
            tecnico.setEspecialidad("Lavadoras");
            tecnico.setDisponible(true);
            usuarioService.guardar(tecnico);
        }

        if (usuarioService.buscarPorUsuario("cliente1").isEmpty()) {
            Cliente cliente = new Cliente();
            cliente.setUsuario("cliente1");
            cliente.setContrasena("cliente123");
            cliente.setNombre("Maria Cliente");
            cliente.setEmail("maria@gmail.com");
            cliente.setRol(Rol.CLIENTE);
            usuarioService.guardar(cliente);
        }
    }
}
