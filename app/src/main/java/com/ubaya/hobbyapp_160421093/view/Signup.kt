package com.ubaya.hobbyapp_160421093.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp_160421093.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp2.setOnClickListener {
            val username = binding.txtUsernameSU.text.toString()
            val namaDepan = binding.txtNamaDepan.text.toString()
            val namaBelakang = binding.txtNamaBelakang.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val ulangPassword = binding.txtUlang.text.toString()

            // Memeriksa apakah semua TextInput terisi
            if (username.isEmpty() || namaDepan.isEmpty() || namaBelakang.isEmpty() ||
                email.isEmpty() || password.isEmpty() || ulangPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Memeriksa apakah kedua password cocok
                if (password == ulangPassword) {
                    registerUser()
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSignIn3.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
    }
    private fun registerUser() {
        val username    = binding.txtUsernameSU.text.toString()
        val namaDepan    = binding.txtNamaDepan.text.toString()
        val namaBelakang  = binding.txtNamaBelakang.text.toString()
        val email  = binding.txtEmail.text.toString()
        val password  = binding.txtPassword.text.toString()

        val url = "https://ubaya.me/native/160421093/signUp.php"

        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response -> Toast.makeText(this, response, Toast.LENGTH_SHORT).show() },
            Response.ErrorListener { error -> Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show() }) {

            override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params["username"]          = username
                params["namaDepan"]        = namaDepan
                params["namaBelakang"]      = namaBelakang
                params["email"]    = email
                params["password"]    = password
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }
}