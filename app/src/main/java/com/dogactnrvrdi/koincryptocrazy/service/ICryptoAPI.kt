package com.dogactnrvrdi.koincryptocrazy.service

import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import retrofit2.Response
import retrofit2.http.GET

interface ICryptoAPI {

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    suspend fun getData(): Response<List<Crypto>>
}