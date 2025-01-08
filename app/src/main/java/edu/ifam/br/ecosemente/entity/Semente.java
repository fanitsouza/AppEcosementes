package edu.ifam.br.ecosemente.entity;

import java.time.Month;
import java.util.List;

public class Semente {

    private int id;
    private String nome;
    private String descricao;
    private String especie;
    private List<Month> espocaPlantio;
    private int tempoMedioColheita;
    private int quantidade;
    private String cuidado;


    public Semente() {
    }

    public Semente(String nome, String descricao, String especie, List<Month> espocaPlantio, int tempoMedioColheita, int quantidade, String cuidado) {
        this.nome = nome;
        this.descricao = descricao;
        this.especie = especie;
        this.espocaPlantio = espocaPlantio;
        this.tempoMedioColheita = tempoMedioColheita;
        this.quantidade = quantidade;
        this.cuidado = cuidado;
    }

    public Semente(int id, String nome, String descricao, String especie, List<Month> espocaPlantio, int tempoMedioColheita, int quantidade, String cuidado) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.especie = especie;
        this.espocaPlantio = espocaPlantio;
        this.tempoMedioColheita = tempoMedioColheita;
        this.quantidade = quantidade;
        this.cuidado = cuidado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<Month> getEspocaPlantio() {
        return espocaPlantio;
    }

    public void setEspocaPlantio(List<Month> espocaPlantio) {
        this.espocaPlantio = espocaPlantio;
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
