package com.dogactnrvrdi.koincryptocrazy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogactnrvrdi.koincryptocrazy.R
import com.dogactnrvrdi.koincryptocrazy.databinding.FragmentListBinding
import com.dogactnrvrdi.koincryptocrazy.model.Crypto
import com.dogactnrvrdi.koincryptocrazy.service.ICryptoAPI
import com.dogactnrvrdi.koincryptocrazy.viewmodel.CryptoViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListFragment : Fragment(R.layout.fragment_list), RecyclerViewAdapter.Listener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var cryptoAdapter = RecyclerViewAdapter(arrayListOf(), this)
    private lateinit var viewModel: CryptoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

        viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
        viewModel.getDataFromAPI()

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.cryptoList.observe(viewLifecycleOwner) { cryptos ->
            cryptos?.let {
                binding.recyclerView.visibility = View.VISIBLE
                binding.cryptoProgressBar.visibility = View.GONE
                binding.cryptoErrorText.visibility = View.GONE

                cryptoAdapter = RecyclerViewAdapter(ArrayList(cryptos), this@ListFragment)
                binding.recyclerView.adapter = cryptoAdapter
            }
        }

        viewModel.cryptoError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (it) {
                    binding.cryptoErrorText.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.cryptoProgressBar.visibility = View.GONE
                } else {
                    binding.cryptoErrorText.visibility = View.GONE
                }
            }
        }

        viewModel.cryptoLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (it) {
                    binding.cryptoProgressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.cryptoErrorText.visibility = View.GONE
                } else {
                    binding.cryptoProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(cryptoModel: Crypto) {
        Toast.makeText(
            requireContext(), "Clicked on: ${cryptoModel.currency}", Toast.LENGTH_LONG
        ).show()
    }
}