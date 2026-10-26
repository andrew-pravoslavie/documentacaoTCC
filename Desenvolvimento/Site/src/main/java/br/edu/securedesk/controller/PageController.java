package br.edu.securedesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller responsável pelo protótipo navegável do SecureDesk (Fase 2).
 * Serve as páginas da aplicação e alimenta os templates Thymeleaf com dados
 * fictícios realistas de uma PME em conformidade com o NIST CSF 2.0 e C2M2.
 */
@Controller
public class PageController {

    @ModelAttribute
    public void popularDadosPme(Model model) {
        // Dados da PME monitorada
        model.addAttribute("empresaNome", "InnovaTech Soluções Ltda.");
        model.addAttribute("empresaCnpj", "12.345.678/0001-90");
        model.addAttribute("empresaSetor", "Tecnologia da Informação");
        model.addAttribute("empresaPorte", "Pequena Empresa");
        model.addAttribute("empresaFuncionarios", 12);
        model.addAttribute("totalAtivos", 42);
        model.addAttribute("limiteAtivos", 100);
        model.addAttribute("ativosTexto", "42/100");
        model.addAttribute("trava100Ativos", "42 de 100 ativos cadastrados (trava PME em conformidade)");

        // Usuário logado
        model.addAttribute("usuarioNome", "Gabriel Andrew");
        model.addAttribute("usuarioEmail", "admin@innovatech.com.br");
        model.addAttribute("usuarioPerfil", "Administrador");
        model.addAttribute("usuarioAvatar", "GA");

        // Indicadores Globais de Maturidade (NIST CSF 2.0 x C2M2)
        model.addAttribute("scoreGlobal", 45);
        model.addAttribute("scoreGlobalFormatado", "45%");
        model.addAttribute("milMedio", 1.35);
        model.addAttribute("milAlvo", 2.0);
        model.addAttribute("funcoesAvaliadas", "6/6");
        model.addAttribute("totalGaps", 5);
        model.addAttribute("gapsCriticos", 2);
        model.addAttribute("tarefasPendentes", 5);
        model.addAttribute("tarefasEmAndamento", 4);
        model.addAttribute("tarefasConcluidas", 7);
        model.addAttribute("tarefasAtrasadas", 2);

        // Scores por Função NIST CSF 2.0 (Percentual e Nível MIL vs MIL 2 Alvo)
        // Governar (GV)
        model.addAttribute("scoreGvPercent", 40);
        model.addAttribute("scoreGvMil", 1.2);
        // Identificar (ID)
        model.addAttribute("scoreIdPercent", 70);
        model.addAttribute("scoreIdMil", 2.1);
        // Proteger (PR)
        model.addAttribute("scorePrPercent", 60);
        model.addAttribute("scorePrMil", 1.8);
        // Detectar (DE)
        model.addAttribute("scoreDePercent", 30);
        model.addAttribute("scoreDeMil", 0.9);
        // Responder (RS)
        model.addAttribute("scoreRsPercent", 50);
        model.addAttribute("scoreRsMil", 1.5);
        // Recuperar (RC)
        model.addAttribute("scoreRcPercent", 20);
        model.addAttribute("scoreRcMil", 0.6);
    }

    @GetMapping("/")
    public String index() {
        return "core/index";
    }

    @GetMapping("/login")
    public String login() {
        return "core/login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "core/dashboard";
    }

    @GetMapping("/diagnostico")
    public String diagnostico() {
        return "diagnostico/diagnostico";
    }

    @GetMapping("/acompanhamento")
    public String acompanhamento() {
        return "diagnostico/acompanhamento";
    }
}
