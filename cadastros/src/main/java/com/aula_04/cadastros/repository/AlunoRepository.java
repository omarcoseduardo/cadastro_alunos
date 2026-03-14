package com.aula_04.cadastros.repository;

import com.aula_04.cadastros.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findByUsuario(String usuario);
}
