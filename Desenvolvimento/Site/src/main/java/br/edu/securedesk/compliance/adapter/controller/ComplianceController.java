package br.edu.securedesk.compliance.adapter.controller;

import br.edu.securedesk.compliance.domain.StatusPolitica;
import br.edu.securedesk.compliance.usecase.boundary.input.PoliticaInputBoundary;
import br.edu.securedesk.compliance.usecase.model.PoliticaRequestModel;
import br.edu.securedesk.compliance.usecase.model.PoliticaResponseModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller especializado da camada de Interface Adapters para Compliance e Políticas (/compliance):
 * adoção dos templates oficiais (PSI, ACESSO, BACKUP), revisão e histórico de versões (CRUD 4).
 */
@Controller
@RequestMapping("/compliance")
public class ComplianceController {

    private static final Long EMPRESA_PADRAO_ID = 1L;

    private static final List<TemplateOpcao> TEMPLATES = List.of(
            new TemplateOpcao("PSI", "Política de Segurança da Informação (PSI)",
                    "Documento base que define diretrizes, responsabilidades e princípios de segurança da informação para toda a organização.",
                    List.of("ISO 27001", "LGPD"), "GV", "📋"),
            new TemplateOpcao("ACESSO", "Política de Controle de Acesso e Credenciais",
                    "Define regras de menor privilégio, autenticação multifator (MFA) e revisão periódica de acessos aos recursos.",
                    List.of("ISO 27002", "LGPD"), "PR", "🔐"),
            new TemplateOpcao("BACKUP", "Política de Backup e Recuperação (Regra 3-2-1)",
                    "Estabelece a estratégia de backup 3-2-1, retenção e testes de restauração contra ransomware e desastres.",
                    List.of("ISO 27002", "NIST RC"), "RC", "💾"));

    private static final Set<String> CODIGOS_TEMPLATE = TEMPLATES.stream()
            .map(TemplateOpcao::codigo)
            .collect(Collectors.toUnmodifiableSet());

    private final PoliticaInputBoundary politicaInputBoundary;

    public ComplianceController(PoliticaInputBoundary politicaInputBoundary) {
        this.politicaInputBoundary = politicaInputBoundary;
    }

    /** Template oficial do OED 2 disponível para adoção; {@code adotado} indica se a PME já o possui. */
    public record TemplateOpcao(String codigo, String titulo, String descricao,
                                List<String> normas, String funcaoSigla, String icone, boolean adotado) {
        public TemplateOpcao(String codigo, String titulo, String descricao,
                             List<String> normas, String funcaoSigla, String icone) {
            this(codigo, titulo, descricao, normas, funcaoSigla, icone, false);
        }

        TemplateOpcao comAdotado(boolean adotado) {
            return new TemplateOpcao(codigo, titulo, descricao, normas, funcaoSigla, icone, adotado);
        }
    }

    /** Política da PME acompanhada dos dados derivados usados na listagem (ícone e dias até a revisão). */
    public record PoliticaCard(PoliticaResponseModel politica, String icone, long diasParaRevisao) {
    }

    /** Opção do seletor de função NIST CSF 2.0 no formulário de política. */
    public record FuncaoNistOpcao(String sigla, String nome) {
    }

    @GetMapping
    public String listar(Model model) {
        boolean formularioReapresentado = model.containsAttribute("politicaForm");
        if (!formularioReapresentado) {
            model.addAttribute("politicaForm", PoliticaRequestModel.padrao(EMPRESA_PADRAO_ID));
        }
        popularModelCompliance(model, null, null);
        model.addAttribute("modalPoliticaAberto", formularioReapresentado);
        return "compliance/compliance";
    }

