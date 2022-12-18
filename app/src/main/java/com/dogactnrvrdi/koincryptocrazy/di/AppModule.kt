package com.dogactnrvrdi.koincryptocrazy.di

import com.dogactnrvrdi.koincryptocrazy.repo.CryptoDownloadImpl
import com.dogactnrvrdi.koincryptocrazy.repo.ICryptoDownload
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import com.dogactnrvrdi.koincryptocrazy.viewmodel.CryptoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val BASE_URL = "https://raw.githubusercontent.com/"

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICryptoAPI::class.java)
    }

    single<ICryptoDownload> {
        CryptoDownloadImpl(get())
    }

    viewModel() {
        CryptoViewModel(get())
    }
}