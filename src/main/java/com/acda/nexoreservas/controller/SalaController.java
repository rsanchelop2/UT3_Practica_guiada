package com.acda.nexoreservas.controller;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.model.entity.Sala;
import com.acda.nexoreservas.model.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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

@Controller
@RequestMapping("/salas")
public class SalaController {

    private final SalaService servicio;

    public SalaController(SalaService servicio) {
        this.servicio = servicio;
    }

    // Spring recibe los filtros de la URL y el controlador prepara los
    // atributos que Thymeleaf utiliza para mostrar la tabla y el formulario.
    @GetMapping
    public String listado(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String ubicacion,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(defaultValue = "codigo") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {
        model.addAttribute(
                "datos",
                servicio.buscar(q, ubicacion, activa, capacidad, sort, dir)
        );
        model.addAttribute("q", q);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("activa", activa);
        model.addAttribute("capacidad", capacidad);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "salas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Sala sala = new Sala();
        model.addAttribute("sala", sala);
        return "salas/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("sala", servicio.obtener(id));
        return "salas/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("sala", servicio.obtener(id));
        return "salas/form";
    }

    // @ModelAttribute rellena Sala con los campos HTML. BindingResult recoge
    // los errores de @Valid y debe ir justo después del objeto validado.
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("sala") Sala sala,
            BindingResult errores,
            Model model,
            RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return "salas/form";
        }

        try {
            servicio.guardar(sala);
            // Tras guardar, una redirección evita repetir el POST al recargar.
            flash.addFlashAttribute("exito", "Sala guardada correctamente");
            return "redirect:/salas";
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "El código ya existe o la sala tiene datos relacionados";
            model.addAttribute("error", mensaje);
            return "salas/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
            flash.addFlashAttribute("exito", "Sala eliminada correctamente");
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "No se puede eliminar la sala porque tiene datos relacionados";
            flash.addFlashAttribute("error", mensaje);
        }
        return "redirect:/salas";
    }
}
