package com.dogactnrvrdi.koincryptocrazy.repo

import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import com.dogactnrvrdi.koincryptocrazy.util.Resource

class CryptoDownloadImpl(
    private val api: ICryptoAPI
) : ICryptoDownload {
    override suspend fun downloadCryptos(): Resource<List<Crypto>> {
        return try {
            val response = api.getData()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            } else {
                Resource.error("Error!", null)
            }
        } catch (e: java.lang.Exception) {
            Resource.error("No data!", null)
        }
    }
}