    @GetMapping("/{id}")
    public String exibirHistorico(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PoliticaResponseModel detalhe = politicaInputBoundary.buscarPorId(id);
            model.addAttribute("politicaForm", PoliticaRequestModel.padrao(EMPRESA_PADRAO_ID));
            popularModelCompliance(model, null, detalhe);
            return "compliance/compliance";
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/compliance";
        }
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioRevisao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PoliticaResponseModel politica = politicaInputBoundary.buscarPorId(id);
            model.addAttribute("politicaForm", PoliticaRequestModel.fromResponse(politica));
            popularModelCompliance(model, id, null);
            return "compliance/compliance";
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/compliance";
        }
    }

    @PostMapping("/templates/{codigo}/adotar")
    public String adotarTemplate(@PathVariable String codigo, RedirectAttributes redirectAttributes) {
        String codigoNormalizado = codigo.trim().toUpperCase();
        if (!CODIGOS_TEMPLATE.contains(codigoNormalizado)) {
            redirectAttributes.addFlashAttribute("erro", "Template de política desconhecido: " + codigo);
            return "redirect:/compliance";
        }
        boolean jaAdotado = politicaInputBoundary.listarPorEmpresa(EMPRESA_PADRAO_ID).stream()
                .anyMatch(p -> codigoNormalizado.equals(p.codigoTemplateOrigem()));
        if (jaAdotado) {
            redirectAttributes.addFlashAttribute("erro",
                    "O template " + codigoNormalizado + " já foi adotado. Use \"Revisar\" para atualizar a política existente.");
            return "redirect:/compliance";
        }
        PoliticaResponseModel adotada = politicaInputBoundary.adotarTemplatePadrao(codigoNormalizado, EMPRESA_PADRAO_ID);
        redirectAttributes.addFlashAttribute("sucesso",
                String.format("Template '%s' adotado (versão %s). Customize o conteúdo para a realidade da PME.",
                        adotada.titulo(), adotada.versaoAtual()));
        return "redirect:/compliance";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("politicaForm") PoliticaRequestModel politicaForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelCompliance(model, null, null);
            model.addAttribute("modalPoliticaAberto", true);
            return "compliance/compliance";
        }
        try {
            PoliticaResponseModel criada = politicaInputBoundary.adotarOuCriar(comEmpresaPadrao(politicaForm));
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Política '%s' registrada (versão %s).", criada.titulo(), criada.versaoAtual()));
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            redirectAttributes.addFlashAttribute("politicaForm", politicaForm);
        }
        return "redirect:/compliance";
    }

    @PostMapping("/{id}/revisar")
    public String revisar(@PathVariable Long id,
                          @Valid @ModelAttribute("politicaForm") PoliticaRequestModel politicaForm,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelCompliance(model, id, null);
            model.addAttribute("modalPoliticaAberto", true);
            return "compliance/compliance";
        }
        try {
            PoliticaResponseModel revisada = politicaInputBoundary.revisarEAtualizar(id, comEmpresaPadrao(politicaForm));
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Política '%s' revisada: nova versão %s registrada no histórico.",
                            revisada.titulo(), revisada.versaoAtual()));
        } catch (IllegalArgumentException | NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/compliance";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            politicaInputBoundary.revogarOuExcluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Política e histórico de revisões removidos com sucesso!");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/compliance";
    }

    private void popularModelCompliance(Model model, Long politicaEmEdicaoId, PoliticaResponseModel detalhe) {
        List<PoliticaResponseModel> politicas = politicaInputBoundary.listarPorEmpresa(EMPRESA_PADRAO_ID);
        LocalDate hoje = LocalDate.now();

        List<PoliticaCard> cards = politicas.stream()
                .map(p -> new PoliticaCard(p, icone(p.codigoTemplateOrigem()),
                        p.proximaRevisao() != null ? ChronoUnit.DAYS.between(hoje, p.proximaRevisao()) : Long.MAX_VALUE))
                .toList();
        List<PoliticaCard> alertas = cards.stream()
                .filter(c -> c.politica().status() == StatusPolitica.VENCIDA || c.politica().status().isPendenteRevisao())
                .sorted(Comparator.comparingLong(PoliticaCard::diasParaRevisao))
                .toList();

        Set<String> adotados = politicas.stream()
                .map(PoliticaResponseModel::codigoTemplateOrigem)
                .collect(Collectors.toSet());

        model.addAttribute("politicas", cards);
        model.addAttribute("alertasRevisao", alertas);
        model.addAttribute("templates", TEMPLATES.stream().map(t -> t.comAdotado(adotados.contains(t.codigo()))).toList());
        model.addAttribute("totalTemplates", TEMPLATES.size());
        model.addAttribute("politicasEmDia", politicas.stream().filter(p -> p.status() == StatusPolitica.EM_DIA).count());
        model.addAttribute("politicasPendentes", politicas.stream().filter(p -> p.status().isPendenteRevisao()).count());
        model.addAttribute("politicasVencidas", politicas.stream().filter(p -> p.status() == StatusPolitica.VENCIDA).count());

        model.addAttribute("funcoesNist", List.of(
                new FuncaoNistOpcao("GV", "Governar"),
                new FuncaoNistOpcao("ID", "Identificar"),
                new FuncaoNistOpcao("PR", "Proteger"),
                new FuncaoNistOpcao("DE", "Detectar"),
                new FuncaoNistOpcao("RS", "Responder"),
                new FuncaoNistOpcao("RC", "Recuperar")));
        model.addAttribute("statusCriacao", List.of(StatusPolitica.EM_ELABORACAO, StatusPolitica.EM_DIA));
        model.addAttribute("politicaEmEdicaoId", politicaEmEdicaoId);
        model.addAttribute("modalPoliticaAberto", politicaEmEdicaoId != null);

        model.addAttribute("politicaDetalhe", detalhe);
        List<PoliticaResponseModel.RevisaoItemModel> historico = new ArrayList<>(
                detalhe != null ? detalhe.historicoRevisoes() : List.of());
        Collections.reverse(historico);
        model.addAttribute("historicoRecente", historico);
    }

    private static String icone(String codigoTemplate) {
        return TEMPLATES.stream()
                .filter(t -> t.codigo().equals(codigoTemplate))
                .map(TemplateOpcao::icone)
                .findFirst()
                .orElse("📄");
    }

    /** Impede que um campo adulterado no navegador grave a política em outra empresa. */
    private static PoliticaRequestModel comEmpresaPadrao(PoliticaRequestModel f) {
        return new PoliticaRequestModel(f.titulo(), f.codigoTemplateOrigem(), f.normaReferencia(),
                f.funcaoNistSigla(), f.conteudoCustomizado(), f.status(), f.periodicidadeMesesRevisao(),
                f.revisorNome(), f.observacoesRevisao(), EMPRESA_PADRAO_ID);
    }
}
