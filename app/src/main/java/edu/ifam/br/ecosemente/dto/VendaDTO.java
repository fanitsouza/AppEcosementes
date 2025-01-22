package edu.ifam.br.ecosemente.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.Comprador;
import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Venda;

public class VendaDTO {

    private long id;
    private LocalDate dataVenda;
    private CompradorDTO comprador;
    private List<ItemVendaDTO> itens;
    private float valorTotal;

    public VendaDTO() {
    }

    public VendaDTO(Venda venda) {
        this.comprador = new CompradorDTO(venda.getComprador());
        List<ItemVendaDTO> itens = new ArrayList<>();
        for(ItemVenda itemVenda : venda.getItens()){
            itens.add(new ItemVendaDTO(itemVenda));
        }
        this.itens = itens;
    }
    public Venda getVenda(){
        List<ItemVenda> itemVendas = new ArrayList<>();
        for(ItemVendaDTO itemVendaDTO : itens){
            itemVendas.add(itemVendaDTO.getItemVenda());
        }
        return new Venda(id, dataVenda,comprador.getComprador(), itemVendas,valorTotal);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public CompradorDTO getComprador() {
        return comprador;
    }

    public void setComprador(CompradorDTO comprador) {
        this.comprador = comprador;
    }

    public List<ItemVendaDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaDTO> itens) {
        this.itens = itens;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }
}
