package br.edu.securedesk.governanca.adapter.gateway;

import br.edu.securedesk.governanca.domain.Evidencia;
import br.edu.securedesk.governanca.domain.TarefaRemediacao;
import br.edu.securedesk.governanca.infrastructure.persistence.EvidenciaDataMapper;
import br.edu.securedesk.governanca.infrastructure.persistence.JpaEvidenciaRepository;
import br.edu.securedesk.governanca.infrastructure.persistence.JpaTarefaRepository;
import br.edu.securedesk.governanca.infrastructure.persistence.TarefaDataMapper;
import br.edu.securedesk.governanca.usecase.boundary.output.TarefaGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do Gateway de persistência para o agregado de Tarefas de Remediação e Evidências.
 * Converte dados entre os modelos de domínio (POJO) e os mappers de persistência JPA.
 * <p>
 * As evidências são persistidas exclusivamente por {@link #salvarEvidencia(Evidencia)}; o método
 * {@link #salvar(TarefaRemediacao)} grava apenas a tarefa, evitando duplicar anexos já existentes.
 */
@Component
public class TarefaGatewayImpl implements TarefaGateway {

    private final JpaTarefaRepository jpaTarefaRepository;
    private final JpaEvidenciaRepository jpaEvidenciaRepository;

    public TarefaGatewayImpl(JpaTarefaRepository jpaTarefaRepository,
                             JpaEvidenciaRepository jpaEvidenciaRepository) {
        this.jpaTarefaRepository = jpaTarefaRepository;
        this.jpaEvidenciaRepository = jpaEvidenciaRepository;
    }

    @Override
    public TarefaRemediacao salvar(TarefaRemediacao tarefa) {
        TarefaDataMapper salvo = jpaTarefaRepository.save(toDataMapper(tarefa));
        return toDomain(salvo, listarEvidenciasPorTarefa(salvo.getId()));
    }

    @Override
    public Optional<TarefaRemediacao> buscarPorId(Long id) {
        return jpaTarefaRepository.findById(id)
                .map(mapper -> toDomain(mapper, listarEvidenciasPorTarefa(mapper.getId())));
    }

    @Override
    public List<TarefaRemediacao> listarPorEmpresa(Long empresaId) {
        List<TarefaDataMapper> tarefas = jpaTarefaRepository.findByEmpresaIdOrderByIdDesc(empresaId);
        if (tarefas.isEmpty()) {
            return List.of();
        }
        List<Long> ids = tarefas.stream().map(TarefaDataMapper::getId).toList();
        Map<Long, List<Evidencia>> evidenciasPorTarefa = jpaEvidenciaRepository
                .findByTarefaIdInOrderByDataUploadAscIdAsc(ids)
                .stream()
                .map(TarefaGatewayImpl::toDomain)
                .collect(Collectors.groupingBy(Evidencia::getTarefaId));
        return tarefas.stream()
                .map(mapper -> toDomain(mapper, evidenciasPorTarefa.getOrDefault(mapper.getId(), List.of())))
                .toList();
    }

    @Override
    public Evidencia salvarEvidencia(Evidencia evidencia) {
        return toDomain(jpaEvidenciaRepository.save(toDataMapper(evidencia)));
    }

    @Override
    public List<Evidencia> listarEvidenciasPorTarefa(Long tarefaId) {
        return jpaEvidenciaRepository.findByTarefaIdOrderByDataUploadAscIdAsc(tarefaId)
                .stream()
                .map(TarefaGatewayImpl::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void removerPorId(Long id) {
        jpaEvidenciaRepository.deleteByTarefaId(id);
        jpaTarefaRepository.deleteById(id);
    }

    public static TarefaRemediacao toDomain(TarefaDataMapper mapper, List<Evidencia> evidencias) {
        if (mapper == null) {
            return null;
        }
        return new TarefaRemediacao(
                mapper.getId(),
                mapper.getEmpresaId(),
                mapper.getGapId(),
                mapper.getFuncaoNistSigla(),
                mapper.getTitulo(),
                mapper.getDescricao(),
                mapper.getResponsavelNome(),
                mapper.getStatus(),
                mapper.getPrioridade(),
                mapper.getPrazoLimite(),
                mapper.getDataConclusao(),
                mapper.getDataCriacao(),
                evidencias
        );
    }

    public static TarefaDataMapper toDataMapper(TarefaRemediacao domain) {
        if (domain == null) {
            return null;
        }
        return TarefaDataMapper.builder()
                .id(domain.getId())
                .empresaId(domain.getEmpresaId())
                .gapId(domain.getGapId())
                .funcaoNistSigla(domain.getFuncaoNistSigla())
                .titulo(domain.getTitulo())
                .descricao(domain.getDescricao())
                .responsavelNome(domain.getResponsavelNome())
                .status(domain.getStatus())
                .prioridade(domain.getPrioridade())
                .prazoLimite(domain.getPrazoLimite())
                .dataConclusao(domain.getDataConclusao())
                .dataCriacao(domain.getDataCriacao())
                .build();
    }

    public static Evidencia toDomain(EvidenciaDataMapper mapper) {
        if (mapper == null) {
            return null;
        }
        return new Evidencia(
                mapper.getId(),
                mapper.getTarefaId(),
                mapper.getNomeArquivo(),
                mapper.getTipoArquivo(),
                mapper.getTamanhoBytes(),
                mapper.getCaminhoArquivo(),
                mapper.getDescricao(),
                mapper.getAutorUpload(),
                mapper.getDataUpload()
        );
    }

    public static EvidenciaDataMapper toDataMapper(Evidencia domain) {
        if (domain == null) {
            return null;
        }
        return EvidenciaDataMapper.builder()
                .id(domain.getId())
                .tarefaId(domain.getTarefaId())
                .nomeArquivo(domain.getNomeArquivo())
                .tipoArquivo(domain.getTipoArquivo())
                .tamanhoBytes(domain.getTamanhoBytes())
                .caminhoArquivo(domain.getCaminhoArquivo())
                .descricao(domain.getDescricao())
                .autorUpload(domain.getAutorUpload())
                .dataUpload(domain.getDataUpload())
                .build();
    }
}
