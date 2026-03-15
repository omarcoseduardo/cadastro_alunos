package com.aula_04.cadastros.controller;

import com.aula_04.cadastros.model.Aluno;
import com.aula_04.cadastros.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AlunoViewController {

    private final AlunoService alunoService;

    public AlunoViewController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("alunos", alunoService.buscarAlunoAll());
        return "crud";
    }

    @GetMapping("/alunos")
    public String listarAlunos(Model model) {
        model.addAttribute("alunos", alunoService.buscarAlunoAll());
        return "lista";
    }

    @PostMapping("/")
    public String cadastrarAluno(@Valid Aluno aluno, BindingResult result,
                                 Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.aluno", result);
            redirect.addFlashAttribute("aluno", aluno);
            model.addAttribute("alunos", alunoService.buscarAlunoAll());
            return "crud";
        }
        alunoService.adicionarAluno(aluno);
        return "redirect:/";
    }

    @PostMapping("/alunos/{id}/deletar")
    public String deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return "redirect:/alunos";
    }

    // Abre página de edição
    @GetMapping("/alunos/{id}/editar")
    public String editarAluno(@PathVariable Long id, Model model) {
        Aluno aluno = alunoService.buscarPorId(id);
        model.addAttribute("aluno", aluno);
        return "editar";
    }

    @PostMapping("/alunos/{id}/editar")
    public String salvarEdicao(@PathVariable Long id, @Valid Aluno aluno, BindingResult result) {
        if (result.hasErrors()) {
            return "editar";
        }
        alunoService.editarAluno(id, aluno);
        return "redirect:/alunos";
    }
}