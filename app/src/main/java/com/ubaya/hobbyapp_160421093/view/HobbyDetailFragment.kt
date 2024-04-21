package com.ubaya.hobbyapp_160421093.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.ubaya.hobbyapp_160421093.R
import com.ubaya.hobbyapp_160421093.databinding.FragmentHobbyDetailBinding

class HobbyDetailFragment : Fragment() {
    private lateinit var  binding:FragmentHobbyDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHobbyDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title =
            HobbyDetailFragmentArgs.fromBundle(requireArguments()).title
        val direction =
            HobbyDetailFragmentArgs.fromBundle(requireArguments()).direction
        val fullDescription =
            HobbyDetailFragmentArgs.fromBundle(requireArguments()).fullDescription
        val images =
            HobbyDetailFragmentArgs.fromBundle(requireArguments()).images
        binding.txtJudul.text = title
        binding.txtPembuat.text = "@"+direction
        binding.txtDeskripsi.text = fullDescription
        Picasso.get()
            .load(images)
            .into(binding.imgCover)
    }
}