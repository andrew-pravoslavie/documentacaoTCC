package br.edu.securedesk.ativo.usecase.interactor;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.domain.LimiteAtivosExcedidoException;
import br.edu.securedesk.ativo.usecase.boundary.input.AtivoInputBoundary;
import br.edu.securedesk.ativo.usecase.boundary.output.AtivoGateway;
import br.edu.securedesk.ativo.usecase.model.AtivoRequestModel;
import br.edu.securedesk.ativo.usecase.model.AtivoResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Use Case Interactor responsável por orquestrar o CRUD de Ativos de TI
 * e impor a Regra de Negócio RN01 (Trava Estrita de até 100 ativos ativos por PME).
 */
@Service
public class AtivoInteractor implements AtivoInputBoundary {

    private final AtivoGateway ativoGateway;

    public AtivoInteractor(AtivoGateway ativoGateway) {
        this.ativoGateway = ativoGateway;
    }

    @Override
    @Transactional
    public AtivoResponseModel cadastrar(AtivoRequestModel request) {
        boolean isAtivo = request.ativo() == null || request.ativo();
        Ativo novoAtivo = new Ativo(
                null,
                request.nome(),
                request.tipo(),
                request.descricao(),
                request.quantidade(),
                request.localizacao(),
                request.responsavel(),
                isAtivo,
                request.empresaId()
        );

        if (novoAtivo.isAtivo()) {
            int totalAtual = ativoGateway.somarQuantidadeAtivosPorEmpresa(novoAtivo.getEmpresaId());
            validarTrava100Ativos(totalAtual, novoAtivo.getQuantidade());
        }

        Ativo salvo = ativoGateway.salvar(novoAtivo);
        return AtivoResponseModel.fromDomain(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponseModel> listarPorEmpresa(Long empresaId) {
        return ativoGateway.listarPorEmpresa(empresaId)
                .stream()
                .map(AtivoResponseModel::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AtivoResponseModel buscarPorId(Long id) {
        Ativo ativo = ativoGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Ativo de TI não encontrado com id: " + id));
        return AtivoResponseModel.fromDomain(ativo);
    }

    @Override
    @Transactional
    public AtivoResponseModel atualizar(Long id, AtivoRequestModel request) {
        Ativo existente = ativoGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Ativo de TI não encontrado com id: " + id));

        boolean novoStatusAtivo = request.ativo() != null ? request.ativo() : existente.isAtivo();
        Long empresaId = request.empresaId() != null ? request.empresaId() : existente.getEmpresaId();

        Ativo atualizado = new Ativo(
                existente.getId(),
                request.nome(),
                request.tipo(),
                request.descricao(),
                request.quantidade(),
                request.localizacao(),
                request.responsavel(),
                novoStatusAtivo,
                empresaId
        );

        if (atualizado.isAtivo()) {
            int totalAtual = ativoGateway.somarQuantidadeAtivosPorEmpresa(empresaId);
            int baseSemEsteRegistro = (existente.isAtivo() && empresaId.equals(existente.getEmpresaId()))
                    ? Math.max(0, totalAtual - existente.getQuantidade())
                    : totalAtual;
            validarTrava100Ativos(baseSemEsteRegistro, atualizado.getQuantidade());
        }

        Ativo salvo = ativoGateway.salvar(atualizado);
        return AtivoResponseModel.fromDomain(salvo);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        Ativo existente = ativoGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Ativo de TI não encontrado com id: " + id));
        existente.inativar();
        ativoGateway.salvar(existente);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (ativoGateway.buscarPorId(id).isEmpty()) {
            throw new NoSuchElementException("Ativo de TI não encontrado com id: " + id);
        }
        ativoGateway.removerPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int consultarTotalAtivosPorEmpresa(Long empresaId) {
        return ativoGateway.somarQuantidadeAtivosPorEmpresa(empresaId);
    }

    private void validarTrava100Ativos(int quantidadeAtualNoParque, int quantidadeASomar) {
        if (quantidadeAtualNoParque + quantidadeASomar > Ativo.LIMITE_MAXIMO_PARQUE) {
            throw new LimiteAtivosExcedidoException(
                    quantidadeAtualNoParque,
                    quantidadeASomar,
                    Ativo.LIMITE_MAXIMO_PARQUE
            );
        }
    }
}
