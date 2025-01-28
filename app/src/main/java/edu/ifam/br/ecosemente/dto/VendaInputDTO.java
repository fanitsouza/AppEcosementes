package edu.ifam.br.ecosemente.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Venda;

public class VendaInputDTO {

    private String dataVenda;
    private String compradorCpfCnpj;
    private List<ItemVendaInputDTO> itens;

    public VendaInputDTO(Venda venda) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dataVenda = dateFormat.format(venda.getDataVenda()); // Formata a data para "2025-01-28"
        this.compradorCpfCnpj = venda.getComprador().getCpfCnpj();
        List<ItemVendaInputDTO> itemVendaInputDTO = new ArrayList<>();
        for (ItemVenda itemVenda : venda.getItens()) {
            itemVendaInputDTO.add(new ItemVendaInputDTO(itemVenda));
        }
        this.itens = itemVendaInputDTO;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getCompradorCpfCnpj() {
        return compradorCpfCnpj;
    }

    public void setCompradorCpfCnpj(String compradorCpfCnpj) {
        this.compradorCpfCnpj = compradorCpfCnpj;
    }

    public List<ItemVendaInputDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaInputDTO> itens) {
        this.itens = itens;
    }
}
