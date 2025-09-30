package com.example.SIA.repository;

import com.example.SIA.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // 📌 Validaciones
    boolean existsByCorreo(String correo);
    boolean existsByNoDocumento(String noDocumento);

    // 📌 Búsquedas personalizadas
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByNoDocumento(String noDocumento);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.estado = :estado WHERE u.idUsuario = :id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") Integer estado);
    
}