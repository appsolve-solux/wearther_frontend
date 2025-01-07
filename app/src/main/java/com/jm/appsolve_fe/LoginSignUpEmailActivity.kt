package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginSignUpEmailActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup_email)

        val spinnerEmailDomain = findViewById<Spinner>(R.id.spinnerEmailDomain)
        val etEmail = findViewById<EditText>(R.id.etEmail)

        val emailDomains = resources.getStringArray(R.array.email_domains).toList()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            emailDomains
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmailDomain.adapter = adapter

        spinnerEmailDomain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDomain = emailDomains[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
    }
}