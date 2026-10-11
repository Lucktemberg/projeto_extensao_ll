package com.doacoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/tela-usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/tela-doadores")
    public String doadores() {
        return "doadores";
    }

    @GetMapping("/tela-receptores")
    public String receptores() {
        return "receptores";
    }

    @GetMapping("/tela-doacoes")
    public String doacoes() {
        return "doacoes";
    }

    @GetMapping("/tela-estoques")
    public String estoques() {
        return "estoques";
    }

    @GetMapping("/tela-distribuicoes")
    public String distribuicoes() {
        return "distribuicoes";
    }

    @GetMapping("/tela-tipos-doacao")
    public String tiposDoacao() {
        return "tipos-doacao";
    }

    @GetMapping("/tela-tamanhos-roupa")
    public String tamanhosRoupa() {
        return "tamanhos-roupa";
    }

    @GetMapping("/tela-campanhas")
    public String campanhas() {
        return "campanhas";
    }

    @GetMapping("/tela-relatorios")
    public String relatorios() {
        return "relatorios";
    }

    @GetMapping("/acompanhar")
    public String acompanhar() {
        return "acompanhar";
    }
}