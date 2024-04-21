package com.ubaya.hobbyapp_160421093.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp_160421093.databinding.ActivitySigninBinding
import org.json.JSONObject

class Signin : AppCompatActivity() {
    private lateinit var binding:ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160421093/signIn.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener {
                    Log.d("apilogin", it.toString())
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "success") {
                        //get name from json
                        val data = obj.getJSONObject("data")
                        Log.d("apiuser", data.toString())

                        //save to shared preference
                        var loginInfo = "com.ubaya.hobbyapp_160421093"
                        val shared: SharedPreferences = getSharedPreferences(loginInfo, Context.MODE_PRIVATE)
                        var editor:SharedPreferences.Editor = shared.edit()

                        editor.putString("username", binding.txtUsername.text.toString())
                        editor.putString("name",data.toString())
                        editor.apply()

                        //Login success
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, obj.toString(),Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener
                {
                    Log.d("apilogin", it.message.toString())
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()

                })
            {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = binding.txtUsername.text.toString()
                    params["password"] = binding.txtPassword.text.toString()
                    return params
                }
            }
            q.add(stringRequest)
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
        var loginInfo = "com.ubaya.hobbyapp_160421093"
        var shared:SharedPreferences = getSharedPreferences(loginInfo,
            Context.MODE_PRIVATE )
        var userid = shared.getString("username","").toString()
        if(userid == "")
        {
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show()
        }
        else{
            super.onBackPressed()
        }
    }
}