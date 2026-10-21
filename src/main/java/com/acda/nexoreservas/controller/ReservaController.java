package com.acda.nexoreservas.controller;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.model.entity.EstadoReserva;
import com.acda.nexoreservas.model.service.ClienteService;
import com.acda.nexoreservas.model.service.ReservaService;
import com.acda.nexoreservas.model.service.SalaService;
import com.acda.nexoreservas.web.form.ReservaForm;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService servicio;
    private final SalaService salas;
    private final ClienteService clientes;

    public ReservaController(
            ReservaService servicio,
            SalaService salas,
            ClienteService clientes) {
        this.servicio = servicio;
        this.salas = salas;
        this.clientes = clientes;
    }

    // Los parámetros de la URL se convierten en filtros para el servicio.
    // El modelo conserva sus valores al renderizar de nuevo el listado.
    @GetMapping
    public String listado(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime desde,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime hasta,
            @RequestParam(required = false) Long salaId,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(defaultValue = "inicio") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model) {
        model.addAttribute(
                "datos",
                servicio.buscar(
                        q, estado, desde, hasta, salaId, clienteId,
                        sort, dir
                )
        );
        model.addAttribute("q", q);
        model.addAttribute("estado", estado);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("salaId", salaId);
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("salas", salas.todas());
        model.addAttribute("clientes", clientes.todos());
        return "reservas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("reservaForm", new ReservaForm());
        opciones(model);
        return "reservas/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", servicio.obtener(id));
        return "reservas/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("reservaForm", ReservaForm.desde(servicio.obtener(id)));
        opciones(model);
        return "reservas/form";
    }

    // @ModelAttribute rellena ReservaForm con los campos HTML y @Valid aplica
    // sus restricciones. BindingResult permite volver al formulario con errores.
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("reservaForm") ReservaForm form,
            BindingResult errores,
            Model model,
            RedirectAttributes flash) {
        if (errores.hasErrors()) {
            opciones(model);
            return "reservas/form";
        }

        try {
            servicio.guardar(form);
            flash.addFlashAttribute("exito", "Reserva guardada correctamente");
            return "redirect:/reservas";
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "No se ha podido guardar la reserva por un conflicto de datos";
            model.addAttribute("error", mensaje);
            opciones(model);
            return "reservas/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
            flash.addFlashAttribute("exito", "Reserva eliminada correctamente");
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "No se ha podido eliminar la reserva";
            flash.addFlashAttribute("error", mensaje);
        }
        return "redirect:/reservas";
    }

    private void opciones(Model model) {
        model.addAttribute("salas", salas.todas());
        model.addAttribute("clientes", clientes.todos());
        model.addAttribute("estados", EstadoReserva.values());
    }
}
