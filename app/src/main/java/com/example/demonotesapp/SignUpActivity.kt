package com.example.demonotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.demonotesapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private var binding: ActivitySignUpBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var firebaseUser: FirebaseUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding?.tvSignin?.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignup?.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser(){
        var email = binding?.etEmail?.text.toString().trim()
        var password = binding?.etPassword?.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }
        else if (password.length < 6){
            Toast.makeText(this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
        }
        else{
            binding?.progressBar?.visibility = View.VISIBLE

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    sendEmailVerification()
                }
                else{
                    Toast.makeText(this, "Registered Failed", Toast.LENGTH_SHORT).show()
                    binding?.progressBar?.visibility = View.INVISIBLE
                }
            }

        }

    }

    private fun sendEmailVerification(){
        firebaseUser = firebaseAuth.currentUser!!

        if (firebaseAuth != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(this){
                task ->
                Toast.makeText(this, "Verify Email To Continue", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
                finish()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        else{
            Toast.makeText(this, "Failed to Send Verification Email", Toast.LENGTH_SHORT).show()
        }

    }
}