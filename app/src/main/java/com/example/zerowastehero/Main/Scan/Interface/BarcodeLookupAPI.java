package com.example.zerowastehero.Main.Scan.Interface;

import com.example.zerowastehero.DataBinding.Model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BarcodeLookupAPI {
    // Endpoint to look up a product by its barcode
    @GET("product")
    Call<ProductResponse> getProductDetails(
            @Query("barcode") String barcode,
            @Query("key") String apiKey
    );
}
