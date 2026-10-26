package br.edu.securedesk.ativo.adapter.controller;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.domain.LimiteAtivosExcedidoException;
import br.edu.securedesk.ativo.domain.TipoAtivo;
import br.edu.securedesk.ativo.usecase.boundary.input.AtivoInputBoundary;
import br.edu.securedesk.ativo.usecase.model.AtivoRequestModel;
import br.edu.securedesk.ativo.usecase.model.AtivoResponseModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller especializado da camada de Interface Adapters para o gerenciamento
 * do Inventário de Ativos de TI (/ativos), respeitando a Clean Architecture.
 */
@Controller
@RequestMapping("/ativos")
public class AtivoController {

    private static final Long EMPRESA_PADRAO_ID = 1L;

    private final AtivoInputBoundary ativoInputBoundary;

    public AtivoController(AtivoInputBoundary ativoInputBoundary) {
        this.ativoInputBoundary = ativoInputBoundary;
    }

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("ativoForm")) {
            model.addAttribute("ativoForm", AtivoRequestModel.padrao(EMPRESA_PADRAO_ID));
        }
        popularModelInventario(model, null);
        return "ativo/ativos";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("ativoForm", AtivoRequestModel.padrao(EMPRESA_PADRAO_ID));
        popularModelInventario(model, null);
        return "ativo/ativos";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AtivoResponseModel ativo = ativoInputBoundary.buscarPorId(id);
            model.addAttribute("ativoForm", AtivoRequestModel.fromResponse(ativo));
            popularModelInventario(model, id);
            return "ativo/ativos";
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/ativos";
        }
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("ativoForm") AtivoRequestModel ativoForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelInventario(model, null);
            return "ativo/ativos";
        }

        try {
            AtivoResponseModel salvo = ativoInputBoundary.cadastrar(ativoForm);
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Ativo '%s' (%d un.) cadastrado com sucesso!", salvo.nome(), salvo.quantidade()));
            return "redirect:/ativos";
        } catch (LimiteAtivosExcedidoException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            redirectAttributes.addFlashAttribute("ativoForm", ativoForm);
            return "redirect:/ativos";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("ativoForm") AtivoRequestModel ativoForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelInventario(model, id);
            return "ativo/ativos";
        }

        try {
            AtivoResponseModel atualizado = ativoInputBoundary.atualizar(id, ativoForm);
            redirectAttributes.addFlashAttribute("sucesso",
                    String.format("Ativo '%s' atualizado com sucesso!", atualizado.nome()));
            return "redirect:/ativos";
        } catch (LimiteAtivosExcedidoException | IllegalArgumentException | NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            redirectAttributes.addFlashAttribute("ativoForm", ativoForm);
            return "redirect:/ativos";
        }
    }

    @PostMapping("/{id}/inativar")
    public String inativar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ativoInputBoundary.inativar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Ativo inativado com sucesso! Cota liberada no parque.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/ativos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ativoInputBoundary.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Ativo removido do inventário com sucesso!");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/ativos";
    }

    private void popularModelInventario(Model model, Long ativoEmEdicaoId) {
        List<AtivoResponseModel> ativos = ativoInputBoundary.listarPorEmpresa(EMPRESA_PADRAO_ID);
        int totalAtivos = ativoInputBoundary.consultarTotalAtivosPorEmpresa(EMPRESA_PADRAO_ID);
        int limiteAtivos = Ativo.LIMITE_MAXIMO_PARQUE;
        int cotaDisponivel = Math.max(0, limiteAtivos - totalAtivos);

        model.addAttribute("ativos", ativos);
        model.addAttribute("tiposAtivo", new TipoAtivo[]{
                TipoAtivo.ENDPOINTS,
                TipoAtivo.SERVIDORES_LOCAIS,
                TipoAtivo.ATIVOS_REDE,
                TipoAtivo.MOBILE_E_NUVEM
        });
        model.addAttribute("ativoEmEdicaoId", ativoEmEdicaoId);
        model.addAttribute("totalAtivos", totalAtivos);
        model.addAttribute("limiteAtivos", limiteAtivos);
        model.addAttribute("cotaDisponivel", cotaDisponivel);
        model.addAttribute("ativosTexto", totalAtivos + "/" + limiteAtivos);
        model.addAttribute("percentualOcupacao", Math.min(100, (totalAtivos * 100) / limiteAtivos));
        model.addAttribute("trava100Ativos",
                String.format("%d de %d ativos gerenciados (%d disponíveis na cota da PME)",
                        totalAtivos, limiteAtivos, cotaDisponivel));

        // Dados de cabeçalho/sidebar da PME
        model.addAttribute("empresaNome", "InnovaTech Soluções Ltda.");
        model.addAttribute("usuarioNome", "Gabriel Andrew");
        model.addAttribute("usuarioPerfil", "Administrador");
        model.addAttribute("usuarioAvatar", "GA");
        model.addAttribute("totalGaps", 5);
    }
}
