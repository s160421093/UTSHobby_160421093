package com.ubaya.hobbyapp_160421093.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.hobbyapp_160421093.databinding.FragmentHomeBinding
import com.ubaya.hobbyapp_160421093.model.Model
import com.ubaya.hobbyapp_160421093.viewmodel.ModelViewModel

class HomeFragment : Fragment() {
    val TAG = "volleyModel"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ModelViewModel
    private var listDataAdapter = HobbyAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ModelViewModel::class.java)
        viewModel.refresh()

        with (binding) {
            binding.recView.layoutManager = LinearLayoutManager(context)
            binding.recView.adapter = listDataAdapter

            refreshLayout.setOnRefreshListener {
                recView.visibility = View.GONE
                txtError.visibility = View.GONE
                progressLoad.visibility = View.VISIBLE
                viewModel.refresh()
                refreshLayout.isRefreshing = false
            }
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.modelLD.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Data received from ViewModel: $it")
            listDataAdapter.updateHobby(it)
        })

        viewModel.modelLoadErrorLD.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "Error loading data: $it")
            if (it == true) {
                binding.txtError.visibility = View.VISIBLE
            } else {
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.modelloadingLD.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Loading status changed: $it")
            if (it == true) {
                binding.recView.visibility = View.GONE
                binding.progressLoad.visibility = View.VISIBLE
            } else {
                binding.recView.visibility = View.VISIBLE
                binding.progressLoad.visibility = View.GONE
            }
        })
    }
}