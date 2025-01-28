package edu.ifam.br.ecosemente.dto;

import edu.ifam.br.ecosemente.entity.ItemVenda;

public class ItemVendaInputDTO {

    private String semente;
    private int quantidade;

    public ItemVendaInputDTO(ItemVenda itemVenda){
        this.semente = itemVenda.getSemente().getNome();
        this.quantidade = itemVenda.getQuantidade();
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
}
