package br.edu.securedesk.compliance.adapter.gateway;

import br.edu.securedesk.compliance.domain.HistoricoRevisao;
import br.edu.securedesk.compliance.domain.PoliticaEmpresa;
import br.edu.securedesk.compliance.infrastructure.persistence.HistoricoRevisaoDataMapper;
import br.edu.securedesk.compliance.infrastructure.persistence.JpaHistoricoRevisaoRepository;
import br.edu.securedesk.compliance.infrastructure.persistence.JpaPoliticaRepository;
import br.edu.securedesk.compliance.infrastructure.persistence.PoliticaDataMapper;
import br.edu.securedesk.compliance.usecase.boundary.output.PoliticaGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do Gateway de persistência para o agregado de Políticas e Histórico de Revisões.
 * Converte dados entre os modelos de domínio (POJO) e os mappers de persistência JPA.
 * <p>
 * O histórico é persistido exclusivamente por {@link #salvarRevisao(HistoricoRevisao)}; o método
 * {@link #salvar(PoliticaEmpresa)} grava apenas a política, evitando duplicar revisões já registradas.
 */
@Component
public class PoliticaGatewayImpl implements PoliticaGateway {

    private final JpaPoliticaRepository jpaPoliticaRepository;
    private final JpaHistoricoRevisaoRepository jpaHistoricoRevisaoRepository;

    public PoliticaGatewayImpl(JpaPoliticaRepository jpaPoliticaRepository,
                               JpaHistoricoRevisaoRepository jpaHistoricoRevisaoRepository) {
        this.jpaPoliticaRepository = jpaPoliticaRepository;
        this.jpaHistoricoRevisaoRepository = jpaHistoricoRevisaoRepository;
    }

    @Override
    public PoliticaEmpresa salvar(PoliticaEmpresa politica) {
        PoliticaDataMapper salvo = jpaPoliticaRepository.save(toDataMapper(politica));
        return toDomain(salvo, listarRevisoesPorPolitica(salvo.getId()));
    }

    @Override
    public Optional<PoliticaEmpresa> buscarPorId(Long id) {
        return jpaPoliticaRepository.findById(id)
                .map(mapper -> toDomain(mapper, listarRevisoesPorPolitica(mapper.getId())));
    }

    @Override
    public List<PoliticaEmpresa> listarPorEmpresa(Long empresaId) {
        List<PoliticaDataMapper> politicas = jpaPoliticaRepository.findByEmpresaIdOrderByTituloAsc(empresaId);
        if (politicas.isEmpty()) {
            return List.of();
        }
        List<Long> ids = politicas.stream().map(PoliticaDataMapper::getId).toList();
        Map<Long, List<HistoricoRevisao>> revisoesPorPolitica = jpaHistoricoRevisaoRepository
                .findByPoliticaEmpresaIdInOrderByDataRevisaoAscIdAsc(ids)
                .stream()
                .map(PoliticaGatewayImpl::toDomain)
                .collect(Collectors.groupingBy(HistoricoRevisao::getPoliticaEmpresaId));
        return politicas.stream()
                .map(mapper -> toDomain(mapper, revisoesPorPolitica.getOrDefault(mapper.getId(), List.of())))
                .toList();
    }

    @Override
    public HistoricoRevisao salvarRevisao(HistoricoRevisao revisao) {
        return toDomain(jpaHistoricoRevisaoRepository.save(toDataMapper(revisao)));
    }

    @Override
    public List<HistoricoRevisao> listarRevisoesPorPolitica(Long politicaEmpresaId) {
        return jpaHistoricoRevisaoRepository.findByPoliticaEmpresaIdOrderByDataRevisaoAscIdAsc(politicaEmpresaId)
                .stream()
                .map(PoliticaGatewayImpl::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void removerPorId(Long id) {
        jpaHistoricoRevisaoRepository.deleteByPoliticaEmpresaId(id);
        jpaPoliticaRepository.deleteById(id);
    }

    public static PoliticaEmpresa toDomain(PoliticaDataMapper mapper, List<HistoricoRevisao> historico) {
        if (mapper == null) {
            return null;
        }
        return new PoliticaEmpresa(
                mapper.getId(),
                mapper.getEmpresaId(),
                mapper.getCodigoTemplateOrigem(),
                mapper.getTitulo(),
                mapper.getNormaReferencia(),
                mapper.getFuncaoNistSigla(),
                mapper.getConteudoCustomizado(),
                mapper.getVersaoAtual(),
                mapper.getStatus(),
                mapper.getDataAprovacao(),
                mapper.getDataUltimaRevisao(),
                mapper.getPeriodicidadeMesesRevisao(),
                mapper.getProximaRevisao(),
                historico
        );
    }

    public static PoliticaDataMapper toDataMapper(PoliticaEmpresa domain) {
        if (domain == null) {
            return null;
        }
        return PoliticaDataMapper.builder()
                .id(domain.getId())
                .empresaId(domain.getEmpresaId())
                .codigoTemplateOrigem(domain.getCodigoTemplateOrigem())
                .titulo(domain.getTitulo())
                .normaReferencia(domain.getNormaReferencia())
                .funcaoNistSigla(domain.getFuncaoNistSigla())
                .conteudoCustomizado(domain.getConteudoCustomizado())
                .versaoAtual(domain.getVersaoAtual())
                .status(domain.getStatus())
                .dataAprovacao(domain.getDataAprovacao())
                .dataUltimaRevisao(domain.getDataUltimaRevisao())
                .periodicidadeMesesRevisao(domain.getPeriodicidadeMesesRevisao())
                .proximaRevisao(domain.getProximaRevisao())
                .build();
    }

    public static HistoricoRevisao toDomain(HistoricoRevisaoDataMapper mapper) {
        if (mapper == null) {
            return null;
        }
        return new HistoricoRevisao(
                mapper.getId(),
                mapper.getPoliticaEmpresaId(),
                mapper.getVersao(),
                mapper.getRevisorNome(),
                mapper.getObservacoes(),
                mapper.getDataRevisao()
        );
    }

    public static HistoricoRevisaoDataMapper toDataMapper(HistoricoRevisao domain) {
        if (domain == null) {
            return null;
        }
        return HistoricoRevisaoDataMapper.builder()
                .id(domain.getId())
                .politicaEmpresaId(domain.getPoliticaEmpresaId())
                .versao(domain.getVersao())
                .revisorNome(domain.getRevisorNome())
                .observacoes(domain.getObservacoes())
                .dataRevisao(domain.getDataRevisao())
                .build();
    }
}
