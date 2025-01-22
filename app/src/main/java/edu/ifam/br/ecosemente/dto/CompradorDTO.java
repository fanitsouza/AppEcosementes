package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.Comprador;

public class CompradorDTO {

    private long id;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;

    public CompradorDTO() {
    }

    public CompradorDTO(Comprador comprador) {
        this.id = comprador.getId();
        this.nome = comprador.getNome();
        this.cpfCnpj = comprador.getCpfCnpj();
        this.telefone = comprador.getTelefone();
        this.email = comprador.getEmail();
    }

    public Comprador getComprador(){
        return new Comprador(id, nome,cpfCnpj, telefone,email);
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
