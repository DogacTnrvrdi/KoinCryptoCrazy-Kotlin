package com.dogactnrvrdi.koincryptocrazy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.repo.ICryptoDownload
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import com.dogactnrvrdi.koincryptocrazy.util.Resource
import com.dogactnrvrdi.koincryptocrazy.view.RecyclerViewAdapter
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoViewModel(
    private val cryptoDownloadRepo: ICryptoDownload
) : ViewModel() {

    val cryptoList = MutableLiveData<Resource<List<Crypto>>>()
    val cryptoError = MutableLiveData<Resource<Boolean>>()
    val cryptoLoading = MutableLiveData<Resource<Boolean>>()

    private var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
        cryptoError.value = Resource.error(throwable.localizedMessage ?: "Error!", data = true)
    }

    fun getDataFromAPI() {
        cryptoLoading.value = Resource.loading(data = true)

        /*
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

         }
         */

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resource = cryptoDownloadRepo.downloadCryptos()
            withContext(Dispatchers.Main) {
                cryptoList.value = resource
                cryptoLoading.value = Resource.loading(data = false)
                cryptoError.value = Resource.error("Error!", data = false)
            }
        }
    }

}