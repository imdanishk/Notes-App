package com.example.demonotesapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.demonotesapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var database: NoteDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        database = Room.databaseBuilder(
            applicationContext, NoteDatabase::class.java, "note")
            .fallbackToDestructiveMigration()
            .build()

        firebaseAuth = FirebaseAuth.getInstance()
/*
        if (firebaseAuth.currentUser == null){
            startActivity(Intent(this, SignInActivity::class.java))
        }
 */
        binding?.btnLogout?.setOnClickListener {
            logout()
        }

        binding?.btnAdd?.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        binding?.btnDeleteAll?.setOnClickListener {
            DataObject.deleteAll()
            GlobalScope.launch {
                database.dao().deleteAll()
            }
            setRecyclerView()
        }

        setRecyclerView()
        //loadAllData()

    }

    private fun setRecyclerView(){
        binding?.recyclerView?.adapter = NoteAdapter(DataObject.getAllData())
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun loadAllData() {
        val noteList = ArrayList<NoteInfo>()

        db.collection("user/note/${firebaseAuth.currentUser!!.uid}")
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val noteModel = document.toObject(NoteInfo::class.java)
                    noteList.add(noteModel)
                }
                for (i in noteList) {
                    val noteInfo = Entity(
                        id = i.id,
                        title = i.title,
                        description = i.description,
                        spinner = i.spinner
                    )
                    GlobalScope.launch {
                        database.dao().insertTask(noteInfo)
                    }
                }
                setRecyclerView()
                //Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){ _, _ ->
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }

        builder.setNegativeButton("No"){ _, _ ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}