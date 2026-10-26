package br.edu.securedesk.governanca.adapter.controller;

import br.edu.securedesk.governanca.adapter.storage.EvidenciaFileStorage;
import br.edu.securedesk.governanca.domain.Prioridade;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import br.edu.securedesk.governanca.usecase.boundary.input.TarefaInputBoundary;
import br.edu.securedesk.governanca.usecase.model.EvidenciaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaResponseModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller especializado da camada de Interface Adapters para a Governança Operacional (/governanca):
 * Kanban de Tarefas de Remediação e Evidências comprobatórias (CRUD 3), respeitando a Clean Architecture.
 */
@Controller
@RequestMapping("/governanca")
public class GovernancaController {

    private static final Long EMPRESA_PADRAO_ID = 1L;
    private static final int DESCRICAO_EVIDENCIA_MAX = 500;
    private static final Set<String> SIGLAS_NIST = Set.of("GV", "ID", "PR", "DE", "RS", "RC");

    private final TarefaInputBoundary tarefaInputBoundary;
    private final EvidenciaFileStorage evidenciaFileStorage;

    public GovernancaController(TarefaInputBoundary tarefaInputBoundary,
                                EvidenciaFileStorage evidenciaFileStorage) {
        this.tarefaInputBoundary = tarefaInputBoundary;
        this.evidenciaFileStorage = evidenciaFileStorage;
    }

