package edu.ifam.br.ecosemente.entity;

public class NomeCientifico {

        private long id;
        private String nome;

        public NomeCientifico() {
        }

        public NomeCientifico(long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        // --- Getters e Setters ---
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

        // --- IMPORTANTE ---
        // O ArrayAdapter usará este método para exibir o texto no ListView e no Spinner.
        @Override
        public String toString() {
            return nome;
        }
}
