package edu.ifam.br.ecosemente.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Venda {

    private long id;
    private Date dataVenda;
    private Comprador comprador;
    private List<ItemVenda> itens;
    private float valorTotal;

    public Venda() {
    }

    public Venda(long id, Date dataVenda, Comprador comprador, List<ItemVenda> itens, float valorTotal) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.comprador = comprador;
        this.itens = itens;
        this.valorTotal = valorTotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }
}
