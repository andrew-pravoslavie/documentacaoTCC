package br.edu.securedesk.compliance.usecase.interactor;

import br.edu.securedesk.compliance.domain.HistoricoRevisao;
import br.edu.securedesk.compliance.domain.PoliticaEmpresa;
import br.edu.securedesk.compliance.domain.StatusPolitica;
import br.edu.securedesk.compliance.usecase.boundary.input.PoliticaInputBoundary;
import br.edu.securedesk.compliance.usecase.boundary.output.PoliticaGateway;
import br.edu.securedesk.compliance.usecase.model.PoliticaRequestModel;
import br.edu.securedesk.compliance.usecase.model.PoliticaResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Use Case Interactor para Adoção, Customização e Versionamento de Políticas de Segurança (CRUD 4).
 * Suporta os templates base do OED 2 (PSI, Controle de Acesso e Backup 3-2-1) exigidos para atingir o MIL 2.
 */
@Service
public class AdotarPoliticaInteractor implements PoliticaInputBoundary {

    private final PoliticaGateway politicaGateway;

    public AdotarPoliticaInteractor(PoliticaGateway politicaGateway) {
        this.politicaGateway = politicaGateway;
    }

    @Override
    @Transactional
    public PoliticaResponseModel adotarOuCriar(PoliticaRequestModel request) {
        LocalDate hoje = LocalDate.now();
        int meses = request.periodicidadeMesesRevisao() != null ? request.periodicidadeMesesRevisao() : 6;
        StatusPolitica statusInicial = request.status() != null ? request.status() : StatusPolitica.EM_DIA;
        LocalDate dataAprovacao = (statusInicial == StatusPolitica.EM_DIA) ? hoje : null;

        PoliticaEmpresa nova = new PoliticaEmpresa(
                null,
                request.empresaId(),
                request.codigoTemplateOrigem(),
                request.titulo(),
                request.normaReferencia(),
                request.funcaoNistSigla(),
                request.conteudoCustomizado(),
                "1.0",
                statusInicial,
                dataAprovacao,
                hoje,
                meses,
                hoje.plusMonths(meses),
                List.of()
        );

        PoliticaEmpresa salva = politicaGateway.salvar(nova);

        HistoricoRevisao revInicial = new HistoricoRevisao(
                null,
                salva.getId(),
                "1.0",
                request.revisorNome(),
                request.observacoesRevisao() != null && !request.observacoesRevisao().isBlank()
                        ? request.observacoesRevisao()
                        : "Versão inicial adotada pela PME",
                LocalDateTime.now()
        );
        HistoricoRevisao revSalva = politicaGateway.salvarRevisao(revInicial);
        salva.getHistoricoRevisoes().add(revSalva);

        return PoliticaResponseModel.fromDomain(salva);
    }

