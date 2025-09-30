package com.example.SIA.repository;

import com.example.SIA.entity.Aprendiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // 👈 este es el que falta


@Repository
public interface AprendizRepository extends JpaRepository<Aprendiz, Integer> {
    List<Aprendiz> findByUsuario_IdUsuario(Integer idUsuario);
}
