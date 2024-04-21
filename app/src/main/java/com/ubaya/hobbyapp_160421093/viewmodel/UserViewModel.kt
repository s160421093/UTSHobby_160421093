package com.ubaya.hobbyapp_160421093.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.hobbyapp_160421093.model.User

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "volleyUser"
    val userLD = MutableLiveData<ArrayList<User>>()
    val userloadingLD = MutableLiveData<Boolean>()
    val userLoadErrorLD = MutableLiveData<Boolean>()
    val updateResult = MutableLiveData<String>() // Menyimpan hasil dari pembaruan

    private var queue: RequestQueue? = null

    fun refresh() {
        userloadingLD.value = true
        userLoadErrorLD.value = false

        val url = "https://ubaya.me/native/160421093/getAccount.php?username="
        queue = Volley.newRequestQueue(getApplication())

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                userloadingLD.value = false
                Log.d("show_volley", response)

                val sType = object : TypeToken<List<User>>() {}.type
                val result = Gson().fromJson<List<User>>(response, sType)

                userLD.value = result as ArrayList<User>
            },
            { error ->
                Log.d("show_volley", error.toString())
            }
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun updateUser(namaDepan: String, namaBelakang: String, password: String) {
        val url = "https://ubaya.me/native/160421093/updateAccount.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                // Tanggapi respons dari server
                if (response.contains("success")) {
                    // Jika pembaruan berhasil, set updateResult menjadi success
                    updateResult.value = "success"
                } else {
                    // Jika pembaruan gagal, set updateResult menjadi error
                    updateResult.value = "error"
                }
            },
            { error ->
                // Tanggapan jika terjadi kesalahan
                Log.e(TAG, "Error updating user: ${error.message}")
                updateResult.value = "error"
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["namaDepan"] = namaDepan
                params["namaBelakang"] = namaBelakang
                params["password"] = password
                // Disesuaikan dengan parameter yang diperlukan oleh endpoint server Anda
                return params
            }
        }
        // Tambahkan tag dan kirim permintaan ke server
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}