    /** Opção do seletor de função NIST CSF 2.0 exibida no formulário e nos filtros do Kanban. */
    public record FuncaoNistOpcao(String sigla, String nome) {
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String funcao,
                         @RequestParam(required = false) String prioridade,
                         Model model) {
        boolean formularioReapresentado = model.containsAttribute("tarefaForm");
        if (!formularioReapresentado) {
            model.addAttribute("tarefaForm", TarefaRequestModel.padrao(EMPRESA_PADRAO_ID));
        }
        popularModelKanban(model, null, funcao, prioridade);
        model.addAttribute("modalTarefaAberto", formularioReapresentado);
        return "governanca/governanca";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("tarefaForm", paraFormulario(tarefaInputBoundary.buscarPorId(id)));
            popularModelKanban(model, id, null, null);
            return "governanca/governanca";
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/governanca";
        }
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("tarefaForm") TarefaRequestModel tarefaForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelKanban(model, null, null, null);
            model.addAttribute("modalTarefaAberto", true);
            return "governanca/governanca";
        }
        try {
            TarefaResponseModel criada = tarefaInputBoundary.criar(comEmpresaPadrao(tarefaForm));
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Tarefa '%s' criada no Kanban de remediação!", criada.titulo()));
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            redirectAttributes.addFlashAttribute("tarefaForm", tarefaForm);
        }
        return "redirect:/governanca";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("tarefaForm") TarefaRequestModel tarefaForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelKanban(model, id, null, null);
            model.addAttribute("modalTarefaAberto", true);
            return "governanca/governanca";
        }
        try {
            TarefaResponseModel atualizada = tarefaInputBoundary.atualizar(id, comEmpresaPadrao(tarefaForm));
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Tarefa '%s' atualizada com sucesso!", atualizada.titulo()));
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/governanca";
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(@PathVariable Long id,
                                @RequestParam StatusTarefa status,
                                RedirectAttributes redirectAttributes) {
        try {
            TarefaResponseModel tarefa = tarefaInputBoundary.alterarStatus(id, status);
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Tarefa '%s' movida para '%s'.", tarefa.titulo(), tarefa.status().getDescricao()));
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/governanca";
    }

    @PostMapping(path = "/{id}/evidencias", consumes = "multipart/form-data")
    public String anexarEvidencia(@PathVariable Long id,
                                  @RequestParam("arquivo") MultipartFile arquivo,
                                  @RequestParam(required = false) String descricao,
                                  RedirectAttributes redirectAttributes) {
        String nomeGravado = null;
        try {
            if (descricao != null && descricao.length() > DESCRICAO_EVIDENCIA_MAX) {
                throw new IllegalArgumentException(
                        "A descrição da evidência deve ter no máximo " + DESCRICAO_EVIDENCIA_MAX + " caracteres.");
            }
            tarefaInputBoundary.buscarPorId(id);
            nomeGravado = evidenciaFileStorage.armazenar(id, arquivo);
            tarefaInputBoundary.anexarEvidencia(id, new EvidenciaRequestModel(
                    nomeGravado, arquivo.getContentType(), arquivo.getSize(),
                    descricao != null && !descricao.isBlank() ? descricao.trim() : null, null));
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Evidência '%s' anexada à tarefa #%d.", nomeGravado, id));
        } catch (RuntimeException ex) {
            if (nomeGravado != null) {
                evidenciaFileStorage.remover(nomeGravado);
            }
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/governanca";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            List<String> arquivos = tarefaInputBoundary.buscarPorId(id).evidencias().stream()
                    .map(TarefaResponseModel.EvidenciaItemModel::nomeArquivo)
                    .toList();
            tarefaInputBoundary.excluir(id);
            arquivos.forEach(evidenciaFileStorage::remover);
            redirectAttributes.addFlashAttribute("sucesso", "Tarefa e evidências removidas do Kanban com sucesso!");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/governanca";
    }

    private void popularModelKanban(Model model, Long tarefaEmEdicaoId, String funcao, String prioridade) {
        List<TarefaResponseModel> todas = tarefaInputBoundary.listarPorEmpresa(EMPRESA_PADRAO_ID);

        String siglaFiltro = funcao != null && SIGLAS_NIST.contains(funcao.toUpperCase()) ? funcao.toUpperCase() : null;
        Prioridade prioridadeFiltro = parsePrioridade(prioridade);
        List<TarefaResponseModel> visiveis = todas.stream()
                .filter(t -> siglaFiltro == null || siglaFiltro.equals(t.funcaoNistSigla()))
                .filter(t -> prioridadeFiltro == null || prioridadeFiltro == t.prioridade())
                .toList();

        model.addAttribute("pendentes", visiveis.stream().filter(t -> t.status().isPendenteOuBacklog()).toList());
        model.addAttribute("emAndamento", visiveis.stream().filter(t -> t.status() == StatusTarefa.EM_ANDAMENTO).toList());
        model.addAttribute("concluidas", visiveis.stream().filter(t -> t.status().isConcluido()).toList());
        model.addAttribute("canceladas", visiveis.stream().filter(t -> t.status() == StatusTarefa.CANCELADA).toList());

        model.addAttribute("tarefasPendentes", todas.stream().filter(t -> t.status().isPendenteOuBacklog()).count());
        model.addAttribute("tarefasEmAndamento", todas.stream().filter(t -> t.status() == StatusTarefa.EM_ANDAMENTO).count());
        model.addAttribute("tarefasConcluidas", todas.stream().filter(t -> t.status().isConcluido()).count());
        model.addAttribute("tarefasAtrasadas", todas.stream().filter(TarefaResponseModel::atrasada).count());

        model.addAttribute("funcoesNist", List.of(
                new FuncaoNistOpcao("GV", "Governar"),
                new FuncaoNistOpcao("ID", "Identificar"),
                new FuncaoNistOpcao("PR", "Proteger"),
                new FuncaoNistOpcao("DE", "Detectar"),
                new FuncaoNistOpcao("RS", "Responder"),
                new FuncaoNistOpcao("RC", "Recuperar")));
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("statusFormulario", List.of(
                StatusTarefa.BACKLOG, StatusTarefa.EM_ANDAMENTO, StatusTarefa.CONCLUIDO, StatusTarefa.CANCELADA));
        model.addAttribute("tarefaEmEdicaoId", tarefaEmEdicaoId);
        model.addAttribute("modalTarefaAberto", tarefaEmEdicaoId != null);
        model.addAttribute("iniciaisPorTarefa", todas.stream().collect(
                Collectors.toMap(TarefaResponseModel::id, t -> iniciais(t.responsavelNome()))));
        model.addAttribute("filtroFuncao", siglaFiltro);
        model.addAttribute("filtroPrioridade", prioridadeFiltro != null ? prioridadeFiltro.name() : null);
    }

    private static Prioridade parsePrioridade(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Prioridade.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /** Iniciais (até duas letras) do responsável para o avatar do card; "?" quando não há responsável. */
    private static String iniciais(String nome) {
        if (nome == null || nome.isBlank()) {
            return "?";
        }
        String[] partes = nome.trim().split("\\s+");
        String iniciais = partes.length > 1
                ? partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)
                : partes[0].substring(0, 1);
        return iniciais.toUpperCase();
    }

    private static TarefaRequestModel paraFormulario(TarefaResponseModel t) {
        StatusTarefa status = switch (t.status()) {
            case PENDENTE -> StatusTarefa.BACKLOG;
            case CONCLUIDA -> StatusTarefa.CONCLUIDO;
            default -> t.status();
        };
        return new TarefaRequestModel(t.titulo(), t.descricao(), t.funcaoNistSigla(), t.responsavelNome(),
                status, t.prioridade(), t.prazoLimite(), t.gapId(), t.empresaId());
    }

    /** Impede que um campo adulterado no navegador grave a tarefa em outra empresa. */
    private static TarefaRequestModel comEmpresaPadrao(TarefaRequestModel f) {
        return new TarefaRequestModel(f.titulo(), f.descricao(), f.funcaoNistSigla(), f.responsavelNome(),
                f.status(), f.prioridade(), f.prazoLimite(), f.gapId(), EMPRESA_PADRAO_ID);
    }
}
