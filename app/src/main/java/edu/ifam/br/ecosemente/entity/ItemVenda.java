package edu.ifam.br.ecosemente.entity;

public class ItemVenda {

    private long id;
    private Semente semente;
    private int quantidade;
    private float precoItem;
    private long vendaId;

    public ItemVenda() {
    }

    public ItemVenda(long id, Semente semente, int quantidade, float precoItem) {
        this.id = id;
        this.semente = semente;
        this.quantidade = quantidade;
        this.precoItem = precoItem;
    }

    public ItemVenda(long id, Semente semente, int quantidade, float precoItem, long vendaId) {
        this.id = id;
        this.semente = semente;
        this.quantidade = quantidade;
        this.precoItem = precoItem;
        this.vendaId = vendaId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Semente getSemente() {
        return semente;
    }

    public void setSemente(Semente semente) {
        this.semente = semente;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoItem() {
        return precoItem;
    }

    public void setPrecoItem(float precoItem) {
        this.precoItem = precoItem;
    }

    public long getVendaId() {
        return vendaId;
    }

    public void setVendaId(long vendaId) {
        this.vendaId = vendaId;
    }
}
