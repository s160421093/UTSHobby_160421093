package com.ubaya.hobbyapp_160421093.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp_160421093.model.Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ModelViewModel(application: Application): AndroidViewModel(application) {
    val modelLD           = MutableLiveData<ArrayList<Model>>()
    val modelloadingLD       = MutableLiveData<Boolean>()
    val modelLoadErrorLD  = MutableLiveData<Boolean>()
    val TAG             = "volleyModel"

    private var queue: RequestQueue?=null

    fun refresh(){
        modelloadingLD.value         = true
        modelLoadErrorLD.value    = false

        val url = "http://10.0.2.2/film.json"
        queue   = Volley.newRequestQueue(getApplication())

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                Log.d("show_volley", it)

                val sType = object: TypeToken<List<Model>>() {}.type
                val res = Gson().fromJson<List<Model>>(it, sType)
                modelloadingLD.value = false
                modelLD.value = res as ArrayList<Model>

                Log.d("data", res.toString())
                Log.d("data", it)
            },
            {
                Log.d("showvolley", it.toString())
                modelloadingLD.value = false
                modelLoadErrorLD.value = false
            }
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)

        modelloadingLD.value = false
        modelLoadErrorLD.value = false
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}