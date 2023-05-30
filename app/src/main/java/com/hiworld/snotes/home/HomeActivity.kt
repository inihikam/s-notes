package com.hiworld.snotes.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hiworld.snotes.R
import com.hiworld.snotes.data.AppDatabase
import com.hiworld.snotes.databinding.ActivityHomeBinding
import com.hiworld.snotes.home.adapter.NotesAdapter
import com.hiworld.snotes.log.login.LogInActivity
import com.hiworld.snotes.model.Notes
import com.hiworld.snotes.utils.Preferences
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

    private lateinit var binding : ActivityHomeBinding
    private var mAdapter: NotesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        binding.helloTxt.setText("Hello,\n"+preferences.getValues("nama"))

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            finishAffinity()
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter(list: List<Notes>){
        mAdapter?.setData(list)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val notesList = AppDatabase(this@HomeActivity).getNotesDao().getAllNotes()

            mAdapter = NotesAdapter()
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(this@HomeActivity)
                adapter = mAdapter
                setAdapter(notesList)
                mAdapter?.setOnActionEditListener {
                        val intent = Intent(this@HomeActivity, AddNotesActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)
                    }
                mAdapter?.setOnActionDeleteListener {
                        val builder = AlertDialog.Builder(this@HomeActivity)
                        builder.setMessage("Yakin ingin menghapus note?")
                        builder.setPositiveButton("YA") { dialog, which ->
                            lifecycleScope.launch {
                                AppDatabase(this@HomeActivity).getNotesDao().deleteNotes(it)
                                val listNotes = AppDatabase(this@HomeActivity).getNotesDao().getAllNotes()
                                setAdapter(listNotes)
                            }
                            dialog.dismiss()
                        }

                        builder.setNegativeButton("TIDAK") { dialog, which ->
                            dialog.dismiss()
                        }

                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
    }