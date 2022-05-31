package com.example.demonotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.demonotesapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {

    private var binding: ActivityForgotPasswordBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding?.tvSignin?.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding?.btnRecover?.setOnClickListener {
            var email = binding?.etEmail?.text.toString().trim()

            if (email.isEmpty()){
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                        task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Mail Sent, You can recover your password", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, "Email is wrong or Account not exists", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}