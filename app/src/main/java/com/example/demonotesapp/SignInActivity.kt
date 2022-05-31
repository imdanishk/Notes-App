package com.example.demonotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.demonotesapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {

    private var binding: ActivitySignInBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        /*

        // User don't have to enter credential to login
        if (firebaseUser != null) {
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }

         */


        binding?.tvForgot?.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding?.tvSignup?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignin?.setOnClickListener {
            validateUser()
        }

    }

    private fun validateUser() {
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }
        else {

            binding?.progressBar?.visibility = View.VISIBLE

            // login the user
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                    task ->
                if (task.isSuccessful){
                    checkEmailVerification()
                }
                else{
                    Toast.makeText(this, "Account not exists", Toast.LENGTH_SHORT).show()
                    binding?.progressBar?.visibility = View.INVISIBLE
                }
            }

        }
    }

    private fun checkEmailVerification(){
        firebaseUser = firebaseAuth.currentUser

        if (firebaseUser?.isEmailVerified == true){
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, MainActivity::class.java))

        }
        else {
            binding?.progressBar?.visibility = View.INVISIBLE
            Toast.makeText(this, "Verify Your Email First", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }

    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}