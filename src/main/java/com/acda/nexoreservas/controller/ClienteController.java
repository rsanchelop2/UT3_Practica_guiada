package com.acda.nexoreservas.controller;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.model.entity.Cliente;
import com.acda.nexoreservas.model.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService servicio;

    public ClienteController(ClienteService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listado(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "apellidos") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {
        model.addAttribute("datos", servicio.buscar(q, activo, sort, dir));
        model.addAttribute("q", q);
        model.addAttribute("activo", activo);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", servicio.obtener(id));
        return "clientes/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", servicio.obtener(id));
        return "clientes/form";
    }

    // @ModelAttribute vincula los campos del formulario. BindingResult deja
    // volver a la misma vista con los errores y los valores introducidos.
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult errores,
            Model model,
            RedirectAttributes flash
    ) {
        if (errores.hasErrors()) {
            return "clientes/form";
        }

        try {
            servicio.guardar(cliente);
            flash.addFlashAttribute("exito", "Cliente guardado correctamente");
            return "redirect:/clientes";
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "El email ya existe o el cliente tiene datos relacionados";
            model.addAttribute("error", mensaje);
            return "clientes/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
            flash.addFlashAttribute("exito", "Cliente eliminado correctamente");
        } catch (BusinessException | DataIntegrityViolationException ex) {
            String mensaje = ex instanceof BusinessException
                    ? ex.getMessage()
                    : "No se puede eliminar el cliente porque tiene datos relacionados";
            flash.addFlashAttribute("error", mensaje);
        }
        return "redirect:/clientes";
    }
}
