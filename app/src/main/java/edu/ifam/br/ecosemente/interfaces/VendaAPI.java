package edu.ifam.br.ecosemente.interfaces;

import java.util.List;

import edu.ifam.br.ecosemente.dto.SementeDTO;
import edu.ifam.br.ecosemente.dto.VendaInputDTO;
import edu.ifam.br.ecosemente.dto.VendaOutputDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VendaAPI {

    @GET("venda")
    Call<List<VendaOutputDTO>> getVenda();

    @GET("venda/{id}")
    Call<VendaOutputDTO> getVenda(@Path("id") Long id);

    @POST("venda")
    Call<VendaOutputDTO> setVenda(@Body VendaInputDTO vendaInputDTO);

    @PUT("venda/{id}")
    Call<VendaOutputDTO> updateVenda(@Path("id") Long id, @Body SementeDTO sementeDTO);

    @DELETE("venda/{id}")
    Call<Void> deleteVenda(@Path("id") Long id);
}
