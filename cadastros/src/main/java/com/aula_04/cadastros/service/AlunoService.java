package com.aula_04.cadastros.service;

import com.aula_04.cadastros.model.Aluno;
import org.springframework.stereotype.Service;
import com.aula_04.cadastros.repository.AlunoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno adicionarAluno(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> buscarAlunoAll() {
        return alunoRepository.findAll();
    }

    public List<Aluno> buscarPorUser(String usuario) {

        return alunoRepository.findByUsuario(usuario);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }

    public Aluno editarAluno(Long id, Aluno alunoEdit) {

        Aluno aluno = alunoRepository.findById(id).
                orElseThrow(()->new RuntimeException("Aluno não encontrado, por favor tente novamente"));

        aluno.setUsuario(alunoEdit.getUsuario());
        aluno.setEmail(alunoEdit.getEmail());
        aluno.setMatricula(alunoEdit.getMatricula());

        return alunoRepository.save(aluno);
    }
}
