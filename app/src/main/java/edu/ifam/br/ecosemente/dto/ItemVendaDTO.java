package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Semente;

public class ItemVendaDTO {

    private long id;
    private SementeDTO semente;
    private int quantidade;
    private float precoItem;

    public ItemVendaDTO() {
    }

    public ItemVendaDTO(ItemVenda itemVenda) {
        this.semente = new SementeDTO(itemVenda.getSemente());
        this.quantidade = itemVenda.getQuantidade();
    }
    public ItemVenda getItemVenda(){
        return new ItemVenda(id, semente.getSemente(), quantidade,precoItem);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SementeDTO getSemente() {
        return semente;
    }

    public void setSemente(SementeDTO semente) {
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
