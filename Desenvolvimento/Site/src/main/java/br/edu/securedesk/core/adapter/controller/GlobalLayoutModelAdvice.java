package br.edu.securedesk.core.adapter.controller;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.usecase.boundary.input.AtivoInputBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Popula globalmente os atributos de cota de ativos de TI da PME em todas as páginas Thymeleaf,
 * garantindo que o badge do menu lateral e os cabeçalhos sempre exibam o total real persistido no banco.
 */
@ControllerAdvice
public class GlobalLayoutModelAdvice {

    private static final Long EMPRESA_PADRAO_ID = 1L;

    private final AtivoInputBoundary ativoInputBoundary;

    public GlobalLayoutModelAdvice(@Autowired(required = false) AtivoInputBoundary ativoInputBoundary) {
        this.ativoInputBoundary = ativoInputBoundary;
    }

    @ModelAttribute
    public void popularCotaAtivosReal(Model model) {
        int totalAtivos = ativoInputBoundary != null
                ? ativoInputBoundary.consultarTotalAtivosPorEmpresa(EMPRESA_PADRAO_ID)
                : 0;
        int limiteAtivos = Ativo.LIMITE_MAXIMO_PARQUE;
        int cotaDisponivel = Math.max(0, limiteAtivos - totalAtivos);

        model.addAttribute("totalAtivos", totalAtivos);
        model.addAttribute("limiteAtivos", limiteAtivos);
        model.addAttribute("cotaDisponivel", cotaDisponivel);
        model.addAttribute("ativosTexto", totalAtivos + "/" + limiteAtivos);
        model.addAttribute("trava100Ativos",
                String.format("%d de %d ativos gerenciados (%d disponíveis na cota da PME)",
                        totalAtivos, limiteAtivos, cotaDisponivel));
    }
}
