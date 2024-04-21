package com.ubaya.hobbyapp_160421093.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ubaya.hobbyapp_160421093.R
import com.ubaya.hobbyapp_160421093.databinding.FragmentReadingHistoryBinding

class ReadingHistoryFragment : Fragment() {
    private var _binding: FragmentReadingHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set listener untuk BottomNavigationView
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemHome -> {
                    // Navigate to HomeFragment
                    findNavController().navigate(ReadingHistoryFragmentDirections.actionReadingHistoryFragmentToHomeFragment())
                    findNavController().navigate(ReadingHistoryFragmentDirections.actionReadingHistoryFragmentToProfileFragment())
                    true
                }
                R.id.itemProfile -> {
                    // Navigate to ProfileFragment
                    findNavController().navigate(ReadingHistoryFragmentDirections.actionReadingHistoryFragmentToProfileFragment())
                    findNavController().navigate(ReadingHistoryFragmentDirections.actionReadingHistoryFragmentToHomeFragment())
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}