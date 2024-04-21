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
import com.ubaya.hobbyapp_160421093.model.Model

class DetailViewModel(application: Application): AndroidViewModel(application) {
    val dataLD = MutableLiveData<Model>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun fetch(id: Int) {
        Log.d("volley", "masukvolley")

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/project_uts_anmp/football_manager.json"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                Log.d("showvolley", it)
                val sType = object : TypeToken<List<Model>>() { }.type
                val res = Gson().fromJson<List<Model>>(it, sType)
                val listData = res as ArrayList<Model>
                dataLD.value = listData[id - 1]

                Log.d("showvolley", res.toString())
            },
            {
                Log.d("showvolley", it.toString())
            }
        )

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}