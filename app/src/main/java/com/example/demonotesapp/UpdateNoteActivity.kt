package com.example.demonotesapp

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.room.Room
import com.example.demonotesapp.databinding.ActivityUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class UpdateNoteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var binding: ActivityUpdateNoteBinding? = null

    private lateinit var database: NoteDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private var priorities = arrayOf("low", "medium", "high")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = Room.databaseBuilder(
            applicationContext, NoteDatabase::class.java, "note"
        )
            .fallbackToDestructiveMigration()
            .build()

        setupPrioritySpinner()
        updateAndDeleteData()
    }

    private fun updateAndDeleteData() {
        val noteid = intent.getStringExtra("id")

        if (!noteid.isNullOrEmpty()) {
            val title = DataObject.getData(noteid)?.title
            val description = DataObject.getData(noteid)?.description

            binding?.etNoteTitle?.setText(title)
            binding?.etDescription?.setText(description)
            binding?.spinnerPriority?.selectedItem.toString()

            binding?.btnDelete?.setOnClickListener {
                DataObject.deleteData(noteid)

                GlobalScope.launch {
                    database.dao().deleteTask(
                        Entity(
                            noteid,
                            binding?.etNoteTitle?.text.toString(),
                            binding?.etDescription?.text.toString(),
                            binding?.spinnerPriority?.selectedItem.toString()
                        )
                    )
                }
                deleteFirebaseData(noteid)
                startActivity(Intent(this, MainActivity::class.java))
            }

            binding?.btnUpdate?.setOnClickListener {
                val title = binding?.etNoteTitle?.text.toString()
                val description = binding?.etDescription?.text.toString()
                val spinner = binding?.spinnerPriority?.selectedItem.toString()

                DataObject.updateData(
                    noteid,
                    binding?.etNoteTitle?.text.toString(),
                    binding?.etDescription?.text.toString(),
                    binding?.spinnerPriority?.selectedItem.toString()
                )
                GlobalScope.launch {
                    database.dao().updateTask(
                        Entity(
                            noteid,
                            binding?.etNoteTitle?.text.toString(),
                            binding?.etDescription?.text.toString(),
                            binding?.spinnerPriority?.selectedItem.toString()
                        )
                    )
                }
                updateFirebaseData(noteid, title, description, spinner)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun deleteFirebaseData(noteid: String) {

        db.collection("user/note/${firebaseAuth.currentUser!!.uid}")
            .document(noteid)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }

    }

    private fun updateFirebaseData(
        noteid: String,
        title: String,
        description: String,
        spinner: String
    ) {
        val note = NoteInfo(noteid, title, description, spinner)

        db.collection("user/note/${firebaseAuth.currentUser!!.uid}")
            .document(noteid)
            .set(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }

    }

    private fun setupPrioritySpinner() {
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