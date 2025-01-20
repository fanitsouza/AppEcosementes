package edu.ifam.br.ecosemente.interfaces;

import java.util.List;

import edu.ifam.br.ecosemente.dto.SementeDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SementeAPI {

    @GET("semente")
    Call<List<SementeDTO>> getSemente();

    @GET("semente/{id}")
    Call<SementeDTO> getSemente(@Path("id") Long id);

    @POST("semente")
    Call<SementeDTO> setSemente(@Body SementeDTO sementeDTO);

    @PUT("semente/{id}")
    Call<SementeDTO> updateSemente(@Path("id") Long id, @Body SementeDTO sementeDTO);

    @DELETE("semente/{id}")
    Call<Void> deleteSemente(@Path("id") Long id);

}
