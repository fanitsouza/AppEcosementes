package edu.ifam.br.ecosemente.entity;

import androidx.annotation.NonNull;

public class Semente {

    private long id;
    private String nome; // Nome Popular
    private Integer tempoMedioColheita;

    // --- MUDANÇA: Alterado de long (primitivo) para Long (Classe) ---
    private Long nomeCientificoId; // Chave Estrangeira

    private String epocaInicio;
    private String epocaFim;
    private String tipoCultivo; // "Natural" ou "Plantada"
    private String tamanhoPorte; // "Pequeno", "Médio", "Grande"
    private Double latitude;
    private Double longitude;
    private String caminhoImagem;

    private NomeCientifico nomeCientifico;

    /**
     * Construtor vazio.
     */
    public Semente() {
        this.nome = "";
        this.tempoMedioColheita = 0;

        // --- MUDANÇA: Inicializado como null em vez de 0 ---
        this.nomeCientificoId = null;

        this.epocaInicio = "";
        this.epocaFim = "";
        this.tipoCultivo = "";
        this.tamanhoPorte = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.caminhoImagem = "";
        this.nomeCientifico = null;
    }

    // --- Getters e Setters (Apenas para os campos que existem) ---

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

    public Integer getTempoMedioColheita() {
        return tempoMedioColheita;
    }

    public void setTempoMedioColheita(Integer tempoMedioColheita) {
        this.tempoMedioColheita = tempoMedioColheita;
    }

    // --- MUDANÇA: Getters/Setters atualizados para Long ---
    public Long getNomeCientificoId() {
        return nomeCientificoId;
    }

    public void setNomeCientificoId(Long nomeCientificoId) {
        this.nomeCientificoId = nomeCientificoId;
    }

    public String getEpocaInicio() {
        return epocaInicio;
    }

    public void setEpocaInicio(String epocaInicio) {
        this.epocaInicio = epocaInicio;
    }

    public String getEpocaFim() {
        return epocaFim;
    }

    public void setEpocaFim(String epocaFim) {
        this.epocaFim = epocaFim;
    }

    public String getTipoCultivo() {
        return tipoCultivo;
    }

    public void setTipoCultivo(String tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }

    public String getTamanhoPorte() {
        return tamanhoPorte;
    }

    public void setTamanhoPorte(String tamanhoPorte) {
        this.tamanhoPorte = tamanhoPorte;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public NomeCientifico getNomeCientifico() {
        return nomeCientifico;
    }

    public void setNomeCientifico(NomeCientifico nomeCientifico) {
        this.nomeCientifico = nomeCientifico;
    }

    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}