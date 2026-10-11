package com.doacoes.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UsuarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Login é obrigatório")
    private String login;

    private String senha;

    @NotBlank(message = "Perfil é obrigatório")
    private String perfil;

    private List<String> permissoes;

    public UsuarioRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(
            List<String> permissoes) {

        this.permissoes = permissoes;
    }
}