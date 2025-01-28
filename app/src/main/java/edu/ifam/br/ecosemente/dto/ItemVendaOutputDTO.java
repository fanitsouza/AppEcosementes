package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Semente;

public class ItemVendaOutputDTO {

    private long id;
    private String semente;
    private int quantidade;
    private float precoItem;

    public ItemVendaOutputDTO() {
    }

    public ItemVendaOutputDTO(ItemVenda itemVenda) {
        this.semente = itemVenda.getSemente().getNome();
        this.quantidade = itemVenda.getQuantidade();
    }
    public ItemVenda getItemVenda(){
        Semente semente = new Semente();
        semente.setNome(this.semente);
        return new ItemVenda(id,semente, quantidade,precoItem);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSemente() {
        return semente;
    }

    public void setSemente(String semente) {
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
}
