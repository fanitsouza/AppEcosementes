package edu.ifam.br.ecosemente.dto;

import java.util.List;

public class VendaInputDTO {

    private String compradorCpfCnpj;
    private List<ItemVendaInputDTO> itens;

    public VendaInputDTO(String compradorCpfCnpj, List<ItemVendaInputDTO> itens) {
        this.compradorCpfCnpj = compradorCpfCnpj;
        this.itens = itens;
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
