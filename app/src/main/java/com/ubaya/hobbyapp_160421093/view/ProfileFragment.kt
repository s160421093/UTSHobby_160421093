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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp_160421093.databinding.FragmentProfileBinding
import com.ubaya.hobbyapp_160421093.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            dialog.setTitle("Konfirmasi")

            binding.btnChange.setOnClickListener {
                dialog.setMessage("Apakah anda yakin ingin mengganti data diri anda?")
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
                dialog.setMessage("Apakah anda yakin ingin melakukan logout?")
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
        val url = "http://10.0.2.2/project_uts_anmp/update_data.php"

        val alert = AlertDialog.Builder(requireActivity())
        alert.setTitle("Informasi")

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Log.d("cekbisa", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    alert.setMessage("Berhasil melakukan update data user.")
                    alert.setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                } else {
                    alert.setMessage("Gagal melakukan update data user.")
                    alert.setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                }
                alert.create().show()
            },
            { error ->
                Log.e("cekerror", error.toString())
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