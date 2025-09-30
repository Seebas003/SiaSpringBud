package com.example.SIA.controller;

import com.example.SIA.entity.Usuario;
import com.example.SIA.entity.Aprendiz;
import com.example.SIA.service.UsuarioService;
import com.example.SIA.service.AprendizService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final UsuarioService usuarioService;
    private final AprendizService aprendizService;

    public DashboardController(UsuarioService usuarioService, AprendizService aprendizService) {
        this.usuarioService = usuarioService;
        this.aprendizService = aprendizService;
    }

    // Ruta general que redirige según perfil (comprobación sencilla)
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        Integer perfil = (Integer) session.getAttribute("id_perfil");

        if (perfil == null) {
            return "redirect:/login";
        }

        switch (perfil) {
            case 1:
                return "redirect:/dashboard/aprendiz";
            case 2:
                return "redirect:/dashboard/admin";
            case 3:
                return "redirect:/dashboard/instructor";
            default:
                return "redirect:/login?error=perfil_no_valido";
        }
    }

    // Panel Aprendiz (ruta en minúsculas)
    @GetMapping("/dashboard/aprendiz")
    public String dashboardAprendiz(HttpSession session, Model model) {
        if (!perfilValido(session, 1)) {
            return "redirect:/login?error=acceso_denegado";
        }

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Usuario usuario = usuarioService.findById(idUsuario);
        java.util.List<Aprendiz> aprendices = aprendizService.findByUsuarioId(idUsuario);
        Aprendiz aprendiz = aprendices.isEmpty() ? new Aprendiz() : aprendices.get(0);

        if (aprendiz.getFichaFormacion() == null) aprendiz.setFichaFormacion("");
        if (aprendiz.getProgramaFormacion() == null) aprendiz.setProgramaFormacion("");

        boolean aprendizCompleto = !aprendiz.getFichaFormacion().isEmpty() &&
                                   !aprendiz.getProgramaFormacion().isEmpty();

        // Agregar equipos al modelo para la tabla en dashboardAprendiz
        java.util.List<com.example.SIA.entity.Equipo> equipos = null;
        try {
            equipos = ((com.example.SIA.service.EquipoService) org.springframework.beans.factory.BeanFactoryUtils.beanOfTypeIncludingAncestors(
                org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()),
                com.example.SIA.service.EquipoService.class)).listarPorUsuario(idUsuario);
        } catch (Exception e) {
            equipos = new java.util.ArrayList<>();
        }
        model.addAttribute("equipos", equipos);

        model.addAttribute("usuario", usuario);
        model.addAttribute("aprendiz", aprendiz);
        model.addAttribute("aprendiz_completo", aprendizCompleto);

        return "dashboardAprendiz";
    }

    // Panel Administrador
    @GetMapping("/dashboard/admin")
    public String dashboardAdministrador(HttpSession session, Model model) {
        if (!perfilValido(session, 2)) {
            return "redirect:/login?error=acceso_denegado";
        }

        model.addAttribute("usuarios", usuarioService.findAll());
        return "dashboardAdministrador";
    }

    // Panel Instructor
    @GetMapping("/dashboard/instructor")
    public String dashboardInstructor(HttpSession session, Model model) {
        if (!perfilValido(session, 3)) {
            return "redirect:/login?error=acceso_denegado";
        }

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        java.util.List<com.example.SIA.entity.Equipo> equipos = new java.util.ArrayList<>();
        try {
            equipos = ((com.example.SIA.service.EquipoService) org.springframework.beans.factory.BeanFactoryUtils.beanOfTypeIncludingAncestors(
                org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()),
                com.example.SIA.service.EquipoService.class)).listarPorUsuario(idUsuario);
        } catch (Exception e) {
            // Si hay error, la lista queda vacía
        }
        model.addAttribute("equipos", equipos);

        return "dashboardInstructor";
    }

    // Validación de perfil
    private boolean perfilValido(HttpSession session, int perfilEsperado) {
        Integer perfil = (Integer) session.getAttribute("id_perfil");
        return perfil != null && perfil == perfilEsperado;
    }
}
