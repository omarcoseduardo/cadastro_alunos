package com.aula_04.cadastros.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno")
public class Aluno {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotBlank(message = "Este campo é obrigatório")
    private String usuario;
    @Setter
    @Getter
    @NotNull(message = "Este campo é obrigatório")
    private Integer matricula;
    @Getter
    @Setter
    private String email;

    public void setUsuario(@NotNull(message = "Este campo é obrigatório") String nome) {
        this.usuario = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
