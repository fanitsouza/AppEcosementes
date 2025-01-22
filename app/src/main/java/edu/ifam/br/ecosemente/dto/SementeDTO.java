package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.Semente;

public class SementeDTO {

    private long id;
    private String nome;
    private String descricao;
    private String especie;
    private String epocaPlantio;
    private int tempoMedioColheita;
    private int quantidade;
    private String cuidado;
    private float preco;

    public SementeDTO() {
    }

    public SementeDTO(Semente semente) {
        this.nome = semente.getNome();
        this.descricao = semente.getDescricao();
        this.especie = semente.getEspecie();
        this.epocaPlantio = semente.getEpocaPlantio();
        this.tempoMedioColheita = semente.getTempoMedioColheita();
        this.quantidade = semente.getQuantidade();
        this.cuidado = semente.getCuidado();
        this.preco = semente.getPreco();
    }

    public Semente getSemente(){
        return new Semente(id, nome, descricao, especie, epocaPlantio, tempoMedioColheita, quantidade, cuidado, preco);
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

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }
}
