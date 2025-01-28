package edu.ifam.br.ecosemente.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ifam.br.ecosemente.entity.Comprador;
import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Venda;

public class VendaOutputDTO {

    private long id;
    private String dataVenda; // Data no formato String para comunicação com a API
    private String comprador;
    private String cpfCnpjComprador;
    private List<ItemVendaOutputDTO> itens;
    private float valorTotal;

    // Formato de data para conversões
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public VendaOutputDTO() {
    }


    public Venda getVenda() {
        Date data = null;
        try {
            data = DATE_FORMAT.parse(this.dataVenda);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Formato de data inválido: " + dataVenda);
        }

        // Converte itens do DTO para entidade
        List<ItemVenda> itemVendas = new ArrayList<>();
        for (ItemVendaOutputDTO itemVendaOutputDTO : this.itens) {
            itemVendas.add(itemVendaOutputDTO.getItemVenda());
        }
        Comprador comprador =  new Comprador();
        comprador.setNome(this.comprador);
        comprador.setCpfCnpj(this.cpfCnpjComprador);

        // Retorna uma nova entidade Venda
        return new Venda(this.id, data, comprador, itemVendas, this.valorTotal);
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public List<ItemVendaOutputDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaOutputDTO> itens) {
        this.itens = itens;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCpfCnpjComprador() {
        return cpfCnpjComprador;
    }

    public void setCpfCnpjComprador(String cpfCnpjComprador) {
        this.cpfCnpjComprador = cpfCnpjComprador;
    }
}
