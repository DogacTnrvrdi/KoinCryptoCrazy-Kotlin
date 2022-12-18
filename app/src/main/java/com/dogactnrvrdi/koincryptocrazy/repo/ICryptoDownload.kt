package com.dogactnrvrdi.koincryptocrazy.repo

import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.util.Resource

interface ICryptoDownload {
    suspend fun downloadCryptos() : Resource<List<Crypto>>
}