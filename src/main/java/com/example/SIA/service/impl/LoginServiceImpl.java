package com.example.SIA.service.impl;

import com.example.SIA.dto.LoginRequest;
import com.example.SIA.dto.LoginResponse;
import com.example.SIA.entity.Usuario;
import com.example.SIA.repository.UsuarioRepository;
import com.example.SIA.service.LoginService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepo.findByNombreUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getEstado() != 1) {
            throw new RuntimeException("Este usuario está inactivo. Contacte al administrador.");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassUsuario())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // 🔹 Determinar la URL de redirección según el perfil
            // Validar que el usuario tenga perfil asignado
            if (usuario.getPerfil() == null) {
                throw new RuntimeException("El usuario no tiene un perfil asignado. Contacte al administrador.");
            }
            Integer idPerfil = usuario.getPerfil().getIdPerfil();
        String redirect;

        switch (idPerfil) {
            case 1 -> redirect = "/dashboard/aprendiz";
            case 2 -> redirect = "/dashboard/admin";
            case 3 -> redirect = "/dashboard/instructor";
            case 4 -> redirect = "/dashboard/administrativo";
            default -> redirect = "/login";
        }

        return new LoginResponse(
                usuario.getIdUsuario(),
                idPerfil, // 👈 nuevo campo para guardar en sesión
                usuario.getNombreUsuario(),
                usuario.getCorreo(),
                usuario.getPerfil().getNombrePerfil(),
                redirect
        );
    }

    @Override
    public void logout() {
        // Aquí podrías invalidar la sesión si usas Spring Security
    }
}