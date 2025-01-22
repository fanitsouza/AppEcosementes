package edu.ifam.br.ecosemente.interfaces;

import retrofit2.Call;

import java.util.List;

import edu.ifam.br.ecosemente.dto.CompradorDTO;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompradorAPI {

    @GET("comprador")
    Call<List<CompradorDTO>> getComprador();

    @GET("comprador/{id}")
    Call<CompradorDTO> getComprador(@Path("id") Long id);

    @POST("comprador")
    Call<CompradorDTO> createComprador(@Body CompradorDTO compradorDTO);

    @PUT("comprador/{id}")
    Call<CompradorDTO> updateComprador(@Path("id") Long id, @Body CompradorDTO compradorDTO);

    @DELETE("comprador/{id}")
    Call<Void> deleteComprador(@Path("id") Long id);




}
