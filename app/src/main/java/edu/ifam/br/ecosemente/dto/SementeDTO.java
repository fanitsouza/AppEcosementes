package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.Semente;

public class SementeDTO {

    // --- Campos Antigos (Mantidos) ---
    private long id;
    private String nome; // Nome Popular
    private String descricao;
    private String especie;
    private Integer tempoMedioColheita; // Alterado para Integer
    private Integer quantidade;         // Alterado para Integer
    private String cuidado;
    private Float preco;                // Alterado para Float

    // --- Campo Removido ---
    // private String epocaPlantio;

    // --- Novos Campos (Banco v7) ---
    private long nomeCientificoId;
    private String epocaInicio;
    private String epocaFim;
    private String tipoCultivo;
    private String tamanhoPorte;
    private Double latitude;
    private Double longitude;
    private String caminhoImagem;

    public SementeDTO() {
    }

    /**
     * Construtor que converte uma Entidade Semente em um DTO.
     */
    public SementeDTO(Semente semente) {
        // --- Campos Antigos ---
        this.id = semente.getId();
        this.nome = semente.getNome();

        this.tempoMedioColheita = semente.getTempoMedioColheita();


        // --- Novos Campos ---
        this.nomeCientificoId = semente.getNomeCientificoId();
        this.epocaInicio = semente.getEpocaInicio();
        this.epocaFim = semente.getEpocaFim();
        this.tipoCultivo = semente.getTipoCultivo();
        this.tamanhoPorte = semente.getTamanhoPorte();
        this.latitude = semente.getLatitude();
        this.longitude = semente.getLongitude();
        this.caminhoImagem = semente.getCaminhoImagem();
    }

    /**
     * Converte este DTO de volta para uma Entidade Semente.
     * Usa o construtor vazio e os setters, pois o construtor antigo foi removido.
     */
    public Semente getSemente(){
        Semente semente = new Semente(); // Usa o construtor vazio

        // --- Campos Antigos ---
        semente.setId(this.id);
        semente.setNome(this.nome);

        semente.setTempoMedioColheita(this.tempoMedioColheita);


        // --- Novos Campos ---
        semente.setNomeCientificoId(this.nomeCientificoId);
        semente.setEpocaInicio(this.epocaInicio);
        semente.setEpocaFim(this.epocaFim);
        semente.setTipoCultivo(this.tipoCultivo);
        semente.setTamanhoPorte(this.tamanhoPorte);
        semente.setLatitude(this.latitude);
        semente.setLongitude(this.longitude);
        semente.setCaminhoImagem(this.caminhoImagem);

        return semente;
    }

    // --- Getters e Setters para TODOS os campos ---
    // (Omitido por brevidade, mas você deve adicioná-los)

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

    public Integer getTempoMedioColheita() {
        return tempoMedioColheita;
    }

    public void setTempoMedioColheita(Integer tempoMedioColheita) {
        this.tempoMedioColheita = tempoMedioColheita;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCuidado() {
        return cuidado;
    }

    public void setCuidado(String cuidado) {
        this.cuidado = cuidado;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public long getNomeCientificoId() {
        return nomeCientificoId;
    }

    public void setNomeCientificoId(long nomeCientificoId) {
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
}