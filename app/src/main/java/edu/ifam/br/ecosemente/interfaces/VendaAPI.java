package edu.ifam.br.ecosemente.interfaces;

import java.util.List;

import edu.ifam.br.ecosemente.dto.SementeDTO;
import edu.ifam.br.ecosemente.dto.VendaDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VendaAPI {

    @GET("venda")
    Call<List<VendaDTO>> getSemente();

    @GET("venda/{id}")
    Call<VendaDTO> getVenda(@Path("id") Long id);

    @POST("venda")
    Call<VendaDTO> setVenda(@Body VendaDTO vendaDTO);

    @PUT("venda/{id}")
    Call<VendaDTO> updateVenda(@Path("id") Long id, @Body SementeDTO sementeDTO);

    @DELETE("venda/{id}")
    Call<Void> deleteVenda(@Path("id") Long id);
}
