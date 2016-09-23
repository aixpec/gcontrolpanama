package com.gisystems.api;

import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Call;


public interface RetrofitAPI {

    @POST("api/peticion")
    Call<String>  enviarPeticionABCDEF(@Body String peticionWSL);

}
