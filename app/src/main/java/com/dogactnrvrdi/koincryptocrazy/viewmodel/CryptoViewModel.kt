package com.dogactnrvrdi.koincryptocrazy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import com.dogactnrvrdi.koincryptocrazy.view.RecyclerViewAdapter
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoViewModel : ViewModel() {

    val cryptoList = MutableLiveData<List<Crypto>>()
    val cryptoError = MutableLiveData<Boolean>()
    val cryptoLoading = MutableLiveData<Boolean>()

    private var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
        cryptoError.value = true
    }

    fun getDataFromAPI() {

        cryptoLoading.value = true

        val BASE_URL = "https://raw.githubusercontent.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICryptoAPI::class.java)

        /*
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

         }
         */

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.getData()
            println("get data called")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    println("successful")
                    cryptoError.value = false
                    cryptoLoading.value = false
                    response.body()?.let {
                        cryptoList.value = it
                        println(it)
                    }
                }
            }
        }
    }

}