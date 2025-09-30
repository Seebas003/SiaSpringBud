package com.example.SIA.controller;

import com.example.SIA.entity.Usuario;
import com.example.SIA.entity.Aprendiz;
import com.example.SIA.service.UsuarioService;
import com.example.SIA.service.AprendizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/aprendiz")
public class FormularioController {
    private final UsuarioService usuarioService;
    private final AprendizService aprendizService;

    public FormularioController(UsuarioService usuarioService, AprendizService aprendizService) {
        this.usuarioService = usuarioService;
        this.aprendizService = aprendizService;
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Usuario usuario = usuarioService.findById(idUsuario);
        java.util.List<Aprendiz> aprendices = aprendizService.findByUsuarioId(idUsuario);
        Aprendiz aprendiz = aprendices.isEmpty() ? new Aprendiz() : aprendices.get(0);

        List<String> programas = Arrays.asList(
            "Análisis y Desarrollo de Software",
            "Gestión de Redes de Datos",
            "Gestión Administrativa",
            "Contabilidad y Finanzas",
            "Diseño e Integración de Multimedia",
            "Mecatrónica",
            "Seguridad y Salud en el Trabajo"
        );

        model.addAttribute("usuario", usuario);
        model.addAttribute("aprendiz", aprendiz);
        model.addAttribute("programas", programas);
        return "formulario"; // 👈 sigue en templates
    }

    @PostMapping("/formulario/guardar")
    public String guardarFormulario(
            HttpSession session,
            @ModelAttribute("usuario") Usuario usuarioForm,
            @ModelAttribute("aprendiz") Aprendiz aprendizForm,
            RedirectAttributes redirectAttrs) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        usuarioForm.setIdUsuario(idUsuario);
        Usuario original = usuarioService.findById(idUsuario);

        if (usuarioForm.getPassUsuario() == null || usuarioForm.getPassUsuario().isBlank()) {
            if (original != null) {
                usuarioForm.setPassUsuario(original.getPassUsuario());
            }
        }

        Usuario usuarioEntity = usuarioService.findById(idUsuario);
        aprendizForm.setUsuario(usuarioEntity);

        usuarioService.actualizar(usuarioForm);
        aprendizService.actualizarAprendiz(aprendizForm);

        redirectAttrs.addFlashAttribute("success", "Datos actualizados correctamente.");
        return "redirect:/dashboard/aprendiz";
    }
}
