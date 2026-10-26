package br.edu.securedesk.governanca.adapter.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Armazenamento em disco dos arquivos de evidência enviados nas tarefas de remediação.
 * O nome gravado (prefixo "tarefa{id}_") coincide com o caminho "/uploads/{nome}" registrado
 * pelo caso de uso, mantendo banco e disco consistentes.
 */
@Component
public class EvidenciaFileStorage {

    private static final int TAMANHO_MAXIMO_NOME = 180;

    private final Path diretorioBase;

    public EvidenciaFileStorage(@Value("${securedesk.uploads.dir:uploads}") String diretorio) {
        this.diretorioBase = Paths.get(diretorio).toAbsolutePath().normalize();
    }

    /**
     * Grava o arquivo e devolve o nome definitivo (sanitizado e prefixado com a tarefa).
     *
     * @throws IllegalArgumentException se o arquivo for vazio ou já existir evidência de mesmo nome na tarefa
     * @throws IllegalStateException    se houver falha de E/S
     */
    public String armazenar(Long tarefaId, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione um arquivo para anexar como evidência.");
        }
        String nome = nomeDefinitivo(tarefaId, arquivo.getOriginalFilename());
        try {
            Files.createDirectories(diretorioBase);
            try (InputStream in = arquivo.getInputStream()) {
                Files.copy(in, resolver(nome));
            }
            return nome;
        } catch (FileAlreadyExistsException ex) {
            throw new IllegalArgumentException(
                    "Já existe uma evidência com o nome '" + nome + "' nesta tarefa. Renomeie o arquivo e tente novamente.");
        } catch (IOException ex) {
            throw new IllegalStateException("Não foi possível gravar o arquivo de evidência.", ex);
        }
    }

    public void remover(String nomeArquivo) {
        try {
            Files.deleteIfExists(resolver(nomeArquivo));
        } catch (IOException | IllegalArgumentException ignored) {
            // Remoção física é best-effort: o registro no banco já foi tratado pelo caso de uso.
        }
    }

    private Path resolver(String nome) {
        Path destino = diretorioBase.resolve(nome).normalize();
        if (!destino.startsWith(diretorioBase) || destino.equals(diretorioBase)) {
            throw new IllegalArgumentException("Nome de arquivo inválido.");
        }
        return destino;
    }

    static String nomeDefinitivo(Long tarefaId, String nomeOriginal) {
        String base = nomeOriginal == null ? "" : nomeOriginal.replace('\\', '/');
        base = base.substring(base.lastIndexOf('/') + 1).replaceAll("[^\\p{L}\\p{N}._() -]", "_").trim();
        if (base.isEmpty() || base.chars().allMatch(c -> c == '.')) {
            base = "evidencia";
        }
        String prefixo = "tarefa" + tarefaId + "_";
        int limite = TAMANHO_MAXIMO_NOME - prefixo.length();
        if (base.length() > limite) {
            int ponto = base.lastIndexOf('.');
            String extensao = (ponto > 0 && base.length() - ponto <= 10) ? base.substring(ponto) : "";
            base = base.substring(0, limite - extensao.length()) + extensao;
        }
        return prefixo + base;
    }
}
