package com.acda.nexoreservas.controller;

import com.acda.nexoreservas.model.service.ClienteService;
import com.acda.nexoreservas.model.service.ReservaService;
import com.acda.nexoreservas.model.service.SalaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SalaService salas;
    private final ClienteService clientes;
    private final ReservaService reservas;

    public HomeController(
            SalaService salas,
            ClienteService clientes,
            ReservaService reservas) {
        this.salas = salas;
        this.clientes = clientes;
        this.reservas = reservas;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        // El controlador prepara los datos de la portada; las consultas
        // y las reglas permanecen en los servicios.
        model.addAttribute("totalSalas", salas.total());
        model.addAttribute("salasActivas", salas.activas());
        model.addAttribute("totalClientes", clientes.total());
        model.addAttribute("reservasFuturas", reservas.totalFuturasConfirmadas());
        model.addAttribute("proximas", reservas.proximas());
        return "home";
    }
}