    @Override
    @Transactional
    public PoliticaResponseModel adotarTemplatePadrao(String codigoTemplate, Long empresaId) {
        String codigo = codigoTemplate != null ? codigoTemplate.trim().toUpperCase() : "PSI";
        PoliticaRequestModel templateReq = switch (codigo) {
            case "ACESSO" -> new PoliticaRequestModel(
                    "Política de Controle de Acesso e Credenciais",
                    "ACESSO",
                    "ABNT NBR ISO/IEC 27002:2022 (5.15, 8.5) & LGPD",
                    "PR",
                    "1. OBJETIVO\nDefinir regras de menor privilégio, autenticação multifator (MFA) e revisão periódica de acessos nos ativos da PME.\n\n2. DIRETRIZES\n- Todo acesso administrativo requer MFA.\n- Contas de ex-colaboradores devem ser revogadas imediatamente.",
                    StatusPolitica.EM_DIA,
                    6,
                    "Gabriel Andrew",
                    "Adoção do Template Oficial de Controle de Acesso (OED 2)",
                    empresaId
            );
            case "BACKUP" -> new PoliticaRequestModel(
                    "Política de Backup e Recuperação (Regra 3-2-1)",
                    "BACKUP",
                    "ABNT NBR ISO/IEC 27002:2022 (8.13) & NIST RC",
                    "RC",
                    "1. OBJETIVO\nEstabelecer a estratégia de Backup 3-2-1 para proteger dados críticos da PME contra ransomware e desastres.\n\n2. DIRETRIZES\n- Manter 3 cópias dos dados, em 2 mídias distintas, sendo 1 cópia offsite/nuvem imutável.\n- Realizar teste trimestral de restauração.",
                    StatusPolitica.EM_DIA,
                    6,
                    "Gabriel Andrew",
                    "Adoção do Template Oficial de Backup 3-2-1 (OED 2)",
                    empresaId
            );
            default -> new PoliticaRequestModel(
                    "Política de Segurança da Informação (PSI)",
                    "PSI",
                    "ABNT NBR ISO/IEC 27001:2022 (5.2) & LGPD",
                    "GV",
                    "1. OBJETIVO\nInstituir as diretrizes corporativas de Segurança da Informação e Proteção de Dados (LGPD) da PME.\n\n2. DIRETRIZES\n- Proteger a confidencialidade, integridade e disponibilidade dos ativos.\n- Cumprir o patamar mínimo recomendado MIL 2.",
                    StatusPolitica.EM_DIA,
                    6,
                    "Gabriel Andrew",
                    "Adoção do Template Oficial de PSI (OED 2)",
                    empresaId
            );
        };
        return adotarOuCriar(templateReq);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoliticaResponseModel> listarPorEmpresa(Long empresaId) {
        return politicaGateway.listarPorEmpresa(empresaId)
                .stream()
                .peek(PoliticaEmpresa::avaliarVencimento)
                .map(PoliticaResponseModel::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PoliticaResponseModel buscarPorId(Long id) {
        PoliticaEmpresa politica = politicaGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Política não encontrada com id: " + id));
        politica.avaliarVencimento();
        return PoliticaResponseModel.fromDomain(politica);
    }

    @Override
    @Transactional
    public PoliticaResponseModel revisarEAtualizar(Long id, PoliticaRequestModel request) {
        PoliticaEmpresa existente = politicaGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Política não encontrada com id: " + id));

        existente.setTitulo(request.titulo());
        if (request.normaReferencia() != null && !request.normaReferencia().isBlank()) {
            existente.setNormaReferencia(request.normaReferencia());
        }
        if (request.funcaoNistSigla() != null && !request.funcaoNistSigla().isBlank()) {
            existente.setFuncaoNistSigla(request.funcaoNistSigla().toUpperCase());
        }
        if (request.periodicidadeMesesRevisao() != null) {
            existente.setPeriodicidadeMesesRevisao(request.periodicidadeMesesRevisao());
        }

        HistoricoRevisao novaRevisao = existente.registrarNovaRevisao(
                request.conteudoCustomizado(),
                request.revisorNome(),
                request.observacoesRevisao()
        );

        if (request.status() != null) {
            existente.setStatus(request.status());
        }
        existente.validarInvariantes();

        PoliticaEmpresa salva = politicaGateway.salvar(existente);
        HistoricoRevisao revSalva = politicaGateway.salvarRevisao(novaRevisao);
        if (salva.getHistoricoRevisoes().stream().noneMatch(r -> Objects.equals(r.getVersao(), revSalva.getVersao()))) {
            salva.getHistoricoRevisoes().add(revSalva);
        }

        return PoliticaResponseModel.fromDomain(salva);
    }

    @Override
    @Transactional
    public void revogarOuExcluir(Long id) {
        if (politicaGateway.buscarPorId(id).isEmpty()) {
            throw new NoSuchElementException("Política não encontrada com id: " + id);
        }
        politicaGateway.removerPorId(id);
    }
}
