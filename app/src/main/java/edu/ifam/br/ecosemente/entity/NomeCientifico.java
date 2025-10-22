package edu.ifam.br.ecosemente.entity;

public class NomeCientifico {

        private long id;
        private String nome;
        private String nomePopular;

        public NomeCientifico() {
        }

    public NomeCientifico(long id, String nome, String nomePopular) {
        this.id = id;
        this.nome = nome;
        this.nomePopular = nomePopular;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomePopular() {
        return nomePopular;
    }

    public void setNomePopular(String nomePopular) {
        this.nomePopular = nomePopular;
    }

    // --- IMPORTANTE ---
        // O ArrayAdapter usará este método para exibir o texto no ListView e no Spinner.
    public String toString() {
        // Ex: "Científico: Zea mays, Popular: Milho"
        return "Científico: " + (nome != null ? nome : "(vazio)") +
                ", Popular: " + (nomePopular != null ? nomePopular : "(vazio)");
    }
}
