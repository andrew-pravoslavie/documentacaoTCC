package br.edu.securedesk.entity.diagnostico;

/**
 * As 6 Funções principais do NIST Cybersecurity Framework (CSF 2.0).
 */
public enum SiglaFuncaoNist {
    GV("Governar"),
    ID("Identificar"),
    PR("Proteger"),
    DE("Detectar"),
    RS("Responder"),
    RC("Recuperar");

    private final String nome;

    SiglaFuncaoNist(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
