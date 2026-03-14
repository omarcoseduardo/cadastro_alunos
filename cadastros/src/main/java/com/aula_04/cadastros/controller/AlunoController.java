package com.aula_04.cadastros.controller;

import com.aula_04.cadastros.model.Aluno;
import com.aula_04.cadastros.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // Endpoint que permite buscar entidades no geral
    @GetMapping
    public ResponseEntity<List<Aluno>> buscarAlunoAll() {
        try {
            List<Aluno> buscar = alunoService.buscarAlunoAll();
            return ResponseEntity.ok(buscar);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        try {
            Aluno aluno = alunoService.buscarPorId(id);
            return ResponseEntity.ok(aluno);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint que permite buscar entidades a partir do atributo usuario
    @GetMapping("/buscar")
    public ResponseEntity<List<Aluno>> buscarPorUser(@RequestParam String usuario) {
        try {
            List<Aluno> alunos = alunoService.buscarPorUser(usuario);
            return ResponseEntity.ok(alunos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // Endpoint para deletar dados apartir do id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
    try {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build(); //404
    } catch (RuntimeException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404
    }
    }

    // Endpoint para permitir a adição de dados por meio do usuário
    @PostMapping
    public ResponseEntity<Aluno> adicionarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno salvo = alunoService.adicionarAluno(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Endpoint para editar entidade: Aluno.
    @PutMapping("/{id}")
    public ResponseEntity<Aluno>editarAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        // Tratamento de erro caso não retorne o dado corretamente
        try {
            return ResponseEntity.ok(alunoService.editarAluno(id, aluno));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
