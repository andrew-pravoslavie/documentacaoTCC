package br.edu.securedesk.compliance.usecase.interactor;

import br.edu.securedesk.compliance.domain.HistoricoRevisao;
import br.edu.securedesk.compliance.domain.PoliticaEmpresa;
import br.edu.securedesk.compliance.domain.StatusPolitica;
import br.edu.securedesk.compliance.usecase.boundary.output.PoliticaGateway;
import br.edu.securedesk.compliance.usecase.model.PoliticaRequestModel;
import br.edu.securedesk.compliance.usecase.model.PoliticaResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários puros JUnit 5 para {@link AdotarPoliticaInteractor} (CRUD 4 - Compliance e Políticas).
 * Não utiliza @SpringBootTest nem banco de dados (execução em memória).
 */
class AdotarPoliticaInteractorTest {

    private AdotarPoliticaInteractor interactor;
    private InMemoryPoliticaGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new InMemoryPoliticaGateway();
        interactor = new AdotarPoliticaInteractor(gateway);
    }

    @Test
    @DisplayName("1) Deve adotar/criar nova política na versão 1.0 e registrar revisão inicial")
    void deveAdotarPoliticaVersaoInicial() {
        PoliticaRequestModel request = new PoliticaRequestModel(
                "Política de Uso Aceitável",
                "CUSTOM",
                "ISO/IEC 27002:2022 (5.10)",
                "PR",
                "1. OBJETIVO\nDefinir o uso aceitável das estações de trabalho da PME.",
                StatusPolitica.EM_DIA,
                6,
                "Lucas Oliveira",
                "Homologação inicial pela diretoria",
                1L
        );

        PoliticaResponseModel response = interactor.adotarOuCriar(request);

        assertNotNull(response.id());
        assertEquals("1.0", response.versaoAtual());
        assertEquals(StatusPolitica.EM_DIA, response.status());
        assertEquals(1, response.historicoRevisoes().size());
        assertEquals("Lucas Oliveira", response.historicoRevisoes().getFirst().revisorNome());
    }

    @Test
    @DisplayName("2) Deve adotar os 3 templates oficiais do OED 2 (PSI, ACESSO, BACKUP)")
    void deveAdotarTemplatesOficiaisOed2() {
        PoliticaResponseModel psi = interactor.adotarTemplatePadrao("PSI", 1L);
        PoliticaResponseModel acesso = interactor.adotarTemplatePadrao("ACESSO", 1L);
        PoliticaResponseModel backup = interactor.adotarTemplatePadrao("BACKUP", 1L);

        assertEquals("GV", psi.funcaoNistSigla());
        assertEquals("PR", acesso.funcaoNistSigla());
        assertEquals("RC", backup.funcaoNistSigla());
        assertEquals(3, interactor.listarPorEmpresa(1L).size());
    }

    @Test
    @DisplayName("3) Deve revisar política existente incrementando versão (1.0 -> 1.1) e atualizando status para EM_DIA")
    void deveRevisarPoliticaIncrementandoVersao() {
        PoliticaResponseModel criada = interactor.adotarTemplatePadrao("PSI", 1L);

        PoliticaRequestModel revisaoReq = new PoliticaRequestModel(
                criada.titulo(),
                criada.codigoTemplateOrigem(),
                criada.normaReferencia(),
                criada.funcaoNistSigla(),
                criada.conteudoCustomizado() + "\n3. PENALIDADES\nDescumprimento sujeito a medidas administrativas.",
                StatusPolitica.EM_DIA,
                12,
                "Gabriel Andrew",
                "Inclusão da cláusula de penalidades",
                1L
        );

        PoliticaResponseModel revisada = interactor.revisarEAtualizar(criada.id(), revisaoReq);

        assertEquals("1.1", revisada.versaoAtual());
        assertEquals(StatusPolitica.EM_DIA, revisada.status());
        assertEquals(12, revisada.periodicidadeMesesRevisao());
        assertEquals(2, revisada.historicoRevisoes().size());
    }

    @Test
    @DisplayName("4) Deve revogar/excluir política existente com sucesso")
    void deveRevogarPolitica() {
        PoliticaResponseModel criada = interactor.adotarTemplatePadrao("BACKUP", 1L);
        interactor.revogarOuExcluir(criada.id());

        assertTrue(interactor.listarPorEmpresa(1L).isEmpty());
    }

    private static class InMemoryPoliticaGateway implements PoliticaGateway {
        private final Map<Long, PoliticaEmpresa> store = new LinkedHashMap<>();
        private final List<HistoricoRevisao> revisoes = new ArrayList<>();
        private final AtomicLong idSeq = new AtomicLong(1);
        private final AtomicLong revSeq = new AtomicLong(1);

        @Override
        public PoliticaEmpresa salvar(PoliticaEmpresa politica) {
            if (politica.getId() == null) {
                politica.setId(idSeq.getAndIncrement());
            }
            store.put(politica.getId(), politica);
            return politica;
        }

        @Override
        public Optional<PoliticaEmpresa> buscarPorId(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<PoliticaEmpresa> listarPorEmpresa(Long empresaId) {
            return store.values().stream()
                    .filter(p -> Objects.equals(p.getEmpresaId(), empresaId))
                    .toList();
        }

        @Override
        public HistoricoRevisao salvarRevisao(HistoricoRevisao revisao) {
            if (revisao.getId() == null) {
                revisao.setId(revSeq.getAndIncrement());
            }
            revisoes.add(revisao);
            return revisao;
        }

        @Override
        public List<HistoricoRevisao> listarRevisoesPorPolitica(Long politicaEmpresaId) {
            return revisoes.stream()
                    .filter(r -> Objects.equals(r.getPoliticaEmpresaId(), politicaEmpresaId))
                    .toList();
        }

        @Override
        public void removerPorId(Long id) {
            store.remove(id);
        }
    }
}
