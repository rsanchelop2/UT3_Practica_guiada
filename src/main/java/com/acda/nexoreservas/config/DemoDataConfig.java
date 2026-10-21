package com.acda.nexoreservas.config;

import com.acda.nexoreservas.model.entity.Cliente;
import com.acda.nexoreservas.model.entity.Sala;
import com.acda.nexoreservas.model.repository.ClienteRepository;
import com.acda.nexoreservas.model.repository.SalaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("demo")
public class DemoDataConfig {
    private static final Logger log = LoggerFactory.getLogger(DemoDataConfig.class);

    @Bean
    CommandLineRunner cargarDemo(SalaRepository salas, ClienteRepository clientes) {
        return args -> {
            // Cada conjunto se carga solo si su tabla está vacía.
            if (salas.count() == 0) {
                String[] nombresSalas = {
                        "Ágora", "Horizonte", "Norte", "Lumen", "Foro", "Vértice",
                        "Atlas", "Brisa", "Prisma", "Orbe", "Cobalto", "Mirador"
                };
                for (int i = 0; i < nombresSalas.length; i++) {
                    Sala sala = new Sala();
                    sala.setCodigo(String.format("SAL-%03d", i + 1));
                    sala.setNombre(nombresSalas[i]);
                    sala.setUbicacion("Edificio " + (i < 6 ? "Norte" : "Sur")
                            + " · planta " + (i % 3 + 1));
                    sala.setCapacidad(8 + (i % 5) * 6);
                    sala.setTarifaHora(BigDecimal.valueOf(18 + i * 3));
                    sala.setActiva(i < 10);
                    salas.save(sala);
                }
                log.info("Datos demo: 12 salas creadas");
            } else {
                log.info("Datos demo: salas existentes; se omite su carga");
            }

            if (clientes.count() == 0) {
                String[] nombres = {
                        "Ana", "Luis", "Marta", "Carlos", "Elena", "Pablo", "Lucía",
                        "David", "Sara", "Iván", "Nuria", "Javier", "Clara", "Mario",
                        "Irene", "Diego", "Laura", "Álvaro", "Paula", "Sergio"
                };
                String[] apellidos = {
                        "García López", "Martín Ruiz", "Sánchez Díaz", "Pérez Cano",
                        "Romero Gil", "Navarro León", "Torres Vega", "Ramos Soler",
                        "Molina Prieto", "Castro Vidal", "Ortega Serra", "Vargas Soto",
                        "Morales Ríos", "Herrera Pons", "Delgado Cruz",
                        "Santos Costa", "Muñoz Alba", "Suárez Mena",
                        "Iglesias Lara", "Campos Rey"
                };
                for (int i = 0; i < nombres.length; i++) {
                    Cliente cliente = new Cliente();
                    cliente.setNombre(nombres[i]);
                    cliente.setApellidos(apellidos[i]);
                    cliente.setEmail("cliente" + (i + 1) + "@ejemplo.test");
                    cliente.setTelefono("600" + String.format("%06d", 100000 + i));
                    cliente.setActivo(i < 18);
                    clientes.save(cliente);
                }
                log.info("Datos demo: 20 clientes creados");
            } else {
                log.info("Datos demo: clientes existentes; se omite su carga");
            }
            log.info("Datos demo: no se crean reservas hasta mapear sus relaciones");
        };
    }
}
