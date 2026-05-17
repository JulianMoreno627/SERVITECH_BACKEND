package com.servitech.config;

import com.servitech.model.*;
import com.servitech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RepuestoRepository repuestoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar Admin si no existe
        if (personaRepository.findByUsuario("admin").isEmpty()) {
            Administrador admin = new Administrador();
            admin.setUsuario("admin");
            admin.setContrasena("admin123"); // En producción usar BCrypt
            admin.setNombre("Administrador Sistema");
            admin.setEmail("admin@servitech.com");
            admin.setRol(Rol.ADMIN);
            personaRepository.save(admin);
            System.out.println("Usuario ADMIN creado por defecto: admin / admin123");
        }

        // Inicializar Técnicos
        if (personaRepository.findAll().stream().noneMatch(p -> p.getRol() == Rol.TECNICO)) {
            Tecnico tecnico = new Tecnico();
            tecnico.setUsuario("tecnico1");
            tecnico.setContrasena("tec123");
            tecnico.setNombre("Juan Pérez");
            tecnico.setEmail("juan.perez@servitech.com");
            tecnico.setRol(Rol.TECNICO);
            tecnico.setEspecialidad("Reparación de Laptops");
            tecnico.setDisponible(true);
            personaRepository.save(tecnico);
            System.out.println("Usuario TECNICO creado: tecnico1 / tec123");
        }

        // Inicializar Repuestos si está vacío
        if (repuestoRepository.count() == 0) {
            Repuesto r1 = new Repuesto();
            r1.setNombre("Pantalla LED 15.6");
            r1.setPrecio(150000.0);
            r1.setStock(10);

            Repuesto r2 = new Repuesto();
            r2.setNombre("Batería HP Original");
            r2.setPrecio(85000.0);
            r2.setStock(5);

            Repuesto r3 = new Repuesto();
            r3.setNombre("SSD 480GB Kingston");
            r3.setPrecio(120000.0);
            r3.setStock(2);

            repuestoRepository.saveAll(Arrays.asList(r1, r2, r3));
            System.out.println("Inventario inicial de repuestos cargado.");
        }
    }
}
