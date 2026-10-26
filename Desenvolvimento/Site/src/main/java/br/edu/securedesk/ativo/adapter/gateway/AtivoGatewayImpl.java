package br.edu.securedesk.ativo.adapter.gateway;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.infrastructure.persistence.AtivoDataMapper;
import br.edu.securedesk.ativo.infrastructure.persistence.JpaAtivoRepository;
import br.edu.securedesk.ativo.usecase.boundary.output.AtivoGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do Gateway de persistência para o agregado de Ativos.
 * Converte dados entre os modelos de domínio (POJO) e os mappers de persistência JPA.
 */
@Component
public class AtivoGatewayImpl implements AtivoGateway {

    private final JpaAtivoRepository jpaAtivoRepository;

    public AtivoGatewayImpl(JpaAtivoRepository jpaAtivoRepository) {
        this.jpaAtivoRepository = jpaAtivoRepository;
    }

    @Override
    public Ativo salvar(Ativo ativo) {
        AtivoDataMapper mapper = toDataMapper(ativo);
        if (mapper.getId() != null && mapper.getDataCadastro() == null) {
            jpaAtivoRepository.findById(mapper.getId())
                    .ifPresent(existing -> mapper.setDataCadastro(existing.getDataCadastro()));
        }
        AtivoDataMapper salvo = jpaAtivoRepository.save(mapper);
        return toDomain(salvo);
    }

    @Override
    public Optional<Ativo> buscarPorId(Long id) {
        return jpaAtivoRepository.findById(id).map(AtivoGatewayImpl::toDomain);
    }

    @Override
    public List<Ativo> listarPorEmpresa(Long empresaId) {
        return jpaAtivoRepository.findByEmpresaIdOrderByIdDesc(empresaId)
                .stream()
                .map(AtivoGatewayImpl::toDomain)
                .toList();
    }

    @Override
    public int somarQuantidadeAtivosPorEmpresa(Long empresaId) {
        Integer total = jpaAtivoRepository.somarQuantidadeAtivosPorEmpresa(empresaId);
        return total != null ? total : 0;
    }

    @Override
    public void removerPorId(Long id) {
        jpaAtivoRepository.deleteById(id);
    }

    public static Ativo toDomain(AtivoDataMapper mapper) {
        if (mapper == null) {
            return null;
        }
        return new Ativo(
                mapper.getId(),
                mapper.getNome(),
                mapper.getTipo(),
                mapper.getDescricao(),
                mapper.getQuantidade(),
                mapper.getLocalizacao(),
                mapper.getResponsavel(),
                mapper.isAtivo(),
                mapper.getEmpresaId()
        );
    }

    public static AtivoDataMapper toDataMapper(Ativo domain) {
        if (domain == null) {
            return null;
        }
        return AtivoDataMapper.builder()
                .id(domain.getId())
                .empresaId(domain.getEmpresaId())
                .nome(domain.getNome())
                .tipo(domain.getTipo())
                .descricao(domain.getDescricao())
                .quantidade(domain.getQuantidade() != null ? domain.getQuantidade() : 1)
                .localizacao(domain.getLocalizacao())
                .responsavel(domain.getResponsavel())
                .ativo(domain.isAtivo())
                .build();
    }
}
