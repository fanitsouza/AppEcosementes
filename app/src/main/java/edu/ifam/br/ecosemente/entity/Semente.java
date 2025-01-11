package edu.ifam.br.ecosemente.entity;

public class Semente {

    private long id;
    private String nome;
    private String descricao;
    private String especie;
    private String epocaPlantio;
    private int tempoMedioColheita;
    private int quantidade;
    private String cuidado;


    public Semente() {
    }

    public Semente(String nome, String descricao, String especie, String epocaPlantio, int tempoMedioColheita, int quantidade, String cuidado) {
        this.nome = nome;
        this.descricao = descricao;
        this.especie = especie;
        this.epocaPlantio = epocaPlantio;
        this.tempoMedioColheita = tempoMedioColheita;
        this.quantidade = quantidade;
        this.cuidado = cuidado;
    }

    public Semente(int id, String nome, String descricao, String especie, String epocaPlantio, int tempoMedioColheita, int quantidade, String cuidado) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.especie = especie;
        this.epocaPlantio = epocaPlantio;
        this.tempoMedioColheita = tempoMedioColheita;
        this.quantidade = quantidade;
        this.cuidado = cuidado;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getEpocaPlantio() {
        return epocaPlantio;
    }

    public void setEpocaPlantio(String epocaPlantio) {
        this.epocaPlantio = epocaPlantio;
    }

    public int getTempoMedioColheita() {
        return tempoMedioColheita;
    }

    public void setTempoMedioColheita(int tempoMedioColheita) {
        this.tempoMedioColheita = tempoMedioColheita;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCuidado() {
        return cuidado;
    }

    public void setCuidado(String cuidado) {
        this.cuidado = cuidado;
    }
}
