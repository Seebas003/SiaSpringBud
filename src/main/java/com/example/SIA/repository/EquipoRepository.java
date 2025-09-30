package com.example.SIA.repository;

import com.example.SIA.entity.Equipo;
import com.example.SIA.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    
    // Listar equipos por usuario (dashboard aprendiz)
    List<Equipo> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, Integer estado);

    // Validar si ya existe un equipo con el mismo número de serie
    boolean existsByNumeroSerie(String numeroSerie);
}
