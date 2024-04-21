package com.ubaya.hobbyapp_160421093.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp_160421093.databinding.FragmentProfileBinding
import com.ubaya.hobbyapp_160421093.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.hobbyapp_160421093.R
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Context.MODE_PRIVATE
        )
        val res = sharedPref.getString("KEY_USER", "")

        if (res != null) {
            val sType = object : TypeToken<User>() {}.type
            val user = Gson().fromJson<User>(res, sType)

            val dialog = AlertDialog.Builder(requireActivity())
            dialog.setTitle("Confirmation")

            binding.btnChange.setOnClickListener {
                dialog.setMessage("Ganti data diri?")
                dialog.setPositiveButton("Ganti", DialogInterface.OnClickListener { dialog, which ->
                    val newNamaDepan = binding.txtUbahNamaDepan.text.toString()
                    val newNamaBelakang = binding.txtUbahNamaBelakang.text.toString()
                    val newPassword = binding.txtUbahPassword.text.toString()
                    updateData(user, newNamaDepan, newNamaBelakang, newPassword)
                })
                dialog.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }

            binding.btnLogout.setOnClickListener {
                dialog.setMessage("Yakin ingin logout?")
                dialog.setPositiveButton(
                    "Logout",
                    DialogInterface.OnClickListener { dialog, which ->
                        sharedPref.edit().clear().apply()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    })
                dialog.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        }
        // Set listener untuk BottomNavigationView
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemHome -> {
                    // Tidak perlu melakukan navigasi karena sudah berada di HomeFragment
                    true
                }
                R.id.itemReadingHistory -> {
                    // Navigate to ReadingHistoryFragment
                    val action1 = HomeFragmentDirections.actionHomeFragmentToReadingHistoryFragment()
                    val action2 = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                    Navigation.findNavController(requireView()).navigate(action1)
                    Navigation.findNavController(requireView()).navigate(action2)
                    true
                }
                R.id.itemProfile -> {
                    // Navigate to ProfileFragment
                    val action1 = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                    val action2 = HomeFragmentDirections.actionHomeFragmentToReadingHistoryFragment()
                    Navigation.findNavController(requireView()).navigate(action1)
                    Navigation.findNavController(requireView()).navigate(action2)
                    true
                }
                else -> false
            }
        }
    }

    private fun updateData(
        user: User,
        newNamaDepan: String,
        newNamaBelakang: String,
        newPassword: String
    ) {
        val newFirstName = if (newNamaDepan.isNotEmpty()) newNamaDepan else user.firstName
        val newLastName = if (newNamaBelakang.isNotEmpty()) newNamaBelakang else user.lastName
        val newPasswordFinal = if (newPassword.isNotEmpty()) newPassword else user.password

        queue = Volley.newRequestQueue(requireContext())
        val url = "https://ubaya.me/native/160421093/updateAccount.php"

        val alert = AlertDialog.Builder(requireActivity())
        alert.setTitle("Informasi")

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Log.d("cek", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    alert.setMessage("Berhasil update")
                    alert.setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                } else {
                    alert.setMessage("Gagal update")
                    alert.setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                }
                alert.create().show()
            },
            { error ->
                Log.e("errorUpdate", error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = user.username ?: ""
                params["firstName"] = newFirstName ?: ""
                params["lastName"] = newLastName ?: ""
                params["password"] = newPasswordFinal ?: ""
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}