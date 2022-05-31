package com.example.demonotesapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.demonotesapp.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class AddNoteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var binding: ActivityAddNoteBinding? = null

    private lateinit var database: NoteDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private var priorities = arrayOf("low", "medium", "high")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        database = Room.databaseBuilder(
            applicationContext, NoteDatabase::class.java, "note")
            .fallbackToDestructiveMigration()
            .build()

        setupPrioritySpinner()

        binding?.btnSave?.setOnClickListener {
            val title = binding?.etNoteTitle?.text.toString()
            val description = binding?.etDescription?.text.toString()
            val spinner = binding?.spinnerPriority?.selectedItem.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val noteId: String = UUID.randomUUID().toString()

                DataObject.setData(noteId, title, description, spinner)

                GlobalScope.launch {
                    database.dao().insertTask(Entity(noteId, title, description, spinner))
                }
                addFirebaseData(noteId, title, description, spinner)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun addFirebaseData(noteId: String, title: String, description: String, spinner: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val noteModel = NoteInfo(noteId, title, description, spinner)
        db.collection("user/note/${firebaseAuth.currentUser!!.uid}")
            .document(noteId)
            .set(noteModel)
            .addOnSuccessListener {
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
                Log.e("Add", "Error Saving: " + it.message)
            }
    }

    private fun setupPrioritySpinner(){
        val spinner: Spinner? = binding?.spinnerPriority
        spinner?.onItemSelectedListener

        //Creating the ArrayAdapter instance having the languages list
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, priorities)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinner?.adapter = arrayAdapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        Toast.makeText(applicationContext, priorities[pos], Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}