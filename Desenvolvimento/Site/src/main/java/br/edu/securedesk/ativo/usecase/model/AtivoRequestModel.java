package br.edu.securedesk.ativo.usecase.model;

import br.edu.securedesk.ativo.domain.TipoAtivo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record de entrada (Request Model) da fronteira de casos de uso para criação e atualização de Ativos.
 * Inclui getters padrão JavaBean para compatibilidade total com th:object / th:field do Thymeleaf.
 */
public record AtivoRequestModel(
        @NotBlank(message = "O nome do ativo é obrigatório.")
        @Size(max = 100, message = "O nome do ativo deve ter no máximo 100 caracteres.")
        String nome,

        @NotNull(message = "A categoria/tipo do ativo é obrigatória.")
        TipoAtivo tipo,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade mínima é de 1 dispositivo.")
        @Max(value = 100, message = "O limite máximo do pequeno parque é de 100 dispositivos.")
        Integer quantidade,

        @Size(max = 255, message = "A localização deve ter no máximo 255 caracteres.")
        String localizacao,

        @Size(max = 100, message = "O responsável deve ter no máximo 100 caracteres.")
        String responsavel,

        Boolean ativo,

        Long empresaId
) {
    public AtivoRequestModel {
        if (quantidade == null) {
            quantidade = 1;
        }
        if (ativo == null) {
            ativo = true;
        }
        if (empresaId == null) {
            empresaId = 1L;
        }
    }

    public static AtivoRequestModel padrao(Long empresaId) {
        return new AtivoRequestModel("", TipoAtivo.ENDPOINTS, "", 1, "", "", true, empresaId);
    }

    public static AtivoRequestModel fromResponse(AtivoResponseModel response) {
        return new AtivoRequestModel(
                response.nome(),
                response.tipo(),
                response.descricao(),
                response.quantidade(),
                response.localizacao(),
                response.responsavel(),
                response.ativo(),
                response.empresaId()
        );
    }

    public String getNome() {
        return nome;
    }

    public TipoAtivo getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Long getEmpresaId() {
        return empresaId;
    }
}
