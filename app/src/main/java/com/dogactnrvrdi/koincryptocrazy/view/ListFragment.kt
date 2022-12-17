package com.dogactnrvrdi.koincryptocrazy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogactnrvrdi.koincryptocrazy.R
import com.dogactnrvrdi.koincryptocrazy.databinding.FragmentListBinding
import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListFragment : Fragment(R.layout.fragment_list), RecyclerViewAdapter.Listener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<Crypto>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.getData()
            println("get data called")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    println("successful")
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@ListFragment)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        _binding = null
    }

    override fun onItemClick(cryptoModel: Crypto) {
        Toast.makeText(
            requireContext(), "Clicked on: ${cryptoModel.currency}", Toast.LENGTH_LONG
        ).show()
    }
}